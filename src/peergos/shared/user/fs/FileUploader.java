package peergos.shared.user.fs;
import java.util.logging.*;

import jsinterop.annotations.*;
import peergos.shared.*;
import peergos.shared.cbor.*;
import peergos.shared.crypto.*;
import peergos.shared.crypto.hash.*;
import peergos.shared.crypto.random.*;
import peergos.shared.crypto.symmetric.*;
import peergos.shared.io.ipfs.multihash.*;
import peergos.shared.storage.*;
import peergos.shared.util.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class FileUploader implements AutoCloseable {
	private static final Logger LOG = Logger.getGlobal();

    private final String name;
    private final long offset, length;
    private final FileProperties props;
    private final SymmetricKey baseKey;
    private final long nchunks;
    private final Location parentLocation;
    private final SymmetricKey parentparentKey;
    private final ProgressConsumer<Long> monitor;
    private final Fragmenter fragmenter;
    private final AsyncReader reader; // resettable input stream
    private final List<Location> locations;

    @JsConstructor
    public FileUploader(String name, String mimeType, AsyncReader fileData,
                        int offsetHi, int offsetLow, int lengthHi, int lengthLow,
                        SymmetricKey baseKey,
                        Location parentLocation, SymmetricKey parentparentKey,
                        ProgressConsumer<Long> monitor,
                        FileProperties fileProperties, Fragmenter fragmenter,
                        List<Location> locations) {
        long length = lengthLow + ((lengthHi & 0xFFFFFFFFL) << 32);
        if (fileProperties == null)
            this.props = new FileProperties(name, mimeType, length, LocalDateTime.now(), false, Optional.empty());
        else
            this.props = fileProperties;
        if (baseKey == null) baseKey = SymmetricKey.random();

        this.fragmenter = fragmenter;

        long offset = offsetLow + ((offsetHi & 0xFFFFFFFFL) << 32);

        // Process and upload chunk by chunk to avoid running out of RAM, in reverse order to build linked list
        this.nchunks = length > 0 ? (length + Chunk.MAX_SIZE - 1) / Chunk.MAX_SIZE : 1;
        this.name = name;
        this.offset = offset;
        this.length = length;
        this.reader = fileData;
        this.baseKey = baseKey;
        this.parentLocation = parentLocation;
        this.parentparentKey = parentparentKey;
        this.monitor = monitor;
        this.locations = locations;
    }

    public FileUploader(String name, String mimeType, AsyncReader fileData, long offset, long length,
                        SymmetricKey baseKey, Location parentLocation, SymmetricKey parentparentKey,
                        ProgressConsumer<Long> monitor, FileProperties fileProperties, Fragmenter fragmenter, List<Location> locations) {
        this(name, mimeType, fileData, (int)(offset >> 32), (int) offset, (int) (length >> 32), (int) length,
                baseKey, parentLocation, parentparentKey, monitor, fileProperties, fragmenter, locations);
    }

    public CompletableFuture<Boolean> uploadChunk(NetworkAccess network,
                                                  PublicKeyHash owner,
                                                  SigningPrivateKeyAndPublicHash writer,
                                                  long chunkIndex,
                                                  MaybeMultihash ourExistingHash,
                                                  ProgressConsumer<Long> monitor) {
        LOG.info("uploading chunk: "+chunkIndex + " of "+name);
        long position = chunkIndex * Chunk.MAX_SIZE;

        long fileLength = length;
        boolean isLastChunk = fileLength < position + Chunk.MAX_SIZE;
        int length =  isLastChunk ? (int)(fileLength -  position) : Chunk.MAX_SIZE;
        byte[] data = new byte[length];
        return reader.readIntoArray(data, 0, data.length).thenCompose(b -> {
            byte[] nonce = baseKey.createNonce();
            byte[] mapKey = locations.get((int) chunkIndex).getMapKey();
            Chunk chunk = new Chunk(data, baseKey, mapKey, nonce);
            LocatedChunk locatedChunk = new LocatedChunk(new Location(owner, writer.publicKeyHash, chunk.mapKey()), ourExistingHash, chunk);
            Location nextLocation = new Location(owner, writer.publicKeyHash, locations.get((int) chunkIndex + 1).getMapKey());
            return uploadChunk(writer, props, parentLocation, parentparentKey, baseKey, locatedChunk,
                    fragmenter, nextLocation, Optional.empty(), network, monitor).thenApply(c -> true);
        });
    }

    public CompletableFuture<Boolean> upload(NetworkAccess network,
                                             PublicKeyHash owner,
                                             SigningPrivateKeyAndPublicHash writer) {
        long t1 = System.currentTimeMillis();

        List<Integer> input = IntStream.range(0, (int) nchunks).mapToObj(i -> Integer.valueOf(i)).collect(Collectors.toList());
        return Futures.reduceAll(input, true, (loc, i) -> uploadChunk(network, owner, writer, i,
                MaybeMultihash.empty(), monitor), (a, b) -> a && b)
                .thenApply(x -> {
                    LOG.info("File encryption, erasure coding and upload took: " +(System.currentTimeMillis()-t1) + " mS");
                    return x;
                });
    }

    public static CompletableFuture<Multihash> uploadChunk(SigningPrivateKeyAndPublicHash writer,
                                                           FileProperties props,
                                                           Location parentLocation,
                                                           SymmetricKey parentparentKey,
                                                           SymmetricKey baseKey,
                                                           LocatedChunk chunk,
                                                           Fragmenter fragmenter,
                                                           Location nextChunkLocation,
                                                           Optional<SymmetricLinkToSigner> writerLink,
                                                           NetworkAccess network,
                                                           ProgressConsumer<Long> monitor) {
        if (! writer.publicKeyHash.equals(chunk.location.writer))
            throw new IllegalStateException("Trying to write a chunk to the wrong signing key space!");
        return chunk.chunk.encrypt().thenCompose(encryptedChunk -> {
            List<Fragment> fragments = encryptedChunk.generateFragments(fragmenter);
            LOG.info(StringUtils.format("Uploading chunk with %d fragments\n", fragments.size()));
            SymmetricKey chunkKey = chunk.chunk.key();
            CipherText encryptedNextChunkLocation = CipherText.build(chunkKey, new CborObject.CborByteArray(nextChunkLocation.getMapKey()));
            return IpfsTransaction.call(chunk.location.owner, tid -> network
                            .uploadFragments(fragments, chunk.location.owner, writer, monitor, fragmenter.storageIncreaseFactor(), tid)
                            .thenCompose(hashes -> {
                                FileRetriever retriever =
                                        new EncryptedChunkRetriever(chunk.chunk.nonce(), encryptedChunk.getAuth(),
                                                hashes, Optional.of(encryptedNextChunkLocation), fragmenter);
                                FileAccess metaBlob = FileAccess.create(chunk.existingHash, baseKey,
                                        chunkKey, props, retriever, parentLocation, parentparentKey).withWriterLink(writerLink);
                                return network.uploadChunk(metaBlob, chunk.location.owner,
                                        chunk.chunk.mapKey(), writer, tid);
                            }),
                    network.dhtClient);
        });
    }

    public void close() {
        reader.close();
    }
}

$wnd.showcase.runAsyncCallback11("function uEb(){}\nfunction wEb(){}\nfunction pEb(a,b){a.b=b}\nfunction qEb(a){if(a==fEb){return true}ez();return a==iEb}\nfunction rEb(a){if(a==eEb){return true}ez();return a==dEb}\nfunction vEb(a){this.b=(ZFb(),UFb).a;this.e=(cGb(),bGb).a;this.a=a}\nfunction nEb(a,b){var c;c=dC(a.fb,153);c.b=b.a;!!c.d&&hzb(c.d,b)}\nfunction oEb(a,b){var c;c=dC(a.fb,153);c.e=b.a;!!c.d&&jzb(c.d,b)}\nfunction jEb(){jEb=DX;cEb=new uEb;fEb=new uEb;eEb=new uEb;dEb=new uEb;gEb=new uEb;hEb=new uEb;iEb=new uEb}\nfunction sEb(){jEb();lzb.call(this);this.b=(ZFb(),UFb);this.c=(cGb(),bGb);(Xvb(),this.e)[pac]=0;this.e[qac]=0}\nfunction kEb(a,b,c){var d;if(c==cEb){if(b==a.a){return}else if(a.a){throw ZW(new XWb('Only one CENTER widget may be added'))}}Rh(b);kQb(a.j,b);c==cEb&&(a.a=b);d=new vEb(c);b.fb=d;nEb(b,a.b);oEb(b,a.c);mEb(a);Th(b,a)}\nfunction lEb(a){var b,c,d,e,f,g,h;TPb((Xvb(),a.hb),'',Zbc);g=new M2b;h=new uQb(a.j);while(h.b<h.c.c){b=sQb(h);f=dC(b.fb,153).a;d=dC(UZb(c3b(g.d,f)),85);c=!d?1:d.a;e=f==gEb?'north'+c:f==hEb?'south'+c:f==iEb?'west'+c:f==dEb?'east'+c:f==fEb?'linestart'+c:f==eEb?'lineend'+c:X8b;TPb(Qo(b.hb),Zbc,e);e$b(g,f,lXb(c+1))}}\nfunction mEb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r;b=(Xvb(),a.d);while(zxb(b)>0){wo(b,yxb(b,0))}o=1;e=1;for(i=new uQb(a.j);i.b<i.c.c;){d=sQb(i);f=dC(d.fb,153).a;f==gEb||f==hEb?++o:(f==dEb||f==iEb||f==fEb||f==eEb)&&++e}p=mB(wR,r6b,262,o,0,1);for(g=0;g<o;++g){p[g]=new wEb;p[g].b=$doc.createElement(nac);so(b,cwb(p[g].b))}k=0;l=e-1;m=0;q=o-1;c=null;for(h=new uQb(a.j);h.b<h.c.c;){d=sQb(h);j=dC(d.fb,153);r=$doc.createElement(oac);j.d=r;j.d[cac]=j.b;j.d.style[dac]=j.e;j.d[J6b]=j.f;j.d[I6b]=j.c;if(j.a==gEb){$vb(p[m].b,r,p[m].a);so(r,cwb(d.hb));r[ebc]=l-k+1;++m}else if(j.a==hEb){$vb(p[q].b,r,p[q].a);so(r,cwb(d.hb));r[ebc]=l-k+1;--q}else if(j.a==cEb){c=r}else if(qEb(j.a)){n=p[m];$vb(n.b,r,n.a++);so(r,cwb(d.hb));r[$bc]=q-m+1;++k}else if(rEb(j.a)){n=p[m];$vb(n.b,r,n.a);so(r,cwb(d.hb));r[$bc]=q-m+1;--l}}if(a.a){n=p[m];$vb(n.b,c,n.a);so(c,cwb(eh(a.a)))}}\nvar Zbc='cwDockPanel';CX(416,1,W8b);_.Bc=function teb(){var a,b,c;WZ(this.a,(a=new sEb,(Xvb(),a.hb).className='cw-DockPanel',a.e[pac]=4,pEb(a,(ZFb(),TFb)),kEb(a,new RCb(Tbc),(jEb(),gEb)),kEb(a,new RCb(Ubc),hEb),kEb(a,new RCb(Vbc),dEb),kEb(a,new RCb(Wbc),iEb),kEb(a,new RCb(Xbc),gEb),kEb(a,new RCb(Ybc),hEb),b=new RCb('\\u8FD9\\u4E2A\\u793A\\u4F8B\\u4E2D\\u5728<code>DockPanel<\\/code> \\u7684\\u4E2D\\u95F4\\u4F4D\\u7F6E\\u6709\\u4E00\\u4E2A<code>ScrollPanel<\\/code>\\u3002\\u5982\\u679C\\u5728\\u4E2D\\u95F4\\u653E\\u5165\\u5F88\\u591A\\u5185\\u5BB9\\uFF0C\\u5B83\\u5C31\\u4F1A\\u53D8\\u6210\\u9875\\u9762\\u5185\\u7684\\u53EF\\u6EDA\\u52A8\\u533A\\u57DF\\uFF0C\\u65E0\\u9700\\u4F7F\\u7528IFRAME\\u3002<br><br>\\u6B64\\u5904\\u4F7F\\u7528\\u4E86\\u76F8\\u5F53\\u591A\\u65E0\\u610F\\u4E49\\u7684\\u6587\\u5B57\\uFF0C\\u4E3B\\u8981\\u662F\\u4E3A\\u4E86\\u53EF\\u4EE5\\u6EDA\\u52A8\\u81F3\\u53EF\\u89C6\\u533A\\u57DF\\u7684\\u5E95\\u90E8\\u3002\\u5426\\u5219\\uFF0C\\u60A8\\u6050\\u6015\\u4E0D\\u5F97\\u4E0D\\u628A\\u5B83\\u7F29\\u5230\\u5F88\\u5C0F\\u624D\\u80FD\\u770B\\u5230\\u90A3\\u5C0F\\u5DE7\\u7684\\u6EDA\\u52A8\\u6761\\u3002'),c=new kAb(b),c.hb.style[J6b]='400px',c.hb.style[I6b]='100px',kEb(a,c,cEb),lEb(a),a))};CX(871,254,O6b,sEb);_.gc=function tEb(a){var b;b=fyb(this,a);if(b){a==this.a&&(this.a=null);mEb(this)}return b};var cEb,dEb,eEb,fEb,gEb,hEb,iEb;var xR=GWb(M6b,'DockPanel',871);CX(152,1,{},uEb);var uR=GWb(M6b,'DockPanel/DockLayoutConstant',152);CX(153,1,{153:1},vEb);_.c='';_.f='';var vR=GWb(M6b,'DockPanel/LayoutData',153);CX(262,1,{262:1},wEb);_.a=0;var wR=GWb(M6b,'DockPanel/TmpRow',262);Y5b(zl)(11);\n//# sourceURL=showcase-11.js\n")
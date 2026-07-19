class Y{String s(boolean p,String[]S){int r=0,i,j,k;for(j=0;j<S.length;j+=8)for(k=j+8;k<S.length;k+=8,r+=i<0?1:0)for(i=35;i-->0&&(S[j+i/5].charAt(i%5)&S[k+i/5].charAt(i%5)&1)<1;);return r+"";}}

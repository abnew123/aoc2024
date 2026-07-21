class Y{String s(boolean p,String[]S){int r=0,i,j=0,k;for(;j<S.length;j+=8)for(k=j;(k+=8)<S.length;r-=i>>31)for(i=35;i-->0&&(S[j+i/5].charAt(i%5)&S[k+i/5].charAt(i%5)&1)<1;);return r+"";}}

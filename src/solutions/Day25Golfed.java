class Y{String s(boolean p,String[]S){int r=0,i,j=0,k,n=S.length;for(;j<n;j+=8)for(k=j;(k+=8)<n;r-=i>>6)for(i=35;i-->0&&(S[j+i/5].charAt(i%5)&S[k+i/5].charAt(i%5)&1)<1;);return r+"";}}

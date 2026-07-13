import java.util.*;
class X{
 Map<String,I>m=new HashMap();
 String s(boolean p,String[]S){
  for(var s:S){
   var A=s.split("\\s+");
   if(A.length<2)continue;
   if(A.length<3)m.put(A[0].replace(":",""),new I(new Integer(A[1])));
   else m.put(A[4],new I(A[0],A[1],A[2],A[4]));
  }
  if(!p)return s();
  long r=0;
  for(var k:m.keySet())if(k.charAt(0)==122)r+=1L*v(k)<<new Integer(k.substring(1));
  return r+"";
 }
 int v(String s){I i=m.get(s);if(i.p<2)return i.p;int x=v(i.a),y=v(i.b);return i.p==65?x&y:i.p==79?x|y:x^y;}
 String s(){
  var z="";
  for(I i:m.values())if(i.p>1&&i.o.charAt(0)==122&&i.o.compareTo(z)>0)z=i.o;
  var b=new TreeSet();
  for(I i:m.values())if(i.p>1){
   boolean f=i.a.endsWith("00"),xy=i.a.charAt(0)>119&i.b.charAt(0)>119,o=i.o.charAt(0)==122;
   if(o&i.o!=z&i.p!=88|i.p==88&!xy&!o|i.p==65&!f&!e(i.o,79)|i.p==88&xy&!f&(!e(i.o,88)|!e(i.o,65)))b.add(i.o);
  }
  return String.join(",",b);
 }
 boolean e(String w,int p){for(I i:m.values())if(i.p==p&&(i.a.equals(w)|i.b.equals(w)))return 1>0;return 1<0;}
 class I{
  String a,b,o;int p;
  I(int x){p=x;}
  I(String x,String q,String y,String z){a=x;p=q.charAt(0);b=y;o=z;}
 }
}

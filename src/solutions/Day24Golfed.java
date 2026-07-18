import java.util.*;
class X{
 Map<String,Integer>m=new HashMap();
 List<I>l=new ArrayList();
 String s(boolean p,String S){
  var A=S.split("\\s+");
  for(int j=0;j<A.length;){
   var s=A[j++];
   if(s.length()>3)m.put(s.substring(0,3),Integer.parseInt(A[j++]));
   else l.add(new I(s,A[j++],A[j++],A[j++],A[j++]));
  }
  if(!p)return s();
  for(int n=100;n-->0;)for(I i:l)if(m.get(i.o)==null&m.get(i.a)!=null&m.get(i.b)!=null)m.put(i.o,i.r());
  long r=0;
  for(var k:m.keySet())if(k.charAt(0)==122)r+=1L*m.get(k)<<k.charAt(1)*10+k.charAt(2)-528;
  return r+"";
 }
 String s(){
  String z="";
  for(I i:l)if(i.o.charAt(0)==122&&i.o.compareTo(z)>0)z=i.o;
  var b=new TreeSet();
  for(I i:l){
   boolean f=i.a.endsWith("00"),xy=i.a.charAt(0)>119&i.b.charAt(0)>119,o=i.o.charAt(0)==122;
   if(o&i.o!=z&i.p!=88|i.p==88&!xy&!o|i.p==65&!f&!e(i.o,79)|i.p==88&xy&!f&(!e(i.o,88)|!e(i.o,65)))b.add(i.o);
  }
  return String.join(",",b);
 }
 boolean e(String w,int p){for(I i:l)if(i.p==p&(i.a.equals(w)|i.b.equals(w)))return 1>0;return 1<0;}
 class I{
  String a,b,o;int p;
  I(String x,String q,String y,String r,String z){a=x;p=q.charAt(0);b=y;o=z;}
  int r(){int x=m.get(a),y=m.get(b);return p==65?x&y:p==79?x|y:x^y;}
 }
}

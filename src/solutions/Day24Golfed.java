import java.util.*;
class X{
 Map<String,Integer>m=new HashMap();
 List<I>l=new ArrayList();
 String s(boolean p,String[]S){
  for(var s:S){
   var A=s.split("\\s+");
   if(A.length<2)continue;
   if(A.length<3)m.put(A[0].substring(0,3),Integer.parseInt(A[1]));
   else l.add(new I(A[0],A[1],A[2],A[3],A[4]));
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

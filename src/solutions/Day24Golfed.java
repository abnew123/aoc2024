import java.util.*;
class X{
 Map<String,String[]>m=new HashMap();
 String s(boolean p,String[]S){
  for(var s:S){var A=s.split("\\s+");if(A.length>1)m.put(A.length<3?A[0].replace(":",""):A[4],A);}
  if(!p)return s();
  long r=0;
  for(var k:m.keySet())if(k.charAt(0)>121)r+=1L*v(k)<<new Integer(k.substring(1));
  return r+"";
 }
 int v(String s){var i=m.get(s);if(i.length<3)return new Integer(i[1]);int x=v(i[0]),y=v(i[2]),p=i[1].charAt(0);return p<66?x&y:p<80?x|y:x^y;}
 String s(){
  var z="";
  for(var k:m.keySet())if(k.charAt(0)>121&&m.get(k).length>2&&k.compareTo(z)>0)z=k;
  var b=new TreeSet();
  for(var E:m.entrySet()){var i=E.getValue();if(i.length>2){var o=E.getKey();int p=i[1].charAt(0);
   boolean f=i[0].endsWith("00"),xy=i[0].charAt(0)>119&i[2].charAt(0)>119,O=o.charAt(0)>121;
   if(O&o!=z&p<88|p>87&!xy&!O|p<66&!f&!e(o,79)|p>87&xy&!f&(!e(o,88)|!e(o,65)))b.add(o);
  }}
  return String.join(",",b);
 }
 boolean e(String w,int p){for(var i:m.values())if(i.length>2&&i[1].charAt(0)==p&&(i[0].equals(w)|i[2].equals(w)))return 1>0;return 1<0;}
}

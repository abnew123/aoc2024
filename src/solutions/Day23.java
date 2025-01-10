package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day23 extends DayTemplate {

    Set<String> computers = new HashSet<>();
    Map<String, Set<String>> connections = new HashMap<>();
    Set<Set<String>> maximalCliques = new HashSet<>();

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            lines.add(line);
        }

        for(String line: lines){
            String first = line.split("-")[0];
            String second = line.split("-")[1];
            Set<String> f = connections.getOrDefault(first, new HashSet<>());
            Set<String> s = connections.getOrDefault(second, new HashSet<>());
            f.add(second);
            s.add(first);
            connections.put(first, f);
            connections.put(second, s);
            computers.add(first);
            computers.add(second);
        }

        List<String> forIndices = new ArrayList<>(computers);

        if(part1){
            for(String first: forIndices){
                for(String second: connections.get(first)){
                    for(String third: connections.get(second)){
                        if(connections.get(third).contains(first)){
                            if(first.startsWith("t") || second.startsWith("t") || third.startsWith("t")){
                                answer++;
                            }
                        }
                    }
                }
            }
            answer/=6;
        }
        else{
            BronKerbosch(new HashSet<>(), computers, new HashSet<>());
            Set<String> biggest = new HashSet<>();
            for(Set<String> candidate: maximalCliques){
                if(candidate.size() > biggest.size()){
                    biggest = candidate;
                }
            }
            return listToString(new ArrayList<>(biggest));
        }
        return answer+"";
    }

    private void BronKerbosch(Set<String> R, Set<String> P, Set<String> X){
        if(P.isEmpty() && X.isEmpty()){
            maximalCliques.add(R);
        }
        else{
            Set<String> P_UNION_X = new HashSet<>(P);
            P_UNION_X.addAll(X);
            String pivot = P_UNION_X.iterator().next();
            P.removeAll(connections.get(pivot));
            for(String v: List.copyOf(P)){
                Set<String> RPrime = new HashSet<>(R);
                RPrime.add(v);
                Set<String> PPrime = new HashSet<>(connections.get(v));
                PPrime.retainAll(P);
                Set<String> XPrime = new HashSet<>(connections.get(v));
                XPrime.retainAll(X);

                BronKerbosch(RPrime, PPrime, XPrime);
                P.remove(v);
                X.add(v);
            }
            Set<String> RPrime = new HashSet<>(R);
            RPrime.add(pivot);
            Set<String> PPrime = new HashSet<>(connections.get(pivot));
            PPrime.retainAll(P);
            Set<String> XPrime = new HashSet<>(connections.get(pivot));
            XPrime.retainAll(X);
            BronKerbosch(RPrime, PPrime, XPrime);
            P.remove(pivot);
            X.add(pivot);
        }
    }

    private String listToString(List<String> computers){
        Collections.sort(computers);
        String result = "";
        for(String computer: computers){
            result+=computer;
            result+=",";
        }
        return result.substring(0, result.length() - 1);
    }
}
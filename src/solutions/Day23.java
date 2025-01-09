package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day23 extends DayTemplate {

    Set<String> computers = new HashSet<>();
    Map<String, Set<String>> connections = new HashMap<>();

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
            List<List<String>> connectedGroups = new ArrayList<>();
            for(String computer: computers){
                List<String> tmp = new ArrayList<>();
                tmp.add(computer);
                connectedGroups.add(tmp);
            }

            while(true){
                List<List<String>> newConnectedGroups = new ArrayList<>();
                Set<String> newStringForms = new HashSet<>();
                for(List<String> connectedGroup: connectedGroups){
                    for(String s: computers){
                        if(connections.get(s).containsAll(connectedGroup)){
                            List<String> newConnectedGroup = new ArrayList<>(connectedGroup);
                            newConnectedGroup.add(s);
                            if(!newStringForms.contains(listToString(newConnectedGroup))){
                                newConnectedGroups.add(newConnectedGroup);
                                newStringForms.add(listToString(newConnectedGroup));
                            }
                        }
                    }
                }
                if(newConnectedGroups.isEmpty()){
                    return listToString(connectedGroups.get(0));
                }
                connectedGroups = newConnectedGroups;
            }
        }
        return answer+"";
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
package src.solutions;

import java.util.*;

public class Day05 {
    List<String[]> rules;

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        rules = new ArrayList<>();
        List<String[]> updates = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            if(line.contains("|")){
                rules.add(line.split("\\|"));
            }
            if(line.contains(",")){
                updates.add(line.split(","));
            }
        }
        for(String[] update : updates){
            answer+=part1?part1(update):part2(update);
        }
        return answer + "";
    }

    private int part1(String[] update){
        Map<Integer, Integer> indices = new HashMap<>();
        for(int i = 0; i < update.length; i++){
            indices.put(Integer.parseInt(update[i]), i);
        }
        for(String[] rule: rules){
            int first = Integer.parseInt(rule[0]);
            int second = Integer.parseInt(rule[1]);
            if(indices.containsKey(first) && indices.containsKey(second) && (indices.get(first) > indices.get(second))) {
                return 0;
            }
        }
        return Integer.parseInt(update[update.length/2]);
    }

    private int part2(String[] update){
        Map<Integer, Integer> indices = new HashMap<>();
        for(int i = 0; i < update.length; i++){
            indices.put(Integer.parseInt(update[i]), i);
        }
        Map<Integer, List<Integer>> priors = new HashMap<>();
        boolean incorrect = false;
        for(String[] rule: rules){
            int first = Integer.parseInt(rule[0]);
            int second = Integer.parseInt(rule[1]);
            if(indices.containsKey(first) && indices.containsKey(second)){
                priors.computeIfAbsent(first, k -> new ArrayList<>()).add(second);
                if(indices.get(first) > indices.get(second)) {
                    incorrect = true;
                }
            }
        }
        if(incorrect){
            List<Integer> ordered = new ArrayList<>();
            while(ordered.size() < update.length ){
                for(int val: indices.keySet()){
                    if(priors.getOrDefault(val, new ArrayList<>()).isEmpty() && !ordered.contains(val)){
                        ordered.add(val);
                    }
                }
                // for better perf you can remove only the newly added elements, but all updates are relatively small in this case
                for(int val: priors.keySet()){
                    priors.computeIfAbsent(val, k -> new ArrayList<>()).removeAll(ordered);
                }
            }
            return ordered.get(ordered.size()/2);
        }
        return 0;
    }
}

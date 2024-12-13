package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day07 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String[]> values = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            String[] parts1 = line.split(":");
            String[] parts2 = parts1[1].trim().split(" ");
            targets.add(parts1[0]);
            values.add(parts2);
        }
        for(int i = 0; i < values.size(); i++){
            answer+= Long.parseLong(oneCycle(targets.get(i), values.get(i), part1));
        }
        return answer + "";
    }

    private String oneCycle(String target, String[] values, boolean part1){
        Set<String> possible = new HashSet<>();
        possible.add(values[0]);
        for(int i = 1; i < values.length; i++){
            Set<String> newPossible = new HashSet<>();
            for(String p: possible){
                if(Long.parseLong(p) < Long.parseLong(target)){
                    newPossible.add(operation(0,p,values[i]));
                    newPossible.add(operation(1,p,values[i]));
                    if(!part1){
                        newPossible.add(operation(2,p,values[i]));
                    }
                }

            }
            possible = newPossible;
        }
        if(possible.contains(target)){
            return target;
        }
        return "0";
    }

    private String operation(int type, String input1, String input2){
        if(type == 0){
            return Long.parseLong(input1) + Long.parseLong(input2) + "";
        }
        if(type == 1){
            return Long.parseLong(input1) * Long.parseLong(input2) + "";
        }
        if(type == 2){
            return input1 + input2;
        }
        return "0";
    }
}
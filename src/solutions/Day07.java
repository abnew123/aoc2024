package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day07 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<List<Integer>> equations = new ArrayList<>();
        List<Long> targets = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            String[] parts1 = line.split(":");
            String[] parts2 = parts1[1].trim().split(" ");
            targets.add(Long.parseLong(parts1[0]));
            List<Integer> equation = new ArrayList<>();
            for(String p: parts2){
                equation.add(Integer.parseInt(p));
            }
            equations.add(equation);
        }

        for(int i = 0; i < targets.size(); i++){
            long target = targets.get(i);
            if(possible(target, equations.get(i), part1)){
                answer+= target;
            }
        }

        return answer + "";
    }

    private boolean possible(long target, List<Integer> equation, boolean part1){
        if(equation.isEmpty()){
            return target == 0;
        }
        int nextVal = equation.get(equation.size() - 1);
        if(target < nextVal){
            return false;
        }
        List<Integer> reducedEquation = new ArrayList<>(equation);
        reducedEquation.remove(equation.size() - 1);
        boolean possible = possible(target - nextVal, reducedEquation, part1);
        if(target % nextVal == 0){
            possible |= possible(target/nextVal, reducedEquation, part1);
        }
        int size = (nextVal+"").length();
        if((target+"").endsWith(nextVal+"") && !part1){
            possible |= possible(target/ (int)(Math.pow(10,size)), reducedEquation, part1);
        }
        return possible;
    }
}
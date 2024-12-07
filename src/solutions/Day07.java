package src.solutions;

import java.util.*;

public class Day07 {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<long[]> values = new ArrayList<>();
        List<Long> targets = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            String[] parts1 = line.split(":");
            String[] parts2 = parts1[1].trim().split(" ");
            targets.add(Long.parseLong(parts1[0]));
            long[] tmp = new long[parts2.length];
            for(int i = 0; i < parts2.length; i++){
                tmp[i] = Long.parseLong(parts2[i]);
            }
            values.add(tmp);
        }

        for(int i = 0; i < values.size(); i++){
            answer+= oneCycle(targets.get(i), values.get(i), part1);
        }
        return answer + "";
    }

    private long oneCycle(long target, long[] values, boolean part1){
        Set<Long> possible = new HashSet<>();
        possible.add(values[0]);
        for(int i = 1; i < values.length; i++){
            Set<Long> newPossible = new HashSet<>();
            for(Long p: possible){
                newPossible.add(operation(0,p,values[i]));
                newPossible.add(operation(1,p,values[i]));
                if(!part1){
                    newPossible.add(operation(2,p,values[i]));
                }
            }
            possible = newPossible;
        }
        if(possible.contains(target)){
            return target;
        }
        return 0;
    }

    private long operation(int type, long input1, long input2){
        if(type == 0){
            return input1 + input2;
        }
        if(type == 1){
            return input1 * input2;
        }
        if(type == 2){
            int size = 0;
            long tmp = input2;
            while(tmp > 0){
                tmp/=10;
                size++;
            }
            return input1 * (long)Math.pow(10, size) + input2;
        }
        return -1;
    }
}
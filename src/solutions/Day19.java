package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day19 extends DayTemplate {

    Map<String, Long> possibilities = new HashMap<>();
    String[] available;

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        available = in.nextLine().split(", ");
        in.nextLine();
        List<String> towels = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            towels.add(line);
        }

        for(String towel: towels){
            if(part1){
                answer += (number(towel)>0)?1:0;
            }
            else{
                answer+= number(towel);
            }
        }
        return answer + "";
    }

    private long number(String towel){
        if(towel.isEmpty()){
            return 1;
        }
        if(possibilities.containsKey(towel)){
            return possibilities.get(towel);
        }
        long result = 0L;
        for(String a: available){
            if(towel.startsWith(a)){
                result += number(towel.substring(a.length()));
            }
        }
        possibilities.put(towel, result);
        return result;
    }
}
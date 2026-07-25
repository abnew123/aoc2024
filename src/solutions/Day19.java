package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day19 extends DayTemplate {

    Map<String, Long> possibilities = new HashMap<>();
    String[] available;

    /**
     * Single pass solver. Parsing is shared, and so is the whole computation: number() is
     * part agnostic, so each towel's arrangement count is computed once and fed to both parts.
     *
     * @param in The solver will read data from this Scanner.
     * @return Returns answer as a string array, with part 1 as index 0 and part 2 as index 1
     */
    public String[] fullSolve(Scanner in) {
        long answer1 = 0;
        long answer2 = 0;
        available = in.nextLine().split(", ");
        in.nextLine();
        List<String> towels = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            towels.add(line);
        }

        for(String towel: towels){
            long count = number(towel);
            answer1 += (count > 0)?1:0;
            answer2 += count;
        }
        return new String[]{answer1 + "", answer2 + ""};
    }

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
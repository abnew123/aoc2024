package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day19 extends DayTemplate {
    private String[] towels;
    private Map<String, Long> memo;

    public String solve(boolean part1, Scanner in) {
        towels = in.nextLine().split(", ");
        in.nextLine();
        long answer = 0;
        while (in.hasNextLine()) {
            memo = new HashMap<>();
            long ways = ways(in.nextLine());
            answer += part1 ? ways > 0 ? 1 : 0 : ways;
        }
        return "" + answer;
    }

    private long ways(String design) {
        if (design.isEmpty()) {
            return 1;
        }
        if (memo.containsKey(design)) {
            return memo.get(design);
        }
        long total = 0;
        for (String towel : towels) {
            if (design.startsWith(towel)) {
                total += ways(design.substring(towel.length()));
            }
        }
        memo.put(design, total);
        return total;
    }
}

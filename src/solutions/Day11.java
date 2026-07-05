package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day11 extends DayTemplate {
    final Map<String, Long> memo = new HashMap<>();

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        for (String s : in.nextLine().split(" ")) {
            answer += count(Long.parseLong(s), part1 ? 25 : 75);
        }
        return "" + answer;
    }

    long count(long stone, int blinks) {
        if (blinks == 0) {
            return 1;
        }
        String key = stone + "," + blinks;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        long answer;
        if (stone == 0) {
            answer = count(1, blinks - 1);
        } else {
            String s = "" + stone;
            int half = s.length() / 2;
            answer = s.length() % 2 == 0
                    ? count(Long.parseLong(s.substring(0, half)), blinks - 1)
                    + count(Long.parseLong(s.substring(half)), blinks - 1)
                    : count(stone * 2024, blinks - 1);
        }
        memo.put(key, answer);
        return answer;
    }
}

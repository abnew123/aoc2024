package src.solutions;

import src.meta.DayTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day11 extends DayTemplate {

    private final Map<State, Long> memo = new HashMap<>();

    public String solve(boolean part1, Scanner in) {
        int blinks = part1 ? 25 : 75;
        long answer = 0;
        String[] stones = in.nextLine().split(" ");
        memo.clear();
        for (String stone : stones) {
            answer += countStones(Long.parseLong(stone), blinks);
        }
        return answer + "";
    }

    private long countStones(long stone, int blinksLeft) {
        if (blinksLeft == 0) {
            return 1;
        }

        State state = new State(stone, blinksLeft);
        Long cached = memo.get(state);
        if (cached != null) {
            return cached;
        }

        long result;
        if (stone == 0) {
            result = countStones(1, blinksLeft - 1);
        } else {
            int digits = digits(stone);
            if (digits % 2 == 0) {
                long divisor = pow10(digits / 2);
                result = countStones(stone / divisor, blinksLeft - 1)
                        + countStones(stone % divisor, blinksLeft - 1);
            } else {
                result = countStones(stone * 2024, blinksLeft - 1);
            }
        }

        memo.put(state, result);
        return result;
    }

    private int digits(long stone) {
        int digits = 1;
        while (stone >= 10) {
            stone /= 10;
            digits++;
        }
        return digits;
    }

    private long pow10(int power) {
        long result = 1;
        for (int i = 0; i < power; i++) {
            result *= 10;
        }
        return result;
    }

    private record State(long stone, int blinksLeft) {}
}

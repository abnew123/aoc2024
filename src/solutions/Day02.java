package src.solutions;

import src.meta.DayTemplate;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Day02 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isBlank()) {
                continue;
            }
            int[] levels = parse(line);
            if (part1 ? isSafe(levels, -1) : isSafeWithDampener(levels)) {
                answer++;
            }
        }
        return answer + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        long part1 = 0;
        long part2 = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isBlank()) {
                continue;
            }
            int[] levels = parse(line);
            boolean safe = isSafe(levels, -1);
            if (safe) {
                part1++;
            }
            if (safe || isSafeAfterRemovingOne(levels)) {
                part2++;
            }
        }
        return new String[]{part1 + "", part2 + ""};
    }

    private int[] parse(String line) {
        StringTokenizer tokens = new StringTokenizer(line);
        int[] levels = new int[tokens.countTokens()];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = Integer.parseInt(tokens.nextToken());
        }
        return levels;
    }

    private boolean isSafeWithDampener(int[] levels) {
        return isSafe(levels, -1) || isSafeAfterRemovingOne(levels);
    }

    private boolean isSafeAfterRemovingOne(int[] levels) {
        for (int skipped = 0; skipped < levels.length; skipped++) {
            if (isSafe(levels, skipped)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSafe(int[] levels, int skipped) {
        int previous = 0;
        int direction = 0;
        boolean hasPrevious = false;
        for (int i = 0; i < levels.length; i++) {
            if (i == skipped) {
                continue;
            }
            int current = levels[i];
            if (!hasPrevious) {
                previous = current;
                hasPrevious = true;
                continue;
            }
            long difference = (long) current - previous;
            if (difference == 0 || difference < -3 || difference > 3) {
                return false;
            }
            int currentDirection = difference > 0 ? 1 : -1;
            if (direction != 0 && direction != currentDirection) {
                return false;
            }
            direction = currentDirection;
            previous = current;
        }
        return true;
    }
}

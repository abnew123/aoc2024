package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day07 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<int[]> equations = new ArrayList<>();
        List<Long> targets = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int colon = line.indexOf(':');
            targets.add(Long.parseLong(line.substring(0, colon)));
            String[] parts = line.substring(colon + 2).split(" ");
            int[] equation = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                equation[i] = Integer.parseInt(parts[i]);
            }
            equations.add(equation);
        }

        for (int i = 0; i < targets.size(); i++) {
            long target = targets.get(i);
            if (possible(target, equations.get(i), equations.get(i).length - 1, part1)) {
                answer += target;
            }
        }

        return answer + "";
    }

    private boolean possible(long target, int[] equation, int index, boolean part1) {
        if (index < 0) {
            return target == 0;
        }
        int nextVal = equation[index];
        if (target < nextVal) {
            return false;
        }
        if (possible(target - nextVal, equation, index - 1, part1)) {
            return true;
        }
        if (target % nextVal == 0 && possible(target / nextVal, equation, index - 1, part1)) {
            return true;
        }
        if (!part1) {
            int divisor = pow10(nextVal);
            return target % divisor == nextVal && possible(target / divisor, equation, index - 1, part1);
        }
        return false;
    }

    private int pow10(int value) {
        int result = 10;
        while (value >= result) {
            result *= 10;
        }
        return result;
    }
}

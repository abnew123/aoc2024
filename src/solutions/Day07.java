package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day07 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        while (in.hasNextLine()) {
            String[] s = in.nextLine().split(":? ");
            long target = Long.parseLong(s[0]);
            int[] values = new int[s.length - 1];
            for (int i = 1; i < s.length; i++) {
                values[i - 1] = Integer.parseInt(s[i]);
            }
            if (ok(target, values, values.length - 1, part1)) {
                answer += target;
            }
        }
        return "" + answer;
    }

    boolean ok(long target, int[] values, int i, boolean part1) {
        if (i < 0) {
            return target == 0;
        }
        int x = values[i], p = 10;
        while (p <= x) {
            p *= 10;
        }
        return target >= x && (ok(target - x, values, i - 1, part1)
                || target % x == 0 && ok(target / x, values, i - 1, part1)
                || !part1 && target % p == x && ok(target / p, values, i - 1, false));
    }
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day02 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        int answer = 0;
        while (in.hasNextLine()) {
            int[] report = Arrays.stream(in.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (safe(report, -1)) {
                answer++;
            } else if (!part1) {
                for (int skip = 0; skip < report.length; skip++) {
                    if (safe(report, skip)) {
                        answer++;
                        break;
                    }
                }
            }
        }
        return "" + answer;
    }

    boolean safe(int[] report, int skip) {
        int last = -1, direction = 0;
        for (int i = 0; i < report.length; i++) {
            if (i == skip) {
                continue;
            }
            if (last >= 0) {
                int diff = report[i] - last, sign = Integer.signum(diff);
                if (diff == 0 || Math.abs(diff) > 3 || direction != 0 && sign != direction) {
                    return false;
                }
                direction = sign;
            }
            last = report[i];
        }
        return true;
    }
}

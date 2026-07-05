package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day05 extends DayTemplate {
    private final boolean[][] before = new boolean[100][100];

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.contains("|")) {
                before[Integer.parseInt(line.substring(0, 2))][Integer.parseInt(line.substring(3))] = true;
            } else if (line.contains(",")) {
                Integer[] update = Arrays.stream(line.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
                boolean ordered = ordered(update);
                if (part1 == ordered) {
                    if (!ordered) {
                        Arrays.sort(update, (a, b) -> before[a][b] ? -1 : before[b][a] ? 1 : 0);
                    }
                    answer += update[update.length / 2];
                }
            }
        }
        return "" + answer;
    }

    private boolean ordered(Integer[] update) {
        for (int i = 0; i < update.length; i++) {
            for (int j = i + 1; j < update.length; j++) {
                if (before[update[j]][update[i]]) {
                    return false;
                }
            }
        }
        return true;
    }
}

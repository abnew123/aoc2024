package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day05 extends DayTemplate {
    private final boolean[][] before = new boolean[100][100];
    private final int[] indexByPage = new int[100];
    private final boolean[] present = new boolean[100];

    public String solve(boolean part1, Scanner in) {
        List<int[]> updates = new ArrayList<>();
        List<int[]> rules = new ArrayList<>();
        for (boolean[] row : before) {
            java.util.Arrays.fill(row, false);
        }

        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.contains("|")) {
                int separator = line.indexOf('|');
                int first = Integer.parseInt(line.substring(0, separator));
                int second = Integer.parseInt(line.substring(separator + 1));
                before[first][second] = true;
                rules.add(new int[]{first, second});
            } else if (line.contains(",")) {
                String[] split = line.split(",");
                int[] update = new int[split.length];
                for (int i = 0; i < split.length; i++) {
                    update[i] = Integer.parseInt(split[i]);
                }
                updates.add(update);
            }
        }

        long answer = 0;
        for (int[] update : updates) {
            answer += part1 ? part1(update, rules) : part2(update, rules);
        }
        return answer + "";
    }

    private int part1(int[] update, List<int[]> rules) {
        markUpdate(update);
        boolean correct = true;
        for (int[] rule : rules) {
            int first = rule[0];
            int second = rule[1];
            if (present[first] && present[second] && indexByPage[first] > indexByPage[second]) {
                correct = false;
                break;
            }
        }
        unmarkUpdate(update);
        return correct ? update[update.length / 2] : 0;
    }

    private int part2(int[] update, List<int[]> rules) {
        markUpdate(update);
        boolean incorrect = false;
        for (int[] rule : rules) {
            int first = rule[0];
            int second = rule[1];
            if (present[first] && present[second] && indexByPage[first] > indexByPage[second]) {
                incorrect = true;
                break;
            }
        }
        unmarkUpdate(update);
        if (!incorrect) {
            return 0;
        }

        Integer[] ordered = new Integer[update.length];
        for (int i = 0; i < update.length; i++) {
            ordered[i] = update[i];
        }
        java.util.Arrays.sort(ordered, this::comparePages);
        return ordered[ordered.length / 2];
    }

    private int comparePages(int first, int second) {
        if (before[first][second]) {
            return -1;
        }
        if (before[second][first]) {
            return 1;
        }
        return 0;
    }

    private void markUpdate(int[] update) {
        for (int i = 0; i < update.length; i++) {
            int page = update[i];
            present[page] = true;
            indexByPage[page] = i;
        }
    }

    private void unmarkUpdate(int[] update) {
        for (int page : update) {
            present[page] = false;
        }
    }
}

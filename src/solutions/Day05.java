package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day05 extends DayTemplate {
    @Override
    public String solve(boolean part1, Scanner in) {
        Answers answers = solve(parse(in));
        return (part1 ? answers.correct : answers.corrected) + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        Answers answers = solve(parse(in));
        return new String[]{answers.correct + "", answers.corrected + ""};
    }

    private ParsedInput parse(Scanner in) {
        Map<Integer, Integer> ids = new HashMap<>();
        List<Integer> pageValues = new ArrayList<>();
        List<int[]> rules = new ArrayList<>();
        List<int[]> updates = new ArrayList<>();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isBlank()) {
                continue;
            }
            int ruleSeparator = line.indexOf('|');
            if (ruleSeparator >= 0) {
                if (ruleSeparator == 0 || ruleSeparator == line.length() - 1
                        || line.indexOf('|', ruleSeparator + 1) >= 0 || line.indexOf(',') >= 0) {
                    throw new IllegalArgumentException("Invalid ordering rule: " + line);
                }
                int first = pageId(parsePage(line, 0, ruleSeparator), ids, pageValues);
                int second = pageId(parsePage(line, ruleSeparator + 1, line.length()), ids, pageValues);
                rules.add(new int[]{first, second});
            } else {
                updates.add(parseUpdate(line, ids, pageValues));
            }
        }

        int[] values = new int[pageValues.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = pageValues.get(i);
        }
        int[][] ruleArray = rules.toArray(int[][]::new);
        int[] successorCounts = new int[values.length];
        for (int[] rule : ruleArray) {
            successorCounts[rule[0]]++;
        }
        int[][] successors = new int[values.length][];
        for (int i = 0; i < successors.length; i++) {
            successors[i] = new int[successorCounts[i]];
        }
        Arrays.fill(successorCounts, 0);
        for (int[] rule : ruleArray) {
            successors[rule[0]][successorCounts[rule[0]]++] = rule[1];
        }
        return new ParsedInput(values, ruleArray, updates.toArray(int[][]::new), successors);
    }

    private int[] parseUpdate(String line, Map<Integer, Integer> ids, List<Integer> pageValues) {
        int count = 1;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ',') {
                count++;
            }
        }
        int[] update = new int[count];
        int start = 0;
        int output = 0;
        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) == ',') {
                update[output++] = pageId(parsePage(line, start, i), ids, pageValues);
                start = i + 1;
            }
        }
        return update;
    }

    private int parsePage(String line, int start, int end) {
        if (start == end) {
            throw new IllegalArgumentException("Missing page number: " + line);
        }
        return Integer.parseInt(line.substring(start, end).trim());
    }

    private int pageId(int page, Map<Integer, Integer> ids, List<Integer> pageValues) {
        Integer id = ids.get(page);
        if (id != null) {
            return id;
        }
        int next = pageValues.size();
        ids.put(page, next);
        pageValues.add(page);
        return next;
    }

    private Answers solve(ParsedInput input) {
        int[] stamp = new int[input.pageValues.length];
        int[] positions = new int[input.pageValues.length];
        int[] indegree = new int[input.pageValues.length];
        int[] queue = new int[input.pageValues.length];
        int epoch = 0;
        long correct = 0;
        long corrected = 0;

        for (int[] update : input.updates) {
            if (++epoch == 0) {
                Arrays.fill(stamp, 0);
                epoch = 1;
            }
            for (int i = 0; i < update.length; i++) {
                int page = update[i];
                if (stamp[page] == epoch) {
                    throw new IllegalArgumentException("An update contains a page more than once");
                }
                stamp[page] = epoch;
                positions[page] = i;
            }

            boolean ordered = true;
            for (int[] rule : input.rules) {
                if (stamp[rule[0]] == epoch && stamp[rule[1]] == epoch
                        && positions[rule[0]] > positions[rule[1]]) {
                    ordered = false;
                    break;
                }
            }
            if (ordered) {
                correct += input.pageValues[update[update.length / 2]];
            } else {
                corrected += correctedMiddle(update, input, stamp, indegree, queue, epoch);
            }
        }
        return new Answers(correct, corrected);
    }

    private int correctedMiddle(int[] update, ParsedInput input, int[] stamp,
                                int[] indegree, int[] queue, int epoch) {
        for (int page : update) {
            indegree[page] = 0;
        }
        for (int[] rule : input.rules) {
            if (stamp[rule[0]] == epoch && stamp[rule[1]] == epoch) {
                indegree[rule[1]]++;
            }
        }
        int head = 0;
        int tail = 0;
        for (int page : update) {
            if (indegree[page] == 0) {
                queue[tail++] = page;
            }
        }

        int produced = 0;
        int middle = -1;
        while (head < tail) {
            int page = queue[head++];
            if (produced++ == update.length / 2) {
                middle = page;
            }
            for (int successor : input.successors[page]) {
                if (stamp[successor] == epoch && --indegree[successor] == 0) {
                    queue[tail++] = successor;
                }
            }
        }
        if (produced != update.length) {
            throw new IllegalArgumentException("Ordering rules contain a cycle");
        }
        return input.pageValues[middle];
    }

    private record ParsedInput(int[] pageValues, int[][] rules, int[][] updates,
                               int[][] successors) {}

    private record Answers(long correct, long corrected) {}
}

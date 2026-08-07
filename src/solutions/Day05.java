package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class Day05 extends DayTemplate {

    private int[] idKeys;
    private int[] idValues;
    private int[] pageValues;
    private int pageCount;

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
        String raw = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        idKeys = new int[64];
        idValues = new int[64];
        Arrays.fill(idValues, -1);
        pageValues = new int[16];
        pageCount = 0;
        int[][] rules = new int[16][];
        int ruleCount = 0;
        int[][] updates = new int[16][];
        int updateCount = 0;

        int length = raw.length();
        int index = 0;
        while (index < length) {
            int start = index;
            while (index < length && raw.charAt(index) != '\n' && raw.charAt(index) != '\r') {
                index++;
            }
            String line = raw.substring(start, index);
            index = nextLineStart(raw, index);
            if (line.isBlank()) {
                continue;
            }
            int ruleSeparator = line.indexOf('|');
            if (ruleSeparator >= 0) {
                if (ruleSeparator == 0 || ruleSeparator == line.length() - 1
                        || line.indexOf('|', ruleSeparator + 1) >= 0 || line.indexOf(',') >= 0) {
                    throw new IllegalArgumentException("Invalid ordering rule: " + line);
                }
                int first = pageId(parsePage(line, 0, ruleSeparator));
                int second = pageId(parsePage(line, ruleSeparator + 1, line.length()));
                if (ruleCount == rules.length) {
                    rules = Arrays.copyOf(rules, ruleCount * 2);
                }
                rules[ruleCount++] = new int[]{first, second};
            } else {
                if (updateCount == updates.length) {
                    updates = Arrays.copyOf(updates, updateCount * 2);
                }
                updates[updateCount++] = parseUpdate(line);
            }
        }

        int[] values = Arrays.copyOf(pageValues, pageCount);
        int[][] ruleArray = Arrays.copyOf(rules, ruleCount);
        int[][] updateArray = Arrays.copyOf(updates, updateCount);
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
        return new ParsedInput(values, ruleArray, updateArray, successors);
    }

    private int nextLineStart(String raw, int separatorIndex) {
        if (separatorIndex >= raw.length()) {
            return separatorIndex;
        }
        if (raw.charAt(separatorIndex) == '\r' && separatorIndex + 1 < raw.length()
                && raw.charAt(separatorIndex + 1) == '\n') {
            return separatorIndex + 2;
        }
        return separatorIndex + 1;
    }

    private int[] parseUpdate(String line) {
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
                update[output++] = pageId(parsePage(line, start, i));
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

    private int pageId(int page) {
        int mask = idKeys.length - 1;
        int slot = hashPage(page) & mask;
        while (idValues[slot] != -1) {
            if (idKeys[slot] == page) {
                return idValues[slot];
            }
            slot = (slot + 1) & mask;
        }
        int id = pageCount;
        if (pageCount == pageValues.length) {
            pageValues = Arrays.copyOf(pageValues, pageCount * 2);
        }
        pageValues[pageCount++] = page;
        idKeys[slot] = page;
        idValues[slot] = id;
        if (pageCount * 2 > idKeys.length) {
            growIdTable();
        }
        return id;
    }

    private void growIdTable() {
        int[] oldKeys = idKeys;
        int[] oldValues = idValues;
        idKeys = new int[oldKeys.length * 2];
        idValues = new int[idKeys.length];
        Arrays.fill(idValues, -1);
        int mask = idKeys.length - 1;
        for (int i = 0; i < oldKeys.length; i++) {
            if (oldValues[i] == -1) {
                continue;
            }
            int slot = hashPage(oldKeys[i]) & mask;
            while (idValues[slot] != -1) {
                slot = (slot + 1) & mask;
            }
            idKeys[slot] = oldKeys[i];
            idValues[slot] = oldValues[i];
        }
    }

    private static int hashPage(int value) {
        int h = value * 0x9E3779B9;
        return h ^ (h >>> 16);
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

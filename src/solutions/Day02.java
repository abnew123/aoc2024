package src.solutions;

import src.meta.DayTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 2024 Day 02 (Red-Nosed Reports), parse-optimized.
 *
 * <p>Strategy: slurp the whole input through the Scanner once, hand-scan
 * ASCII digits into one reusable {@code int[]} buffer per line (no
 * String.split, no per-line Strings, no boxing), and evaluate both parts in
 * the same pass. The dampener is O(k): if the first violating adjacent pair
 * is (i, i+1), a single removal can only help at index i-1, i, or i+1 —
 * removing any j &le; i-2 or j &ge; i+2 leaves elements i-1, i, i+1 (and the
 * whole prefix) consecutive, so the same first violation (bad difference, or
 * direction conflict with the already-established prefix direction) survives.
 * Each candidate removal is re-checked in O(k), so a report costs at most
 * four O(k) scans.</p>
 */
public class Day02 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long[] counts = countSafeReports(slurp(in));
        return String.valueOf(part1 ? counts[0] : counts[1]);
    }

    @Override
    public String[] fullSolve(Scanner in) {
        long[] counts = countSafeReports(slurp(in));
        return new String[]{String.valueOf(counts[0]), String.valueOf(counts[1])};
    }

    private static String slurp(Scanner in) {
        return in.useDelimiter("\\A").hasNext() ? in.next() : "";
    }

    /** Returns {part1Count, part2Count} over every non-blank line. */
    private static long[] countSafeReports(String input) {
        byte[] text = input.getBytes(StandardCharsets.ISO_8859_1);
        int length = text.length;
        int[] levels = new int[16];
        long part1 = 0;
        long part2 = 0;
        int i = 0;
        while (i < length) {
            int count = 0;
            while (i < length) {
                byte c = text[i];
                if (c == '\n') {
                    i++;
                    break;
                }
                if (c >= '0' && c <= '9') {
                    int value = c - '0';
                    i++;
                    while (i < length && text[i] >= '0' && text[i] <= '9') {
                        value = value * 10 + (text[i] - '0');
                        i++;
                    }
                    if (count == levels.length) {
                        levels = Arrays.copyOf(levels, count * 2);
                    }
                    levels[count++] = value;
                } else {
                    i++;
                }
            }
            if (count == 0) {
                continue;
            }
            int bad = firstBadPair(levels, count);
            if (bad < 0) {
                part1++;
                part2++;
            } else if (safeSkipping(levels, count, bad)
                    || safeSkipping(levels, count, bad + 1)
                    || (bad > 0 && safeSkipping(levels, count, bad - 1))) {
                part2++;
            }
        }
        return new long[]{part1, part2};
    }

    /**
     * Index of the first violating adjacent pair (levels[t], levels[t+1]),
     * or -1 if the report is safe.
     */
    private static int firstBadPair(int[] levels, int count) {
        int direction = 0;
        for (int t = 1; t < count; t++) {
            int difference = levels[t] - levels[t - 1];
            if (difference == 0 || difference > 3 || difference < -3) {
                return t - 1;
            }
            int sign = difference > 0 ? 1 : -1;
            if (direction != 0 && sign != direction) {
                return t - 1;
            }
            direction = sign;
        }
        return -1;
    }

    private static boolean safeSkipping(int[] levels, int count, int skip) {
        int direction = 0;
        int previous = 0;
        boolean hasPrevious = false;
        for (int t = 0; t < count; t++) {
            if (t == skip) {
                continue;
            }
            int current = levels[t];
            if (!hasPrevious) {
                previous = current;
                hasPrevious = true;
                continue;
            }
            int difference = current - previous;
            if (difference == 0 || difference > 3 || difference < -3) {
                return false;
            }
            int sign = difference > 0 ? 1 : -1;
            if (direction != 0 && sign != direction) {
                return false;
            }
            direction = sign;
            previous = current;
        }
        return true;
    }
}

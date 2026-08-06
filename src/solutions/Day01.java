package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class Day01 extends DayTemplate {

    /** Largest magnitude a location ID may reach while still fitting an int. */
    private static final long MAX_MAGNITUDE = 1L << 31;

    @Override
    public String solve(boolean part1, Scanner in) {
        long[] answers = solveBoth(slurp(in));
        return Long.toString(answers[part1 ? 0 : 1]);
    }

    @Override
    public String[] fullSolve(Scanner in) {
        long[] answers = solveBoth(slurp(in));
        return new String[]{Long.toString(answers[0]), Long.toString(answers[1])};
    }

    private static String slurp(Scanner in) {
        return in.useDelimiter("\\A").hasNext() ? in.next() : "";
    }

    private long[] solveBoth(String input) {
        Pairs pairs = parse(input);
        int size = pairs.size;
        int[] left = Arrays.copyOf(pairs.left, size);
        int[] right = Arrays.copyOf(pairs.right, size);
        Arrays.sort(left);
        Arrays.sort(right);

        long distance = 0;
        for (int i = 0; i < size; i++) {
            distance += Math.abs((long) left[i] - right[i]);
        }

        long similarity = 0;
        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < size && rightIndex < size) {
            int leftValue = left[leftIndex];
            int rightValue = right[rightIndex];
            if (leftValue < rightValue) {
                leftIndex++;
            } else if (leftValue > rightValue) {
                rightIndex++;
            } else {
                int leftEnd = leftIndex + 1;
                while (leftEnd < size && left[leftEnd] == leftValue) {
                    leftEnd++;
                }
                int rightEnd = rightIndex + 1;
                while (rightEnd < size && right[rightEnd] == leftValue) {
                    rightEnd++;
                }
                long pairCount = (long) (leftEnd - leftIndex) * (rightEnd - rightIndex);
                similarity += leftValue * pairCount;
                leftIndex = leftEnd;
                rightIndex = rightEnd;
            }
        }
        return new long[]{distance, similarity};
    }

    /**
     * Single linear scan over the raw input. Integer tokens are assigned to the
     * left and right columns alternately in reading order; every non-numeric
     * character is a separator. An optional leading minus sign is honored.
     */
    private Pairs parse(String input) {
        int[] left = new int[1024];
        int[] right = new int[1024];
        int size = 0;
        boolean expectLeft = true;
        int length = input.length();
        int i = 0;
        while (i < length) {
            char c = input.charAt(i);
            boolean negative = false;
            if (c == '-' && i + 1 < length
                    && input.charAt(i + 1) >= '0' && input.charAt(i + 1) <= '9') {
                negative = true;
                i++;
                c = input.charAt(i);
            }
            if (c < '0' || c > '9') {
                i++;
                continue;
            }
            long magnitude = 0;
            while (i < length && (c = input.charAt(i)) >= '0' && c <= '9') {
                magnitude = magnitude * 10 + (c - '0');
                if (magnitude > MAX_MAGNITUDE) {
                    throw new IllegalArgumentException("Location ID exceeds int range");
                }
                i++;
            }
            long value = negative ? -magnitude : magnitude;
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Location ID exceeds int range");
            }
            if (expectLeft) {
                if (size == left.length) {
                    left = Arrays.copyOf(left, size * 2);
                    right = Arrays.copyOf(right, size * 2);
                }
                left[size] = (int) value;
                expectLeft = false;
            } else {
                right[size] = (int) value;
                size++;
                expectLeft = true;
            }
        }
        if (!expectLeft) {
            throw new IllegalArgumentException("Expected location IDs in pairs");
        }
        return new Pairs(left, right, size);
    }

    private record Pairs(int[] left, int[] right, int size) {}
}

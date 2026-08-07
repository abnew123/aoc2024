package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class Day25 extends DayTemplate {

    private static final int WIDTH = 5;
    private static final int HEIGHT = 7;
    private static final int DEPTH_VALUES = HEIGHT - 1;
    private static final int PROFILE_COUNT = 6 * 6 * 6 * 6 * 6;
    private static final String PART_TWO = "Merry Christmas!";

    @Override
    public String[] fullSolve(Scanner in) {
        return new String[]{Long.toString(countFits(in)), PART_TWO};
    }

    @Override
    public String solve(boolean part1, Scanner in) {
        return part1 ? Long.toString(countFits(in)) : PART_TWO;
    }

    private static long countFits(Scanner in) {
        int[] lockFrequency = new int[PROFILE_COUNT];
        int[] keyFrequency = new int[PROFILE_COUNT];
        int[] filled = new int[WIDTH];
        int rows = 0;
        boolean lock = false;

        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = input.length();
        int position = 0;
        while (position < length) {
            int lineStart = position;
            int lineEnd = position;
            while (lineEnd < length
                    && input.charAt(lineEnd) != '\n' && input.charAt(lineEnd) != '\r') {
                lineEnd++;
            }
            position = lineEnd;
            if (position < length && input.charAt(position) == '\r') {
                position++;
            }
            if (position < length && input.charAt(position) == '\n') {
                position++;
            }
            if (lineEnd == lineStart) {
                if (rows > 0) {
                    addProfile(lockFrequency, keyFrequency, filled, rows, lock);
                    Arrays.fill(filled, 0);
                    rows = 0;
                }
                continue;
            }
            if (lineEnd - lineStart != WIDTH || rows >= HEIGHT) {
                throw new IllegalArgumentException("invalid lock or key schematic");
            }
            if (rows == 0) {
                lock = input.charAt(lineStart) == '#';
            }
            for (int col = 0; col < WIDTH; col++) {
                char tile = input.charAt(lineStart + col);
                if (tile == '#') {
                    filled[col]++;
                } else if (tile != '.') {
                    throw new IllegalArgumentException("invalid schematic tile");
                }
            }
            rows++;
        }
        if (rows > 0) {
            addProfile(lockFrequency, keyFrequency, filled, rows, lock);
        }

        int[] compatibleKeys = keyFrequency.clone();
        int stride = 1;
        for (int dimension = 0; dimension < WIDTH; dimension++) {
            for (int profile = 0; profile < PROFILE_COUNT; profile++) {
                if ((profile / stride) % DEPTH_VALUES > 0) {
                    compatibleKeys[profile] += compatibleKeys[profile - stride];
                }
            }
            stride *= DEPTH_VALUES;
        }

        long answer = 0;
        for (int profile = 0; profile < PROFILE_COUNT; profile++) {
            if (lockFrequency[profile] == 0) {
                continue;
            }
            int remaining = profile;
            int complement = 0;
            int place = 1;
            for (int col = 0; col < WIDTH; col++) {
                int depth = remaining % DEPTH_VALUES;
                remaining /= DEPTH_VALUES;
                complement += (HEIGHT - 2 - depth) * place;
                place *= DEPTH_VALUES;
            }
            answer += (long) lockFrequency[profile] * compatibleKeys[complement];
        }
        return answer;
    }

    private static void addProfile(int[] lockFrequency, int[] keyFrequency,
                                   int[] filled, int rows, boolean lock) {
        if (rows != HEIGHT) {
            throw new IllegalArgumentException("incomplete lock or key schematic");
        }
        int profile = 0;
        int place = 1;
        for (int count : filled) {
            int depth = count - 1;
            if (depth < 0 || depth >= DEPTH_VALUES) {
                throw new IllegalArgumentException("invalid lock or key depth");
            }
            profile += depth * place;
            place *= DEPTH_VALUES;
        }
        (lock ? lockFrequency : keyFrequency)[profile]++;
    }
}

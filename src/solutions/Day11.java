package src.solutions;

import src.meta.DayTemplate;

import java.util.Scanner;

public class Day11 extends DayTemplate {

    private static final long[] POW10 = {
            1L,
            10L,
            100L,
            1_000L,
            10_000L,
            100_000L,
            1_000_000L,
            10_000_000L,
            100_000_000L,
            1_000_000_000L,
            10_000_000_000L,
            100_000_000_000L,
            1_000_000_000_000L,
            10_000_000_000_000L,
            100_000_000_000_000L,
            1_000_000_000_000_000L,
            10_000_000_000_000_000L,
            100_000_000_000_000_000L,
            1_000_000_000_000_000_000L
    };

    private long[] memoStones;
    private int[] memoBlinks;
    private long[] memoValues;
    private boolean[] memoUsed;
    private int memoSize;
    private boolean memoFound;

    public String solve(boolean part1, Scanner in) {
        int blinks = part1 ? 25 : 75;
        long answer = 0;
        resetMemo();
        String line = in.nextLine();
        long stone = 0;
        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) == ' ') {
                answer += countStones(stone, blinks);
                stone = 0;
            } else {
                stone = stone * 10 + line.charAt(i) - '0';
            }
        }
        return answer + "";
    }

    private long countStones(long stone, int blinksLeft) {
        if (blinksLeft == 0) {
            return 1;
        }

        long cached = memoGet(stone, blinksLeft);
        if (memoFound) {
            return cached;
        }

        long result;
        if (stone == 0) {
            result = countStones(1, blinksLeft - 1);
        } else {
            int digits = digits(stone);
            if ((digits & 1) == 0) {
                long divisor = POW10[digits / 2];
                result = countStones(stone / divisor, blinksLeft - 1)
                        + countStones(stone % divisor, blinksLeft - 1);
            } else {
                result = countStones(stone * 2024, blinksLeft - 1);
            }
        }

        memoPut(stone, blinksLeft, result);
        return result;
    }

    private void resetMemo() {
        memoStones = new long[1 << 15];
        memoBlinks = new int[memoStones.length];
        memoValues = new long[memoStones.length];
        memoUsed = new boolean[memoStones.length];
        memoSize = 0;
    }

    private long memoGet(long stone, int blinksLeft) {
        int index = memoIndex(stone, blinksLeft, memoStones.length);
        while (memoUsed[index]) {
            if (memoStones[index] == stone && memoBlinks[index] == blinksLeft) {
                memoFound = true;
                return memoValues[index];
            }
            index = (index + 1) & (memoStones.length - 1);
        }
        memoFound = false;
        return 0;
    }

    private void memoPut(long stone, int blinksLeft, long value) {
        if (memoSize * 2 >= memoStones.length) {
            growMemo();
        }
        int index = memoIndex(stone, blinksLeft, memoStones.length);
        while (memoUsed[index]) {
            if (memoStones[index] == stone && memoBlinks[index] == blinksLeft) {
                memoValues[index] = value;
                return;
            }
            index = (index + 1) & (memoStones.length - 1);
        }
        memoUsed[index] = true;
        memoStones[index] = stone;
        memoBlinks[index] = blinksLeft;
        memoValues[index] = value;
        memoSize++;
    }

    private void growMemo() {
        long[] oldStones = memoStones;
        int[] oldBlinks = memoBlinks;
        long[] oldValues = memoValues;
        boolean[] oldUsed = memoUsed;
        memoStones = new long[oldStones.length * 2];
        memoBlinks = new int[memoStones.length];
        memoValues = new long[memoStones.length];
        memoUsed = new boolean[memoStones.length];
        memoSize = 0;
        for (int i = 0; i < oldStones.length; i++) {
            if (oldUsed[i]) {
                memoPut(oldStones[i], oldBlinks[i], oldValues[i]);
            }
        }
    }

    private int memoIndex(long stone, int blinksLeft, int length) {
        long hash = stone ^ (stone >>> 32) ^ ((long) blinksLeft * 0x9E3779B97F4A7C15L);
        hash ^= hash >>> 33;
        hash *= 0xff51afd7ed558ccdL;
        hash ^= hash >>> 33;
        return (int) hash & (length - 1);
    }

    private int digits(long stone) {
        int low = 0;
        int high = POW10.length - 1;
        while (low < high) {
            int mid = (low + high + 1) >>> 1;
            if (stone >= POW10[mid]) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low + 1;
    }
}

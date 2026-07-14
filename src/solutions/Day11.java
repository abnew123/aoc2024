package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public String solve(boolean part1, Scanner in) {
        String[] stones = parse(in);
        return solveOne(stones, part1 ? 25 : 75);
    }

    @Override
    public String[] fullSolve(Scanner in) {
        String[] stones = parse(in);
        try {
            long[] values = parseLongs(stones);
            resetMemo();
            long first = 0;
            long second = 0;
            for (long stone : values) {
                first = Math.addExact(first, countStones(stone, 25));
                second = Math.addExact(second, countStones(stone, 75));
            }
            return new String[]{Long.toString(first), Long.toString(second)};
        } catch (ArithmeticException overflow) {
            return solveExact(stones, true);
        }
    }

    private String solveOne(String[] stones, int blinks) {
        try {
            long[] values = parseLongs(stones);
            resetMemo();
            long answer = 0;
            for (long stone : values) {
                answer = Math.addExact(answer, countStones(stone, blinks));
            }
            return Long.toString(answer);
        } catch (ArithmeticException overflow) {
            return solveExact(stones, blinks == 25)[blinks == 25 ? 0 : 1];
        }
    }

    private String[] parse(Scanner in) {
        List<String> stones = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int start = 0;
            while (start < line.length()) {
                while (start < line.length() && Character.isWhitespace(line.charAt(start))) {
                    start++;
                }
                int end = start;
                while (end < line.length() && !Character.isWhitespace(line.charAt(end))) {
                    end++;
                }
                if (start < end) {
                    String stone = line.substring(start, end);
                    for (int i = 0; i < stone.length(); i++) {
                        if (stone.charAt(i) < '0' || stone.charAt(i) > '9') {
                            throw new IllegalArgumentException("Invalid stone: " + stone);
                        }
                    }
                    stones.add(stone);
                }
                start = end;
            }
        }
        if (stones.isEmpty()) {
            throw new IllegalArgumentException("Missing stones");
        }
        return stones.toArray(String[]::new);
    }

    private long[] parseLongs(String[] stones) {
        long[] values = new long[stones.length];
        for (int i = 0; i < stones.length; i++) {
            try {
                values[i] = Long.parseLong(stones[i]);
            } catch (NumberFormatException tooLarge) {
                throw new ArithmeticException("Stone exceeds long");
            }
        }
        return values;
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
                result = Math.addExact(countStones(stone / divisor, blinksLeft - 1),
                        countStones(stone % divisor, blinksLeft - 1));
            } else {
                result = countStones(Math.multiplyExact(stone, 2024), blinksLeft - 1);
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

    private String[] solveExact(String[] stones, boolean bothParts) {
        Map<ExactState, BigInteger> memo = new HashMap<>();
        BigInteger first = BigInteger.ZERO;
        BigInteger second = BigInteger.ZERO;
        for (String token : stones) {
            BigInteger stone = new BigInteger(token);
            if (bothParts) {
                first = first.add(countStonesExact(stone, 25, memo));
            }
            second = second.add(countStonesExact(stone, 75, memo));
        }
        return new String[]{first.toString(), second.toString()};
    }

    private BigInteger countStonesExact(BigInteger stone, int blinksLeft,
                                        Map<ExactState, BigInteger> memo) {
        if (blinksLeft == 0) {
            return BigInteger.ONE;
        }
        ExactState state = new ExactState(stone, blinksLeft);
        BigInteger cached = memo.get(state);
        if (cached != null) {
            return cached;
        }

        BigInteger result;
        if (stone.signum() == 0) {
            result = countStonesExact(BigInteger.ONE, blinksLeft - 1, memo);
        } else {
            int digits = stone.toString().length();
            if ((digits & 1) == 0) {
                BigInteger divisor = BigInteger.TEN.pow(digits / 2);
                BigInteger[] halves = stone.divideAndRemainder(divisor);
                result = countStonesExact(halves[0], blinksLeft - 1, memo)
                        .add(countStonesExact(halves[1], blinksLeft - 1, memo));
            } else {
                result = countStonesExact(stone.multiply(BigInteger.valueOf(2024)),
                        blinksLeft - 1, memo);
            }
        }
        memo.put(state, result);
        return result;
    }

    private record ExactState(BigInteger stone, int blinksLeft) {}
}

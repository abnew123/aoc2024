package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
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

    private long[] tableKeys;
    private int[] tableIds;
    private int tableSize;
    private long[] stoneValues;
    private int[] firstSuccessor;
    private int[] secondSuccessor;
    private int stoneCount;

    @Override
    public String solve(boolean part1, Scanner in) {
        String[] stones = parse(in);
        try {
            long[] totals = blinkTotals(parseLongs(stones), part1 ? 25 : 75);
            return Long.toString(totals[part1 ? 0 : 1]);
        } catch (ArithmeticException overflow) {
            return solveExact(stones, part1)[part1 ? 0 : 1];
        }
    }

    @Override
    public String[] fullSolve(Scanner in) {
        String[] stones = parse(in);
        try {
            long[] totals = blinkTotals(parseLongs(stones), 75);
            return new String[]{Long.toString(totals[0]), Long.toString(totals[1])};
        } catch (ArithmeticException overflow) {
            return solveExact(stones, true);
        }
    }

    /**
     * Counts stones after 25 and after {@code totalBlinks} blinks.
     *
     * <p>The set of distinct stone values reachable by blinking is finite and
     * small, so it is expanded once to closure: every stone gets a dense int
     * id and precomputed successor ids (one or two per stone). The blinks are
     * then dense count-vector updates over the id space, with no hashing, no
     * boxing and no per-blink allocation.</p>
     */
    private long[] blinkTotals(long[] values, int totalBlinks) {
        buildUniverse(values);
        int count = stoneCount;
        long[] current = new long[count];
        long[] next = new long[count];
        for (long value : values) {
            int id = idFor(value);
            current[id] = Math.addExact(current[id], 1L);
        }
        long first = 0;
        for (int blink = 1; blink <= totalBlinks; blink++) {
            Arrays.fill(next, 0, count, 0L);
            for (int id = 0; id < count; id++) {
                long amount = current[id];
                if (amount == 0) {
                    continue;
                }
                int left = firstSuccessor[id];
                next[left] = Math.addExact(next[left], amount);
                int right = secondSuccessor[id];
                if (right >= 0) {
                    next[right] = Math.addExact(next[right], amount);
                }
            }
            long[] swap = current;
            current = next;
            next = swap;
            if (blink == 25) {
                first = sum(current, count);
            }
        }
        return new long[]{first, sum(current, count)};
    }

    private void buildUniverse(long[] seeds) {
        tableKeys = new long[1 << 12];
        tableIds = new int[tableKeys.length];
        Arrays.fill(tableIds, -1);
        tableSize = 0;
        stoneValues = new long[1 << 10];
        firstSuccessor = new int[stoneValues.length];
        secondSuccessor = new int[stoneValues.length];
        stoneCount = 0;
        for (long seed : seeds) {
            idFor(seed);
        }
        for (int id = 0; id < stoneCount; id++) {
            long stone = stoneValues[id];
            int left;
            int right = -1;
            if (stone == 0) {
                left = idFor(1);
            } else {
                int digits = digits(stone);
                if ((digits & 1) == 0) {
                    long divisor = POW10[digits / 2];
                    left = idFor(stone / divisor);
                    right = idFor(stone % divisor);
                } else {
                    left = idFor(Math.multiplyExact(stone, 2024));
                }
            }
            firstSuccessor[id] = left;
            secondSuccessor[id] = right;
        }
    }

    private int idFor(long stone) {
        int mask = tableKeys.length - 1;
        int index = hashIndex(stone, mask);
        while (tableIds[index] >= 0) {
            if (tableKeys[index] == stone) {
                return tableIds[index];
            }
            index = (index + 1) & mask;
        }
        int id = stoneCount;
        if (id == stoneValues.length) {
            growStones();
        }
        stoneValues[id] = stone;
        stoneCount = id + 1;
        tableKeys[index] = stone;
        tableIds[index] = id;
        tableSize++;
        if (tableSize * 2 >= tableKeys.length) {
            growTable();
        }
        return id;
    }

    private void growStones() {
        stoneValues = Arrays.copyOf(stoneValues, stoneValues.length * 2);
        firstSuccessor = Arrays.copyOf(firstSuccessor, stoneValues.length);
        secondSuccessor = Arrays.copyOf(secondSuccessor, stoneValues.length);
    }

    private void growTable() {
        long[] oldKeys = tableKeys;
        int[] oldIds = tableIds;
        tableKeys = new long[oldKeys.length * 2];
        tableIds = new int[tableKeys.length];
        Arrays.fill(tableIds, -1);
        int mask = tableKeys.length - 1;
        for (int i = 0; i < oldKeys.length; i++) {
            if (oldIds[i] >= 0) {
                int index = hashIndex(oldKeys[i], mask);
                while (tableIds[index] >= 0) {
                    index = (index + 1) & mask;
                }
                tableKeys[index] = oldKeys[i];
                tableIds[index] = oldIds[i];
            }
        }
    }

    private int hashIndex(long stone, int mask) {
        long hash = stone;
        hash ^= hash >>> 33;
        hash *= 0xff51afd7ed558ccdL;
        hash ^= hash >>> 33;
        return (int) hash & mask;
    }

    private long sum(long[] counts, int count) {
        long total = 0;
        for (int id = 0; id < count; id++) {
            total = Math.addExact(total, counts[id]);
        }
        return total;
    }

    private static String slurp(Scanner in) {
        return in.useDelimiter("\\A").hasNext() ? in.next() : "";
    }

    private String[] parse(Scanner in) {
        String input = slurp(in);
        String[] stones = new String[16];
        int count = 0;
        int length = input.length();
        int start = 0;
        while (start < length) {
            while (start < length && Character.isWhitespace(input.charAt(start))) {
                start++;
            }
            int end = start;
            while (end < length && !Character.isWhitespace(input.charAt(end))) {
                end++;
            }
            if (start < end) {
                String stone = input.substring(start, end);
                for (int i = 0; i < stone.length(); i++) {
                    if (stone.charAt(i) < '0' || stone.charAt(i) > '9') {
                        throw new IllegalArgumentException("Invalid stone: " + stone);
                    }
                }
                if (count == stones.length) {
                    stones = Arrays.copyOf(stones, count * 2);
                }
                stones[count++] = stone;
            }
            start = end;
        }
        if (count == 0) {
            throw new IllegalArgumentException("Missing stones");
        }
        return Arrays.copyOf(stones, count);
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

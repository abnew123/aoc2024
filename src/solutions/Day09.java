package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Day09 extends DayTemplate {
    @Override
    public String solve(boolean part1, Scanner in) {
        int[] parts = parse(in);
        return (part1 ? part1(parts) : part2(parts)).toString();
    }

    @Override
    public String[] fullSolve(Scanner in) {
        int[] parts = parse(in);
        return new String[]{part1(parts).toString(), part2(parts).toString()};
    }

    private int[] parse(Scanner in) {
        String raw = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Missing disk map");
        }
        int lineEnd = 0;
        while (lineEnd < raw.length()
                && raw.charAt(lineEnd) != '\n' && raw.charAt(lineEnd) != '\r') {
            lineEnd++;
        }
        String line = raw.substring(0, lineEnd).trim();
        if (line.isEmpty()) {
            throw new IllegalArgumentException("Empty disk map");
        }
        for (int i = lineEnd; i < raw.length(); i++) {
            if (raw.charAt(i) > ' ') {
                throw new IllegalArgumentException("Disk map must be one line");
            }
        }
        int[] parts = new int[line.length()];
        for (int i = 0; i < parts.length; i++) {
            char digit = line.charAt(i);
            if (digit < '0' || digit > '9') {
                throw new IllegalArgumentException("Invalid disk-map digit: " + digit);
            }
            parts[i] = digit - '0';
        }
        return parts;
    }

    private ExactTotal part1(int[] parts) {
        long[] starts = new long[parts.length + 1];
        ExactTotal answer = new ExactTotal();
        for (int run = 0; run < parts.length; run++) {
            starts[run + 1] = starts[run] + parts[run];
            if ((run & 1) == 0) {
                answer.addChecksum(run / 2L, starts[run], parts[run]);
            }
        }

        int leftRun = 1;
        while (leftRun < parts.length && parts[leftRun] == 0) {
            leftRun += 2;
        }
        int rightRun = (parts.length - 1) & ~1;
        while (rightRun >= 0 && parts[rightRun] == 0) {
            rightRun -= 2;
        }
        int leftUsed = 0;
        int rightUsed = 0;
        while (leftRun < parts.length && rightRun >= 0) {
            long leftPosition = starts[leftRun] + leftUsed;
            long rightPosition = starts[rightRun] + parts[rightRun] - 1L - rightUsed;
            if (leftPosition >= rightPosition) {
                break;
            }

            int gapRemaining = parts[leftRun] - leftUsed;
            int fileRemaining = parts[rightRun] - rightUsed;
            long crossingLimit = (rightPosition - leftPosition + 1) / 2;
            int moved = (int) Math.min(Math.min(gapRemaining, fileRemaining), crossingLimit);
            answer.addProduct(rightRun / 2L, moved,
                    leftPosition - rightPosition + moved - 1L);

            leftUsed += moved;
            if (leftUsed == parts[leftRun]) {
                leftRun += 2;
                leftUsed = 0;
                while (leftRun < parts.length && parts[leftRun] == 0) {
                    leftRun += 2;
                }
            }
            rightUsed += moved;
            if (rightUsed == parts[rightRun]) {
                rightRun -= 2;
                rightUsed = 0;
                while (rightRun >= 0 && parts[rightRun] == 0) {
                    rightRun -= 2;
                }
            }
        }
        return answer;
    }

    private ExactTotal part2(int[] parts) {
        int fileCount = (parts.length + 1) / 2;
        long[] fileStarts = new long[fileCount];
        int[] fileSizes = new int[fileCount];
        long[][] gapStarts = new long[10][];
        long[][] gapSizes = new long[10][];
        int[] gapCounts = new int[10];
        for (int i = 0; i < gapStarts.length; i++) {
            gapStarts[i] = new long[16];
            gapSizes[i] = new long[16];
        }

        long index = 0;
        long pendingGapStart = 0;
        long pendingGapSize = 0;
        for (int i = 0; i < parts.length; i++) {
            int size = parts[i];
            if ((i & 1) == 0) {
                if (size > 0 && pendingGapSize > 0) {
                    addGap(gapStarts, gapSizes, gapCounts, pendingGapStart, pendingGapSize);
                    pendingGapSize = 0;
                }
                int file = i / 2;
                fileStarts[file] = index;
                fileSizes[file] = size;
            } else if (size > 0) {
                if (pendingGapSize == 0) {
                    pendingGapStart = index;
                }
                pendingGapSize += size;
            }
            index += size;
        }
        if (pendingGapSize > 0) {
            addGap(gapStarts, gapSizes, gapCounts, pendingGapStart, pendingGapSize);
        }

        ExactTotal answer = new ExactTotal();
        for (int file = fileCount - 1; file >= 0; file--) {
            int fileSize = fileSizes[file];
            long fileStart = fileStarts[file];
            if (fileSize == 0) {
                continue;
            }
            int bestBucket = -1;
            long bestStart = 0;
            for (int size = fileSize; size < gapStarts.length; size++) {
                if (gapCounts[size] > 0) {
                    long start = gapStarts[size][0];
                    if (start < fileStart && (bestBucket < 0 || start < bestStart)) {
                        bestBucket = size;
                        bestStart = start;
                    }
                }
            }

            long finalStart = fileStart;
            if (bestBucket >= 0) {
                long gapStart = gapStarts[bestBucket][0];
                long gapSize = gapSizes[bestBucket][0];
                pollGap(gapStarts[bestBucket], gapSizes[bestBucket], gapCounts, bestBucket);
                finalStart = gapStart;
                long remaining = gapSize - fileSize;
                if (remaining > 0) {
                    addGap(gapStarts, gapSizes, gapCounts, gapStart + fileSize, remaining);
                }
            }
            answer.addChecksum(file, finalStart, fileSize);
        }
        return answer;
    }

    private void addGap(long[][] gapStarts, long[][] gapSizes, int[] gapCounts,
                        long start, long size) {
        int bucket = (int) Math.min(size, 9);
        long[] starts = gapStarts[bucket];
        long[] sizes = gapSizes[bucket];
        int count = gapCounts[bucket];
        if (count == starts.length) {
            starts = Arrays.copyOf(starts, count * 2);
            sizes = Arrays.copyOf(sizes, count * 2);
            gapStarts[bucket] = starts;
            gapSizes[bucket] = sizes;
        }
        starts[count] = start;
        sizes[count] = size;
        gapCounts[bucket] = count + 1;
        int child = count;
        while (child > 0) {
            int parent = (child - 1) / 2;
            if (starts[parent] <= starts[child]) {
                break;
            }
            long swapStart = starts[parent];
            long swapSize = sizes[parent];
            starts[parent] = starts[child];
            sizes[parent] = sizes[child];
            starts[child] = swapStart;
            sizes[child] = swapSize;
            child = parent;
        }
    }

    private void pollGap(long[] starts, long[] sizes, int[] gapCounts, int bucket) {
        int count = gapCounts[bucket] - 1;
        gapCounts[bucket] = count;
        starts[0] = starts[count];
        sizes[0] = sizes[count];
        int parent = 0;
        while (true) {
            int left = 2 * parent + 1;
            if (left >= count) {
                break;
            }
            int smallest = left;
            int right = left + 1;
            if (right < count && starts[right] < starts[left]) {
                smallest = right;
            }
            if (starts[parent] <= starts[smallest]) {
                break;
            }
            long swapStart = starts[parent];
            long swapSize = sizes[parent];
            starts[parent] = starts[smallest];
            sizes[parent] = sizes[smallest];
            starts[smallest] = swapStart;
            sizes[smallest] = swapSize;
            parent = smallest;
        }
    }

    private static final class ExactTotal {
        private long value;
        private BigInteger largeValue;

        private void addChecksum(long fileId, long start, long size) {
            long span = 2 * start + size - 1;
            if ((size & 1) == 0) {
                addProduct(fileId, size / 2, span);
            } else {
                addProduct(fileId, size, span / 2);
            }
        }

        private void addProduct(long first, long second, long third) {
            if (largeValue != null) {
                largeValue = largeValue.add(product(first, second, third));
                return;
            }
            try {
                long amount = Math.multiplyExact(Math.multiplyExact(first, second), third);
                value = Math.addExact(value, amount);
            } catch (ArithmeticException overflow) {
                largeValue = BigInteger.valueOf(value).add(product(first, second, third));
            }
        }

        private BigInteger product(long first, long second, long third) {
            return BigInteger.valueOf(first).multiply(BigInteger.valueOf(second))
                    .multiply(BigInteger.valueOf(third));
        }

        @Override
        public String toString() {
            return largeValue == null ? Long.toString(value) : largeValue.toString();
        }
    }
}

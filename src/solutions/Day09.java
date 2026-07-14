package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.*;

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
        if (!in.hasNextLine()) {
            throw new IllegalArgumentException("Missing disk map");
        }
        String line = in.nextLine().trim();
        if (line.isEmpty()) {
            throw new IllegalArgumentException("Empty disk map");
        }
        while (in.hasNextLine()) {
            if (!in.nextLine().trim().isEmpty()) {
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
        @SuppressWarnings("unchecked")
        PriorityQueue<Gap>[] gapsBySize = new PriorityQueue[10];
        for (int i = 0; i < gapsBySize.length; i++) {
            gapsBySize[i] = new PriorityQueue<>(Comparator.comparingLong(gap -> gap.start));
        }

        long index = 0;
        long pendingGapStart = 0;
        long pendingGapSize = 0;
        for (int i = 0; i < parts.length; i++) {
            int size = parts[i];
            if ((i & 1) == 0) {
                if (size > 0 && pendingGapSize > 0) {
                    addGap(gapsBySize, new Gap(pendingGapStart, pendingGapSize));
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
            addGap(gapsBySize, new Gap(pendingGapStart, pendingGapSize));
        }

        ExactTotal answer = new ExactTotal();
        for (int file = fileCount - 1; file >= 0; file--) {
            int fileSize = fileSizes[file];
            long fileStart = fileStarts[file];
            if (fileSize == 0) {
                continue;
            }
            Gap bestGap = null;
            int bestSize = -1;
            for (int size = fileSize; size < gapsBySize.length; size++) {
                Gap gap = gapsBySize[size].peek();
                if (gap != null && gap.start < fileStart && (bestGap == null || gap.start < bestGap.start)) {
                    bestGap = gap;
                    bestSize = size;
                }
            }

            long finalStart = fileStart;
            if (bestGap != null) {
                gapsBySize[bestSize].poll();
                finalStart = bestGap.start;
                long remaining = bestGap.size - fileSize;
                if (remaining > 0) {
                    addGap(gapsBySize, new Gap(bestGap.start + fileSize, remaining));
                }
            }
            answer.addChecksum(file, finalStart, fileSize);
        }
        return answer;
    }

    private void addGap(PriorityQueue<Gap>[] gapsBySize, Gap gap) {
        gapsBySize[(int) Math.min(gap.size, 9)].add(gap);
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

    private record Gap(long start, long size) {}
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.Scanner;

public class Day10 extends DayTemplate {

    @Override
    public String solve(boolean part1, Scanner in) {
        long[] answers = solve(slurp(in), part1, !part1);
        return Long.toString(answers[part1 ? 0 : 1]);
    }

    @Override
    public String[] fullSolve(Scanner in) {
        long[] answers = solve(slurp(in), true, true);
        return new String[]{Long.toString(answers[0]), Long.toString(answers[1])};
    }

    private static String slurp(Scanner in) {
        return in.useDelimiter("\\A").hasNext() ? in.next() : "";
    }

    private static int countLines(String input) {
        int length = input.length();
        int lines = 0;
        int position = 0;
        while (position < length) {
            while (position < length) {
                char c = input.charAt(position);
                if (c == '\n' || c == '\r') {
                    break;
                }
                position++;
            }
            if (position < length) {
                position = input.charAt(position) == '\r' && position + 1 < length
                        && input.charAt(position + 1) == '\n'
                        ? position + 2 : position + 1;
            }
            lines++;
        }
        return lines;
    }

    private long[] solve(String input, boolean needScores, boolean needRatings) {
        int length = input.length();
        if (length == 0) {
            return new long[2];
        }

        int rows = countLines(input);
        int columns = 0;
        while (columns < length) {
            char c = input.charAt(columns);
            if (c == '\n' || c == '\r') {
                break;
            }
            columns++;
        }
        int cells = Math.multiplyExact(rows, columns);
        byte[] heights = new byte[cells];
        int peakCount = 0;
        int position = 0;
        for (int row = 0; row < rows; row++) {
            int lineStart = position;
            int lineEnd = position;
            while (lineEnd < length) {
                char c = input.charAt(lineEnd);
                if (c == '\n' || c == '\r') {
                    break;
                }
                lineEnd++;
            }
            if (lineEnd < length) {
                position = input.charAt(lineEnd) == '\r' && lineEnd + 1 < length
                        && input.charAt(lineEnd + 1) == '\n'
                        ? lineEnd + 2 : lineEnd + 1;
            } else {
                position = length;
            }
            if (lineEnd - lineStart != columns) {
                throw new IllegalArgumentException("Height map must be rectangular");
            }
            for (int column = 0; column < columns; column++) {
                char height = input.charAt(lineStart + column);
                if (height < '0' || height > '9') {
                    throw new IllegalArgumentException("Invalid height: " + height);
                }
                int index = row * columns + column;
                heights[index] = (byte) (height - '0');
                if (height == '9') {
                    peakCount++;
                }
            }
        }

        int words = (peakCount >>> 6) + ((peakCount & 63) == 0 ? 0 : 1);
        long[] reachable = needScores
                ? new long[Math.multiplyExact(cells, words)]
                : null;
        long[] paths = needRatings ? new long[cells] : null;
        int peakId = 0;
        for (int cell = 0; cell < cells; cell++) {
            if (heights[cell] != 9) {
                continue;
            }
            if (needScores) {
                reachable[cell * words + (peakId >>> 6)] = 1L << (peakId & 63);
            }
            if (needRatings) {
                paths[cell] = 1;
            }
            peakId++;
        }

        for (int height = 8; height >= 0; height--) {
            for (int cell = 0; cell < cells; cell++) {
                if (heights[cell] != height) {
                    continue;
                }
                long pathCount = 0;
                int row = cell / columns;
                int column = cell - row * columns;
                if (row > 0 && heights[cell - columns] == height + 1) {
                    if (needScores) {
                        merge(reachable, cell, cell - columns, words);
                    }
                    if (needRatings) {
                        pathCount += paths[cell - columns];
                    }
                }
                if (row + 1 < rows && heights[cell + columns] == height + 1) {
                    if (needScores) {
                        merge(reachable, cell, cell + columns, words);
                    }
                    if (needRatings) {
                        pathCount += paths[cell + columns];
                    }
                }
                if (column > 0 && heights[cell - 1] == height + 1) {
                    if (needScores) {
                        merge(reachable, cell, cell - 1, words);
                    }
                    if (needRatings) {
                        pathCount += paths[cell - 1];
                    }
                }
                if (column + 1 < columns && heights[cell + 1] == height + 1) {
                    if (needScores) {
                        merge(reachable, cell, cell + 1, words);
                    }
                    if (needRatings) {
                        pathCount += paths[cell + 1];
                    }
                }
                if (needRatings) {
                    paths[cell] = pathCount;
                }
            }
        }

        long score = 0;
        long rating = 0;
        for (int cell = 0; cell < cells; cell++) {
            if (heights[cell] != 0) {
                continue;
            }
            if (needScores) {
                int offset = cell * words;
                for (int word = 0; word < words; word++) {
                    score += Long.bitCount(reachable[offset + word]);
                }
            }
            if (needRatings) {
                rating += paths[cell];
            }
        }
        return new long[]{score, rating};
    }

    private void merge(long[] reachable, int destination, int source, int words) {
        int destinationOffset = destination * words;
        int sourceOffset = source * words;
        for (int word = 0; word < words; word++) {
            reachable[destinationOffset + word] |= reachable[sourceOffset + word];
        }
    }
}

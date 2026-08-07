package src.solutions;

import src.meta.DayTemplate;

import java.util.Scanner;

public class Day12 extends DayTemplate {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    public String solve(boolean part1, Scanner in) {
        long[] prices = prices(in, part1, !part1);
        return (part1 ? prices[0] : prices[1]) + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        long[] prices = prices(in, true, true);
        return new String[]{prices[0] + "", prices[1] + ""};
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

    private long[] prices(Scanner in, boolean needPerimeter, boolean needSides) {
        String input = slurp(in);
        int length = input.length();
        if (length == 0) {
            throw new IllegalArgumentException("Missing garden map");
        }

        int rows = countLines(input);
        int cols = 0;
        while (cols < length) {
            char c = input.charAt(cols);
            if (c == '\n' || c == '\r') {
                break;
            }
            cols++;
        }
        char[] grid = new char[rows * cols];
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
            if (lineEnd - lineStart < cols) {
                throw new IllegalArgumentException("Garden map rows must span the full width");
            }
            for (int col = 0; col < cols; col++) {
                grid[row * cols + col] = input.charAt(lineStart + col);
            }
        }

        boolean[] visited = new boolean[grid.length];
        boolean[] inRegion = new boolean[grid.length];
        int[] queue = new int[grid.length];
        int[] region = new int[grid.length];
        long perimeterPrice = 0;
        long sidePrice = 0;

        for (int start = 0; start < grid.length; start++) {
            if (visited[start]) {
                continue;
            }

            char crop = grid[start];
            int head = 0;
            int tail = 0;
            int regionSize = 0;
            int perimeter = 0;
            visited[start] = true;
            queue[tail++] = start;

            while (head < tail) {
                int current = queue[head++];
                region[regionSize++] = current;
                if (needSides) {
                    inRegion[current] = true;
                }
                int row = current / cols;
                int col = current % cols;

                for (int dir = 0; dir < 4; dir++) {
                    int nextRow = row + DR[dir];
                    int nextCol = col + DC[dir];
                    if (nextRow < 0 || nextCol < 0 || nextRow >= rows || nextCol >= cols) {
                        perimeter++;
                        continue;
                    }

                    int next = nextRow * cols + nextCol;
                    if (grid[next] != crop) {
                        perimeter++;
                    } else if (!visited[next]) {
                        visited[next] = true;
                        queue[tail++] = next;
                    }
                }
            }

            if (needPerimeter) {
                perimeterPrice += (long) regionSize * perimeter;
            }
            if (needSides) {
                sidePrice += (long) regionSize * countSides(region, regionSize, inRegion, rows, cols);
                for (int i = 0; i < regionSize; i++) {
                    inRegion[region[i]] = false;
                }
            }
        }
        return new long[]{perimeterPrice, sidePrice};
    }

    private int countSides(int[] region, int regionSize, boolean[] inRegion, int rows, int cols) {
        int sides = 0;
        for (int i = 0; i < regionSize; i++) {
            int current = region[i];
            int row = current / cols;
            int col = current % cols;

            sides += countCorner(row, col, -1, -1, inRegion, rows, cols);
            sides += countCorner(row, col, -1, 1, inRegion, rows, cols);
            sides += countCorner(row, col, 1, -1, inRegion, rows, cols);
            sides += countCorner(row, col, 1, 1, inRegion, rows, cols);
        }
        return sides;
    }

    private int countCorner(int row, int col, int rowStep, int colStep, boolean[] inRegion, int rows, int cols) {
        boolean rowNeighbor = contains(row + rowStep, col, inRegion, rows, cols);
        boolean colNeighbor = contains(row, col + colStep, inRegion, rows, cols);
        if (!rowNeighbor && !colNeighbor) {
            return 1;
        }
        boolean diagonal = contains(row + rowStep, col + colStep, inRegion, rows, cols);
        return rowNeighbor && colNeighbor && !diagonal ? 1 : 0;
    }

    private boolean contains(int row, int col, boolean[] inRegion, int rows, int cols) {
        return row >= 0 && col >= 0 && row < rows && col < cols && inRegion[row * cols + col];
    }
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day12 extends DayTemplate {

    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[] grid = new char[rows * cols];
        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < cols; col++) {
                grid[row * cols + col] = line.charAt(col);
            }
        }

        boolean[] visited = new boolean[grid.length];
        boolean[] inRegion = new boolean[grid.length];
        int[] queue = new int[grid.length];
        int[] region = new int[grid.length];
        long answer = 0;

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
                inRegion[current] = true;
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

            long price = part1 ? perimeter : countSides(region, regionSize, inRegion, rows, cols);
            answer += regionSize * price;
            for (int i = 0; i < regionSize; i++) {
                inRegion[region[i]] = false;
            }
        }
        return answer + "";
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

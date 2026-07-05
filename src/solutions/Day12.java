package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day12 extends DayTemplate {
    private static final int[] DR = {-1, 1, 0, 0}, DC = {0, 0, -1, 1};
    private int rows, cols;
    private char[] grid;
    private boolean[] seen, region;

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        rows = lines.size();
        cols = lines.get(0).length();
        grid = new char[rows * cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[id(r, c)] = lines.get(r).charAt(c);
            }
        }
        seen = new boolean[grid.length];
        region = new boolean[grid.length];
        int[] q = new int[grid.length], cells = new int[grid.length];
        long answer = 0;
        for (int start = 0; start < grid.length; start++) {
            if (seen[start]) {
                continue;
            }
            int head = 0, tail = 1, n = 0, perimeter = 0;
            char crop = grid[start];
            q[0] = start;
            seen[start] = true;
            while (head < tail) {
                int p = q[head++], r = p / cols, c = p % cols;
                cells[n++] = p;
                region[p] = true;
                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr < 0 || nc < 0 || nr >= rows || nc >= cols || grid[id(nr, nc)] != crop) {
                        perimeter++;
                    } else if (!seen[id(nr, nc)]) {
                        seen[id(nr, nc)] = true;
                        q[tail++] = id(nr, nc);
                    }
                }
            }
            answer += (long) n * (part1 ? perimeter : sides(cells, n));
            for (int i = 0; i < n; i++) {
                region[cells[i]] = false;
            }
        }
        return "" + answer;
    }

    private int sides(int[] cells, int n) {
        int total = 0;
        for (int i = 0; i < n; i++) {
            int r = cells[i] / cols, c = cells[i] % cols;
            for (int dr = -1; dr <= 1; dr += 2) {
                for (int dc = -1; dc <= 1; dc += 2) {
                    boolean a = has(r + dr, c), b = has(r, c + dc);
                    if ((!a && !b) || (a && b && !has(r + dr, c + dc))) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    private boolean has(int r, int c) {
        return r >= 0 && c >= 0 && r < rows && c < cols && region[id(r, c)];
    }

    private int id(int r, int c) {
        return r * cols + c;
    }
}

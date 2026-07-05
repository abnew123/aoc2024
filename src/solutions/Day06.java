package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day06 extends DayTemplate {
    private static final int[] DR = {-1, 0, 1, 0}, DC = {0, 1, 0, -1};
    private int rows, cols, start;
    private boolean[] wall;

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        rows = lines.size();
        cols = lines.get(0).length();
        wall = new boolean[rows * cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                if (ch == '#') {
                    wall[id(r, c)] = true;
                } else if (ch == '^') {
                    start = id(r, c);
                }
            }
        }
        boolean[] path = walk(-1);
        if (part1) {
            int n = 0;
            for (boolean b : path) {
                if (b) {
                    n++;
                }
            }
            return "" + n;
        }
        int loops = 0;
        for (int block = 0; block < path.length; block++) {
            if (block != start && path[block] && walk(block) == null) {
                loops++;
            }
        }
        return "" + loops;
    }

    private boolean[] walk(int block) {
        boolean[] cells = new boolean[rows * cols], states = new boolean[rows * cols * 4];
        int r = start / cols, c = start % cols, d = 0;
        for (;;) {
            int state = id(r, c) * 4 + d;
            if (states[state]) {
                return null;
            }
            states[state] = cells[id(r, c)] = true;
            int nr = r + DR[d], nc = c + DC[d], next = id(nr, nc);
            if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                return cells;
            }
            if (next == block || wall[next]) {
                d = (d + 1) & 3;
            } else {
                r = nr;
                c = nc;
            }
        }
    }

    private int id(int r, int c) {
        return r * cols + c;
    }
}

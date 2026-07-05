package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day20 extends DayTemplate {
    int rows, cols;
    boolean[] wall;

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        rows = lines.size();
        cols = lines.get(0).length();
        wall = new boolean[rows * cols];
        int start = 0, end = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                if (ch == '#') {
                    wall[id(r, c)] = true;
                } else if (ch == 'S') {
                    start = id(r, c);
                } else if (ch == 'E') {
                    end = id(r, c);
                }
            }
        }
        int[] fromStart = bfs(start), toEnd = bfs(end);
        int max = part1 ? 2 : 20, target = toEnd[start] - 100;
        long total = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int a = fromStart[id(r, c)];
                if (a < 0) {
                    continue;
                }
                for (int dr = -max; dr <= max; dr++) {
                    int nr = r + dr, left = max - Math.abs(dr);
                    if (nr < 0 || nr >= rows) {
                        continue;
                    }
                    for (int nc = Math.max(0, c - left); nc <= Math.min(cols - 1, c + left); nc++) {
                        int b = toEnd[id(nr, nc)];
                        if (b >= 0 && a + b + Math.abs(dr) + Math.abs(nc - c) <= target) {
                            total++;
                        }
                    }
                }
            }
        }
        return "" + total;
    }

    int[] bfs(int start) {
        int[] dist = new int[rows * cols], q = new int[dist.length], step = {-cols, cols, -1, 1};
        Arrays.fill(dist, -1);
        dist[start] = 0;
        int head = 0, tail = 1;
        q[0] = start;
        while (head < tail) {
            int p = q[head++], r = p / cols, c = p % cols;
            for (int d : step) {
                int n = p + d;
                if ((d == -1 && c == 0) || (d == 1 && c == cols - 1) || n < 0 || n >= dist.length) {
                    continue;
                }
                if (!wall[n] && dist[n] < 0) {
                    dist[n] = dist[p] + 1;
                    q[tail++] = n;
                }
            }
        }
        return dist;
    }

    int id(int r, int c) {
        return r * cols + c;
    }
}

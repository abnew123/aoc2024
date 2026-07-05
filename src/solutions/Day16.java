package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day16 extends DayTemplate {
    static final int[] DR = {0, 1, 0, -1}, DC = {1, 0, -1, 0};
    int rows, cols, start, end;
    char[][] grid;

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        rows = lines.size();
        cols = lines.get(0).length();
        grid = new char[rows][];
        for (int r = 0; r < rows; r++) {
            grid[r] = lines.get(r).toCharArray();
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'S') {
                    start = cell(r, c);
                } else if (grid[r][c] == 'E') {
                    end = cell(r, c);
                }
            }
        }
        int[] fromStart = dijkstra(false), toEnd = dijkstra(true);
        int best = Integer.MAX_VALUE / 4;
        for (int d = 0; d < 4; d++) {
            best = Math.min(best, fromStart[state(end, d)]);
        }
        if (part1) {
            return "" + best;
        }
        int tiles = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '#') {
                    continue;
                }
                for (int d = 0; d < 4; d++) {
                    int s = state(cell(r, c), d);
                    if (fromStart[s] + toEnd[s] == best) {
                        tiles++;
                        break;
                    }
                }
            }
        }
        return "" + tiles;
    }

    int[] dijkstra(boolean reverse) {
        int inf = Integer.MAX_VALUE / 4;
        int[] dist = new int[rows * cols * 4];
        Arrays.fill(dist, inf);
        PriorityQueue<int[]> q = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        if (reverse) {
            for (int d = 0; d < 4; d++) {
                add(q, dist, 0, state(end, d));
            }
        } else {
            add(q, dist, 0, state(start, 0));
        }
        while (!q.isEmpty()) {
            int[] x = q.poll();
            int score = x[0], s = x[1], d = s & 3, p = s >> 2, r = p / cols, c = p % cols;
            if (score != dist[s]) {
                continue;
            }
            add(q, dist, score + 1000, state(p, (d + 1) & 3));
            add(q, dist, score + 1000, state(p, (d + 3) & 3));
            int nr = r + (reverse ? -DR[d] : DR[d]), nc = c + (reverse ? -DC[d] : DC[d]);
            if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && grid[nr][nc] != '#') {
                add(q, dist, score + 1, state(cell(nr, nc), d));
            }
        }
        return dist;
    }

    void add(PriorityQueue<int[]> q, int[] dist, int score, int state) {
        if (score < dist[state]) {
            dist[state] = score;
            q.add(new int[]{score, state});
        }
    }

    int cell(int r, int c) {
        return r * cols + c;
    }

    int state(int cell, int direction) {
        return cell * 4 + direction;
    }
}

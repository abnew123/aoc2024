package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day18 extends DayTemplate {
    private static final int N = 71;

    public String solve(boolean part1, Scanner in) {
        List<Integer> bytes = new ArrayList<>();
        while (in.hasNextLine()) {
            String[] p = in.nextLine().split(",");
            bytes.add(Integer.parseInt(p[1]) * N + Integer.parseInt(p[0]));
        }
        if (part1) {
            return "" + bfs(bytes, 1024);
        }
        int lo = 0, hi = bytes.size() - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (bfs(bytes, mid) < 0) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        int p = bytes.get(lo - 1);
        return p % N + "," + p / N;
    }

    private int bfs(List<Integer> bytes, int limit) {
        boolean[] bad = new boolean[N * N], seen = new boolean[N * N];
        for (int i = 0; i < limit; i++) {
            bad[bytes.get(i)] = true;
        }
        int[] q = new int[N * N], dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
        int head = 0, tail = 1;
        seen[0] = true;
        for (int steps = 0; head < tail; steps++) {
            for (int end = tail; head < end;) {
                int p = q[head++], r = p / N, c = p % N;
                if (p == N * N - 1) {
                    return steps;
                }
                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d], nc = c + dc[d], n = nr * N + nc;
                    if (nr >= 0 && nc >= 0 && nr < N && nc < N && !bad[n] && !seen[n]) {
                        seen[n] = true;
                        q[tail++] = n;
                    }
                }
            }
        }
        return -1;
    }
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day10 extends DayTemplate {
    private static final int[] DR = {-1, 1, 0, 0}, DC = {0, 0, -1, 1};

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        int rows = lines.size(), cols = lines.get(0).length();
        int[][] paths = new int[rows][cols];
        long answer = 0;
        for (int h = 9; h >= 0; h--) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (lines.get(r).charAt(c) - '0' != h) {
                        continue;
                    }
                    if (h == 9) {
                        paths[r][c] = 1;
                    } else {
                        for (int d = 0; d < 4; d++) {
                            int nr = r + DR[d], nc = c + DC[d];
                            if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && lines.get(nr).charAt(nc) - '0' == h + 1) {
                                paths[r][c] += part1 && h == 0 ? reaches(lines, nr, nc, new boolean[rows][cols]) : paths[nr][nc];
                            }
                        }
                    }
                    if (!part1 && h == 0) {
                        answer += paths[r][c];
                    }
                }
            }
        }
        return "" + (part1 ? scoreHeads(lines) : answer);
    }

    private int scoreHeads(List<String> lines) {
        int answer = 0;
        for (int r = 0; r < lines.size(); r++) {
            for (int c = 0; c < lines.get(0).length(); c++) {
                if (lines.get(r).charAt(c) == '0') {
                    answer += reaches(lines, r, c, new boolean[lines.size()][lines.get(0).length()]);
                }
            }
        }
        return answer;
    }

    private int reaches(List<String> lines, int r, int c, boolean[][] seen) {
        int h = lines.get(r).charAt(c) - '0';
        if (h == 9) {
            return seen[r][c] ? 0 : (seen[r][c] = true) ? 1 : 0;
        }
        int total = 0;
        for (int d = 0; d < 4; d++) {
            int nr = r + DR[d], nc = c + DC[d];
            if (nr >= 0 && nc >= 0 && nr < lines.size() && nc < lines.get(0).length()
                    && lines.get(nr).charAt(nc) - '0' == h + 1) {
                total += reaches(lines, nr, nc, seen);
            }
        }
        return total;
    }
}

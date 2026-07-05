package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day08 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        int rows = lines.size(), cols = lines.get(0).length();
        Map<Character, List<int[]>> antennas = new HashMap<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                if (ch != '.') {
                    antennas.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[]{r, c});
                }
            }
        }
        boolean[][] seen = new boolean[rows][cols];
        for (List<int[]> list : antennas.values()) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    int[] a = list.get(i), b = list.get(j);
                    add(seen, a[0], a[1], a[0] - b[0], a[1] - b[1], part1);
                    add(seen, b[0], b[1], b[0] - a[0], b[1] - a[1], part1);
                }
            }
        }
        int answer = 0;
        for (boolean[] row : seen) {
            for (boolean x : row) {
                if (x) {
                    answer++;
                }
            }
        }
        return "" + answer;
    }

    private void add(boolean[][] seen, int r, int c, int dr, int dc, boolean part1) {
        for (int k = part1 ? 1 : 0; r + k * dr >= 0 && r + k * dr < seen.length
                && c + k * dc >= 0 && c + k * dc < seen[0].length; k++) {
            seen[r + k * dr][c + k * dc] = true;
            if (part1) {
                return;
            }
        }
    }
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day04 extends DayTemplate {
    char[][] grid;

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        int answer = 0, rows = grid.length, cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (part1) {
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            if ((dr != 0 || dc != 0) && word(r, c, dr, dc)) {
                                answer++;
                            }
                        }
                    }
                } else if (r > 0 && c > 0 && r + 1 < rows && c + 1 < cols && grid[r][c] == 'A'
                        && mas(grid[r - 1][c - 1], grid[r + 1][c + 1])
                        && mas(grid[r - 1][c + 1], grid[r + 1][c - 1])) {
                    answer++;
                }
            }
        }
        return "" + answer;
    }

    boolean word(int r, int c, int dr, int dc) {
        String x = "XMAS";
        for (int i = 0; i < x.length(); i++, r += dr, c += dc) {
            if (r < 0 || c < 0 || r == grid.length || c == grid[0].length || grid[r][c] != x.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    boolean mas(char a, char b) {
        return a + b == 'M' + 'S' && a != b;
    }
}

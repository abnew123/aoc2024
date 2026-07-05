package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day15 extends DayTemplate {
    private static final int[] DR = {-1, 1, 0, 0}, DC = {0, 0, -1, 1};
    private char[][] grid;
    private int r, c;

    public String solve(boolean part1, Scanner in) {
        List<String> map = new ArrayList<>();
        StringBuilder moves = new StringBuilder();
        for (boolean readingMoves = false; in.hasNextLine();) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                readingMoves = true;
            } else if (readingMoves) {
                moves.append(line);
            } else {
                map.add(part1 ? line : line.replace("#", "##").replace(".", "..").replace("O", "[]").replace("@", "@."));
            }
        }
        grid = new char[map.size()][];
        for (int i = 0; i < map.size(); i++) {
            grid[i] = map.get(i).toCharArray();
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '@') {
                    r = i;
                    c = j;
                    grid[i][j] = '.';
                }
            }
        }
        for (char move : moves.toString().toCharArray()) {
            step("^v<>".indexOf(move), part1);
        }
        long answer = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'O' || grid[i][j] == '[') {
                    answer += 100L * i + j;
                }
            }
        }
        return "" + answer;
    }

    private void step(int d, boolean part1) {
        int nr = r + DR[d], nc = c + DC[d];
        if (grid[nr][nc] == '#') {
            return;
        }
        if (grid[nr][nc] == '.') {
            r = nr;
            c = nc;
        } else if (part1 || DC[d] != 0) {
            int er = nr, ec = nc;
            while (grid[er][ec] != '#' && grid[er][ec] != '.') {
                er += DR[d];
                ec += DC[d];
            }
            if (grid[er][ec] == '.') {
                while (er != nr || ec != nc) {
                    grid[er][ec] = grid[er - DR[d]][ec - DC[d]];
                    er -= DR[d];
                    ec -= DC[d];
                }
                grid[nr][nc] = '.';
                r = nr;
                c = nc;
            }
        } else if (pushVertical(d)) {
            r = nr;
        }
    }

    private boolean pushVertical(int d) {
        List<Set<Integer>> layers = new ArrayList<>();
        layers.add(boxes(r + DR[d], c));
        for (int row = r + DR[d];;) {
            Set<Integer> next = new HashSet<>();
            row += DR[d];
            for (int col : layers.get(layers.size() - 1)) {
                if (grid[row][col] == '#') {
                    return false;
                }
                if (grid[row][col] == '[' || grid[row][col] == ']') {
                    next.addAll(boxes(row, col));
                }
            }
            if (next.isEmpty()) {
                break;
            }
            layers.add(next);
        }
        for (int i = layers.size() - 1; i >= 0; i--) {
            int row = r + (i + 1) * DR[d];
            for (int col : layers.get(i)) {
                grid[row + DR[d]][col] = grid[row][col];
                grid[row][col] = '.';
            }
        }
        return true;
    }

    private Set<Integer> boxes(int row, int col) {
        Set<Integer> s = new HashSet<>();
        s.add(col);
        s.add(grid[row][col] == '[' ? col + 1 : col - 1);
        return s;
    }
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day06 extends DayTemplate {

    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    private int rows;
    private int cols;
    private int[] loopSeen;
    private int loopStamp = 1;

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        rows = lines.size();
        cols = lines.get(0).length();
        boolean[] walls = new boolean[rows * cols];
        int start = -1;
        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < cols; col++) {
                char c = line.charAt(col);
                int index = row * cols + col;
                if (c == '#') {
                    walls[index] = true;
                } else if (c == '^') {
                    start = index;
                }
            }
        }

        if (part1) {
            return countVisited(start, walls) + "";
        }
        return countLoopObstructions(start, walls) + "";
    }

    private int countVisited(int start, boolean[] walls) {
        boolean[] visited = new boolean[walls.length];
        int count = 0;
        int row = start / cols;
        int col = start % cols;
        int dir = 0;
        visited[start] = true;
        count++;
        while (true) {
            int nextRow = row + DR[dir];
            int nextCol = col + DC[dir];
            if (!inBounds(nextRow, nextCol)) {
                return count;
            }
            int next = nextRow * cols + nextCol;
            if (walls[next]) {
                dir = (dir + 1) & 3;
            } else {
                row = nextRow;
                col = nextCol;
                if (!visited[next]) {
                    visited[next] = true;
                    count++;
                }
            }
        }
    }

    private int countLoopObstructions(int start, boolean[] walls) {
        boolean[] tested = new boolean[walls.length];
        loopSeen = new int[walls.length * 4];
        int loops = 0;
        int row = start / cols;
        int col = start % cols;
        int dir = 0;

        while (true) {
            int nextRow = row + DR[dir];
            int nextCol = col + DC[dir];
            if (!inBounds(nextRow, nextCol)) {
                return loops;
            }

            int next = nextRow * cols + nextCol;
            if (walls[next]) {
                dir = (dir + 1) & 3;
                continue;
            }

            if (next != start && !tested[next]) {
                tested[next] = true;
                if (loopsWithObstacle(row, col, (dir + 1) & 3, next, walls)) {
                    loops++;
                }
            }
            row = nextRow;
            col = nextCol;
        }
    }

    private boolean loopsWithObstacle(int row, int col, int dir, int blocked, boolean[] walls) {
        int stamp = nextLoopStamp();
        while (true) {
            int state = ((row * cols + col) << 2) | dir;
            if (loopSeen[state] == stamp) {
                return true;
            }
            loopSeen[state] = stamp;

            int nextRow = row + DR[dir];
            int nextCol = col + DC[dir];
            if (!inBounds(nextRow, nextCol)) {
                return false;
            }

            int next = nextRow * cols + nextCol;
            if (next == blocked || walls[next]) {
                dir = (dir + 1) & 3;
            } else {
                row = nextRow;
                col = nextCol;
            }
        }
    }

    private int nextLoopStamp() {
        if (loopStamp == Integer.MAX_VALUE) {
            loopSeen = new int[loopSeen.length];
            loopStamp = 1;
        }
        return loopStamp++;
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }
}

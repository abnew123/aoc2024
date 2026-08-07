package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class Day06 extends DayTemplate {

    private static final int[] DR = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    private int rows;
    private int cols;
    private int[] loopSeen;
    private int loopStamp = 1;
    private int[] jumpSeen;
    private int jumpStamp = 1;

    public String solve(boolean part1, Scanner in) {
        Grid grid = parse(in);
        if (part1) {
            return countVisited(grid.start, grid.walls) + "";
        }
        return countVisitedAndLoopObstructions(grid.start, grid.walls)[1] + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        Grid grid = parse(in);
        int[] counts = countVisitedAndJumpObstructions(grid.start, grid.walls);
        return new String[]{counts[0] + "", counts[1] + ""};
    }

    private Grid parse(Scanner in) {
        String raw = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = raw.length();
        int lineCount = 0;
        int firstLineLength = -1;
        int index = 0;
        while (index < length) {
            int lineStart = index;
            while (index < length && raw.charAt(index) != '\n' && raw.charAt(index) != '\r') {
                index++;
            }
            if (firstLineLength < 0) {
                firstLineLength = index - lineStart;
            }
            lineCount++;
            index = nextLineStart(raw, index);
        }

        rows = lineCount;
        cols = firstLineLength;
        boolean[] walls = new boolean[rows * cols];
        int start = -1;
        int row = 0;
        index = 0;
        while (index < length) {
            int lineStart = index;
            while (index < length && raw.charAt(index) != '\n' && raw.charAt(index) != '\r') {
                index++;
            }
            int lineLength = index - lineStart;
            for (int col = 0; col < cols; col++) {
                if (col >= lineLength) {
                    throw new StringIndexOutOfBoundsException(col);
                }
                char c = raw.charAt(lineStart + col);
                int cell = row * cols + col;
                if (c == '#') {
                    walls[cell] = true;
                } else if (c == '^') {
                    start = cell;
                }
            }
            row++;
            index = nextLineStart(raw, index);
        }
        return new Grid(walls, start);
    }

    private int nextLineStart(String raw, int separatorIndex) {
        if (separatorIndex >= raw.length()) {
            return separatorIndex;
        }
        if (raw.charAt(separatorIndex) == '\r' && separatorIndex + 1 < raw.length()
                && raw.charAt(separatorIndex + 1) == '\n') {
            return separatorIndex + 2;
        }
        return separatorIndex + 1;
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

    private int[] countVisitedAndLoopObstructions(int start, boolean[] walls) {
        boolean[] tested = new boolean[walls.length];
        loopSeen = new int[walls.length * 4];
        int visited = 1;
        int loops = 0;
        int row = start / cols;
        int col = start % cols;
        int dir = 0;

        while (true) {
            int nextRow = row + DR[dir];
            int nextCol = col + DC[dir];
            if (!inBounds(nextRow, nextCol)) {
                return new int[]{visited, loops};
            }

            int next = nextRow * cols + nextCol;
            if (walls[next]) {
                dir = (dir + 1) & 3;
                continue;
            }

            if (next != start && !tested[next]) {
                tested[next] = true;
                visited++;
                if (loopsWithObstacle(row, col, (dir + 1) & 3, next, walls)) {
                    loops++;
                }
            }
            row = nextRow;
            col = nextCol;
        }
    }

    private int[] countVisitedAndJumpObstructions(int start, boolean[] walls) {
        RayGraph graph = buildRayGraph(walls);
        boolean[] tested = new boolean[walls.length];
        jumpSeen = new int[walls.length * 4];
        int visited = 1;
        int loops = 0;
        int row = start / cols;
        int col = start % cols;
        int dir = 0;

        while (true) {
            int nextRow = row + DR[dir];
            int nextCol = col + DC[dir];
            if (!inBounds(nextRow, nextCol)) {
                return new int[]{visited, loops};
            }
            int next = nextRow * cols + nextCol;
            if (walls[next]) {
                dir = (dir + 1) & 3;
                continue;
            }
            if (next != start && !tested[next]) {
                tested[next] = true;
                visited++;
                if (loopsWithJumpObstacle(row * cols + col, (dir + 1) & 3, next, graph)) {
                    loops++;
                }
            }
            row = nextRow;
            col = nextCol;
        }
    }

    private RayGraph buildRayGraph(boolean[] walls) {
        int[] rayEnd = new int[walls.length * 4];
        int[] baseNext = new int[walls.length * 4];
        Arrays.fill(baseNext, -1);

        for (int row = 0; row < rows; row++) {
            int endCol = 0;
            boolean exits = true;
            for (int col = 0; col < cols; col++) {
                int cell = row * cols + col;
                if (walls[cell]) {
                    endCol = col + 1;
                    exits = false;
                } else {
                    int end = row * cols + endCol;
                    int state = (cell << 2) | 3;
                    rayEnd[state] = end;
                    if (!exits) baseNext[state] = (end << 2);
                }
            }

            endCol = cols - 1;
            exits = true;
            for (int col = cols - 1; col >= 0; col--) {
                int cell = row * cols + col;
                if (walls[cell]) {
                    endCol = col - 1;
                    exits = false;
                } else {
                    int end = row * cols + endCol;
                    int state = (cell << 2) | 1;
                    rayEnd[state] = end;
                    if (!exits) baseNext[state] = (end << 2) | 2;
                }
            }
        }

        for (int col = 0; col < cols; col++) {
            int endRow = 0;
            boolean exits = true;
            for (int row = 0; row < rows; row++) {
                int cell = row * cols + col;
                if (walls[cell]) {
                    endRow = row + 1;
                    exits = false;
                } else {
                    int end = endRow * cols + col;
                    int state = cell << 2;
                    rayEnd[state] = end;
                    if (!exits) baseNext[state] = (end << 2) | 1;
                }
            }

            endRow = rows - 1;
            exits = true;
            for (int row = rows - 1; row >= 0; row--) {
                int cell = row * cols + col;
                if (walls[cell]) {
                    endRow = row - 1;
                    exits = false;
                } else {
                    int end = endRow * cols + col;
                    int state = (cell << 2) | 2;
                    rayEnd[state] = end;
                    if (!exits) baseNext[state] = (end << 2) | 3;
                }
            }
        }
        return new RayGraph(rayEnd, baseNext);
    }

    private boolean loopsWithJumpObstacle(int cell, int dir, int blocked, RayGraph graph) {
        int stamp = nextJumpStamp();
        int blockedRow = blocked / cols;
        int blockedCol = blocked % cols;
        int state = (cell << 2) | dir;
        while (state >= 0) {
            if (jumpSeen[state] == stamp) return true;
            jumpSeen[state] = stamp;
            cell = state >>> 2;
            dir = state & 3;
            int end = graph.rayEnd[state];
            int row = cell / cols;
            int col = cell % cols;
            boolean intercepted = switch (dir) {
                case 0 -> col == blockedCol && blocked < cell && blocked >= end;
                case 1 -> row == blockedRow && blocked > cell && blocked <= end;
                case 2 -> col == blockedCol && blocked > cell && blocked <= end;
                default -> row == blockedRow && blocked < cell && blocked >= end;
            };
            if (intercepted) {
                int step = dir == 0 ? -cols : dir == 1 ? 1 : dir == 2 ? cols : -1;
                state = ((blocked - step) << 2) | ((dir + 1) & 3);
            } else {
                state = graph.baseNext[state];
            }
        }
        return false;
    }

    private int nextJumpStamp() {
        if (jumpStamp == Integer.MAX_VALUE) {
            jumpSeen = new int[jumpSeen.length];
            jumpStamp = 1;
        }
        return jumpStamp++;
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

    private static class Grid {
        private final boolean[] walls;
        private final int start;

        private Grid(boolean[] walls, int start) {
            this.walls = walls;
            this.start = start;
        }
    }

    private record RayGraph(int[] rayEnd, int[] baseNext) {
    }
}

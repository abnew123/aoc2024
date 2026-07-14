package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class Day18 extends DayTemplate {

    private static final int GRID_SIZE = 71;
    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    @Override
    public String solve(boolean part1, Scanner in) {
        return solveForGrid(part1, in, GRID_SIZE, 1024);
    }

    @Override
    public String[] fullSolve(Scanner in) {
        return fullSolveForGrid(in, GRID_SIZE, 1024);
    }

    String solveForGrid(boolean part1, Scanner in, int gridSize, int part1Limit) {
        int[] bytes = parse(in, gridSize);
        Pathfinder pathfinder = new Pathfinder(bytes, gridSize);
        return part1 ? part1(bytes, pathfinder, part1Limit) : part2(bytes, pathfinder);
    }

    String[] fullSolveForGrid(Scanner in, int gridSize, int part1Limit) {
        int[] bytes = parse(in, gridSize);
        Pathfinder pathfinder = new Pathfinder(bytes, gridSize);
        return new String[]{part1(bytes, pathfinder, part1Limit), part2(bytes, pathfinder)};
    }

    private int[] parse(Scanner in, int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException("Grid size must be positive");
        }
        int[] bytes = new int[128];
        int size = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int comma = line.indexOf(',');
            if (comma <= 0 || comma == line.length() - 1 || line.indexOf(',', comma + 1) >= 0) {
                throw new IllegalArgumentException("Invalid byte coordinate: " + line);
            }
            int x = Integer.parseInt(line.substring(0, comma));
            int y = Integer.parseInt(line.substring(comma + 1));
            if (x < 0 || x >= gridSize || y < 0 || y >= gridSize) {
                throw new IllegalArgumentException("Byte coordinate outside grid: " + line);
            }
            if (size == bytes.length) {
                bytes = Arrays.copyOf(bytes, size * 2);
            }
            bytes[size++] = y * gridSize + x;
        }
        return Arrays.copyOf(bytes, size);
    }

    private String part1(int[] bytes, Pathfinder pathfinder, int limit) {
        if (limit < 0 || limit > bytes.length) {
            throw new IllegalArgumentException("Not enough bytes for part 1");
        }
        return pathfinder.bfs(limit) + "";
    }

    private String part2(int[] bytes, Pathfinder pathfinder) {
        int high = bytes.length;
        int low = 0;
        if (pathfinder.bfs(high) != -1) {
            throw new IllegalArgumentException("No byte blocks the exit");
        }
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (pathfinder.bfs(mid) == -1) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        int answer = bytes[low - 1];
        return (answer % pathfinder.gridSize) + "," + (answer / pathfinder.gridSize);
    }

    private static final class Pathfinder {
        private final int gridSize;
        private final int[] blockedAt;
        private final int[] seen;
        private final int[] queue;
        private int epoch;

        private Pathfinder(int[] bytes, int gridSize) {
            this.gridSize = gridSize;
            blockedAt = new int[gridSize * gridSize];
            Arrays.fill(blockedAt, Integer.MAX_VALUE);
            for (int i = 0; i < bytes.length; i++) {
                if (blockedAt[bytes[i]] == Integer.MAX_VALUE) {
                    blockedAt[bytes[i]] = i + 1;
                }
            }
            seen = new int[blockedAt.length];
            queue = new int[blockedAt.length];
        }

        private int bfs(int limit) {
            int target = blockedAt.length - 1;
            if (blockedAt[0] <= limit || blockedAt[target] <= limit) {
                return -1;
            }
            if (++epoch == 0) {
                Arrays.fill(seen, 0);
                epoch = 1;
            }
            int head = 0;
            int tail = 0;
            queue[tail++] = 0;
            seen[0] = epoch;
            int steps = 0;

            while (head < tail) {
                int layerEnd = tail;
                while (head < layerEnd) {
                    int current = queue[head++];
                    if (current == target) {
                        return steps;
                    }

                    int row = current / gridSize;
                    int col = current % gridSize;
                    for (int dir = 0; dir < 4; dir++) {
                        int nextRow = row + DR[dir];
                        int nextCol = col + DC[dir];
                        if (nextRow < 0 || nextCol < 0 || nextRow >= gridSize || nextCol >= gridSize) {
                            continue;
                        }
                        int next = nextRow * gridSize + nextCol;
                        if (blockedAt[next] > limit && seen[next] != epoch) {
                            seen[next] = epoch;
                            queue[tail++] = next;
                        }
                    }
                }
                steps++;
            }
            return -1;
        }
    }
}

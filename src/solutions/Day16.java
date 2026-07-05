package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day16 extends DayTemplate {

    private static final int INF = Integer.MAX_VALUE / 4;
    private static final int TURN_COST = 1000;
    private static final int[] DR = {0, 1, 0, -1};
    private static final int[] DC = {1, 0, -1, 0};

    private int rows;
    private int cols;
    private boolean[] walls;

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        rows = lines.size();
        cols = lines.get(0).length();
        walls = new boolean[rows * cols];
        int start = -1;
        int exit = -1;
        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);
            for (int col = 0; col < cols; col++) {
                char c = line.charAt(col);
                int cell = row * cols + col;
                if (c == '#') {
                    walls[cell] = true;
                } else if (c == 'S') {
                    start = cell;
                } else if (c == 'E') {
                    exit = cell;
                }
            }
        }

        int[] fromStart = distancesFromStart(start);
        int bestScore = bestExitScore(fromStart, exit);
        if (part1) {
            return bestScore + "";
        }

        int[] toExit = distancesToExit(exit);
        return countBestPathTiles(fromStart, toExit, bestScore) + "";
    }

    private int[] distancesFromStart(int start) {
        int[] distances = emptyDistances();
        LongHeap heap = new LongHeap();
        distances[state(start, 0)] = 0;
        heap.add(0, state(start, 0));
        while (!heap.isEmpty()) {
            long entry = heap.poll();
            int score = score(entry);
            int state = state(entry);
            if (score != distances[state]) {
                continue;
            }
            addMove(distances, heap, state, score, true);
            addTurn(distances, heap, state, score, (direction(state) + 1) & 3);
            addTurn(distances, heap, state, score, (direction(state) + 3) & 3);
        }
        return distances;
    }

    private int[] distancesToExit(int exit) {
        int[] distances = emptyDistances();
        LongHeap heap = new LongHeap();
        for (int direction = 0; direction < 4; direction++) {
            int state = state(exit, direction);
            distances[state] = 0;
            heap.add(0, state);
        }
        while (!heap.isEmpty()) {
            long entry = heap.poll();
            int score = score(entry);
            int state = state(entry);
            if (score != distances[state]) {
                continue;
            }
            addMove(distances, heap, state, score, false);
            addTurn(distances, heap, state, score, (direction(state) + 1) & 3);
            addTurn(distances, heap, state, score, (direction(state) + 3) & 3);
        }
        return distances;
    }

    private int[] emptyDistances() {
        int[] distances = new int[rows * cols * 4];
        Arrays.fill(distances, INF);
        return distances;
    }

    private void addMove(int[] distances, LongHeap heap, int state, int score, boolean forward) {
        int direction = direction(state);
        int cell = cell(state);
        int row = cell / cols;
        int col = cell % cols;
        int nextRow = forward ? row + DR[direction] : row - DR[direction];
        int nextCol = forward ? col + DC[direction] : col - DC[direction];
        if (nextRow < 0 || nextCol < 0 || nextRow >= rows || nextCol >= cols) {
            return;
        }
        int nextCell = nextRow * cols + nextCol;
        if (!walls[nextCell]) {
            updateDistance(distances, heap, score + 1, state(nextCell, direction));
        }
    }

    private void addTurn(int[] distances, LongHeap heap, int state, int score, int newDirection) {
        updateDistance(distances, heap, score + TURN_COST, state(cell(state), newDirection));
    }

    private void updateDistance(int[] distances, LongHeap heap, int score, int state) {
        if (score < distances[state]) {
            distances[state] = score;
            heap.add(score, state);
        }
    }

    private int bestExitScore(int[] distances, int exit) {
        int best = INF;
        for (int direction = 0; direction < 4; direction++) {
            best = Math.min(best, distances[state(exit, direction)]);
        }
        return best;
    }

    private long countBestPathTiles(int[] fromStart, int[] toExit, int bestScore) {
        long total = 0;
        for (int cell = 0; cell < walls.length; cell++) {
            if (walls[cell]) {
                continue;
            }
            for (int direction = 0; direction < 4; direction++) {
                int state = state(cell, direction);
                if (fromStart[state] + toExit[state] == bestScore) {
                    total++;
                    break;
                }
            }
        }
        return total;
    }

    private int state(int cell, int direction) {
        return (cell << 2) | direction;
    }

    private int cell(int state) {
        return state >> 2;
    }

    private int direction(int state) {
        return state & 3;
    }

    private int score(long entry) {
        return (int) (entry >>> 32);
    }

    private int state(long entry) {
        return (int) entry;
    }

    private static final class LongHeap {
        private long[] heap = new long[1024];
        private int size;

        boolean isEmpty() {
            return size == 0;
        }

        void add(int score, int state) {
            if (size == heap.length) {
                heap = Arrays.copyOf(heap, heap.length * 2);
            }
            long entry = ((long) score << 32) | (state & 0xffffffffL);
            int index = size++;
            while (index > 0) {
                int parent = (index - 1) >>> 1;
                if (heap[parent] <= entry) {
                    break;
                }
                heap[index] = heap[parent];
                index = parent;
            }
            heap[index] = entry;
        }

        long poll() {
            long result = heap[0];
            long replacement = heap[--size];
            int index = 0;
            while (true) {
                int child = index * 2 + 1;
                if (child >= size) {
                    break;
                }
                int right = child + 1;
                if (right < size && heap[right] < heap[child]) {
                    child = right;
                }
                if (heap[child] >= replacement) {
                    break;
                }
                heap[index] = heap[child];
                index = child;
            }
            heap[index] = replacement;
            return result;
        }
    }
}

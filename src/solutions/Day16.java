package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day16 extends DayTemplate {

    private static final int WALL = 2;
    private static final long INF = Long.MAX_VALUE / 4;
    private static final int TURN_COST = 1000;
    private static final int[] xs = new int[]{0, 1, 0, -1};
    private static final int[] ys = new int[]{1, 0, -1, 0};

    public String solve(boolean part1, Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            lines.add(line);
        }
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        Coordinate reindeer = new Coordinate(-1,-1);
        Coordinate exit = new Coordinate(-1,-1);

        for(int i = 0 ; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                char c = lines.get(i).charAt(j);
                if(c == 'S'){
                    reindeer = new Coordinate(i,j);
                    grid[i][j] = 1;
                }
                if(c == 'E'){
                    exit = new Coordinate(i,j);
                    grid[i][j] = 1;
                }
                if(c == '#'){
                    grid[i][j] = WALL;
                }
                if(c == '.'){
                    grid[i][j] = 1;
                }
            }
        }

        long[][][] fromStart = distancesFromStart(grid, reindeer);
        long bestScore = bestExitScore(fromStart, exit);
        if (part1) {
            return bestScore + "";
        }

        long[][][] toExit = distancesToExit(grid, exit);
        return countBestPathTiles(grid, fromStart, toExit, bestScore) + "";
    }

    private long[][][] distancesFromStart(int[][] grid, Coordinate start) {
        long[][][] distances = emptyDistances(grid);
        distances[start.x][start.y][0] = 0;
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingLong(State::score));
        queue.add(new State(start.x, start.y, 0, 0));
        while (!queue.isEmpty()) {
            State current = queue.poll();
            if (current.score() != distances[current.x()][current.y()][current.direction()]) {
                continue;
            }
            addForwardMove(grid, distances, queue, current);
            addTurn(distances, queue, current, (current.direction() + 1) % 4);
            addTurn(distances, queue, current, (current.direction() + 3) % 4);
        }
        return distances;
    }

    private long[][][] distancesToExit(int[][] grid, Coordinate exit) {
        long[][][] distances = emptyDistances(grid);
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingLong(State::score));
        for (int direction = 0; direction < 4; direction++) {
            distances[exit.x][exit.y][direction] = 0;
            queue.add(new State(exit.x, exit.y, direction, 0));
        }
        while (!queue.isEmpty()) {
            State current = queue.poll();
            if (current.score() != distances[current.x()][current.y()][current.direction()]) {
                continue;
            }
            addBackwardMove(grid, distances, queue, current);
            addTurn(distances, queue, current, (current.direction() + 1) % 4);
            addTurn(distances, queue, current, (current.direction() + 3) % 4);
        }
        return distances;
    }

    private long[][][] emptyDistances(int[][] grid) {
        long[][][] distances = new long[grid.length][grid[0].length][4];
        for (long[][] row : distances) {
            for (long[] cell : row) {
                Arrays.fill(cell, INF);
            }
        }
        return distances;
    }

    private void addForwardMove(int[][] grid, long[][][] distances, PriorityQueue<State> queue, State current) {
        int newX = current.x() + xs[current.direction()];
        int newY = current.y() + ys[current.direction()];
        addMove(grid, distances, queue, current, newX, newY, 1);
    }

    private void addBackwardMove(int[][] grid, long[][][] distances, PriorityQueue<State> queue, State current) {
        int newX = current.x() - xs[current.direction()];
        int newY = current.y() - ys[current.direction()];
        addMove(grid, distances, queue, current, newX, newY, 1);
    }

    private void addMove(int[][] grid, long[][][] distances, PriorityQueue<State> queue, State current, int newX, int newY, int cost) {
        if (safe(newX, newY, grid) && grid[newX][newY] != WALL) {
            updateDistance(distances, queue, newX, newY, current.direction(), current.score() + cost);
        }
    }

    private void addTurn(long[][][] distances, PriorityQueue<State> queue, State current, int newDirection) {
        updateDistance(distances, queue, current.x(), current.y(), newDirection, current.score() + TURN_COST);
    }

    private void updateDistance(long[][][] distances, PriorityQueue<State> queue, int x, int y, int direction, long score) {
        if (score < distances[x][y][direction]) {
            distances[x][y][direction] = score;
            queue.add(new State(x, y, direction, score));
        }
    }

    private long bestExitScore(long[][][] distances, Coordinate exit) {
        long best = INF;
        for (int direction = 0; direction < 4; direction++) {
            best = Math.min(best, distances[exit.x][exit.y][direction]);
        }
        return best;
    }

    private long countBestPathTiles(int[][] grid, long[][][] fromStart, long[][][] toExit, long bestScore) {
        long total = 0;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[x][y] == WALL) {
                    continue;
                }
                for (int direction = 0; direction < 4; direction++) {
                    if (fromStart[x][y][direction] + toExit[x][y][direction] == bestScore) {
                        total++;
                        break;
                    }
                }
            }
        }
        return total;
    }

    private record State(int x, int y, int direction, long score) {}

}

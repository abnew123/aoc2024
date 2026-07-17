package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

public class Day15 extends DayTemplate {

    private static final int[] XS = {1, -1, 0, 0};
    private static final int[] YS = {0, 0, 1, -1};

    @Override
    public String solve(boolean part1, Scanner in) {
        ParsedInput input = parse(in);
        return run(input, !part1) + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        ParsedInput input = parse(in);
        return new String[]{run(input, false) + "", run(input, true) + ""};
    }

    private ParsedInput parse(Scanner in) {
        List<String> lines = new ArrayList<>();
        StringBuilder movements = new StringBuilder();
        boolean movement = false;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                movement = true;
                continue;
            }
            if (!movement) {
                lines.add(line);
            } else {
                movements.append(line);
            }
        }
        if (lines.isEmpty() || !movement) {
            throw new IllegalArgumentException("Missing warehouse map or movement separator");
        }

        int width = lines.get(0).length();
        int robots = 0;
        for (String line : lines) {
            if (line.length() != width) {
                throw new IllegalArgumentException("Warehouse map must be rectangular");
            }
            for (int column = 0; column < width; column++) {
                char cell = line.charAt(column);
                if (cell == '@') {
                    robots++;
                } else if (cell != '#' && cell != '.' && cell != 'O') {
                    throw new IllegalArgumentException("Invalid warehouse cell: " + cell);
                }
            }
        }
        if (width == 0 || robots != 1) {
            throw new IllegalArgumentException("Warehouse must contain exactly one robot");
        }
        for (int i = 0; i < movements.length(); i++) {
            char move = movements.charAt(i);
            if (move != '^' && move != 'v' && move != '<' && move != '>') {
                throw new IllegalArgumentException("Invalid movement: " + move);
            }
        }
        return new ParsedInput(lines.toArray(String[]::new), movements.toString().toCharArray());
    }

    private long run(ParsedInput input, boolean wide) {
        PreparedWarehouse warehouse = prepare(input.map, wide);
        for (char move : input.moves) {
            oneCycle(move, warehouse, !wide);
        }

        long answer = 0;
        for (int i = 0; i < warehouse.grid.length; i++) {
            for (int j = 0; j < warehouse.grid[0].length; j++) {
                int cell = warehouse.grid[i][j];
                if (cell == 2 || cell == 3) {
                    answer += 100L * i + j;
                }
            }
        }
        return answer;
    }

    private PreparedWarehouse prepare(String[] map, boolean wide) {
        int scale = wide ? 2 : 1;
        int[][] grid = new int[map.length][map[0].length() * scale];
        Coordinate robot = null;
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length(); column++) {
                char cell = map[row].charAt(column);
                int output = column * scale;
                if (!wide) {
                    grid[row][output] = switch (cell) {
                        case '#' -> 1000;
                        case 'O' -> 2;
                        case '.', '@' -> 1;
                        default -> throw new IllegalStateException();
                    };
                    if (cell == '@') {
                        robot = new Coordinate(row, output);
                    }
                } else {
                    switch (cell) {
                        case '#' -> grid[row][output] = grid[row][output + 1] = 1000;
                        case 'O' -> {
                            grid[row][output] = 3;
                            grid[row][output + 1] = 4;
                        }
                        case '.', '@' -> {
                            grid[row][output] = grid[row][output + 1] = 1;
                            if (cell == '@') {
                                robot = new Coordinate(row, output);
                            }
                        }
                        default -> throw new IllegalStateException();
                    }
                }
            }
        }
        return new PreparedWarehouse(grid, robot, wide);
    }

    private void oneCycle(char c, PreparedWarehouse warehouse, boolean part1){
        Coordinate robot = warehouse.robot;
        int[][] grid = warehouse.grid;
        int direction = -1;
        if(c == '^'){
            direction = 1;
        }
        if(c == 'v'){
            direction = 0;
        }
        if(c == '>'){
            direction = 2;
        }
        if(c == '<'){
            direction = 3;
        }
        int goalRow = robot.x + XS[direction];
        int goalColumn = robot.y + YS[direction];
        if(grid[goalRow][goalColumn] == 1){
            robot.x = goalRow;
            robot.y = goalColumn;
            return;
        }
        if(grid[goalRow][goalColumn] == 1000){
            return;
        }
        if(direction == 2 || direction == 3 || part1){
            int counter = 0;
            while(true){
                counter++;
                goalRow += XS[direction];
                goalColumn += YS[direction];
                if(grid[goalRow][goalColumn] == 1000){
                    return;
                }
                if(grid[goalRow][goalColumn] == 1){
                    break;
                }
            }
            while(counter-- > 0){
                grid[goalRow][goalColumn] = grid[goalRow - XS[direction]][goalColumn - YS[direction]];
                goalRow -= XS[direction];
                goalColumn -= YS[direction];
            }
            robot.x += XS[direction];
            robot.y += YS[direction];
            grid[robot.x][robot.y] = 1;
        }
        else{
            int stamp = warehouse.nextStamp();
            int queueSize = enqueueBox(warehouse, goalRow, goalColumn, stamp, 0);
            int width = grid[0].length;
            for (int head = 0; head < queueSize; head++) {
                int box = warehouse.boxQueue[head];
                int row = box / width;
                int column = box % width;
                int nextRow = row + XS[direction];
                if (grid[nextRow][column] == 1000 || grid[nextRow][column + 1] == 1000) {
                    return;
                }
                queueSize = enqueueBox(warehouse, nextRow, column, stamp, queueSize);
                queueSize = enqueueBox(warehouse, nextRow, column + 1, stamp, queueSize);
            }
            for (int index = queueSize - 1; index >= 0; index--) {
                int box = warehouse.boxQueue[index];
                int row = box / width;
                int column = box % width;
                int nextRow = row + XS[direction];
                grid[row][column] = grid[row][column + 1] = 1;
                grid[nextRow][column] = 3;
                grid[nextRow][column + 1] = 4;
            }
            robot.x += XS[direction];
            robot.y += YS[direction];
        }
    }

    private int enqueueBox(PreparedWarehouse warehouse, int row, int column, int stamp, int queueSize) {
        int cell = warehouse.grid[row][column];
        if (cell != 3 && cell != 4) {
            return queueSize;
        }
        int leftColumn = cell == 3 ? column : column - 1;
        int box = row * warehouse.grid[0].length + leftColumn;
        if (warehouse.seenStamp[box] != stamp) {
            warehouse.seenStamp[box] = stamp;
            warehouse.boxQueue[queueSize++] = box;
        }
        return queueSize;
    }

    private record ParsedInput(String[] map, char[] moves) {}

    private static final class PreparedWarehouse {
        final int[][] grid;
        final Coordinate robot;
        final int[] boxQueue;
        final int[] seenStamp;
        int stamp;

        PreparedWarehouse(int[][] grid, Coordinate robot, boolean wide) {
            this.grid = grid;
            this.robot = robot;
            int cells = wide ? grid.length * grid[0].length : 0;
            boxQueue = new int[cells];
            seenStamp = new int[cells];
        }

        int nextStamp() {
            if (++stamp == 0) {
                Arrays.fill(seenStamp, 0);
                stamp = 1;
            }
            return stamp;
        }
    }
}

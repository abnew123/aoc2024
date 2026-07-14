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
            oneCycle(move, warehouse.robot, warehouse.grid, !wide);
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
        return new PreparedWarehouse(grid, robot);
    }

    private void oneCycle(char c, Coordinate robot, int[][] grid, boolean part1){
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
        Coordinate goal = new Coordinate(robot.x + XS[direction], robot.y + YS[direction]);
        if(grid[goal.x][goal.y] == 1){
            robot.x = goal.x;
            robot.y = goal.y;
            return;
        }
        if(grid[goal.x][goal.y] == 1000){
            return;
        }
        if(direction == 2 || direction == 3 || part1){
            boolean someEmpty = false;
            Coordinate firstEmpty = new Coordinate(1,-1);
            int counter = 0;
            while(true){
                counter++;
                goal = new Coordinate(goal.x + XS[direction], goal.y + YS[direction]);
                if(grid[goal.x][goal.y] == 1000){
                    break;
                }
                if(grid[goal.x][goal.y] == 1){
                    someEmpty = true;
                    firstEmpty = new Coordinate(goal.x, goal.y);
                    break;
                }
            }
            if(someEmpty){
                while(counter-- > 0){
                    grid[firstEmpty.x][firstEmpty.y] = grid[firstEmpty.x - XS[direction]][firstEmpty.y - YS[direction]];
                    firstEmpty.x -= XS[direction];
                    firstEmpty.y -= YS[direction];
                }
                robot.x += XS[direction];
                robot.y += YS[direction];
                grid[robot.x][robot.y] = 1;
            }
        }
        else{
            List<Set<Integer>> indices = new ArrayList<>();
            indices.add(new HashSet<>());
            indices.get(0).add(robot.y);
            boolean allEmpty = false;
            int level = robot.x;
            while(!allEmpty) {
                Set<Integer> nextIndices = new HashSet<>();
                level += XS[direction];
                allEmpty = true;
                for (int index : indices.get(indices.size() - 1)) {
                    if (grid[level][index] == 1000) {
                        return;
                    }
                    if (grid[level][index] != 1) {
                        allEmpty = false;
                    }
                    if (grid[level][index] == 3) {
                        nextIndices.add(index + 1);
                        nextIndices.add(index);
                    }
                    if (grid[level][index] == 4) {
                        nextIndices.add(index - 1);
                        nextIndices.add(index);
                    }
                }
                if(!allEmpty){
                    indices.add(nextIndices);
                }
            }
            for(int i = indices.size() - 1; i > 0; i--) {
                Set<Integer> indicesLayer = indices.get(i);
                for(int index: indicesLayer){
                    grid[robot.x + ((i + 1) * XS[direction])][index] = grid[robot.x + (i * XS[direction])][index];
                    grid[robot.x + (i * XS[direction])][index] = 1;
                }
            }
            robot.x += XS[direction];
            robot.y += YS[direction];
        }
    }

    private record ParsedInput(String[] map, char[] moves) {}

    private record PreparedWarehouse(int[][] grid, Coordinate robot) {}
}

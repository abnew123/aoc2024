package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

public class Day15 extends DayTemplate {

    int[]xs = new int[]{1,-1,0,0};
    int[]ys = new int[]{0,0,1,-1};

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        List<String> movements = new ArrayList<>();
        boolean movement = false;
        while (in.hasNext()) {
            String line = in.nextLine();
            if(line.isEmpty()){
                movement = true;
                continue;
            }
            if(!movement){
                lines.add(line);
            }
            else{
                movements.add(line);
            }
        }
        if(!part1){
            lines = convert(lines);
        }

        int[][] grid = new int[lines.size()][lines.get(0).length()];
        Coordinate robot = new Coordinate(0,0);
        Map<Character, Integer> mapping = Map.of('#',1000,'.',1,'O',2,'[',3,']',4);
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                char c = lines.get(i).charAt(j);
                if(c == '@'){
                    robot = new Coordinate(i,j);
                    grid[i][j] = 1;
                }
                else{
                    grid[i][j] = mapping.get(c);
                }
            }
        }

        StringBuilder allMovements = new StringBuilder();
        for(String move: movements){
            allMovements.append(move);
        }
        for(char c: allMovements.toString().toCharArray()){
            oneCycle(c, robot, grid, part1);
        }
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2 || grid[i][j] == 3) {
                    answer += 100L * i + j;
                }
            }
        }
        return answer + "";
    }

    private static List<String> convert(List<String> lines) {
        List<String> newLines = new ArrayList<>();
        Map<Character, String> mapping = Map.of('#',"##", '.',"..",'O',"[]",'@',"@.");
        for (String line : lines) {
            StringBuilder newLine = new StringBuilder();
            for (char c : line.toCharArray()) {
                newLine.append(mapping.get(c));
            }
            newLines.add(newLine.toString());
        }
        return newLines;
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
        Coordinate goal = new Coordinate(robot.x + xs[direction], robot.y + ys[direction]);
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
                goal = new Coordinate(goal.x + xs[direction], goal.y + ys[direction]);
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
                    grid[firstEmpty.x][firstEmpty.y] = grid[firstEmpty.x - xs[direction]][firstEmpty.y - ys[direction]];
                    firstEmpty.x -= xs[direction];
                    firstEmpty.y -= ys[direction];
                }
                robot.x += xs[direction];
                robot.y += ys[direction];
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
                level += xs[direction];
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
                    grid[robot.x + ((i + 1) * xs[direction])][index] = grid[robot.x + (i * xs[direction])][index];
                    grid[robot.x + (i * xs[direction])][index] = 1;
                }
            }
            robot.x += xs[direction];
            robot.y += ys[direction];
        }
    }
}
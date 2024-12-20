package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;
import java.util.*;
import static src.meta.Utils.*;

public class Day20 extends DayTemplate {

    int[][] grid;

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            lines.add(line);
        }
        grid = new int[lines.size()][lines.get(0).length()];
        Coordinate start = new Coordinate(-1,-1);
        Coordinate end = new Coordinate(-1,-1);
        for(int i = 0 ; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                char c = lines.get(i).charAt(j);
                if(c == 'S'){
                    start = new Coordinate(i,j);
                    grid[i][j] = 1;
                }
                if(c == 'E'){
                    end = new Coordinate(i,j);
                    grid[i][j] = 1;
                }
                if(c == '#'){
                    grid[i][j] = 2;
                }
                if(c == '.'){
                    grid[i][j] = 1;
                }
            }
        }

        int[][] bfsFromEnd = bfs(end);
        int[][] bfsFromStart = bfs(start);
        for(int i = 0; i < bfsFromEnd.length; i++){
            for(int j = 0; j < bfsFromEnd.length; j++){
                if(grid[i][j] == 2){
                    bfsFromEnd[i][j] = grid.length * grid[0].length * 2;
                }
            }
        }
        int currentDistance = bfsFromEnd[start.x][start.y];
        for(int i = 0; i < bfsFromEnd.length; i++){
            for(int j = 0; j < bfsFromEnd[0].length; j++){
                if(grid[i][j] != 2){
                    Set<Coordinate> allNeighbors = new HashSet<>();
                    Coordinate s = new Coordinate(i,j);
                    allNeighbors.add(s);
                    for(int k = 0; k < (part1?2:20); k++){
                        Set<Coordinate> newAllNeighbors = new HashSet<>(allNeighbors);
                        for(Coordinate c: allNeighbors){
                            newAllNeighbors.addAll(getNeighbors(c.x, c.y, grid));
                        }
                        allNeighbors = newAllNeighbors;
                    }
                    for(Coordinate c: allNeighbors){
                        if(grid[c.x][c.y] != 2 && (bfsFromStart[i][j] + bfsFromEnd[c.x][c.y] + Math.abs(c.x - i) + Math.abs(c.y - j)) <= currentDistance - 100){
                            answer++;
                        }
                    }
                }
            }
        }
        return answer + "";
    }

    private int[][] bfs(Coordinate end){
        int[][] bfs = new int[grid.length][grid[0].length];
        List<Coordinate> queue = new ArrayList<>();
        queue.add(end);
        Set<Coordinate> seen = new HashSet<>();
        seen.add(end);
        int counter = 0;
        while(!queue.isEmpty()){
            List<Coordinate> newQueue = new ArrayList<>();
            for(Coordinate c: queue){
                bfs[c.x][c.y] = counter;
                for(Coordinate neighbor: getNeighbors(c.x, c.y, grid)){
                    if(!seen.contains(neighbor) && grid[neighbor.x][neighbor.y] != 2){
                        newQueue.add(neighbor);
                    }
                }
            }
            counter++;
            queue = newQueue;
            seen.addAll(newQueue);
        }
        return bfs;
    }
}
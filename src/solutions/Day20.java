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
        int currentDistance = bfsFromEnd[start.x][start.y];
        for(int i = 0; i < bfsFromEnd.length; i++){
            for(int j = 0; j < bfsFromEnd[0].length; j++){
                if(grid[i][j] != 2){
                    for(int k = 0; k < bfsFromStart.length; k++){
                        for(int l = 0; l < bfsFromStart[0].length; l++){
                            if(grid[k][l] != 2 && (Math.abs(k - i) + Math.abs(l - j)) <= (part1?2:20) && (bfsFromStart[i][j] + bfsFromEnd[k][l] + Math.abs(k - i) + Math.abs(l - j)) <= currentDistance - 100){
                                answer++;
                            }
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
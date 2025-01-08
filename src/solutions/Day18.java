package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day18 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        int gridSize = 71;
        int[][] grid = new int[gridSize][gridSize];
        List<Coordinate> lines = new ArrayList<>();
        while(in.hasNext()){
            String[] parts = in.nextLine().split(",");
            lines.add(new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        if(part1){
            return bfs(1024, lines, grid) + "";

        }
        else{
            int high = lines.size() - 1;
            int low = 0;
            while(low < high){
                grid = new int[gridSize][gridSize];
                int med = (low + high)/2;
                if(bfs(med, lines, grid) == -1){
                    high = med;
                }
                else{
                    low = med + 1;
                }
            }
            return lines.get(low - 1).x + "," + lines.get(low - 1).y;
        }
    }

    private int bfs(int limit, List<Coordinate> memoryBlock, int[][] grid){
        for(int i = 0; i < limit; i++){
            grid[memoryBlock.get(i).x][memoryBlock.get(i).y] = 1;
        }
        Set<Coordinate> lst = new HashSet<>();
        lst.add(new Coordinate(0,0));
        int counter = 0;
        Coordinate end = new Coordinate(grid.length -1, grid.length - 1);
        Set<Coordinate> allSeen = new HashSet<>();
        while(!lst.contains(end) && counter < grid.length * grid[0].length * 2){
            counter++;
            allSeen.addAll(lst);
            Set<Coordinate> newList = new HashSet<>();
            for(Coordinate c: lst){
                for(Coordinate neighbor: getNeighbors(c.x, c.y, grid)){
                    if(grid[neighbor.x][neighbor.y] != 1 && !allSeen.contains(neighbor)){
                        newList.add(neighbor);
                    }
                }
            }
            lst = newList;
        }
        if(counter >= grid.length * grid[0].length){
            return -1;
        }
        return counter;

    }
}
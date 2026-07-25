package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day18 extends DayTemplate {

    /**
     * Single pass solver. Parsing is shared. bfs() writes the fallen bytes into the grid it is
     * given, so every call gets a freshly allocated grid; the parsed coordinate list is only read.
     *
     * @param in The solver will read data from this Scanner.
     * @return Returns answer as a string array, with part 1 as index 0 and part 2 as index 1
     */
    public String[] fullSolve(Scanner in) {
        int gridSize = 71;
        List<Coordinate> lines = new ArrayList<>();
        while(in.hasNext()){
            String[] parts = in.nextLine().split(",");
            lines.add(new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        String answer1 = bfs(1024, lines, new int[gridSize][gridSize]) + "";

        int high = lines.size() - 1;
        int low = 0;
        while(low < high){
            int[][] grid = new int[gridSize][gridSize];
            int med = (low + high)/2;
            if(bfs(med, lines, grid) == -1){
                high = med;
            }
            else{
                low = med + 1;
            }
        }
        String answer2 = lines.get(low - 1).x + "," + lines.get(low - 1).y;
        return new String[]{answer1, answer2};
    }

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
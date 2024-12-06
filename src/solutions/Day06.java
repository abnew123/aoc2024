package src.solutions;

import src.objects.Coordinate;

import java.util.*;
import static src.meta.Utils.*;

public class Day06 {

    int[] xs = new int[]{0,1,0,-1};
    int[] ys = new int[]{-1,0,1,0};

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            lines.add(in.nextLine());
        }
        Coordinate start = new Coordinate(0,0);
        int direction = 0; //0 up, 1 right, 2 down, 3 left
        int[][] grid = buildGrid(lines, c -> c == '.' ? 1 : (c == '#' ? 2 : 3));
        Set<Coordinate> locations = new HashSet<>();
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
               if(grid[i][j] == 3){
                   grid[i][j] = 1;
                   start = new Coordinate(i,j);
                   locations.add(start);
               }
            }
        }

        if(part1){
            Coordinate guard = start;
            while(safe(guard.x, guard.y, grid)){
                int x = guard.x + xs[direction%4];
                int y = guard.y + ys[direction%4];
                if(!safe(x,y,grid)){
                    return locations.size() + "";
                }
                if(grid[x][y] == 2){
                    direction++;
                }
                else{
                    locations.add(new Coordinate(x,y));
                    guard = new Coordinate(x,y);
                }
            }
        }
        else{
            for(int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    int[][] gridCopy = new int[grid.length][grid[0].length];
                    for(int k = 0; k < grid.length; k++) {
                        for (int l = 0; l < grid[0].length; l++) {
                            gridCopy[k][l] = grid[k][l];
                        }
                    }
                    gridCopy[i][j] = 2;
                    if(loop(start, gridCopy)){
                        answer++;
                    }
                }
            }
        }
        return answer + "";
    }

    private boolean loop(Coordinate start, int[][]gridCopy){
        int counter = 100000;
        Coordinate guard = start;
        int direction = 0;
        while(safe(guard.x, guard.y, gridCopy) && counter-->0){
            int x = guard.x + xs[direction%4];
            int y = guard.y + ys[direction%4];
            if(!safe(x,y,gridCopy)){
                break;
            }
            if(gridCopy[x][y] == 2){
                direction++;
            }
            else{
                guard = new Coordinate(x,y);
            }
        }
        return counter < 0;
    }
}

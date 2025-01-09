package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day06 extends DayTemplate {

    int[] xs = new int[]{0,1,0,-1};
    int[] ys = new int[]{-1,0,1,0};

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            lines.add(in.nextLine());
        }
        GuardLocation start = new GuardLocation(0,0, 0);
        int direction = 0; //0 up, 1 right, 2 down, 3 left
        int[][] grid = buildGrid(lines, c -> c == '.' ? 1 : (c == '#' ? 2 : 3));

        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
               if(grid[i][j] == 3){
                   grid[i][j] = 1;
                   start = new GuardLocation(i,j, 0);
               }
            }
        }

        if(part1){
            Set<Coordinate> locations = new HashSet<>();
            locations.add(new Coordinate(start.x(), start.y()));
            GuardLocation guard = start;
            while(safe(guard.x(), guard.y(), grid)){
                int x = guard.x() + xs[direction%4];
                int y = guard.y() + ys[direction%4];
                if(!safe(x,y,grid)){
                    return locations.size() + "";
                }
                if(grid[x][y] == 2){
                    direction++;
                }
                else{
                    locations.add(new Coordinate(x,y));
                    guard = new GuardLocation(x,y, direction);
                }
            }
        }
        else{
            return part2(start, direction, grid);

        }
        return answer + "";
    }

    private boolean loop(GuardLocation start, int[][]gridCopy, int direction){
        Set<GuardLocation> visited = new HashSet<>();
        GuardLocation guard = start;
        while(!visited.contains(guard)){
            int x = guard.x() + xs[direction%4];
            int y = guard.y() + ys[direction%4];
            if(!safe(x,y,gridCopy)){
                return false;
            }
            if(gridCopy[x][y] == 2){
                direction++;
            }
            else{
                visited.add(guard);
                guard = new GuardLocation(x,y, direction%4);
            }
        }
        return true;
    }

    private String part2(GuardLocation start, int direction, int[][] grid){
        Set<Coordinate> places = new HashSet<>();
        Set<Coordinate> seen = new HashSet<>();
        GuardLocation guard = start;
        while(true){
            int x = guard.x() + xs[direction%4];
            int y = guard.y() + ys[direction%4];
            if(!safe(x,y,grid)){
                break;
            }
            if(!seen.contains(new Coordinate(x,y)) && grid[x][y] == 1){
                grid[x][y] = 2;
                if(loop(guard, grid, direction + 1)){
                    places.add(new Coordinate(x, y));
                }
                grid[x][y] = 1;
            }

            if(grid[x][y] == 2){
                direction++;
            }
            else{
                seen.add(new Coordinate(x,y));
                guard = new GuardLocation(x,y, direction);
            }
        }
        return places.size() + "";
    }
}

record GuardLocation (int x, int y, int direction){}
package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;
public class Day12 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            lines.add(line);
        }
        int[][] grid = buildGrid( lines, a -> a - 'A');
        while(true){
            long val = part1?flood(grid):flood2(grid);
            if(val == -1){
                break;
            }
            else{
                answer += val;
            }
        }
        return answer + "";
    }

    private Set<Coordinate> getGarden(int[][] grid){
        Coordinate start = new Coordinate(-1,-1);
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j< grid[0].length; j++){
                if(grid[i][j] != -1){
                    start = new Coordinate(i,j);
                }
            }
        }
        if(start.x == -1){
            return null;
        }
        Set<Coordinate> garden = new HashSet<>();
        garden.add(start);
        int c = grid[start.x][start.y];
        grid[start.x][start.y] = -2;
        while(true){
            Set<Coordinate> newGarden = new HashSet<>();
            for(Coordinate coord: garden){
                for(Coordinate neighbor: getNeighbors(coord.x, coord.y, grid)){
                    if(grid[neighbor.x][neighbor.y] == c){
                        grid[neighbor.x][neighbor.y] = -2;
                        newGarden.add(neighbor);
                    }
                }
            }
            newGarden.addAll(garden);
            if(newGarden.size() == garden.size()){
                break;

            }
            else{
                garden = newGarden;
            }
        }
        return garden;
    }

    private void cleanup(int[][] grid){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == -2){
                    grid[i][j] = -1;
                }
            }
        }
    }

    private long flood(int[][] grid){
        Set<Coordinate> garden = getGarden(grid);
        if(garden == null){
            return -1;
        }
        long perimeter = garden.size() * 4L;
        for(Coordinate c1: garden){
            for(Coordinate c2: garden){
                if(c1.x == c2.x){
                    if(Math.abs(c1.y - c2.y) == 1){
                        perimeter--;
                    }
                }
                if(c1.y == c2.y){
                    if(Math.abs(c1.x - c2.x) == 1){
                        perimeter--;
                    }
                }
            }
        }
        cleanup(grid);
        return garden.size() * perimeter;
    }

    private long flood2(int[][] grid){
        Set<Coordinate> garden = getGarden(grid);
        if(garden == null){
            return -1;
        }
        long sides = 0L;

        List<Integer> vert = new ArrayList<>();
        for(int i = 0; i < grid.length; i++){
            List<Integer> tmp = new ArrayList<>();
            for(Coordinate coord: garden){
                if(coord.x == i){
                    tmp.add((coord.y + 1) * -1);
                    tmp.add(coord.y + 2);
                }
            }
            List<Integer> tmp2 = new ArrayList<>();
            for(Integer in: tmp){
                if(!tmp.contains(-1 * in)){
                    tmp2.add(in);
                }
            }
            for(Integer in: tmp2){
                if(!vert.contains(in)){
                    sides++;
                }
            }
            vert = tmp2;
        }

        List<Integer> horiz = new ArrayList<>();
        for(int i = 0; i < grid[0].length; i++){
            List<Integer> tmp = new ArrayList<>();
            for(Coordinate coord: garden){
                if(coord.y == i){
                    tmp.add((coord.x + 1) * -1);
                    tmp.add(coord.x + 2);
                }
            }
            List<Integer> tmp2 = new ArrayList<>();
            for(Integer in: tmp){
                if(!tmp.contains(-1 * in)){
                    tmp2.add(in);
                }
            }
            for(Integer in: tmp2){
                if(!horiz.contains(in)){
                    sides++;
                }
            }
            horiz = tmp2;
        }
        cleanup(grid);
        return garden.size() * sides;
    }

}
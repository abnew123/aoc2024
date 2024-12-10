package src.solutions;

import java.util.Scanner;

import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day10 {
    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            lines.add(line);
        }
        int[][] grid = buildGrid( lines, a -> a - '0');
        List<Coordinate> trailheads = new ArrayList<>();
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == 0){
                    trailheads.add(new Coordinate(i,j));
                }
            }
        }

        if(part1){
            for(Coordinate c: trailheads){
                answer += reachable(grid, c);
            }
        }
        else{
            answer = rating(grid);
        }
        return answer + "";
    }

    private int reachable(int[][] grid, Coordinate c){
        Set<Coordinate> level = new HashSet<>();
        level.add(c);
        int counter = 0;
        while(counter++ < 9){
            Set<Coordinate> newLevel = new HashSet<>();
            for(Coordinate l: level){
                List<Coordinate> neighbors = getNeighbors(l.x, l.y, grid);
                for(Coordinate n: neighbors){
                    if(grid[n.x][n.y] == counter){
                        newLevel.add(n);
                    }
                }
            }
            level = newLevel;
        }
        return level.size();
    }

    private long rating(int[][] grid){
        long answer = 0;
        Set<Coordinate> peaks = new HashSet<>();
        long[][] paths = new long[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == 9){
                    peaks.add(new Coordinate(i,j));
                    paths[i][j] = 1;
                }
            }
        }
        int level = 9;
        while(level-- > 0){
            Set<Coordinate> newPeaks = new HashSet<>();
            for(Coordinate l: peaks){
                List<Coordinate> neighbors = getNeighbors(l.x, l.y, grid);
                for(Coordinate n: neighbors){
                    if(grid[n.x][n.y] == level){
                        newPeaks.add(n);
                        paths[n.x][n.y] += paths[l.x][l.y];
                    }
                }
            }
            peaks = newPeaks;
        }

        for(Coordinate c: peaks){
            answer += paths[c.x][c.y];
        }
        return answer;
    }
}

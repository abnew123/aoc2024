package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day25 extends DayTemplate {

    /**
     * Single pass solver. Day 25 has no part 2 in Advent of Code, so all the work is part 1's
     * and index 1 carries the same constant solve(false, in) returns.
     *
     * @param in The solver will read data from this Scanner.
     * @return Returns answer as a string array, with part 1 as index 0 and part 2 as index 1
     */
    public String[] fullSolve(Scanner in){
        return new String[]{solve(true, in), "Merry Christmas!"};
    }

    public String solve(boolean part1, Scanner in){
        if(!part1) { //part 2 doesn't exist for this day
            return "Merry Christmas!";
        }
        long answer = 0;
        List<String> grid = new ArrayList<>();
        List<List<String>> grids = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            if(line.isEmpty()){
                grids.add(grid);
                grid = new ArrayList<>();
            }
            else{
                grid.add(line);
            }
        }

        if(!grid.isEmpty()){
            grids.add(grid);
        }

        List<int[]> locks = new ArrayList<>();
        List<int[]> keys = new ArrayList<>();
        for(List<String> lines: grids){
            int[][] g = new int[lines.get(0).length()][lines.size()];
            for(int i = 0; i < lines.size(); i++){
                String line = lines.get(i);
                for(int j = 0; j < line.length(); j++){
                    g[j][i] = lines.get(i).charAt(j) == '#'? 1 : 0;
                }
            }
            int[] heights = new int[5];

            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 7; j++){
                    if(g[i][j] == 1){
                        heights[i]++;
                    }
                }
            }
            if(lines.get(0).startsWith(".")){
                locks.add(heights);
            }
            else{
                keys.add(heights);
            }
        }

        for(int[] lock: locks){
            for(int[] key: keys){
                boolean fits = true;
                for(int i = 0; i < 5; i++){
                    if(lock[i] + key[i] > 7){
                        fits = false;
                    }
                }
                if(fits){
                    answer++;
                }
            }
        }
        return answer + "";
    }
}

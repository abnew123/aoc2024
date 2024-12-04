package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static src.meta.Utils.*;

public class Day04 extends DayTemplate {

    char[][] grid;

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<char[]> tmp = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            tmp.add(line.toCharArray());
        }
        grid = new char[tmp.get(0).length][tmp.size()];
        for(int i = 0; i < grid.length; i++){
            System.arraycopy(tmp.get(i), 0, grid[i], 0, grid[0].length);
        }
        if (part1) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    answer += checkPattern(i, j, 1, 0) +  checkPattern(i, j, 0, 1) + checkPattern(i, j, 1, 1) + checkPattern(i, j, -1, 1);
                }
            }
        }
        else{
            for(int i = 0; i < grid.length - 2; i++){
                for(int j = 0; j < grid[0].length - 2; j++){
                    if(grid[i + 1][j+1] == 'A'){
                        String result = "" + grid[i][j] + grid[i + 2][j] + grid[i][j + 2] + grid[i + 2][j + 2];
                        if(result.equals("MMSS") || result.equals("SSMM") || result.equals("MSMS") || result.equals("SMSM")){
                            answer++;
                        }
                    }
                }
            }
        }
        return answer + "";
    }

    private int checkPattern(int i, int j, int di, int dj) {
        StringBuilder result = new StringBuilder();
        for (int k = 0; k < 4; k++) {
            result.append(safeGet(i + k * di, j + k * dj));
        }
        return (result.toString().equals("XMAS") || result.toString().equals("SAMX"))?1:0;
    }

    private char safeGet(int i, int j){
        if(safe(j,i,grid)){
            return grid[i][j];
        }
        return '0';
    }

}

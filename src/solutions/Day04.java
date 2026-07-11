package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day04 extends DayTemplate {

    private char[][] grid;
    private int rows;
    private int cols;

    public String solve(boolean part1, Scanner in) {
        parse(in);
        return (part1 ? countXmas() : countMasCrosses()) + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        parse(in);
        long xmas = 0;
        long crosses = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char current = grid[row][col];
                if (current == 'X' || current == 'S') {
                    xmas += checkPattern(row, col, 1, 0)
                            + checkPattern(row, col, 0, 1)
                            + checkPattern(row, col, 1, 1)
                            + checkPattern(row, col, -1, 1);
                }
                if (current == 'A' && row > 0 && row + 1 < rows
                        && col > 0 && col + 1 < cols && isMasCross(row, col)) {
                    crosses++;
                }
            }
        }
        return new String[]{xmas + "", crosses + ""};
    }

    private void parse(Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }

        rows = lines.size();
        cols = lines.get(0).length();
        grid = new char[rows][cols];
        for (int row = 0; row < rows; row++) {
            grid[row] = lines.get(row).toCharArray();
        }
    }

    private long countXmas() {
        long answer = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char current = grid[row][col];
                if (current == 'X' || current == 'S') {
                    answer += checkPattern(row, col, 1, 0)
                            + checkPattern(row, col, 0, 1)
                            + checkPattern(row, col, 1, 1)
                            + checkPattern(row, col, -1, 1);
                }
            }
        }
        return answer;
    }

    private long countMasCrosses() {
        long answer = 0;
        for (int row = 1; row + 1 < rows; row++) {
            for (int col = 1; col + 1 < cols; col++) {
                if (grid[row][col] == 'A' && isMasCross(row, col)) {
                    answer++;
                }
            }
        }
        return answer;
    }

    private int checkPattern(int row, int col, int rowStep, int colStep) {
        if (!inBounds(row + 3 * rowStep, col + 3 * colStep)) {
            return 0;
        }

        char first = grid[row][col];
        char second = grid[row + rowStep][col + colStep];
        char third = grid[row + 2 * rowStep][col + 2 * colStep];
        char fourth = grid[row + 3 * rowStep][col + 3 * colStep];
        boolean xmas = first == 'X' && second == 'M' && third == 'A' && fourth == 'S';
        boolean samx = first == 'S' && second == 'A' && third == 'M' && fourth == 'X';
        return xmas || samx ? 1 : 0;
    }

    private boolean isMasCross(int row, int col) {
        char nw = grid[row - 1][col - 1];
        char ne = grid[row - 1][col + 1];
        char sw = grid[row + 1][col - 1];
        char se = grid[row + 1][col + 1];
        return isMas(nw, se) && isMas(ne, sw);
    }

    private boolean isMas(char first, char second) {
        return (first == 'M' && second == 'S') || (first == 'S' && second == 'M');
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }
}

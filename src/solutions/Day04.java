package src.solutions;

import src.meta.DayTemplate;

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
        String raw = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = raw.length();
        int lineCount = 0;
        int index = 0;
        while (index < length) {
            lineCount++;
            while (index < length && raw.charAt(index) != '\n' && raw.charAt(index) != '\r') {
                index++;
            }
            index = nextLineStart(raw, index);
        }

        grid = new char[lineCount][];
        int row = 0;
        index = 0;
        while (index < length) {
            int start = index;
            while (index < length && raw.charAt(index) != '\n' && raw.charAt(index) != '\r') {
                index++;
            }
            char[] line = new char[index - start];
            raw.getChars(start, index, line, 0);
            grid[row++] = line;
            index = nextLineStart(raw, index);
        }

        rows = lineCount;
        cols = grid[0].length;
    }

    private int nextLineStart(String raw, int separatorIndex) {
        if (separatorIndex >= raw.length()) {
            return separatorIndex;
        }
        if (raw.charAt(separatorIndex) == '\r' && separatorIndex + 1 < raw.length()
                && raw.charAt(separatorIndex + 1) == '\n') {
            return separatorIndex + 2;
        }
        return separatorIndex + 1;
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

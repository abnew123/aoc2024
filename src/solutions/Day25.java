package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Day25 extends DayTemplate {

    private static final String NO_PART_TWO = "Merry Christmas!";

    @Override
    public String[] fullSolve(Scanner in) {
        return new String[]{countFits(in) + "", NO_PART_TWO};
    }

    public String solve(boolean part1, Scanner in){
        if(!part1) { //part 2 doesn't exist for this day
            return NO_PART_TWO;
        }
        return countFits(in) + "";
    }

    private long countFits(Scanner in) {
        List<Schematic> locks = new ArrayList<>();
        List<Schematic> keys = new ArrayList<>();
        int[] heights = null;
        int rows = 0;
        boolean lock = false;
        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        Iterator<String> lines = input.lines().iterator();

        while (lines.hasNext()) {
            String line = lines.next();
            if (line.isEmpty()) {
                if (heights != null) {
                    (lock ? locks : keys).add(new Schematic(heights, rows));
                    heights = null;
                    rows = 0;
                }
                continue;
            }

            if (heights == null) {
                heights = new int[line.length()];
                lock = line.charAt(0) == '#';
            } else if (line.length() != heights.length) {
                throw new IllegalArgumentException("Inconsistent schematic width");
            }
            for (int column = 0; column < heights.length; column++) {
                if (line.charAt(column) == '#') {
                    heights[column]++;
                }
            }
            rows++;
        }

        if (heights != null) {
            (lock ? locks : keys).add(new Schematic(heights, rows));
        }

        long answer = 0;
        for (Schematic lockSchematic : locks) {
            for (Schematic keySchematic : keys) {
                int[] lockHeights = lockSchematic.heights();
                int[] keyHeights = keySchematic.heights();
                if (lockSchematic.rows() != keySchematic.rows()
                        || lockHeights.length != keyHeights.length) {
                    throw new IllegalArgumentException("Inconsistent schematic dimensions");
                }
                boolean fits = true;
                for (int column = 0; column < lockHeights.length; column++) {
                    if (lockHeights[column] + keyHeights[column] > lockSchematic.rows()) {
                        fits = false;
                        break;
                    }
                }
                if (fits) {
                    answer++;
                }
            }
        }
        return answer;
    }

    private record Schematic(int[] heights, int rows) {
    }
}

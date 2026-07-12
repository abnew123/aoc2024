package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day08 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        Input input = parse(in);
        boolean[] antinodes = new boolean[input.grid.length * input.grid[0].length];
        markAntinodes(input, part1 ? antinodes : null, part1 ? null : antinodes);
        return count(antinodes) + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        Input input = parse(in);
        int cells = input.grid.length * input.grid[0].length;
        boolean[] part1 = new boolean[cells];
        boolean[] part2 = new boolean[cells];
        markAntinodes(input, part1, part2);
        return new String[]{count(part1) + "", count(part2) + ""};
    }

    private Input parse(Scanner in) {
        Map<Character, List<Coordinate>> freqs = new HashMap<>();
        char[][] grid = getGrid(in);
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] != '.'){
                    freqs.computeIfAbsent(grid[i][j], k -> new ArrayList<>());
                    freqs.get(grid[i][j]).add(new Coordinate(i,j));
                }
            }
        }
        return new Input(grid, freqs);
    }

    private void markAntinodes(Input input, boolean[] part1, boolean[] part2) {
        for (List<Coordinate> lst : input.frequencies.values()) {
            for(int i = 0; i < lst.size(); i++){
                for(int j = i + 1; j < lst.size(); j++){
                    Coordinate a = lst.get(i);
                    Coordinate b = lst.get(j);
                    int diffX = a.x - b.x;
                    int diffY = a.y - b.y;
                    if (part1 != null) {
                        mark(b.x - diffX, b.y - diffY, input.grid, part1);
                        mark(a.x + diffX, a.y + diffY, input.grid, part1);
                    }
                    if (part2 != null) {
                        int divisor = Math.abs(gcd(diffX, diffY));
                        int stepX = diffX / divisor;
                        int stepY = diffY / divisor;
                        for (int x = a.x, y = a.y; safe(x, y, input.grid); x += stepX, y += stepY) {
                            part2[x * input.grid[0].length + y] = true;
                        }
                        for (int x = a.x - stepX, y = a.y - stepY;
                             safe(x, y, input.grid); x -= stepX, y -= stepY) {
                            part2[x * input.grid[0].length + y] = true;
                        }
                    }
                }
            }
        }
    }

    private void mark(int x, int y, char[][] grid, boolean[] antinodes) {
        if (safe(x, y, grid)) {
            antinodes[x * grid[0].length + y] = true;
        }
    }

    private int count(boolean[] antinodes) {
        int total = 0;
        for (boolean antinode : antinodes) {
            if (antinode) {
                total++;
            }
        }
        return total;
    }

    private static class Input {
        private final char[][] grid;
        private final Map<Character, List<Coordinate>> frequencies;

        private Input(char[][] grid, Map<Character, List<Coordinate>> frequencies) {
            this.grid = grid;
            this.frequencies = frequencies;
        }
    }
}

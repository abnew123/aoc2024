package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day08 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        long answer = 0;
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
        Set<Coordinate> antinodes = new HashSet<>();
        for(Character c: freqs.keySet()){
            List<Coordinate> lst = freqs.get(c);
            for(int i = 0; i < lst.size(); i++){
                for(int j = i + 1; j < lst.size(); j++){
                    Coordinate a = lst.get(i);
                    Coordinate b = lst.get(j);
                    addAntiNodes(a,b,antinodes, part1);
                }
            }
        }
        for(Coordinate c: antinodes){
            if(safe(c.x, c.y, grid)){
                answer++;
            }
        }
        return answer + "";
    }

    private void addAntiNodes(Coordinate a, Coordinate b, Set<Coordinate> antinodes, boolean part1){
        int diffX = a.x - b.x;
        int diffY = a.y - b.y;
        if (part1) {
            antinodes.add(new Coordinate(b.x - diffX, b.y - diffY));
            antinodes.add(new Coordinate(a.x + diffX, a.y + diffY));
        }
        else{
            int gcd = gcd(diffY, diffX);
            for(int i = 0; i < 51; i++){
                antinodes.add(new Coordinate(b.x - (i * diffX/gcd), b.y - (i * diffY/gcd)));
                antinodes.add(new Coordinate(b.x + (i * diffX/gcd), b.y + (i * diffY/gcd)));
            }
        }
    }
}

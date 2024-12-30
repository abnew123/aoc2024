package src.solutions;

import src.meta.DayTemplate;
import src.objects.Coordinate;

import java.util.*;

import static src.meta.Utils.*;

public class Day16 extends DayTemplate {

    int[] xs = new int[]{0, 1, 0, -1};
    int[] ys = new int[]{1, 0, -1, 0};

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            lines.add(line);
        }
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        Coordinate reindeer = new Coordinate(-1,-1);
        Coordinate exit = new Coordinate(-1,-1);

        for(int i = 0 ; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                char c = lines.get(i).charAt(j);
                if(c == 'S'){
                    reindeer = new Coordinate(i,j);
                    grid[i][j] = 1;
                }
                if(c == 'E'){
                    exit = new Coordinate(i,j);
                    grid[i][j] = 1;
                }
                if(c == '#'){
                    grid[i][j] = 2;
                }
                if(c == '.'){
                    grid[i][j] = 1;
                }
            }
        }

        if(part1){
            answer = bfs(grid, reindeer, exit);
        }
        else{
            int[][] bestSeats = new int[grid.length][grid[0].length];
            String path = bestPath(grid, reindeer, exit);
            mark(bestSeats, path, reindeer);
            List<String> otherBests = findOthers(grid, reindeer, exit, path);
            for(String other: otherBests){
                mark(bestSeats, other, reindeer);
            }
            for(int i = 0; i < bestSeats.length; i++){
                for(int j = 0; j < bestSeats.length; j++){
                    answer+= bestSeats[i][j];
                }
            }
        }

        return answer + "";
    }

    private void mark(int[][] bestSeats, String path, Coordinate reindeer){
        path = path.substring(1);
        bestSeats[reindeer.x][reindeer.y] = 1;
        int deltax = 0;
        int deltay = 0;
        int direction = 0;
        for(char c: path.toCharArray()){
            if(c == 'R'){
                direction = (direction + 1)%4;
            }
            else{
                deltax += xs[direction];
                deltay += ys[direction];
                bestSeats[reindeer.x + deltax][reindeer.y + deltay] = 1;
            }
        }
    }

    private long bfs(int[][] grid, Coordinate reindeer, Coordinate exit){
        long answer = Long.MAX_VALUE;
        Queue<String> queue = new PriorityQueue<>();
        queue.add(reindeer.x + " " + reindeer.y + " " + 0 + " " + 0 + " S");
        Map<String, Integer> cache = new HashMap<>();
        while(!queue.isEmpty()){
            String element = queue.poll();
            String[] parts = element.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int direction = Integer.parseInt(parts[2]);
            int current = Integer.parseInt(parts[3]);
            String currentPath = parts[4];
            if(x == exit.x && y == exit.y){
                answer = Math.min(answer, current);
            }
            else{
                int newx = x + xs[direction];
                int newy = y + ys[direction];
                if(safe(newx, newy, grid) && grid[newx][newy] != 2) {
                    Coordinate newReindeer = new Coordinate(newx, newy);
                    String newHash = newReindeer.x + " " + newReindeer.y + " " + direction + " " + (current + 1) + " " + currentPath + "F";
                    if((cache.getOrDefault(newReindeer.x + " " + newReindeer.y + " " + direction, Integer.MAX_VALUE) > current + 1)){
                        queue.add(newHash);
                        cache.put(newReindeer.x + " " + newReindeer.y + " " + direction, current + 1);
                    }
                }
                String right = x + " " + y + " " + ((direction + 1) % 4) + " " + (current + 1000) + " " + currentPath + "R";
                if((cache.getOrDefault(x + " " + y + " " + ((direction + 1) % 4), Integer.MAX_VALUE) > current + 1000) && !currentPath.endsWith("RRR")){
                    queue.add(right);
                    cache.put(x + " " + y + " " + ((direction + 1) % 4), current + 1000);
                }
                String right3 = x + " " + y + " " + ((direction + 3) % 4) + " " + (current + 1000) + " "+ currentPath + "RRR";
                if((cache.getOrDefault(x + " " + y + " " + ((direction + 3) % 4), Integer.MAX_VALUE) > current + 1000) && !currentPath.endsWith("R")){
                    queue.add(right3);
                    cache.put(x + " " + y + " " + ((direction + 3) % 4), current + 1000);
                }
            }
        }
        return answer;
    }

    private List<String> findOthers(int[][] grid, Coordinate reindeer, Coordinate exit, String best){
        long answer = Long.MAX_VALUE;
        List<String> result = new ArrayList<>();
        Queue<String> queue = new PriorityQueue<>();
        queue.add(reindeer.x + " " + reindeer.y + " " + 0 + " " + 0 + " S");
        Map<String, Integer> cache = new HashMap<>();
        while(!queue.isEmpty()){
            String element = queue.poll();
            String[] parts = element.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int direction = Integer.parseInt(parts[2]);
            int current = Integer.parseInt(parts[3]);
            String currentPath = parts[4];
            if(x == exit.x && y == exit.y){
                if(current < answer){
                    result = new ArrayList<>();
                    answer = current;
                }
                else{
                    if(current == answer){
                        result.add(currentPath);
                    }
                }
            }
            else{
                int newx = x + xs[direction];
                int newy = y + ys[direction];
                if(safe(newx, newy, grid) && grid[newx][newy] != 2) {
                    Coordinate newReindeer = new Coordinate(newx, newy);
                    String newHash = newReindeer.x + " " + newReindeer.y + " " + direction + " " + (current + 1) + " " + currentPath + "F";
                    if((cache.getOrDefault(newReindeer.x + " " + newReindeer.y + " " + direction, Integer.MAX_VALUE) >= current + 1)){
                        queue.add(newHash);
                        cache.put(newReindeer.x + " " + newReindeer.y + " " + direction, current + 1);

                    }
                }
                String right = x + " " + y + " " + ((direction + 1) % 4) + " " + (current + 1000) + " " + currentPath + "R";
                if((cache.getOrDefault(x + " " + y + " " + ((direction + 1) % 4), Integer.MAX_VALUE) >= current + 1000) && !currentPath.endsWith("RRR")){
                    queue.add(right);
                    cache.put(x + " " + y + " " + ((direction + 1) % 4), current + 1000);
                }
                String right3 = x + " " + y + " " + ((direction + 3) % 4) + " " + (current + 1000) + " "+ currentPath + "RRR";
                if((cache.getOrDefault(x + " " + y + " " + ((direction + 3) % 4), Integer.MAX_VALUE) >= current + 1000) && !currentPath.endsWith("R")){
                    queue.add(right3);
                    cache.put(x + " " + y + " " + ((direction + 3) % 4), current + 1000);
                }
            }
        }
        return result;
    }

    private String bestPath(int[][] grid, Coordinate reindeer, Coordinate exit){
        long answer = Long.MAX_VALUE;
        String result = "";
        Queue<String> queue = new PriorityQueue<>();
        queue.add(reindeer.x + " " + reindeer.y + " " + 0 + " " + 0 + " S");
        Map<String, Integer> cache = new HashMap<>();
        while(!queue.isEmpty()){
            String element = queue.poll();
            String[] parts = element.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int direction = Integer.parseInt(parts[2]);
            int current = Integer.parseInt(parts[3]);
            String currentPath = parts[4];
            if(x == exit.x && y == exit.y){
                if(answer > current){
                    result = currentPath;
                    answer = current;
                }
            }
            else{
                int newx = x + xs[direction];
                int newy = y + ys[direction];
                if(safe(newx, newy, grid) && grid[newx][newy] != 2) {
                    Coordinate newReindeer = new Coordinate(newx, newy);
                    String newHash = newReindeer.x + " " + newReindeer.y + " " + direction + " " + (current + 1) + " " + currentPath + "F";
                    if((cache.getOrDefault(newReindeer.x + " " + newReindeer.y + " " + direction, Integer.MAX_VALUE) > current + 1)){
                        queue.add(newHash);
                        cache.put(newReindeer.x + " " + newReindeer.y + " " + direction, current + 1);
                    }
                }
                String right = x + " " + y + " " + ((direction + 1) % 4) + " " + (current + 1000) + " " + currentPath + "R";
                if((cache.getOrDefault(x + " " + y + " " + ((direction + 1) % 4), Integer.MAX_VALUE) > current + 1000) && !currentPath.endsWith("RRR")){
                    queue.add(right);
                    cache.put(x + " " + y + " " + ((direction + 1) % 4), current + 1000);
                }
                String right3 = x + " " + y + " " + ((direction + 3) % 4) + " " + (current + 1000) + " "+ currentPath + "RRR";
                if((cache.getOrDefault(x + " " + y + " " + ((direction + 3) % 4), Integer.MAX_VALUE) > current + 1000) && !currentPath.endsWith("R")){
                    queue.add(right3);
                    cache.put(x + " " + y + " " + ((direction + 3) % 4), current + 1000);
                }
            }
        }
        return result;
    }

}
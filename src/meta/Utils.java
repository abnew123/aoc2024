package src.meta;

import src.objects.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class Utils {
    public static long gcd(long a, long b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    public static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 4) == 0) return false;
        }
        return true;
    }

    public static int gcd(int[] nums) {
        int result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result = gcd(result, nums[i]);
        }
        return result;
    }

    public static int lcm(int[] nums){
        int result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result = lcm(result, nums[i]);
        }
        return result;
    }

    public static long gcd(long[] nums) {
        long result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result = gcd(result, nums[i]);
        }
        return result;
    }

    public static long lcm(long[] nums){
        long result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result = lcm(result, nums[i]);
        }
        return result;
    }

    public static boolean safe(int x, int y, int[][] grid){
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public static boolean safe(int x, int y, char[][] grid){
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    public static List<Coordinate> getNeighbors(int x, int y, int[][] grid){
        List<Coordinate> neighbors = new ArrayList<>();
        int[] xs = new int[]{1,-1,0,0};
        int[] ys = new int[]{0,0,1,-1};
        for (int i = 0; i < 4; i++) {
            int nx = x + xs[i];
            int ny = y + ys[i];
            if (safe(nx, ny, grid)) {
                neighbors.add(new Coordinate(nx, ny));
            }
        }
        return neighbors;
    }

    public static List<Coordinate> getNeighborsFull(int x, int y, int[][] grid){
        List<Coordinate> neighbors = new ArrayList<>();
        int[] xs = new int[]{1,1,1,0,0,-1,-1,-1};
        int[] ys = new int[]{1,0,-1,1,-1,1,0,-1};
        for (int i = 0; i < 4; i++) {
            int nx = x + xs[i];
            int ny = y + ys[i];
            if (safe(nx, ny, grid)) {
                neighbors.add(new Coordinate(nx, ny));
            }
        }
        return neighbors;
    }

    public static int[][] convertCharArrayToIntArray(char[][] charArray, Function<Character, Integer> converter) {
        int[][] result = new int[charArray.length][charArray[0].length];
        for (int i = 0; i < charArray.length; i++) {
            for (int j = 0; j < charArray[i].length; j++) {
                result[i][j] = converter.apply(charArray[i][j]);
            }
        }
        return result;
    }



    public static int[][] buildGrid(List<String> lines, Function<Character, Integer> converter){
        int[][] grid = new int[lines.get(0).length()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                grid[j][i] = converter.apply(line.charAt(j));
            }
        }
        return grid;
    }

    public static char[][] buildGrid(List<String> lines){
        char[][] grid = new char[lines.get(0).length()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                grid[j][i] = line.charAt(j);
            }
        }
        return grid;
    }

    public static char[][] getGrid(Scanner in){
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            lines.add(in.nextLine());
        }
        return buildGrid(lines);
    }

    public static long det(long x1, long x2, long y1, long y2){
        return x1 * y2 - x2 * y1;
    }

    //TODO: kruskal's algorithm, prim's algorithm, dijkstras, bfs, dfs

}
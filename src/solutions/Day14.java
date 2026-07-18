package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.Scanner;

public class Day14 extends DayTemplate {

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int DENSITY_THRESHOLD = 30;

    @Override
    public String[] fullSolve(Scanner in) {
        Robots robots = parse(in);
        return new String[]{
                safetyFactor(robots, WIDTH, HEIGHT, 100) + "",
                treeTime(robots, WIDTH, HEIGHT, DENSITY_THRESHOLD) + ""
        };
    }

    @Override
    public String solve(boolean part1, Scanner in) {
        Robots robots = parse(in);
        return (part1
                ? safetyFactor(robots, WIDTH, HEIGHT, 100)
                : treeTime(robots, WIDTH, HEIGHT, DENSITY_THRESHOLD)) + "";
    }

    private static Robots parse(Scanner in) {
        int[] x = new int[512];
        int[] y = new int[512];
        int[] vx = new int[512];
        int[] vy = new int[512];
        int count = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            if (count == x.length) {
                int size = x.length * 2;
                x = Arrays.copyOf(x, size);
                y = Arrays.copyOf(y, size);
                vx = Arrays.copyOf(vx, size);
                vy = Arrays.copyOf(vy, size);
            }
            int[] values = new int[4];
            int valueCount = 0;
            for (int index = 0; index < line.length() && valueCount < values.length;) {
                char current = line.charAt(index);
                if (current == '-' || current >= '0' && current <= '9') {
                    int sign = 1;
                    if (current == '-') {
                        sign = -1;
                        index++;
                    }
                    int value = 0;
                    while (index < line.length()) {
                        char digit = line.charAt(index);
                        if (digit < '0' || digit > '9') {
                            break;
                        }
                        value = value * 10 + digit - '0';
                        index++;
                    }
                    values[valueCount++] = sign * value;
                } else {
                    index++;
                }
            }
            if (valueCount != 4) {
                throw new IllegalArgumentException("Malformed robot: " + line);
            }
            x[count] = values[0];
            y[count] = values[1];
            vx[count] = values[2];
            vy[count] = values[3];
            count++;
        }
        return new Robots(
                Arrays.copyOf(x, count),
                Arrays.copyOf(y, count),
                Arrays.copyOf(vx, count),
                Arrays.copyOf(vy, count));
    }

    private static long safetyFactor(Robots robots, int width, int height, int time) {
        int middleX = width / 2;
        int middleY = height / 2;
        int[] quadrants = new int[4];
        for (int robot = 0; robot < robots.x.length; robot++) {
            int x = Math.floorMod(robots.x[robot] + robots.vx[robot] * time, width);
            int y = Math.floorMod(robots.y[robot] + robots.vy[robot] * time, height);
            if (x == middleX || y == middleY) {
                continue;
            }
            int quadrant = (x > middleX ? 0 : 2) + (y < middleY ? 1 : 0);
            quadrants[quadrant]++;
        }
        return (long) quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3];
    }

    private static int treeTime(Robots robots, int width, int height, int threshold) {
        boolean[] denseX = denseResidues(robots.x, robots.vx, width, threshold);
        boolean[] denseY = denseResidues(robots.y, robots.vy, height, threshold);
        int inverse = modularInverse(width, height);
        int period = width * height;
        int best = Integer.MAX_VALUE;
        for (int xResidue = 0; xResidue < width; xResidue++) {
            if (!denseX[xResidue]) {
                continue;
            }
            for (int yResidue = 0; yResidue < height; yResidue++) {
                if (!denseY[yResidue]) {
                    continue;
                }
                int multiplier = Math.floorMod((yResidue - xResidue) * inverse, height);
                int time = xResidue + width * multiplier;
                if (time == 0) {
                    time = period;
                }
                best = Math.min(best, time);
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    private static boolean[] denseResidues(int[] positions, int[] velocities,
                                           int modulus, int threshold) {
        boolean[] dense = new boolean[modulus];
        int[] counts = new int[modulus];
        for (int time = 0; time < modulus; time++) {
            Arrays.fill(counts, 0);
            for (int robot = 0; robot < positions.length; robot++) {
                int position = Math.floorMod(positions[robot] + velocities[robot] * time, modulus);
                if (++counts[position] > threshold) {
                    dense[time] = true;
                    break;
                }
            }
        }
        return dense;
    }

    private static int modularInverse(int value, int modulus) {
        int oldRemainder = value;
        int remainder = modulus;
        int oldCoefficient = 1;
        int coefficient = 0;
        while (remainder != 0) {
            int quotient = oldRemainder / remainder;
            int nextRemainder = oldRemainder - quotient * remainder;
            oldRemainder = remainder;
            remainder = nextRemainder;
            int nextCoefficient = oldCoefficient - quotient * coefficient;
            oldCoefficient = coefficient;
            coefficient = nextCoefficient;
        }
        if (oldRemainder != 1) {
            throw new IllegalArgumentException("Grid dimensions must be coprime");
        }
        return Math.floorMod(oldCoefficient, modulus);
    }

    private record Robots(int[] x, int[] y, int[] vx, int[] vy) {
    }
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day14 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        List<int[]> robots = new ArrayList<>();
        while (in.hasNextLine()) {
            String[] p = in.nextLine().split(" |,|=");
            robots.add(new int[]{Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[4]), Integer.parseInt(p[5])});
        }
        if (part1) {
            int[] q = new int[4];
            for (int[] r : robots) {
                move(r, 100);
                if (r[0] != 50 && r[1] != 51) {
                    q[(r[0] < 50 ? 0 : 1) + (r[1] < 51 ? 0 : 2)]++;
                }
            }
            return "" + (long) q[0] * q[1] * q[2] * q[3];
        }
        int[] xs = new int[101], ys = new int[103];
        for (int time = 1, jump = 1; time < 10403; time += jump) {
            for (int[] r : robots) {
                move(r, jump);
            }
            boolean bx = crowded(robots, xs, 0), by = crowded(robots, ys, 1);
            if (bx && by) {
                return "" + time;
            }
            if (bx) {
                jump = 101;
            }
        }
        return "0";
    }

    private void move(int[] r, int n) {
        r[0] = ((r[0] + r[2] * n) % 101 + 101) % 101;
        r[1] = ((r[1] + r[3] * n) % 103 + 103) % 103;
    }

    private boolean crowded(List<int[]> robots, int[] count, int axis) {
        Arrays.fill(count, 0);
        for (int[] r : robots) {
            if (++count[r[axis]] > 30) {
                return true;
            }
        }
        return false;
    }
}

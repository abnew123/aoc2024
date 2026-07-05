package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day18 extends DayTemplate {

    private static final int GRID_SIZE = 71;
    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};

    public String solve(boolean part1, Scanner in) {
        List<Integer> bytes = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int comma = line.indexOf(',');
            int x = Integer.parseInt(line.substring(0, comma));
            int y = Integer.parseInt(line.substring(comma + 1));
            bytes.add(y * GRID_SIZE + x);
        }

        if (part1) {
            return bfs(1024, bytes) + "";
        }

        int high = bytes.size() - 1;
        int low = 0;
        while (low < high) {
            int mid = (low + high) / 2;
            if (bfs(mid, bytes) == -1) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        int answer = bytes.get(low - 1);
        return (answer % GRID_SIZE) + "," + (answer / GRID_SIZE);
    }

    private int bfs(int limit, List<Integer> bytes) {
        boolean[] blocked = new boolean[GRID_SIZE * GRID_SIZE];
        for (int i = 0; i < limit; i++) {
            blocked[bytes.get(i)] = true;
        }

        boolean[] seen = new boolean[blocked.length];
        int[] queue = new int[blocked.length];
        int head = 0;
        int tail = 0;
        queue[tail++] = 0;
        seen[0] = true;
        int target = blocked.length - 1;
        int steps = 0;

        while (head < tail) {
            int layerEnd = tail;
            while (head < layerEnd) {
                int current = queue[head++];
                if (current == target) {
                    return steps;
                }

                int row = current / GRID_SIZE;
                int col = current % GRID_SIZE;
                for (int dir = 0; dir < 4; dir++) {
                    int nextRow = row + DR[dir];
                    int nextCol = col + DC[dir];
                    if (nextRow < 0 || nextCol < 0 || nextRow >= GRID_SIZE || nextCol >= GRID_SIZE) {
                        continue;
                    }
                    int next = nextRow * GRID_SIZE + nextCol;
                    if (!blocked[next] && !seen[next]) {
                        seen[next] = true;
                        queue[tail++] = next;
                    }
                }
            }
            steps++;
        }
        return -1;
    }
}

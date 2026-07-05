package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day09 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        int[] a = in.nextLine().chars().map(c -> c - '0').toArray();
        return "" + (part1 ? part1(a) : part2(a));
    }

    private long part1(int[] a) {
        int n = Arrays.stream(a).sum(), p = 0;
        int[] disk = new int[n];
        Arrays.fill(disk, -1);
        for (int i = 0; i < a.length; p += a[i++]) {
            if (i % 2 == 0) {
                Arrays.fill(disk, p, p + a[i], i / 2);
            }
        }
        long answer = 0;
        for (int i = 0, j = n - 1; i < n; i++) {
            if (disk[i] < 0) {
                while (j > i && disk[j] < 0) {
                    j--;
                }
                if (j > i) {
                    disk[i] = disk[j];
                    disk[j--] = -1;
                }
            }
            if (disk[i] >= 0) {
                answer += (long) i * disk[i];
            }
        }
        return answer;
    }

    private long part2(int[] a) {
        int files = (a.length + 1) / 2, p = 0;
        int[] start = new int[files], size = new int[files];
        PriorityQueue<Gap>[] gaps = new PriorityQueue[10];
        for (int i = 0; i < gaps.length; i++) {
            gaps[i] = new PriorityQueue<>(Comparator.comparingInt(g -> g.start));
        }
        for (int i = 0; i < a.length; p += a[i++]) {
            if (i % 2 == 0) {
                start[i / 2] = p;
                size[i / 2] = a[i];
            } else if (a[i] > 0) {
                gaps[a[i]].add(new Gap(p, a[i]));
            }
        }
        long answer = 0;
        for (int f = files - 1; f >= 0; f--) {
            Gap best = null;
            int bucket = 0;
            for (int s = size[f]; s < gaps.length; s++) {
                Gap g = gaps[s].peek();
                if (g != null && g.start < start[f] && (best == null || g.start < best.start)) {
                    best = g;
                    bucket = s;
                }
            }
            int at = start[f];
            if (best != null) {
                gaps[bucket].poll();
                at = best.start;
                if (best.size > size[f]) {
                    gaps[best.size - size[f]].add(new Gap(at + size[f], best.size - size[f]));
                }
            }
            answer += (long) f * size[f] * (2L * at + size[f] - 1) / 2;
        }
        return answer;
    }

    private record Gap(int start, int size) {}
}

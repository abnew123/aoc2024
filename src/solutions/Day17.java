package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day17 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        long a = 15401536;
        in.nextLine();
        long b = Long.parseLong(in.nextLine().split(" ")[2]);
        long c = Long.parseLong(in.nextLine().split(" ")[2]);
        in.nextLine();
        int[] p = Arrays.stream(in.nextLine().split(" ")[1].split(",")).mapToInt(Integer::parseInt).toArray();
        if (part1) {
            return run(p, a, b, c).toString().replace(" ", "");
        }
        List<Long> candidates = List.of(0L);
        for (int from = p.length - 1; from >= 0; from--) {
            List<Long> next = new ArrayList<>();
            for (long prefix : candidates) {
                for (int digit = 0; digit < 8; digit++) {
                    long value = prefix << 3 | digit;
                    if (suffix(run(p, value, 0, 0), p, from)) {
                        next.add(value);
                    }
                }
            }
            candidates = next;
        }
        return "" + Collections.min(candidates);
    }

    private List<Integer> run(int[] p, long a, long b, long c) {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < p.length;) {
            int op = p[i++], x = p[i++];
            long combo = x < 4 ? x : x == 4 ? a : x == 5 ? b : c;
            switch (op) {
                case 0 -> a = shr(a, combo);
                case 1 -> b ^= x;
                case 2 -> b = combo & 7;
                case 3 -> {
                    if (a != 0) {
                        i = x;
                    }
                }
                case 4 -> b ^= c;
                case 5 -> out.add((int) (combo & 7));
                case 6 -> b = shr(a, combo);
                case 7 -> c = shr(a, combo);
            }
        }
        return out;
    }

    private long shr(long a, long b) {
        return b >= 64 ? 0 : a >> b;
    }

    private boolean suffix(List<Integer> out, int[] p, int from) {
        if (out.size() != p.length - from) {
            return false;
        }
        for (int i = from; i < p.length; i++) {
            if (out.get(i - from) != p[i]) {
                return false;
            }
        }
        return true;
    }
}

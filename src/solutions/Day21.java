package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day21 extends DayTemplate {
    private static final String[] NUM = {"789", "456", "123", " 0A"}, DIR = {" ^A", "<v>"};
    private final Map<String, Long> memo = new HashMap<>();

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        while (in.hasNextLine()) {
            String code = in.nextLine();
            long n = Long.parseLong(code.substring(0, code.length() - 1)), cost = 0;
            char from = 'A';
            for (char to : code.toCharArray()) {
                cost += cost(from, to, part1 ? 2 : 25, NUM);
                from = to;
            }
            answer += n * cost;
        }
        return "" + answer;
    }

    private long cost(char from, char to, int depth, String[] pad) {
        String key = (pad == NUM ? "N" : "D") + from + to + depth;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        long best = Long.MAX_VALUE;
        for (String path : paths(from, to, pad)) {
            best = Math.min(best, sequence(path + "A", depth));
        }
        memo.put(key, best);
        return best;
    }

    private long sequence(String path, int depth) {
        if (depth == 0) {
            return path.length();
        }
        long total = 0;
        char from = 'A';
        for (char to : path.toCharArray()) {
            total += cost(from, to, depth - 1, DIR);
            from = to;
        }
        return total;
    }

    private List<String> paths(char from, char to, String[] pad) {
        int ar = 0, ac = 0, br = 0, bc = 0;
        for (int r = 0; r < pad.length; r++) {
            for (int c = 0; c < pad[r].length(); c++) {
                if (pad[r].charAt(c) == from) {
                    ar = r;
                    ac = c;
                }
                if (pad[r].charAt(c) == to) {
                    br = r;
                    bc = c;
                }
            }
        }
        List<String> out = new ArrayList<>(), q = new ArrayList<>();
        q.add(ar + "," + ac + ",");
        for (int i = 0; i < q.size(); i++) {
            String[] state = q.get(i).split(",", 3);
            int r = Integer.parseInt(state[0]), c = Integer.parseInt(state[1]);
            String path = state[2];
            if (r == br && c == bc) {
                out.add(path);
                continue;
            }
            add(q, pad, r, c, r - 1, c, br, bc, path, '^');
            add(q, pad, r, c, r + 1, c, br, bc, path, 'v');
            add(q, pad, r, c, r, c - 1, br, bc, path, '<');
            add(q, pad, r, c, r, c + 1, br, bc, path, '>');
        }
        return out;
    }

    private void add(List<String> q, String[] pad, int oldR, int oldC, int r, int c, int br, int bc, String path, char move) {
        if (r >= 0 && r < pad.length && c >= 0 && c < pad[r].length()
                && pad[r].charAt(c) != ' '
                && Math.abs(r - br) + Math.abs(c - bc) < Math.abs(oldR - br) + Math.abs(oldC - bc)) {
            q.add(r + "," + c + "," + path + move);
        }
    }
}

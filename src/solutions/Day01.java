package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day01 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        List<Integer> a = new ArrayList<>(), b = new ArrayList<>();
        Map<Integer, Integer> count = new HashMap<>();
        while (in.hasNextInt()) {
            a.add(in.nextInt());
            int x = in.nextInt();
            b.add(x);
            count.merge(x, 1, Integer::sum);
        }
        long answer = 0;
        if (part1) {
            Collections.sort(a);
            Collections.sort(b);
            for (int i = 0; i < a.size(); i++) {
                answer += Math.abs(a.get(i) - b.get(i));
            }
        } else {
            for (int x : a) {
                answer += (long) x * count.getOrDefault(x, 0);
            }
        }
        return "" + answer;
    }
}

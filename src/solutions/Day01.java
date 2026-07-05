package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day01 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        Map<Integer, Integer> rightCounts = new HashMap<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int separator = line.indexOf(' ');
            int leftValue = Integer.parseInt(line.substring(0, separator));
            int rightValue = Integer.parseInt(line.substring(line.lastIndexOf(' ') + 1));
            left.add(leftValue);
            right.add(rightValue);
            rightCounts.merge(rightValue, 1, Integer::sum);
        }

        long answer = 0;
        if (part1) {
            Collections.sort(left);
            Collections.sort(right);
            for (int i = 0; i < left.size(); i++) {
                answer += Math.abs(left.get(i) - right.get(i));
            }
        } else {
            for (int value : left) {
                answer += (long) value * rightCounts.getOrDefault(value, 0);
            }
        }
        return answer + "";
    }
}

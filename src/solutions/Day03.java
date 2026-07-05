package src.solutions;

import src.meta.DayTemplate;

import java.util.*;
import java.util.regex.*;

public class Day03 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        String text = in.useDelimiter("\\A").next();
        Matcher m = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)").matcher(text);
        long answer = 0;
        boolean on = true;
        while (m.find()) {
            if (m.group().equals("do()")) {
                on = true;
            } else if (m.group().equals("don't()")) {
                on = false;
            } else if (part1 || on) {
                answer += (long) Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
            }
        }
        return "" + answer;
    }
}

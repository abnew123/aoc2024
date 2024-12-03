package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        StringBuilder input = new StringBuilder();
        while (in.hasNext()) {
            input.append(in.nextLine());
        }

        if (part1) {
            answer += multiply(input.toString());
        } else {
            input.insert(0, "do()");
            input.append("don't()");
            Pattern pattern = Pattern.compile("do\\(\\)(.*?)don't\\(\\)");
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                answer += multiply(matcher.group(1));
            }
        }
        return answer + "";
    }

    private int multiply(String line) {
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(line);
        int sum = 0;
        while (matcher.find()) {
            int factor1 = Integer.parseInt(matcher.group(1));
            int factor2 = Integer.parseInt(matcher.group(2));
            sum += factor1 * factor2;
        }
        return sum;
    }
}

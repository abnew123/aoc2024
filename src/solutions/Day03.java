package src.solutions;

import src.meta.DayTemplate;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends DayTemplate {

    public String[] fullSolve(Scanner in) {
        StringBuilder input = new StringBuilder();
        while (in.hasNext()) {
            input.append(in.nextLine());
        }

        long answer1 = multiply(input.toString());

        // Part 2 mutates the buffer (insert/append). Read part 1's value off it first, then apply
        // exactly the same mutations solve(false, ...) applies.
        long answer2 = 0;
        input.insert(0, "do()");
        input.append("don't()");
        Pattern pattern = Pattern.compile("do\\(\\)(.*?)don't\\(\\)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            answer2 += multiply(matcher.group(1));
        }
        return new String[]{answer1 + "", answer2 + ""};
    }

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

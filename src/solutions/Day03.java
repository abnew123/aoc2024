package src.solutions;

import src.meta.DayTemplate;

import java.util.Scanner;

public class Day03 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long[] answers = calculate(readInput(in));
        return answers[part1 ? 0 : 1] + "";
    }

    @Override
    public String[] fullSolve(Scanner in) {
        long[] answers = calculate(readInput(in));
        return new String[]{answers[0] + "", answers[1] + ""};
    }

    private String readInput(Scanner in) {
        StringBuilder input = new StringBuilder();
        while (in.hasNextLine()) {
            input.append(in.nextLine());
        }
        return input.toString();
    }

    private long[] calculate(String input) {
        long part1 = 0;
        long part2 = 0;
        boolean enabled = true;
        for (int index = 0; index < input.length(); index++) {
            if (input.startsWith("do()", index)) {
                enabled = true;
                index += 3;
                continue;
            }
            if (input.startsWith("don't()", index)) {
                enabled = false;
                index += 6;
                continue;
            }
            if (!input.startsWith("mul(", index)) {
                continue;
            }

            int cursor = index + 4;
            int first = 0;
            int firstDigits = 0;
            while (cursor < input.length() && firstDigits < 3) {
                char digit = input.charAt(cursor);
                if (digit < '0' || digit > '9') {
                    break;
                }
                first = first * 10 + digit - '0';
                firstDigits++;
                cursor++;
            }
            if (firstDigits == 0 || cursor >= input.length() || input.charAt(cursor) != ',') {
                continue;
            }

            cursor++;
            int second = 0;
            int secondDigits = 0;
            while (cursor < input.length() && secondDigits < 3) {
                char digit = input.charAt(cursor);
                if (digit < '0' || digit > '9') {
                    break;
                }
                second = second * 10 + digit - '0';
                secondDigits++;
                cursor++;
            }
            if (secondDigits == 0 || cursor >= input.length() || input.charAt(cursor) != ')') {
                continue;
            }

            long product = (long) first * second;
            part1 += product;
            if (enabled) {
                part2 += product;
            }
            index = cursor;
        }
        return new long[]{part1, part2};
    }
}

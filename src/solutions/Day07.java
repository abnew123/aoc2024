package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Day07 extends DayTemplate {

    private static final int PART_ONE = 1;
    private static final int PART_TWO = 2;

    @Override
    public String[] fullSolve(Scanner in) {
        Answers answers = analyze(in);
        return new String[]{answers.partOne(), answers.partTwo()};
    }

    @Override
    public String solve(boolean part1, Scanner in) {
        Answers answers = analyze(in);
        return part1 ? answers.partOne() : answers.partTwo();
    }

    private Answers analyze(Scanner in) {
        ExactTotal partOne = new ExactTotal();
        ExactTotal partTwo = new ExactTotal();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isBlank()) {
                continue;
            }
            try {
                LongEquation equation = parseLongEquation(line);
                int reachable = reachable(equation.target(), equation.values(),
                        equation.divisors(), equation.values().length - 1);
                if ((reachable & PART_ONE) != 0) {
                    partOne.add(equation.target());
                }
                if ((reachable & PART_TWO) != 0) {
                    partTwo.add(equation.target());
                }
            } catch (ArithmeticException | NumberFormatException e) {
                BigEquation equation = parseBigEquation(line);
                int reachable = reachable(equation.target(), equation.values(),
                        equation.divisors(), equation.values().length - 1);
                if ((reachable & PART_ONE) != 0) {
                    partOne.add(equation.target());
                }
                if ((reachable & PART_TWO) != 0) {
                    partTwo.add(equation.target());
                }
            }
        }
        return new Answers(partOne.toString(), partTwo.toString());
    }

    private int reachable(long target, long[] values, long[] divisors, int index) {
        if (index == 0) {
            return target == values[0] ? PART_ONE | PART_TWO : 0;
        }
        long operand = values[index];
        int result = 0;
        if (target >= operand) {
            result |= reachable(target - operand, values, divisors, index - 1);
            if (result == (PART_ONE | PART_TWO)) {
                return result;
            }
        }
        if (operand == 0) {
            if (target == 0) {
                result |= PART_ONE | PART_TWO;
            }
        } else if (target % operand == 0) {
            result |= reachable(target / operand, values, divisors, index - 1);
        }
        if (result == (PART_ONE | PART_TWO)) {
            return result;
        }
        long divisor = divisors[index];
        if (target >= operand && target % divisor == operand) {
            result |= reachable(target / divisor, values, divisors, index - 1) & PART_TWO;
        }
        return result;
    }

    private int reachable(BigInteger target, BigInteger[] values,
                          BigInteger[] divisors, int index) {
        if (index == 0) {
            return target.equals(values[0]) ? PART_ONE | PART_TWO : 0;
        }
        BigInteger operand = values[index];
        int result = 0;
        if (target.compareTo(operand) >= 0) {
            result |= reachable(target.subtract(operand), values, divisors, index - 1);
            if (result == (PART_ONE | PART_TWO)) {
                return result;
            }
        }
        if (operand.signum() == 0) {
            if (target.signum() == 0) {
                result |= PART_ONE | PART_TWO;
            }
        } else {
            BigInteger[] division = target.divideAndRemainder(operand);
            if (division[1].signum() == 0) {
                result |= reachable(division[0], values, divisors, index - 1);
            }
        }
        if (result == (PART_ONE | PART_TWO)) {
            return result;
        }
        BigInteger[] concatenation = target.divideAndRemainder(divisors[index]);
        if (concatenation[1].equals(operand)) {
            result |= reachable(concatenation[0], values, divisors, index - 1) & PART_TWO;
        }
        return result;
    }

    private LongEquation parseLongEquation(String line) {
        int colon = line.indexOf(':');
        if (colon < 0) {
            throw new NumberFormatException("Missing colon");
        }
        long target = parseLongToken(line, 0, colon);
        long[] values = new long[16];
        long[] divisors = new long[16];
        int size = 0;
        int index = colon + 1;
        while (true) {
            while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
                index++;
            }
            if (index == line.length()) {
                break;
            }
            int start = index;
            while (index < line.length() && !Character.isWhitespace(line.charAt(index))) {
                index++;
            }
            if (size == values.length) {
                values = Arrays.copyOf(values, size * 2);
                divisors = Arrays.copyOf(divisors, size * 2);
            }
            values[size] = parseLongToken(line, start, index);
            long divisor = 1;
            for (int digit = start; digit < index; digit++) {
                divisor = Math.multiplyExact(divisor, 10);
            }
            divisors[size++] = divisor;
        }
        if (size == 0) {
            throw new NumberFormatException("Missing operands");
        }
        return new LongEquation(target, Arrays.copyOf(values, size),
                Arrays.copyOf(divisors, size));
    }

    private BigEquation parseBigEquation(String line) {
        int colon = line.indexOf(':');
        if (colon < 0) {
            throw new NumberFormatException("Missing colon");
        }
        BigInteger target = parseBigToken(line, 0, colon);
        BigInteger[] values = new BigInteger[16];
        BigInteger[] divisors = new BigInteger[16];
        int size = 0;
        int index = colon + 1;
        while (true) {
            while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
                index++;
            }
            if (index == line.length()) {
                break;
            }
            int start = index;
            while (index < line.length() && !Character.isWhitespace(line.charAt(index))) {
                index++;
            }
            if (size == values.length) {
                values = Arrays.copyOf(values, size * 2);
                divisors = Arrays.copyOf(divisors, size * 2);
            }
            values[size] = parseBigToken(line, start, index);
            divisors[size++] = BigInteger.TEN.pow(index - start);
        }
        if (size == 0) {
            throw new NumberFormatException("Missing operands");
        }
        return new BigEquation(target, Arrays.copyOf(values, size),
                Arrays.copyOf(divisors, size));
    }

    private long parseLongToken(String line, int from, int to) {
        int start = from;
        int end = to;
        while (start < end && Character.isWhitespace(line.charAt(start))) {
            start++;
        }
        while (end > start && Character.isWhitespace(line.charAt(end - 1))) {
            end--;
        }
        if (start == end) {
            throw new NumberFormatException("Empty number");
        }
        for (int i = start; i < end; i++) {
            if (line.charAt(i) < '0' || line.charAt(i) > '9') {
                throw new NumberFormatException("Numbers must be unsigned decimal integers");
            }
        }
        return Long.parseLong(line, start, end, 10);
    }

    private BigInteger parseBigToken(String line, int from, int to) {
        int start = from;
        int end = to;
        while (start < end && Character.isWhitespace(line.charAt(start))) {
            start++;
        }
        while (end > start && Character.isWhitespace(line.charAt(end - 1))) {
            end--;
        }
        if (start == end) {
            throw new NumberFormatException("Empty number");
        }
        for (int i = start; i < end; i++) {
            if (line.charAt(i) < '0' || line.charAt(i) > '9') {
                throw new NumberFormatException("Numbers must be unsigned decimal integers");
            }
        }
        return new BigInteger(line.substring(start, end));
    }

    private record Answers(String partOne, String partTwo) {}
    private record LongEquation(long target, long[] values, long[] divisors) {}
    private record BigEquation(BigInteger target, BigInteger[] values, BigInteger[] divisors) {}

    private static final class ExactTotal {
        private long small;
        private BigInteger big;

        private void add(long value) {
            if (big != null) {
                big = big.add(BigInteger.valueOf(value));
                return;
            }
            try {
                small = Math.addExact(small, value);
            } catch (ArithmeticException e) {
                big = BigInteger.valueOf(small).add(BigInteger.valueOf(value));
            }
        }

        private void add(BigInteger value) {
            if (big == null) {
                big = BigInteger.valueOf(small);
            }
            big = big.add(value);
        }

        @Override
        public String toString() {
            return big == null ? Long.toString(small) : big.toString();
        }
    }
}

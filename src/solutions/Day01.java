package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Day01 extends DayTemplate {

    @Override
    public String solve(boolean part1, Scanner in) {
        BigInteger[] answers = solveBoth(in);
        return answers[part1 ? 0 : 1].toString();
    }

    @Override
    public String[] fullSolve(Scanner in) {
        BigInteger[] answers = solveBoth(in);
        return new String[]{answers[0].toString(), answers[1].toString()};
    }

    private BigInteger[] solveBoth(Scanner in) {
        Pairs pairs = parse(in);
        Arrays.sort(pairs.left);
        Arrays.sort(pairs.right);

        BigInteger distance = BigInteger.ZERO;
        for (int i = 0; i < pairs.left.length; i++) {
            distance = distance.add(pairs.left[i].subtract(pairs.right[i]).abs());
        }

        BigInteger similarity = BigInteger.ZERO;
        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < pairs.left.length && rightIndex < pairs.right.length) {
            int comparison = pairs.left[leftIndex].compareTo(pairs.right[rightIndex]);
            if (comparison < 0) {
                leftIndex++;
            } else if (comparison > 0) {
                rightIndex++;
            } else {
                BigInteger value = pairs.left[leftIndex];
                int leftEnd = leftIndex + 1;
                while (leftEnd < pairs.left.length && pairs.left[leftEnd].equals(value)) {
                    leftEnd++;
                }
                int rightEnd = rightIndex + 1;
                while (rightEnd < pairs.right.length && pairs.right[rightEnd].equals(value)) {
                    rightEnd++;
                }
                long pairCount = (long) (leftEnd - leftIndex) * (rightEnd - rightIndex);
                similarity = similarity.add(value.multiply(BigInteger.valueOf(pairCount)));
                leftIndex = leftEnd;
                rightIndex = rightEnd;
            }
        }
        return new BigInteger[]{distance, similarity};
    }

    private Pairs parse(Scanner in) {
        BigInteger[] left = new BigInteger[128];
        BigInteger[] right = new BigInteger[128];
        int size = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int index = skipWhitespace(line, 0);
            if (index == line.length()) {
                continue;
            }
            int leftStart = index;
            index = skipToken(line, index);
            int leftEnd = index;
            index = skipWhitespace(line, index);
            int rightStart = index;
            index = skipToken(line, index);
            int rightEnd = index;
            if (rightStart == rightEnd || skipWhitespace(line, index) != line.length()) {
                throw new IllegalArgumentException("Expected two location IDs: " + line);
            }
            if (size == left.length) {
                left = Arrays.copyOf(left, size * 2);
                right = Arrays.copyOf(right, size * 2);
            }
            left[size] = new BigInteger(line.substring(leftStart, leftEnd));
            right[size] = new BigInteger(line.substring(rightStart, rightEnd));
            size++;
        }
        return new Pairs(Arrays.copyOf(left, size), Arrays.copyOf(right, size));
    }

    private int skipWhitespace(String line, int index) {
        while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        return index;
    }

    private int skipToken(String line, int index) {
        while (index < line.length() && !Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        return index;
    }

    private record Pairs(BigInteger[] left, BigInteger[] right) {}
}

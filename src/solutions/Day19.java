package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.Scanner;

public class Day19 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        Answers answers = analyze(in, !part1);
        return part1 ? answers.possible + "" : answers.arrangements;
    }

    @Override
    public String[] fullSolve(Scanner in) {
        Answers answers = analyze(in, true);
        return new String[]{answers.possible + "", answers.arrangements};
    }

    private Answers analyze(Scanner in, boolean countArrangements) {
        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = input.length();
        if (length == 0) {
            throw new IllegalArgumentException("Missing towel patterns");
        }
        TowelNode root = new TowelNode();
        int patternsEnd = 0;
        while (patternsEnd < length
                && input.charAt(patternsEnd) != '\n' && input.charAt(patternsEnd) != '\r') {
            patternsEnd++;
        }
        int start = 0;
        for (int index = 0; index <= patternsEnd; index++) {
            if (index == patternsEnd || input.charAt(index) == ',') {
                String towel = input.substring(start, index).trim();
                if (towel.isEmpty()) {
                    throw new IllegalArgumentException("Towel patterns must not be empty");
                }
                addTowel(root, towel);
                start = index + 1;
            }
        }
        int position = patternsEnd;
        if (position < length && input.charAt(position) == '\r') {
            position++;
        }
        if (position < length && input.charAt(position) == '\n') {
            position++;
        }
        int blankEnd = position;
        while (blankEnd < length
                && input.charAt(blankEnd) != '\n' && input.charAt(blankEnd) != '\r') {
            blankEnd++;
        }
        if (position >= length || !input.substring(position, blankEnd).isBlank()) {
            throw new IllegalArgumentException("Missing blank line before designs");
        }
        position = blankEnd;

        long possibleCount = 0;
        long arrangementCount = 0;
        BigInteger largeArrangementCount = null;
        while (position < length) {
            char current = input.charAt(position);
            if (current == '\n' || current == '\r') {
                position++;
                continue;
            }
            int designEnd = position;
            while (designEnd < length
                    && input.charAt(designEnd) != '\n' && input.charAt(designEnd) != '\r') {
                designEnd++;
            }
            String design = input.substring(position, designEnd);
            position = designEnd;
            if (!countArrangements) {
                if (canMake(root, design)) {
                    possibleCount++;
                }
                continue;
            }
            DesignResult result = countDesign(root, design);
            if (result.possible) {
                possibleCount++;
            }
            if (result.largeArrangements != null) {
                if (largeArrangementCount == null) {
                    largeArrangementCount = BigInteger.valueOf(arrangementCount);
                }
                largeArrangementCount = largeArrangementCount.add(result.largeArrangements);
            } else if (largeArrangementCount != null) {
                largeArrangementCount = largeArrangementCount.add(BigInteger.valueOf(result.arrangements));
            } else {
                try {
                    arrangementCount = Math.addExact(arrangementCount, result.arrangements);
                } catch (ArithmeticException overflow) {
                    largeArrangementCount = BigInteger.valueOf(arrangementCount)
                            .add(BigInteger.valueOf(result.arrangements));
                }
            }
        }
        String arrangements = largeArrangementCount == null
                ? arrangementCount + "" : largeArrangementCount.toString();
        return new Answers(possibleCount, arrangements);
    }

    private boolean canMake(TowelNode root, String design) {
        boolean[] possible = new boolean[design.length() + 1];
        possible[design.length()] = true;
        for (int start = design.length() - 1; start >= 0; start--) {
            TowelNode current = root;
            for (int end = start; end < design.length(); end++) {
                current = current.next[colorIndex(design.charAt(end))];
                if (current == null) {
                    break;
                }
                if (current.end && possible[end + 1]) {
                    possible[start] = true;
                    break;
                }
            }
        }
        return possible[0];
    }

    private void addTowel(TowelNode root, String towel) {
        TowelNode current = root;
        for (int index = 0; index < towel.length(); index++) {
            int color = colorIndex(towel.charAt(index));
            if (current.next[color] == null) {
                current.next[color] = new TowelNode();
            }
            current = current.next[color];
        }
        current.end = true;
    }

    private DesignResult countDesign(TowelNode root, String design) {
        try {
            return countDesignLong(root, design);
        } catch (ArithmeticException overflow) {
            return countDesignBig(root, design);
        }
    }

    private DesignResult countDesignLong(TowelNode root, String design) {
        int length = design.length();
        boolean[] possible = new boolean[length + 1];
        long[] arrangements = new long[length + 1];
        possible[length] = true;
        arrangements[length] = 1;
        for (int start = length - 1; start >= 0; start--) {
            TowelNode current = root;
            boolean canFinish = false;
            long count = 0;
            for (int end = start; end < length; end++) {
                current = current.next[colorIndex(design.charAt(end))];
                if (current == null) {
                    break;
                }
                if (current.end) {
                    canFinish |= possible[end + 1];
                    count = Math.addExact(count, arrangements[end + 1]);
                }
            }
            possible[start] = canFinish;
            arrangements[start] = count;
        }
        return new DesignResult(possible[0], arrangements[0], null);
    }

    private DesignResult countDesignBig(TowelNode root, String design) {
        int length = design.length();
        boolean[] possible = new boolean[length + 1];
        BigInteger[] arrangements = new BigInteger[length + 1];
        possible[length] = true;
        arrangements[length] = BigInteger.ONE;
        for (int start = length - 1; start >= 0; start--) {
            TowelNode current = root;
            boolean canFinish = false;
            BigInteger count = BigInteger.ZERO;
            for (int end = start; end < length; end++) {
                current = current.next[colorIndex(design.charAt(end))];
                if (current == null) {
                    break;
                }
                if (current.end) {
                    canFinish |= possible[end + 1];
                    count = count.add(arrangements[end + 1]);
                }
            }
            possible[start] = canFinish;
            arrangements[start] = count;
        }
        return new DesignResult(possible[0], 0, arrangements[0]);
    }

    private int colorIndex(char color) {
        return switch (color) {
            case 'w' -> 0;
            case 'u' -> 1;
            case 'b' -> 2;
            case 'r' -> 3;
            case 'g' -> 4;
            default -> throw new IllegalArgumentException("Unknown towel color: " + color);
        };
    }

    private static class TowelNode {
        private boolean end;
        private final TowelNode[] next = new TowelNode[5];
    }

    private record DesignResult(boolean possible, long arrangements, BigInteger largeArrangements) {
    }

    private record Answers(long possible, String arrangements) {
    }
}

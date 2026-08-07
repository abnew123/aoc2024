package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Day21 extends DayTemplate {
    private static final char[] MOVES = {'^', 'v', '<', '>'};
    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final Keypad NUMERIC = new Keypad("789", "456", "123", " 0A");
    private static final Keypad DIRECTIONAL = new Keypad(" ^A", "<v>");

    @Override
    public String solve(boolean part1, Scanner in) {
        String[] codes = parse(in);
        return complexity(codes, numericCosts(part1 ? 2 : 25)).toString();
    }

    @Override
    public String[] fullSolve(Scanner in) {
        String[] codes = parse(in);
        long[][] lower = humanCosts();
        long[][] part1Costs = null;
        long[][] part2Costs = null;
        for (int depth = 1; depth <= 25; depth++) {
            lower = transitionCosts(DIRECTIONAL, lower);
            if (depth == 2) {
                part1Costs = transitionCosts(NUMERIC, lower);
            }
            if (depth == 25) {
                part2Costs = transitionCosts(NUMERIC, lower);
            }
        }
        return new String[]{complexity(codes, part1Costs).toString(),
                complexity(codes, part2Costs).toString()};
    }

    private String[] parse(Scanner in) {
        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = input.length();
        String[] codes = new String[8];
        int count = 0;
        int position = 0;
        while (position < length) {
            char current = input.charAt(position);
            if (current == '\n' || current == '\r') {
                position++;
                continue;
            }
            int lineEnd = position;
            while (lineEnd < length
                    && input.charAt(lineEnd) != '\n' && input.charAt(lineEnd) != '\r') {
                lineEnd++;
            }
            String code = input.substring(position, lineEnd).trim();
            position = lineEnd;
            if (code.isEmpty()) {
                continue;
            }
            if (code.charAt(code.length() - 1) != 'A') {
                throw new IllegalArgumentException("Code must end in A: " + code);
            }
            for (int i = 0; i < code.length() - 1; i++) {
                if (code.charAt(i) < '0' || code.charAt(i) > '9') {
                    throw new IllegalArgumentException("Invalid numeric code: " + code);
                }
            }
            if (count == codes.length) {
                codes = Arrays.copyOf(codes, count * 2);
            }
            codes[count++] = code;
        }
        if (count == 0) {
            throw new IllegalArgumentException("Missing door codes");
        }
        return Arrays.copyOf(codes, count);
    }

    private long[][] numericCosts(int depth) {
        long[][] lower = humanCosts();
        for (int i = 0; i < depth; i++) {
            lower = transitionCosts(DIRECTIONAL, lower);
        }
        return transitionCosts(NUMERIC, lower);
    }

    private long[][] humanCosts() {
        long[][] result = new long[DIRECTIONAL.size][DIRECTIONAL.size];
        for (long[] row : result) {
            Arrays.fill(row, 1);
        }
        return result;
    }

    private long[][] transitionCosts(Keypad controlled, long[][] lowerCosts) {
        long[][] result = new long[controlled.size][controlled.size];
        int commandCount = DIRECTIONAL.size;
        int activate = DIRECTIONAL.index('A');
        int states = controlled.size * commandCount;
        long[] distance = new long[states];
        boolean[] settled = new boolean[states];

        for (int from = 0; from < controlled.size; from++) {
            for (int to = 0; to < controlled.size; to++) {
                Arrays.fill(distance, Long.MAX_VALUE);
                Arrays.fill(settled, false);
                distance[from * commandCount + activate] = 0;
                long best = Long.MAX_VALUE;

                for (int iteration = 0; iteration < states; iteration++) {
                    int state = -1;
                    long stateCost = Long.MAX_VALUE;
                    for (int candidate = 0; candidate < states; candidate++) {
                        if (!settled[candidate] && distance[candidate] < stateCost) {
                            state = candidate;
                            stateCost = distance[candidate];
                        }
                    }
                    if (state < 0) {
                        break;
                    }
                    settled[state] = true;
                    int position = state / commandCount;
                    int lastCommand = state % commandCount;
                    if (position == to) {
                        best = Math.min(best, Math.addExact(stateCost,
                                lowerCosts[lastCommand][activate]));
                    }
                    for (int direction = 0; direction < MOVES.length; direction++) {
                        int nextPosition = controlled.neighbors[position][direction];
                        if (nextPosition < 0) {
                            continue;
                        }
                        int command = DIRECTIONAL.index(MOVES[direction]);
                        long nextCost = Math.addExact(stateCost, lowerCosts[lastCommand][command]);
                        int nextState = nextPosition * commandCount + command;
                        if (nextCost < distance[nextState]) {
                            distance[nextState] = nextCost;
                        }
                    }
                }
                if (best == Long.MAX_VALUE) {
                    throw new IllegalStateException("Unreachable keypad transition");
                }
                result[from][to] = best;
            }
        }
        return result;
    }

    private BigInteger complexity(String[] codes, long[][] costs) {
        BigInteger answer = BigInteger.ZERO;
        int activate = NUMERIC.index('A');
        for (String code : codes) {
            int current = activate;
            BigInteger presses = BigInteger.ZERO;
            for (int i = 0; i < code.length(); i++) {
                int next = NUMERIC.index(code.charAt(i));
                presses = presses.add(BigInteger.valueOf(costs[current][next]));
                current = next;
            }
            String numeric = code.substring(0, code.length() - 1);
            BigInteger value = numeric.isEmpty() ? BigInteger.ZERO : new BigInteger(numeric);
            answer = answer.add(presses.multiply(value));
        }
        return answer;
    }

    private static final class Keypad {
        private final int size;
        private final int[] indexByCharacter = new int[128];
        private final int[][] neighbors;

        private Keypad(String... rows) {
            Arrays.fill(indexByCharacter, -1);
            int[][] cell = new int[rows.length][];
            int count = 0;
            for (int row = 0; row < rows.length; row++) {
                cell[row] = new int[rows[row].length()];
                Arrays.fill(cell[row], -1);
                for (int column = 0; column < rows[row].length(); column++) {
                    char key = rows[row].charAt(column);
                    if (key != ' ') {
                        cell[row][column] = count;
                        indexByCharacter[key] = count++;
                    }
                }
            }
            size = count;
            neighbors = new int[size][MOVES.length];
            for (int[] row : neighbors) {
                Arrays.fill(row, -1);
            }
            for (int row = 0; row < cell.length; row++) {
                for (int column = 0; column < cell[row].length; column++) {
                    int key = cell[row][column];
                    if (key < 0) {
                        continue;
                    }
                    for (int direction = 0; direction < MOVES.length; direction++) {
                        int nextRow = row + DR[direction];
                        int nextColumn = column + DC[direction];
                        if (nextRow >= 0 && nextRow < cell.length && nextColumn >= 0
                                && nextColumn < cell[nextRow].length) {
                            neighbors[key][direction] = cell[nextRow][nextColumn];
                        }
                    }
                }
            }
        }

        private int index(char key) {
            if (key >= indexByCharacter.length || indexByCharacter[key] < 0) {
                throw new IllegalArgumentException("Key is not on keypad: " + key);
            }
            return indexByCharacter[key];
        }
    }
}

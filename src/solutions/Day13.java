package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.Scanner;

public class Day13 extends DayTemplate {

    private static final BigInteger PART_TWO_OFFSET = new BigInteger("10000000000000");
    private static final BigInteger PART_ONE_LIMIT = BigInteger.valueOf(100);
    private static final BigInteger THREE = BigInteger.valueOf(3);

    private int position;

    @Override
    public String solve(boolean part1, Scanner in) {
        return solveBoth(in)[part1 ? 0 : 1].toString();
    }

    @Override
    public String[] fullSolve(Scanner in) {
        BigInteger[] answers = solveBoth(in);
        return new String[]{answers[0].toString(), answers[1].toString()};
    }

    private static String slurp(Scanner in) {
        return in.useDelimiter("\\A").hasNext() ? in.next() : "";
    }

    private BigInteger[] solveBoth(Scanner in) {
        String input = slurp(in);
        position = 0;
        BigInteger part1 = BigInteger.ZERO;
        BigInteger part2 = BigInteger.ZERO;
        String first;
        while ((first = nextNonBlank(input)) != null) {
            String second = nextNonBlank(input);
            String prize = nextNonBlank(input);
            if (second == null || prize == null) {
                throw new IllegalArgumentException("Incomplete claw machine");
            }
            BigInteger[] a = extractPair(first);
            BigInteger[] b = extractPair(second);
            BigInteger[] target = extractPair(prize);
            Machine machine = new Machine(a[0], a[1], b[0], b[1], target[0], target[1]);
            part1 = part1.add(tokens(machine, BigInteger.ZERO, PART_ONE_LIMIT));
            part2 = part2.add(tokens(machine, PART_TWO_OFFSET, null));
        }
        return new BigInteger[]{part1, part2};
    }

    private String nextNonBlank(String input) {
        int length = input.length();
        while (position < length) {
            int start = position;
            int end = start;
            while (end < length) {
                char c = input.charAt(end);
                if (c == '\n' || c == '\r') {
                    break;
                }
                end++;
            }
            if (end < length) {
                position = input.charAt(end) == '\r' && end + 1 < length
                        && input.charAt(end + 1) == '\n'
                        ? end + 2 : end + 1;
            } else {
                position = length;
            }
            boolean blank = true;
            for (int i = start; i < end; i++) {
                if (!Character.isWhitespace(input.charAt(i))) {
                    blank = false;
                    break;
                }
            }
            if (!blank) {
                return input.substring(start, end);
            }
        }
        return null;
    }

    private BigInteger[] extractPair(String line) {
        BigInteger[] values = new BigInteger[2];
        int count = 0;
        for (int index = 0; index < line.length();) {
            char current = line.charAt(index);
            boolean signed = (current == '+' || current == '-')
                    && index + 1 < line.length() && Character.isDigit(line.charAt(index + 1));
            if (Character.isDigit(current) || signed) {
                int start = index++;
                while (index < line.length() && Character.isDigit(line.charAt(index))) {
                    index++;
                }
                if (count == values.length) {
                    throw new IllegalArgumentException("Expected two coordinates: " + line);
                }
                values[count++] = new BigInteger(line.substring(start, index));
            } else {
                index++;
            }
        }
        if (count != values.length) {
            throw new IllegalArgumentException("Expected two coordinates: " + line);
        }
        return values;
    }

    private BigInteger tokens(Machine machine, BigInteger offset, BigInteger limit) {
        BigInteger px = machine.px.add(offset);
        BigInteger py = machine.py.add(offset);
        BigInteger determinant = machine.ax.multiply(machine.by)
                .subtract(machine.ay.multiply(machine.bx));
        Presses presses;
        if (determinant.signum() != 0) {
            BigInteger aNumerator = px.multiply(machine.by).subtract(py.multiply(machine.bx));
            BigInteger bNumerator = machine.ax.multiply(py).subtract(machine.ay.multiply(px));
            BigInteger[] aResult = aNumerator.divideAndRemainder(determinant);
            BigInteger[] bResult = bNumerator.divideAndRemainder(determinant);
            if (aResult[1].signum() != 0 || bResult[1].signum() != 0) {
                return BigInteger.ZERO;
            }
            presses = new Presses(aResult[0], bResult[0]);
        } else {
            if (machine.ax.multiply(py).subtract(machine.ay.multiply(px)).signum() != 0
                    || machine.bx.multiply(py).subtract(machine.by.multiply(px)).signum() != 0) {
                return BigInteger.ZERO;
            }
            if (machine.ax.signum() != 0 || machine.bx.signum() != 0) {
                presses = solveLinear(machine.ax, machine.bx, px, limit);
            } else {
                presses = solveLinear(machine.ay, machine.by, py, limit);
            }
            if (presses == null) {
                return BigInteger.ZERO;
            }
        }

        if (!valid(presses.a, limit) || !valid(presses.b, limit)
                || !machine.ax.multiply(presses.a).add(machine.bx.multiply(presses.b)).equals(px)
                || !machine.ay.multiply(presses.a).add(machine.by.multiply(presses.b)).equals(py)) {
            return BigInteger.ZERO;
        }
        return presses.a.multiply(THREE).add(presses.b);
    }

    private Presses solveLinear(BigInteger u, BigInteger v, BigInteger target,
            BigInteger limit) {
        if (u.signum() < 0 || v.signum() < 0 || target.signum() < 0) {
            throw new IllegalArgumentException("Button movements and prizes must be nonnegative");
        }
        if (u.signum() == 0 && v.signum() == 0) {
            return target.signum() == 0 ? new Presses(BigInteger.ZERO, BigInteger.ZERO) : null;
        }
        if (u.signum() == 0) {
            BigInteger[] result = target.divideAndRemainder(v);
            Presses presses = new Presses(BigInteger.ZERO, result[0]);
            return result[1].signum() == 0 && valid(presses.b, limit) ? presses : null;
        }
        if (v.signum() == 0) {
            BigInteger[] result = target.divideAndRemainder(u);
            Presses presses = new Presses(result[0], BigInteger.ZERO);
            return result[1].signum() == 0 && valid(presses.a, limit) ? presses : null;
        }

        BigInteger gcd = u.gcd(v);
        BigInteger[] targetResult = target.divideAndRemainder(gcd);
        if (targetResult[1].signum() != 0) {
            return null;
        }
        BigInteger reducedU = u.divide(gcd);
        BigInteger reducedV = v.divide(gcd);
        BigInteger reducedTarget = targetResult[0];
        BigInteger a = reducedV.equals(BigInteger.ONE) ? BigInteger.ZERO
                : reducedTarget.multiply(reducedU.modInverse(reducedV)).mod(reducedV);
        BigInteger[] bResult = target.subtract(u.multiply(a)).divideAndRemainder(v);
        if (bResult[1].signum() != 0 || bResult[0].signum() < 0) {
            return null;
        }
        BigInteger b = bResult[0];
        BigInteger minK = BigInteger.ZERO;
        BigInteger maxK = b.divide(reducedU);
        if (limit != null) {
            if (a.compareTo(limit) > 0) {
                return null;
            }
            maxK = maxK.min(limit.subtract(a).divide(reducedV));
            if (b.compareTo(limit) > 0) {
                minK = ceilDivide(b.subtract(limit), reducedU);
            }
        }
        if (minK.compareTo(maxK) > 0) {
            return null;
        }
        BigInteger costStep = reducedV.multiply(THREE).subtract(reducedU);
        BigInteger k = costStep.signum() < 0 ? maxK : minK;
        return new Presses(a.add(k.multiply(reducedV)), b.subtract(k.multiply(reducedU)));
    }

    private BigInteger ceilDivide(BigInteger numerator, BigInteger denominator) {
        BigInteger[] result = numerator.divideAndRemainder(denominator);
        return result[1].signum() == 0 ? result[0] : result[0].add(BigInteger.ONE);
    }

    private boolean valid(BigInteger presses, BigInteger limit) {
        return presses.signum() >= 0 && (limit == null || presses.compareTo(limit) <= 0);
    }

    private record Machine(BigInteger ax, BigInteger ay, BigInteger bx, BigInteger by,
            BigInteger px, BigInteger py) {}

    private record Presses(BigInteger a, BigInteger b) {}
}

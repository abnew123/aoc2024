package src.solutions;

import src.meta.DayTemplate;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Day17 extends DayTemplate {

    private int position;

    public String solve(boolean part1, Scanner in) {
        return solve(part1, parse(in));
    }

    @Override
    public String[] fullSolve(Scanner in) {
        Input input = parse(in);
        return new String[] { solve(true, input), solve(false, input) };
    }

    private static String slurp(Scanner in) {
        return in.useDelimiter("\\A").hasNext() ? in.next() : "";
    }

    private Input parse(Scanner in) {
        String input = slurp(in);
        position = 0;
        long registerA = registerValue(input);
        long registerB = registerValue(input);
        long registerC = registerValue(input);
        skipLine(input);
        if (position >= input.length()) {
            throw new NoSuchElementException("No line found");
        }
        int start = position;
        int end = lineEnd(input, start);
        position = advance(input, end);
        int[] program = new int[16];
        int programCount = 0;
        for (int i = start; i < end;) {
            char c = input.charAt(i);
            if (c >= '0' && c <= '9') {
                int value = 0;
                while (i < end) {
                    char digit = input.charAt(i);
                    if (digit < '0' || digit > '9') {
                        break;
                    }
                    int amount = digit - '0';
                    if (value > (Integer.MAX_VALUE - amount) / 10) {
                        throw new NumberFormatException("Program value exceeds int");
                    }
                    value = value * 10 + amount;
                    i++;
                }
                if (programCount == program.length) {
                    program = Arrays.copyOf(program, programCount * 2);
                }
                program[programCount++] = value;
            } else {
                i++;
            }
        }
        return new Input(registerA, registerB, registerC, Arrays.copyOf(program, programCount));
    }

    private long registerValue(String input) {
        if (position >= input.length()) {
            throw new NoSuchElementException("No line found");
        }
        int start = position;
        int end = lineEnd(input, start);
        position = advance(input, end);
        for (int i = start; i < end; i++) {
            char c = input.charAt(i);
            boolean negative = c == '-' && i + 1 < end
                    && input.charAt(i + 1) >= '0' && input.charAt(i + 1) <= '9';
            if (negative) {
                i++;
                c = input.charAt(i);
            }
            if (c >= '0' && c <= '9') {
                long value = 0;
                while (i < end) {
                    char digit = input.charAt(i);
                    if (digit < '0' || digit > '9') {
                        break;
                    }
                    int amount = digit - '0';
                    if (value > (Long.MAX_VALUE - amount) / 10) {
                        throw new NumberFormatException("Register value exceeds long");
                    }
                    value = value * 10 + amount;
                    i++;
                }
                return negative ? -value : value;
            }
        }
        throw new NumberFormatException("Missing register value");
    }

    private void skipLine(String input) {
        if (position >= input.length()) {
            throw new NoSuchElementException("No line found");
        }
        position = advance(input, lineEnd(input, position));
    }

    private static int lineEnd(String input, int start) {
        int length = input.length();
        int end = start;
        while (end < length) {
            char c = input.charAt(end);
            if (c == '\n' || c == '\r') {
                break;
            }
            end++;
        }
        return end;
    }

    private static int advance(String input, int end) {
        int length = input.length();
        if (end >= length) {
            return length;
        }
        return input.charAt(end) == '\r' && end + 1 < length && input.charAt(end + 1) == '\n'
                ? end + 2 : end + 1;
    }

    private String solve(boolean part1, Input input) {
        long registerA = input.registerA;
        long registerB = input.registerB;
        long registerC = input.registerC;
        int[] program = input.program;
        if(part1){
            int[] result = run(program, registerA, registerB, registerC);
            return formatOutput(result);
        }
        long[] possibilities = {0L};
        int possibilityCount = 1;
        for(int outputStart = program.length - 1; outputStart >= 0; outputStart--){
            long[] newPossibilities = new long[8];
            int newCount = 0;
            for(int index = 0; index < possibilityCount; index++){
                long possibility = possibilities[index];
                // This program emits once per base-8 digit of A.  Build A from
                // the most significant digit down, keeping only candidates that
                // reproduce the target suffix seen so far.
                for(int digit = 0; digit < 8; digit++){
                    long potentialValue = (possibility << 3) + digit;
                    if(matchesSuffix(run(program, potentialValue, registerB, registerC),
                            program, outputStart)){
                        if (newCount == newPossibilities.length) {
                            newPossibilities = Arrays.copyOf(newPossibilities, newCount * 2);
                        }
                        newPossibilities[newCount++] = potentialValue;
                    }
                }
            }
            possibilities = newPossibilities;
            possibilityCount = newCount;
        }
        if (possibilityCount == 0) {
            throw new NoSuchElementException("No value present");
        }
        long minimum = possibilities[0];
        for (int index = 1; index < possibilityCount; index++) {
            if (possibilities[index] < minimum) {
                minimum = possibilities[index];
            }
        }
        return minimum + "";
    }

    private boolean matchesSuffix(int[] output, int[] program, int from) {
        if (output.length != program.length - from) {
            return false;
        }
        for (int i = 0; i < output.length; i++) {
            if (output[i] != program[from + i]) {
                return false;
            }
        }
        return true;
    }

    private record Input(long registerA, long registerB, long registerC, int[] program) {
    }

    private String formatOutput(int[] values) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                output.append(',');
            }
            output.append(values[i]);
        }
        return output.toString();
    }

    private int[] run(int[] program, long registerA, long registerB, long registerC){
        int instructionPointer = 0;
        int[] result = new int[16];
        int resultCount = 0;
        while(instructionPointer < program.length){
            int operator = program[instructionPointer];
            int operand = program[instructionPointer + 1]; //literal
            long combo = 0;
            if(operand < 4){
                combo = operand;
            }
            if(operand == 4){
                combo = registerA;
            }
            if(operand == 5){
                combo = registerB;
            }
            if(operand == 6){
                combo = registerC;
            }
            if(operator == 0){
                registerA = divideByPowerOfTwo(registerA, combo);
            }
            if(operator == 1){
                registerB ^= operand;
            }
            if(operator == 2){
                registerB = combo % 8;
            }
            if(operator == 3){
                if(registerA != 0){
                    instructionPointer = operand;
                    continue;
                }
            }
            if(operator == 4){
                registerB ^= registerC;
            }
            if(operator == 5){
                if (resultCount == result.length) {
                    result = Arrays.copyOf(result, resultCount * 2);
                }
                result[resultCount++] = (int) (combo % 8);
            }
            if(operator == 6){
                registerB = divideByPowerOfTwo(registerA, combo);
            }
            if(operator == 7){
                registerC = divideByPowerOfTwo(registerA, combo);
            }
            instructionPointer += 2;
        }
        return Arrays.copyOf(result, resultCount);
    }

    private long divideByPowerOfTwo(long value, long power){
        if(power >= Long.SIZE){
            return 0;
        }
        return value >> power;
    }
}

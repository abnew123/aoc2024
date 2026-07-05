package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day17 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long registerA = Long.parseLong(in.nextLine().split(" ")[2]);
        long registerB = Long.parseLong(in.nextLine().split(" ")[2]);
        long registerC = Long.parseLong(in.nextLine().split(" ")[2]);
        in.nextLine();
        String stringProgram = in.nextLine().split(" ")[1];
        List<Integer> program = new ArrayList<>();
        for(String s: stringProgram.split(",")){
            program.add(Integer.parseInt(s));
        }
        if(part1){
            List<Integer> result = run(program, registerA, registerB, registerC);
            return formatOutput(result);
        }
        List<Long> possibilities = new ArrayList<>(List.of(0L));
        for(int outputStart = program.size() - 1; outputStart >= 0; outputStart--){
            List<Integer> targetSuffix = program.subList(outputStart, program.size());
            List<Long> newPossibilities = new ArrayList<>();
            for(long possibility: possibilities){
                // This program emits once per base-8 digit of A.  Build A from
                // the most significant digit down, keeping only candidates that
                // reproduce the target suffix seen so far.
                for(int digit = 0; digit < 8; digit++){
                    long potentialValue = (possibility << 3) + digit;
                    if(run(program, potentialValue, registerB, registerC).equals(targetSuffix)){
                        newPossibilities.add(potentialValue);
                    }
                }
            }
            possibilities = newPossibilities;
        }
        return possibilities.stream().min(Long::compareTo).orElseThrow() + "";
    }

    private String formatOutput(List<Integer> values) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                output.append(',');
            }
            output.append(values.get(i));
        }
        return output.toString();
    }

    private List<Integer> run(List<Integer> program, long registerA, long registerB, long registerC){
        int instructionPointer = 0;
        List<Integer> result = new ArrayList<>();
        while(instructionPointer < program.size()){
            int operator = program.get(instructionPointer);
            int operand = program.get(instructionPointer + 1); //literal
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
                result.add((int) (combo % 8));
            }
            if(operator == 6){
                registerB = divideByPowerOfTwo(registerA, combo);
            }
            if(operator == 7){
                registerC = divideByPowerOfTwo(registerA, combo);
            }
            instructionPointer += 2;
        }
        return result;
    }

    private long divideByPowerOfTwo(long value, long power){
        if(power >= Long.SIZE){
            return 0;
        }
        return value >> power;
    }
}

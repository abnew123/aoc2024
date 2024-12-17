package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day17 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long registerA = 15401536L;
        in.nextLine();
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
            return (result + "").replace(" ", "");
        }
        int division = 2; // brute forcing 16 number takes 2^48 cycles roughly, so splitting into two separate 2^24 runs.
        List<Long> possibilities = new ArrayList<>();
        List<Integer> programEnd = new ArrayList<>();
        possibilities.add(0L);
        for(int i = division - 1; i >= 0; i--){
            List<Integer> programPart = new ArrayList<>();
            for(int j = i * 16/division; j < (i + 1) * (16/division); j++){
                programPart.add(program.get(j));
            }
            programEnd.addAll(0, programPart);
            List<Long> newPossibilities = new ArrayList<>();
            for(Long possibility: possibilities){
                long potential = 0;
                while(potential < 1L<<(programPart.size() * 3)){
                    long potentialValue = potential + possibility * (1L << ((division - i - 1) * 16/division * 3));
                    if(run(program, potentialValue, 0,0).equals(programEnd)){
                        newPossibilities.add(potentialValue);
                    }
                    potential++;
                }
            }
            possibilities = newPossibilities;
        }
        return possibilities.get(0) + "";
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
                long denominator = (long)Math.pow(2, combo);
                registerA /=denominator;
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
                long denominator = (long)Math.pow(2, combo);
                registerB = registerA /denominator;
            }
            if(operator == 7){
                long denominator = (long)Math.pow(2, combo);
                registerC = registerA /denominator;
            }
            instructionPointer += 2;
        }
        return result;
    }
}
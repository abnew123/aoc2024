package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day24 extends DayTemplate {


    List<String> registers = new ArrayList<>();

    Map<String, Integer> map = new HashMap<>();

    List<Instruction> instructions = new ArrayList<>();

    boolean hitEmpty = false;

    /**
     * Single pass solver. Parsing is shared. Both parts destructively remove entries from the
     * instruction list, so each part is handed its own copy of it (the Instruction objects
     * themselves are never mutated, so a shallow copy is enough). Part 1 additionally writes the
     * gate outputs into the register map, which part 2 never reads.
     *
     * Note that part 2 of this day is answered by reading the printed candidate gates, so, exactly
     * as in solve(), the returned part 2 string is empty.
     *
     * @param in The solver will read data from this Scanner.
     * @return Returns answer as a string array, with part 1 as index 0 and part 2 as index 1
     */
    public String[] fullSolve(Scanner in) {
        parse(in);
        String answer1 = solvePart1(new ArrayList<>(instructions), map);
        String answer2 = solvePart2(new ArrayList<>(instructions));
        return new String[]{answer1, answer2};
    }

    public String solve(boolean part1, Scanner in) {
        parse(in);
        if(part1){
            return solvePart1(instructions, map);
        }
        return solvePart2(instructions);
    }

    private void parse(Scanner in){
        while (in.hasNext()) {
            String line = in.nextLine();
            if(line.equals("")){
                hitEmpty = true;
                continue;
            }
            if(!hitEmpty){
                Register r = new Register(line);
                registers.add(r.name);
                map.put(r.name, r.value);
            }
            else{
                instructions.add(new Instruction(line));
            }
        }
    }

    private String solvePart1(List<Instruction> instructions, Map<String, Integer> map){
        StringBuilder answer = new StringBuilder();
        {
            int counter = 0;
            while(!instructions.isEmpty() && counter++<100){
                for(int i = instructions.size() - 1; i >= 0; i--){
                    Instruction instruction = instructions.get(i);
                    if (map.containsKey(instruction.firstReg) && map.containsKey(instruction.secondReg)) {
                        map.put(instruction.output, instruction.run(map));
                        instructions.remove(instruction);
                    }
                }
            }
            List<String> outputs = new ArrayList<>();

            for(String key: map.keySet()){
                if(key.startsWith("z")){
                    outputs.add(key);
                }
            }
            Collections.sort(outputs);
            Collections.reverse(outputs);

            for(String output: outputs){
                answer.append(map.get(output));
            }
            answer = new StringBuilder(String.valueOf(Long.parseLong(answer.toString(), 2)));
        }
        return answer + "";
    }

    private String solvePart2(List<Instruction> instructions){
        StringBuilder answer = new StringBuilder();
        {
            Map<String, Integer> generateBits = new HashMap<>(); //determines whether the current x and y bits will generate a carry
            Map<String, Integer> propagateBits = new HashMap<>(); //determines whether the current carry will propagate up
            Map<String, Integer> intermediateOrs = new HashMap<>(); //don't really know conceptually what it does, but only type of operation with OR
            Map<String, Integer> intermediateAnds = new HashMap<>(); //the ANDs that don't involve x and y
            Map<String, Integer> outputs = new HashMap<>(); //ops that involve z
            Set<Instruction> potentialSwaps = new HashSet<>();

            //special cases
            intermediateAnds.put("fake0bitAND", 0); //there's no intermediate AND for the first bit
            for(int i = instructions.size() - 1; i >= 0; i--) {
                Instruction instruction = instructions.get(i);
                if(instruction.firstReg.equals("x00") || instruction.firstReg.equals("y00")){
                    if(instruction.operation.equals("XOR")){
                        //x00 XOR y00 = z00 is kind of both an output and a propagate bit, since there's no carry
                        outputs.put(instruction.output, 0);
                        propagateBits.put(instruction.output, 0);
                        instructions.remove(i);
                    }
                    if(instruction.operation.equals("AND")){
                        //Similar to above, x00 AND y00 = ??? is kind of both an intermediate or and a generate bit, since there's no carry
                        generateBits.put(instruction.output, 0);
                        intermediateOrs.put(instruction.output, 0);
                        instructions.remove(i);
                    }
                }
            }

            //generate and propagates
            for(int i = instructions.size() - 1; i >= 0; i--){
                Instruction instruction = instructions.get(i);
                if(instruction.firstReg.startsWith("x") || instruction.firstReg.startsWith("y")){ //x and y bits are always paired together since only the outputs are swapped
                    if(instruction.output.startsWith("z")){
                        potentialSwaps.add(instruction);
                        continue;
                    }
                    if(instruction.operation.equals("AND")){
                        generateBits.put(instruction.output, Integer.parseInt(instruction.firstReg.substring(1)));
                    }
                    else{ //always XOR, x and y bits are never used in an OR
                        propagateBits.put(instruction.output, Integer.parseInt(instruction.firstReg.substring(1)));
                    }
                    instructions.remove(i);
                }
            }

            //intermediate Ors
            for(Instruction instruction: instructions){
                if(instruction.operation.equals("OR")){
                    if(generateBits.containsKey(instruction.firstReg)){
                        int level = generateBits.get(instruction.firstReg);
                        intermediateOrs.put(instruction.output, level);
                        continue;
                    }
                    if(generateBits.containsKey(instruction.secondReg)){
                        int level = generateBits.get(instruction.secondReg);
                        intermediateOrs.put(instruction.output, level);
                        continue;
                    }
                    potentialSwaps.add(instruction);
                }
            }

           // intermediate ands and outputs
            for(Instruction instruction: instructions){
                if(instruction.operation.equals("AND")){
                    if(propagateBits.containsKey(instruction.firstReg)){
                        if(intermediateOrs.containsKey(instruction.secondReg)){
                            int level = propagateBits.get(instruction.firstReg);
                            intermediateAnds.put(instruction.output, level);
                            continue;
                        }
                    }
                    if(propagateBits.containsKey(instruction.secondReg)){
                        if(intermediateOrs.containsKey(instruction.firstReg)){
                            int level = propagateBits.get(instruction.secondReg);
                            intermediateAnds.put(instruction.output, level);
                            continue;
                        }
                    }
                    potentialSwaps.add(instruction);
                }
                if(instruction.operation.equals("XOR")){
                    if(propagateBits.containsKey(instruction.firstReg)){
                        if(intermediateOrs.containsKey(instruction.secondReg)){
                            int level = propagateBits.get(instruction.firstReg);
                            outputs.put(instruction.output, level);
                            continue;
                        }
                    }
                    if(propagateBits.containsKey(instruction.secondReg)){
                        if(intermediateOrs.containsKey(instruction.firstReg)){
                            int level = propagateBits.get(instruction.secondReg);
                            outputs.put(instruction.output, level);
                            continue;
                        }
                    }
                    potentialSwaps.add(instruction);
                }
            }
//            for(int i = 0; i < registers.size()/2; i++){
//                if(!propagateBits.containsValue(i)){
//                    System.out.println("missing a propagate symbol for bit: " + i);
//                }
//                if(!generateBits.containsValue(i)){
//                    System.out.println("missing a generate symbol for bit: " + i);
//                }
//                if(!intermediateOrs.containsValue(i)){
//                    System.out.println("missing a intermediate or symbol for bit: " + i);
//                }
//                if(!intermediateAnds.containsValue(i)){
//                    System.out.println("missing a intermediate and symbol for bit: " + i);
//                }
//                if(!outputs.containsValue(i)){
//                    System.out.println("missing a output symbol for bit: " + i);
//                }
//            }

            for(Instruction i: potentialSwaps){
                System.out.println(i.output);
            }


        }

        return answer + "";
    }


}

class Register{
    int value;
    String name;

    public Register(String line){
        value = Integer.parseInt(line.split(": ")[1]);
        name = line.split(": ")[0];
    }
}

class Instruction{
    String firstReg;
    String secondReg;
    String operation;

    String output;

    public Instruction(String line){
        String[] parts = line.split(" |->");
        firstReg = parts[0];
        secondReg = parts[2];
        operation = parts[1];
        output = parts[5];
    }

    public int run(Map<String, Integer> map){
        int input1 = map.get(firstReg);
        int input2 = map.get(secondReg);
        if(operation.equals("AND")){
            return input1 & input2;
        }
        if(operation.equals("OR")){
            return input1 | input2;
        }
        if(operation.equals("XOR")){
            return input1 ^ input2;
        }
        return -1;
    }

    public String toString(){
        return firstReg + " " + operation + " " + secondReg + " " + output;
    }
}
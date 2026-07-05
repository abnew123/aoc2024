package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day24 extends DayTemplate {
    private final Map<String, Integer> values = new HashMap<>();
    private final List<Gate> gates = new ArrayList<>();

    public String solve(boolean part1, Scanner in) {
        values.clear();
        gates.clear();
        for (boolean gateSection = false; in.hasNextLine();) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                gateSection = true;
            } else if (gateSection) {
                gates.add(new Gate(line));
            } else {
                String[] parts = line.split(": ");
                values.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        if (!part1) {
            return swappedOutputs();
        }
        for (int n = 0; !gates.isEmpty() && n++ < 100;) {
            for (ListIterator<Gate> it = gates.listIterator(gates.size()); it.hasPrevious();) {
                Gate gate = it.previous();
                if (values.containsKey(gate.left) && values.containsKey(gate.right)) {
                    values.put(gate.output, gate.run(values));
                    it.remove();
                }
            }
        }
        List<String> outputs = new ArrayList<>();
        for (String wire : values.keySet()) {
            if (wire.startsWith("z")) {
                outputs.add(wire);
            }
        }
        outputs.sort(Comparator.reverseOrder());
        StringBuilder bits = new StringBuilder();
        for (String output : outputs) {
            bits.append(values.get(output));
        }
        return "" + Long.parseLong(bits.toString(), 2);
    }

    private String swappedOutputs() {
        int finalZ = gates.stream()
                .filter(gate -> gate.output.startsWith("z"))
                .mapToInt(gate -> Integer.parseInt(gate.output.substring(1)))
                .max()
                .orElseThrow();
        Set<String> bad = new TreeSet<>();
        for (Gate gate : gates) {
            boolean firstInputBit = gate.left.equals("x00") || gate.left.equals("y00");
            boolean xyInput = isXY(gate.left) && isXY(gate.right);
            // A ripple-carry adder has x/y XORs feeding sum XORs and carry ANDs,
            // carry-generating ANDs feeding ORs, and z outputs from XORs except the final carry.
            if (gate.output.startsWith("z")
                    && Integer.parseInt(gate.output.substring(1)) != finalZ
                    && !gate.op.equals("XOR")) {
                bad.add(gate.output);
            }
            if (gate.op.equals("XOR") && !xyInput && !gate.output.startsWith("z")) {
                bad.add(gate.output);
            }
            if (gate.op.equals("AND") && !firstInputBit && !feeds(gate.output, "OR")) {
                bad.add(gate.output);
            }
            if (gate.op.equals("XOR") && xyInput && !firstInputBit
                    && (!feeds(gate.output, "XOR") || !feeds(gate.output, "AND"))) {
                bad.add(gate.output);
            }
        }
        return String.join(",", bad);
    }

    private boolean isXY(String wire) {
        return wire.startsWith("x") || wire.startsWith("y");
    }

    private boolean feeds(String wire, String op) {
        for (Gate gate : gates) {
            if (gate.op.equals(op) && (gate.left.equals(wire) || gate.right.equals(wire))) {
                return true;
            }
        }
        return false;
    }
}

class Gate {
    String left;
    String right;
    String op;
    String output;

    Gate(String line) {
        String[] parts = line.split(" ");
        left = parts[0];
        op = parts[1];
        right = parts[2];
        output = parts[4];
    }

    int run(Map<String, Integer> values) {
        int first = values.get(left);
        int second = values.get(right);
        return switch (op) {
            case "AND" -> first & second;
            case "OR" -> first | second;
            default -> first ^ second;
        };
    }
}

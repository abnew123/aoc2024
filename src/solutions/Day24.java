package src.solutions;

import src.meta.DayTemplate;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Day24 extends DayTemplate {
    private static final int AND = 1;
    private static final int OR = 2;
    private static final int XOR = 4;

    @Override
    public String[] fullSolve(Scanner in) {
        Circuit circuit = parse(in);
        return new String[]{evaluate(circuit), swappedOutputs(circuit)};
    }

    @Override
    public String solve(boolean part1, Scanner in) {
        Circuit circuit = parse(in);
        return part1 ? evaluate(circuit) : swappedOutputs(circuit);
    }

    private Circuit parse(Scanner in) {
        Map<String, Integer> initial = new HashMap<>();
        List<Gate> gates = new ArrayList<>();
        Map<String, List<Integer>> consumers = new HashMap<>();
        Map<String, Integer> feedMasks = new HashMap<>();
        Set<String> outputs = new HashSet<>();
        boolean gateSection = false;
        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int inputLength = input.length();
        int position = 0;
        while (position < inputLength) {
            int lineEnd = position;
            while (lineEnd < inputLength
                    && input.charAt(lineEnd) != '\n' && input.charAt(lineEnd) != '\r') {
                lineEnd++;
            }
            String line = input.substring(position, lineEnd).trim();
            position = lineEnd;
            if (position < inputLength && input.charAt(position) == '\r') {
                position++;
            }
            if (position < inputLength && input.charAt(position) == '\n') {
                position++;
            }
            if (line.isEmpty()) {
                gateSection = true;
                continue;
            }
            if (!gateSection) {
                int colon = line.indexOf(':');
                if (colon <= 0 || colon == line.length() - 1) {
                    throw new IllegalArgumentException("Malformed initial wire: " + line);
                }
                String wire = line.substring(0, colon).trim();
                int value;
                try {
                    value = Integer.parseInt(line.substring(colon + 1).trim());
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("Malformed initial wire: " + line, exception);
                }
                if ((value != 0 && value != 1) || initial.putIfAbsent(wire, value) != null) {
                    throw new IllegalArgumentException("Invalid or duplicate initial wire: " + wire);
                }
                continue;
            }

            String[] parts = splitTokens(line);
            if (parts.length != 5 || !parts[3].equals("->")) {
                throw new IllegalArgumentException("Malformed gate: " + line);
            }
            int operation = switch (parts[1]) {
                case "AND" -> AND;
                case "OR" -> OR;
                case "XOR" -> XOR;
                default -> throw new IllegalArgumentException("Unknown gate operation: " + parts[1]);
            };
            if (!outputs.add(parts[4]) || initial.containsKey(parts[4])) {
                throw new IllegalArgumentException("Duplicate wire producer: " + parts[4]);
            }
            int index = gates.size();
            gates.add(new Gate(parts[0], parts[2], parts[4], operation));
            addConsumer(consumers, parts[0], index);
            if (!parts[0].equals(parts[2])) {
                addConsumer(consumers, parts[2], index);
            }
            mergeFeedMask(feedMasks, parts[0], operation);
            mergeFeedMask(feedMasks, parts[2], operation);
        }
        if (gates.isEmpty()) {
            throw new IllegalArgumentException("Circuit has no gates");
        }
        return new Circuit(Map.copyOf(initial), List.copyOf(gates), consumers, feedMasks);
    }

    private String[] splitTokens(String line) {
        String[] parts = new String[5];
        int count = 0;
        int position = 0;
        int length = line.length();
        while (position < length) {
            while (position < length && line.charAt(position) <= ' ') {
                position++;
            }
            if (position >= length) {
                break;
            }
            int tokenEnd = position;
            while (tokenEnd < length && line.charAt(tokenEnd) > ' ') {
                tokenEnd++;
            }
            if (count == parts.length) {
                parts = Arrays.copyOf(parts, count * 2);
            }
            parts[count++] = line.substring(position, tokenEnd);
            position = tokenEnd;
        }
        return count == parts.length ? parts : Arrays.copyOf(parts, count);
    }

    private void mergeFeedMask(Map<String, Integer> feedMasks, String wire, int operation) {
        Integer existing = feedMasks.get(wire);
        feedMasks.put(wire, existing == null ? operation : existing | operation);
    }

    private void addConsumer(Map<String, List<Integer>> consumers, String wire, int gate) {
        List<Integer> list = consumers.get(wire);
        if (list == null) {
            list = new ArrayList<>();
            consumers.put(wire, list);
        }
        list.add(gate);
    }

    private String evaluate(Circuit circuit) {
        Map<String, Integer> values = new HashMap<>(circuit.initial());
        List<Gate> gates = circuit.gates();
        int[] missing = new int[gates.size()];
        ArrayDeque<Integer> ready = new ArrayDeque<>();
        for (int index = 0; index < gates.size(); index++) {
            Gate gate = gates.get(index);
            int count = values.containsKey(gate.left()) ? 0 : 1;
            if (!gate.left().equals(gate.right()) && !values.containsKey(gate.right())) {
                count++;
            }
            missing[index] = count;
            if (count == 0) {
                ready.addLast(index);
            }
        }

        int completed = 0;
        while (!ready.isEmpty()) {
            Gate gate = gates.get(ready.removeFirst());
            Integer first = values.get(gate.left());
            Integer second = values.get(gate.right());
            if (first == null || second == null) {
                throw new IllegalStateException("Gate became ready without both inputs");
            }
            int value = gate.run(first, second);
            if (values.putIfAbsent(gate.output(), value) != null) {
                throw new IllegalArgumentException("Duplicate wire value: " + gate.output());
            }
            completed++;
            for (int consumer : circuit.consumers().getOrDefault(gate.output(), List.of())) {
                if (--missing[consumer] == 0) {
                    ready.addLast(consumer);
                }
            }
        }
        if (completed != gates.size()) {
            throw new IllegalArgumentException("Circuit contains a cycle or an unknown input wire");
        }

        BigInteger answer = BigInteger.ZERO;
        Set<Integer> seenBits = new HashSet<>();
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            int bit = wireIndex(entry.getKey(), 'z');
            if (bit >= 0) {
                if (!seenBits.add(bit)) {
                    throw new IllegalArgumentException("Duplicate z bit index: " + bit);
                }
                if (entry.getValue() == 1) {
                    answer = answer.setBit(bit);
                }
            }
        }
        return answer.toString();
    }

    private String swappedOutputs(Circuit circuit) {
        int finalZ = Integer.MIN_VALUE;
        for (Gate gate : circuit.gates()) {
            int outputZ = wireIndex(gate.output(), 'z');
            if (outputZ > finalZ) {
                finalZ = outputZ;
            }
        }
        Set<String> bad = new TreeSet<>();
        for (Gate gate : circuit.gates()) {
            boolean firstInputBit = wireIndex(gate.left(), 'x') == 0
                    || wireIndex(gate.left(), 'y') == 0
                    || wireIndex(gate.right(), 'x') == 0
                    || wireIndex(gate.right(), 'y') == 0;
            boolean xyInput = isXY(gate.left()) && isXY(gate.right());
            int outputZ = wireIndex(gate.output(), 'z');
            int feeds = circuit.feedMasks().getOrDefault(gate.output(), 0);
            // A ripple-carry adder has x/y XORs feeding sum XORs and carry ANDs,
            // carry-generating ANDs feeding ORs, and z outputs from XORs except the final carry.
            if (outputZ >= 0 && outputZ != finalZ && gate.operation() != XOR) {
                bad.add(gate.output());
            }
            if (gate.operation() == XOR && !xyInput && outputZ < 0) {
                bad.add(gate.output());
            }
            if (gate.operation() == AND && !firstInputBit && (feeds & OR) == 0) {
                bad.add(gate.output());
            }
            if (gate.operation() == XOR && xyInput && !firstInputBit
                    && ((feeds & XOR) == 0 || (feeds & AND) == 0)) {
                bad.add(gate.output());
            }
        }
        return String.join(",", bad);
    }

    private boolean isXY(String wire) {
        return wireIndex(wire, 'x') >= 0 || wireIndex(wire, 'y') >= 0;
    }

    private int wireIndex(String wire, char prefix) {
        if (wire.length() < 2 || wire.charAt(0) != prefix) {
            return -1;
        }
        int value = 0;
        for (int index = 1; index < wire.length(); index++) {
            char c = wire.charAt(index);
            if (c < '0' || c > '9') {
                return -1;
            }
            value = Math.addExact(Math.multiplyExact(value, 10), c - '0');
        }
        return value;
    }

    private record Circuit(Map<String, Integer> initial, List<Gate> gates,
                           Map<String, List<Integer>> consumers,
                           Map<String, Integer> feedMasks) {
    }

    private record Gate(String left, String right, String output, int operation) {
        int run(int first, int second) {
            return switch (operation) {
                case AND -> first & second;
                case OR -> first | second;
                default -> first ^ second;
            };
        }
    }
}

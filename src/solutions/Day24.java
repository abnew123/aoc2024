package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day24 extends DayTemplate {
    private final Map<String, Integer> map = new HashMap<>();
    private final List<Instruction> instructions = new ArrayList<>();

    public String solve(boolean part1, Scanner in) {
        for (boolean gates = false; in.hasNextLine();) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                gates = true;
            } else if (gates) {
                instructions.add(new Instruction(line));
            } else {
                String[] p = line.split(": ");
                map.put(p[0], Integer.parseInt(p[1]));
            }
        }
        if (!part1) {
            swaps();
            return "";
        }
        for (int n = 0; !instructions.isEmpty() && n++ < 100;) {
            for (ListIterator<Instruction> it = instructions.listIterator(instructions.size()); it.hasPrevious();) {
                Instruction i = it.previous();
                if (map.containsKey(i.a) && map.containsKey(i.b)) {
                    map.put(i.out, i.run(map));
                    it.remove();
                }
            }
        }
        List<String> z = new ArrayList<>();
        for (String k : map.keySet()) {
            if (k.startsWith("z")) {
                z.add(k);
            }
        }
        z.sort(Comparator.reverseOrder());
        StringBuilder bits = new StringBuilder();
        for (String k : z) {
            bits.append(map.get(k));
        }
        return "" + Long.parseLong(bits.toString(), 2);
    }

    private void swaps() {
        Map<String, Integer> gen = new HashMap<>(), prop = new HashMap<>(), or = new HashMap<>(),
                and = new HashMap<>(), out = new HashMap<>();
        Set<Instruction> bad = new HashSet<>();
        and.put("fake0bitAND", 0);
        for (ListIterator<Instruction> it = instructions.listIterator(instructions.size()); it.hasPrevious();) {
            Instruction i = it.previous();
            if ((i.a.equals("x00") || i.a.equals("y00"))) {
                (i.op.equals("AND") ? gen : prop).put(i.out, 0);
                (i.op.equals("AND") ? or : out).put(i.out, 0);
                it.remove();
            }
        }
        for (ListIterator<Instruction> it = instructions.listIterator(instructions.size()); it.hasPrevious();) {
            Instruction i = it.previous();
            if (i.a.startsWith("x") || i.a.startsWith("y")) {
                if (i.out.startsWith("z")) {
                    bad.add(i);
                } else {
                    (i.op.equals("AND") ? gen : prop).put(i.out, Integer.parseInt(i.a.substring(1)));
                }
                it.remove();
            }
        }
        for (Instruction i : instructions) {
            if (i.op.equals("OR")) {
                Integer n = gen.getOrDefault(i.a, gen.get(i.b));
                if (n == null) {
                    bad.add(i);
                } else {
                    or.put(i.out, n);
                }
            }
        }
        for (Instruction i : instructions) {
            int n = link(prop, or, i);
            if (i.op.equals("AND")) {
                if (n < 0) {
                    bad.add(i);
                } else {
                    and.put(i.out, n);
                }
            } else if (i.op.equals("XOR")) {
                if (n < 0) {
                    bad.add(i);
                } else {
                    out.put(i.out, n);
                }
            }
        }
        bad.stream().map(i -> i.out).sorted().forEach(System.out::println);
    }

    private int link(Map<String, Integer> a, Map<String, Integer> b, Instruction i) {
        return a.containsKey(i.a) && b.containsKey(i.b) ? a.get(i.a)
                : a.containsKey(i.b) && b.containsKey(i.a) ? a.get(i.b) : -1;
    }
}

class Instruction {
    String a, b, op, out;

    Instruction(String line) {
        String[] p = line.split(" ");
        a = p[0];
        op = p[1];
        b = p[2];
        out = p[4];
    }

    int run(Map<String, Integer> map) {
        int x = map.get(a), y = map.get(b);
        return switch (op) {
            case "AND" -> x & y;
            case "OR" -> x | y;
            default -> x ^ y;
        };
    }
}

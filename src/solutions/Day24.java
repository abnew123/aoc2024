package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day24 extends DayTemplate {
    private final Map<String, Integer> v = new HashMap<>();
    private final List<I> ins = new ArrayList<>();

    public String solve(boolean part1, Scanner in) {
        v.clear();
        ins.clear();
        for (boolean gates = false; in.hasNextLine();) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                gates = true;
            } else if (gates) {
                ins.add(new I(line));
            } else {
                String[] p = line.split(": ");
                v.put(p[0], Integer.parseInt(p[1]));
            }
        }
        if (!part1) {
            return swaps();
        }
        for (int n = 0; !ins.isEmpty() && n++ < 100;) {
            for (ListIterator<I> it = ins.listIterator(ins.size()); it.hasPrevious();) {
                I i = it.previous();
                if (v.containsKey(i.a) && v.containsKey(i.b)) {
                    v.put(i.o, i.run(v));
                    it.remove();
                }
            }
        }
        List<String> z = new ArrayList<>();
        for (String k : v.keySet()) {
            if (k.startsWith("z")) {
                z.add(k);
            }
        }
        z.sort(Comparator.reverseOrder());
        StringBuilder bits = new StringBuilder();
        for (String k : z) {
            bits.append(v.get(k));
        }
        return "" + Long.parseLong(bits.toString(), 2);
    }

    private String swaps() {
        int finalZ = ins.stream()
                .filter(i -> i.o.startsWith("z"))
                .mapToInt(i -> Integer.parseInt(i.o.substring(1)))
                .max()
                .orElseThrow();
        Set<String> bad = new TreeSet<>();
        for (I i : ins) {
            boolean firstInputBit = i.a.equals("x00") || i.a.equals("y00");
            boolean xyInput = isXY(i.a) && isXY(i.b);
            if (i.o.startsWith("z") && Integer.parseInt(i.o.substring(1)) != finalZ && !i.op.equals("XOR")) {
                bad.add(i.o);
            }
            if (i.op.equals("XOR") && !xyInput && !i.o.startsWith("z")) {
                bad.add(i.o);
            }
            if (i.op.equals("AND") && !firstInputBit && !feeds(i.o, "OR")) {
                bad.add(i.o);
            }
            if (i.op.equals("XOR") && xyInput && !firstInputBit && (!feeds(i.o, "XOR") || !feeds(i.o, "AND"))) {
                bad.add(i.o);
            }
        }
        return String.join(",", bad);
    }

    private boolean isXY(String wire) {
        return wire.startsWith("x") || wire.startsWith("y");
    }

    private boolean feeds(String wire, String op) {
        for (I i : ins) {
            if (i.op.equals(op) && (i.a.equals(wire) || i.b.equals(wire))) {
                return true;
            }
        }
        return false;
    }
}

class I {
    String a, b, op, o;

    I(String line) {
        String[] p = line.split(" ");
        a = p[0];
        op = p[1];
        b = p[2];
        o = p[4];
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

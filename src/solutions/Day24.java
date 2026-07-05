package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day24 extends DayTemplate {
    private final Map<String, Integer> v = new HashMap<>();
    private final List<I> ins = new ArrayList<>();

    public String solve(boolean part1, Scanner in) {
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
            swaps();
            return "";
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

    private void swaps() {
        Map<String, Integer> gen = new HashMap<>(), prop = new HashMap<>(), or = new HashMap<>();
        Set<I> bad = new HashSet<>();
        for (ListIterator<I> it = ins.listIterator(ins.size()); it.hasPrevious();) {
            I i = it.previous();
            if ((i.a.equals("x00") || i.a.equals("y00"))) {
                (i.op.equals("AND") ? gen : prop).put(i.o, 0);
                if (i.op.equals("AND")) {
                    or.put(i.o, 0);
                }
                it.remove();
            }
        }
        for (ListIterator<I> it = ins.listIterator(ins.size()); it.hasPrevious();) {
            I i = it.previous();
            if (i.a.startsWith("x") || i.a.startsWith("y")) {
                if (i.o.startsWith("z")) {
                    bad.add(i);
                } else {
                    (i.op.equals("AND") ? gen : prop).put(i.o, Integer.parseInt(i.a.substring(1)));
                }
                it.remove();
            }
        }
        for (I i : ins) {
            if (i.op.equals("OR")) {
                Integer n = gen.getOrDefault(i.a, gen.get(i.b));
                if (n == null) {
                    bad.add(i);
                } else {
                    or.put(i.o, n);
                }
            }
        }
        for (I i : ins) {
            int n = link(prop, or, i);
            if ((i.op.equals("AND") || i.op.equals("XOR")) && n < 0) {
                bad.add(i);
            }
        }
        bad.stream().map(i -> i.o).sorted().forEach(System.out::println);
    }

    private int link(Map<String, Integer> a, Map<String, Integer> b, I i) {
        return a.containsKey(i.a) && b.containsKey(i.b) ? a.get(i.a)
                : a.containsKey(i.b) && b.containsKey(i.a) ? a.get(i.b) : -1;
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

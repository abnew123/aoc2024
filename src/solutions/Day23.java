package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day23 extends DayTemplate {
    private final Map<String, Integer> ids = new HashMap<>();
    private final List<String> names = new ArrayList<>();
    private final List<BitSet> graph = new ArrayList<>();
    private boolean[] startsT;
    private BitSet best = new BitSet();
    private int bs;

    public String solve(boolean part1, Scanner in) {
        while (in.hasNextLine()) {
            String[] p = in.nextLine().split("-");
            int a = id(p[0]), b = id(p[1]);
            graph.get(a).set(b);
            graph.get(b).set(a);
        }
        startsT = new boolean[names.size()];
        for (int i = 0; i < names.size(); i++) {
            startsT[i] = names.get(i).startsWith("t");
        }
        if (part1) {
            return "" + triangles();
        }
        BitSet all = new BitSet(names.size());
        all.set(0, names.size());
        clique(new BitSet(), all, new BitSet());
        List<String> out = new ArrayList<>();
        for (int i = best.nextSetBit(0); i >= 0; i = best.nextSetBit(i + 1)) {
            out.add(names.get(i));
        }
        Collections.sort(out);
        return String.join(",", out);
    }

    private int id(String name) {
        return ids.computeIfAbsent(name, k -> {
            names.add(k);
            graph.add(new BitSet());
            return names.size() - 1;
        });
    }

    private long triangles() {
        long total = 0;
        for (int a = 0; a < names.size(); a++) {
            for (int b = graph.get(a).nextSetBit(a + 1); b >= 0; b = graph.get(a).nextSetBit(b + 1)) {
                BitSet c = (BitSet) graph.get(a).clone();
                c.and(graph.get(b));
                for (int x = c.nextSetBit(b + 1); x >= 0; x = c.nextSetBit(x + 1)) {
                    if (startsT[a] || startsT[b] || startsT[x]) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    private void clique(BitSet keep, BitSet can, BitSet skip) {
        int size = keep.cardinality();
        if (size + can.cardinality() <= bs) {
            return;
        }
        if (can.isEmpty() && skip.isEmpty()) {
            bs = size;
            best = (BitSet) keep.clone();
            return;
        }
        BitSet visit = (BitSet) can.clone();
        int p = pivot(can, skip);
        if (p >= 0) {
            visit.andNot(graph.get(p));
        }
        for (int v = visit.nextSetBit(0); v >= 0; v = visit.nextSetBit(v + 1)) {
            BitSet k = (BitSet) keep.clone(), c = (BitSet) can.clone(), s = (BitSet) skip.clone();
            k.set(v);
            c.and(graph.get(v));
            s.and(graph.get(v));
            clique(k, c, s);
            can.clear(v);
            skip.set(v);
        }
    }

    private int pivot(BitSet can, BitSet skip) {
        BitSet all = (BitSet) can.clone();
        all.or(skip);
        int pick = -1, most = -1;
        for (int v = all.nextSetBit(0); v >= 0; v = all.nextSetBit(v + 1)) {
            BitSet reach = (BitSet) graph.get(v).clone();
            reach.and(can);
            if (reach.cardinality() > most) {
                most = reach.cardinality();
                pick = v;
            }
        }
        return pick;
    }
}

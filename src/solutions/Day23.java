package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day23 extends DayTemplate {

    private final Map<String, Integer> nameToIndex = new HashMap<>();
    private final List<String> names = new ArrayList<>();
    private final List<BitSet> connections = new ArrayList<>();
    private boolean[] startsWithT;
    private BitSet bestClique;
    private int bestCliqueSize;

    public String solve(boolean part1, Scanner in) {
        readConnections(in);
        if (part1) {
            return countTrianglesWithT() + "";
        }
        findLargestClique();
        return cliqueToString(bestClique);
    }

    private void readConnections(Scanner in) {
        nameToIndex.clear();
        names.clear();
        connections.clear();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            int split = line.indexOf('-');
            int first = indexFor(line.substring(0, split));
            int second = indexFor(line.substring(split + 1));
            connections.get(first).set(second);
            connections.get(second).set(first);
        }

        startsWithT = new boolean[names.size()];
        for (int i = 0; i < names.size(); i++) {
            startsWithT[i] = names.get(i).startsWith("t");
        }
    }

    private int indexFor(String name) {
        Integer existing = nameToIndex.get(name);
        if (existing != null) {
            return existing;
        }
        int index = names.size();
        nameToIndex.put(name, index);
        names.add(name);
        connections.add(new BitSet());
        return index;
    }

    private long countTrianglesWithT() {
        long total = 0;
        for (int first = 0; first < names.size(); first++) {
            BitSet firstConnections = connections.get(first);
            for (int second = firstConnections.nextSetBit(first + 1); second >= 0; second = firstConnections.nextSetBit(second + 1)) {
                BitSet common = (BitSet) firstConnections.clone();
                common.and(connections.get(second));
                for (int third = common.nextSetBit(second + 1); third >= 0; third = common.nextSetBit(third + 1)) {
                    if (startsWithT[first] || startsWithT[second] || startsWithT[third]) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    private void findLargestClique() {
        bestClique = new BitSet();
        bestCliqueSize = 0;

        BitSet candidates = new BitSet(names.size());
        candidates.set(0, names.size());
        bronKerbosch(new BitSet(names.size()), candidates, new BitSet(names.size()));
    }

    private void bronKerbosch(BitSet required, BitSet candidates, BitSet excluded) {
        int requiredSize = required.cardinality();
        if (requiredSize + candidates.cardinality() <= bestCliqueSize) {
            return;
        }
        if (candidates.isEmpty() && excluded.isEmpty()) {
            if (requiredSize > bestCliqueSize) {
                bestCliqueSize = requiredSize;
                bestClique = (BitSet) required.clone();
            }
            return;
        }

        BitSet toVisit = (BitSet) candidates.clone();
        int pivot = choosePivot(candidates, excluded);
        if (pivot >= 0) {
            toVisit.andNot(connections.get(pivot));
        }

        for (int vertex = toVisit.nextSetBit(0); vertex >= 0; vertex = toVisit.nextSetBit(vertex + 1)) {
            BitSet nextRequired = (BitSet) required.clone();
            nextRequired.set(vertex);

            BitSet nextCandidates = (BitSet) candidates.clone();
            nextCandidates.and(connections.get(vertex));

            BitSet nextExcluded = (BitSet) excluded.clone();
            nextExcluded.and(connections.get(vertex));

            bronKerbosch(nextRequired, nextCandidates, nextExcluded);

            candidates.clear(vertex);
            excluded.set(vertex);
            if (requiredSize + candidates.cardinality() <= bestCliqueSize) {
                return;
            }
        }
    }

    private int choosePivot(BitSet candidates, BitSet excluded) {
        BitSet choices = (BitSet) candidates.clone();
        choices.or(excluded);

        int bestPivot = -1;
        int bestReach = -1;
        for (int vertex = choices.nextSetBit(0); vertex >= 0; vertex = choices.nextSetBit(vertex + 1)) {
            BitSet reachableCandidates = (BitSet) connections.get(vertex).clone();
            reachableCandidates.and(candidates);
            int reach = reachableCandidates.cardinality();
            if (reach > bestReach) {
                bestReach = reach;
                bestPivot = vertex;
            }
        }
        return bestPivot;
    }

    private String cliqueToString(BitSet clique) {
        List<String> cliqueNames = new ArrayList<>(clique.cardinality());
        for (int index = clique.nextSetBit(0); index >= 0; index = clique.nextSetBit(index + 1)) {
            cliqueNames.add(names.get(index));
        }
        Collections.sort(cliqueNames);
        return String.join(",", cliqueNames);
    }
}

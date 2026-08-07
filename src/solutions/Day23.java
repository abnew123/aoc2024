package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day23 extends DayTemplate {

    private final Map<String, Integer> nameToIndex = new HashMap<>();
    private String[] names;
    private BitSet[] connections;
    private int nodeCount;
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

    @Override
    public String[] fullSolve(Scanner in) {
        readConnections(in);
        long triangles = countTrianglesWithT();
        findLargestClique();
        return new String[]{triangles + "", cliqueToString(bestClique)};
    }

    private void readConnections(Scanner in) {
        nameToIndex.clear();
        names = new String[128];
        connections = new BitSet[128];
        nodeCount = 0;

        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = input.length();
        int position = 0;
        while (position < length) {
            char current = input.charAt(position);
            if (current == '\n' || current == '\r') {
                position++;
                continue;
            }
            int lineEnd = position;
            int dash = -1;
            while (lineEnd < length
                    && (current = input.charAt(lineEnd)) != '\n' && current != '\r') {
                if (current == '-' && dash < 0) {
                    dash = lineEnd;
                }
                lineEnd++;
            }
            if (dash < 0) {
                throw new IllegalArgumentException("Connection must contain a dash");
            }
            int first = indexFor(input.substring(position, dash));
            int second = indexFor(input.substring(dash + 1, lineEnd));
            connections[first].set(second);
            connections[second].set(first);
            position = lineEnd;
        }

        startsWithT = new boolean[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            startsWithT[i] = names[i].startsWith("t");
        }
    }

    private int indexFor(String name) {
        Integer existing = nameToIndex.get(name);
        if (existing != null) {
            return existing;
        }
        int index = nodeCount;
        nameToIndex.put(name, index);
        if (index == names.length) {
            names = Arrays.copyOf(names, index * 2);
            connections = Arrays.copyOf(connections, index * 2);
        }
        names[index] = name;
        connections[index] = new BitSet();
        nodeCount++;
        return index;
    }

    private long countTrianglesWithT() {
        long total = 0;
        for (int first = 0; first < nodeCount; first++) {
            BitSet firstConnections = connections[first];
            for (int second = firstConnections.nextSetBit(first + 1); second >= 0; second = firstConnections.nextSetBit(second + 1)) {
                BitSet common = (BitSet) firstConnections.clone();
                common.and(connections[second]);
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

        BitSet candidates = new BitSet(nodeCount);
        candidates.set(0, nodeCount);
        bronKerbosch(new BitSet(nodeCount), candidates, new BitSet(nodeCount));
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
            toVisit.andNot(connections[pivot]);
        }

        for (int vertex = toVisit.nextSetBit(0); vertex >= 0; vertex = toVisit.nextSetBit(vertex + 1)) {
            BitSet nextRequired = (BitSet) required.clone();
            nextRequired.set(vertex);

            BitSet nextCandidates = (BitSet) candidates.clone();
            nextCandidates.and(connections[vertex]);

            BitSet nextExcluded = (BitSet) excluded.clone();
            nextExcluded.and(connections[vertex]);

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
            BitSet reachableCandidates = (BitSet) connections[vertex].clone();
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
        String[] cliqueNames = new String[clique.cardinality()];
        int count = 0;
        for (int index = clique.nextSetBit(0); index >= 0; index = clique.nextSetBit(index + 1)) {
            cliqueNames[count++] = names[index];
        }
        Arrays.sort(cliqueNames);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < cliqueNames.length; i++) {
            if (i > 0) {
                result.append(',');
            }
            result.append(cliqueNames[i]);
        }
        return result.toString();
    }
}

package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day19 extends DayTemplate {

    private TowelNode root;

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        root = new TowelNode();
        for (String towel : in.nextLine().split(", ")) {
            addTowel(towel);
        }
        in.nextLine();
        while(in.hasNext()){
            String line = in.nextLine();
            if(part1){
                answer += canMake(line) ? 1 : 0;
            }
            else{
                answer += number(line);
            }
        }
        return answer + "";
    }

    private void addTowel(String towel) {
        TowelNode current = root;
        for (int i = 0; i < towel.length(); i++) {
            current = current.next.computeIfAbsent(towel.charAt(i), key -> new TowelNode());
        }
        current.end = true;
    }

    private long number(String towel){
        long[] possibilities = new long[towel.length() + 1];
        possibilities[towel.length()] = 1L;
        for (int start = towel.length() - 1; start >= 0; start--) {
            TowelNode current = root;
            long result = 0L;
            for (int end = start; end < towel.length(); end++) {
                current = current.next.get(towel.charAt(end));
                if (current == null) {
                    break;
                }
                if (current.end) {
                    result += possibilities[end + 1];
                }
            }
            possibilities[start] = result;
        }
        return possibilities[0];
    }

    private boolean canMake(String towel) {
        boolean[] possible = new boolean[towel.length() + 1];
        possible[towel.length()] = true;
        for (int start = towel.length() - 1; start >= 0; start--) {
            TowelNode current = root;
            for (int end = start; end < towel.length(); end++) {
                current = current.next.get(towel.charAt(end));
                if (current == null) {
                    break;
                }
                if (current.end && possible[end + 1]) {
                    possible[start] = true;
                    break;
                }
            }
        }
        return possible[0];
    }

    private static class TowelNode {
        private boolean end;
        private Map<Character, TowelNode> next = new HashMap<>();
    }
}

package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day22 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<Long> lines = new ArrayList<>();
        while (in.hasNext()) {
            Long line = Long.parseLong(in.nextLine());
            lines.add(line);
        }

        if(part1){
            for(long l: lines){
                for(int i = 0; i < 2000; i++){
                    l = oneIteration(l);
                }
                answer += l;
            }
        }
        else{
            Map<Integer, Long> sequenceValues = new HashMap<>();
            for (Long line : lines) {
                int diffHash = 0;
                long past = line;
                Set<Integer> viewedHashes = new HashSet<>();
                for (int j = 0; j < 2000; j++) {
                    long future = oneIteration(past);
                    diffHash <<= 5;
                    diffHash += (int) (future % 10 - past % 10 + 9);
                    if (j >= 3) {
                        if (!viewedHashes.contains(diffHash)) {
                            sequenceValues.merge(diffHash, future % 10, Long::sum);
                            viewedHashes.add(diffHash);
                        }
                        diffHash %= 1 << 15;
                    }
                    past = future;
                }
            }
            for(Integer key: sequenceValues.keySet()){
                if(sequenceValues.get(key) > answer){
                    answer = sequenceValues.get(key);
                }
            }
        }

        return answer + "";
    }

    private long oneIteration(long l){
        l ^= l<<6;
        l %= 16777216L;
        l ^= l>>5;
        l %= 16777216L;
        l ^= l<<11;
        l %= 16777216L;
        return l;
    }
}
package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day22 extends DayTemplate {

    /**
     * Single pass solver. Parsing is shared, and so are the 2000 pseudorandom iterations per
     * buyer: the value the part 2 loop ends on after 2000 steps is exactly the value part 1 sums.
     *
     * @param in The solver will read data from this Scanner.
     * @return Returns answer as a string array, with part 1 as index 0 and part 2 as index 1
     */
    public String[] fullSolve(Scanner in) {
        long answer1 = 0;
        long answer2 = 0;
        List<Long> lines = new ArrayList<>();
        while (in.hasNext()) {
            Long line = Long.parseLong(in.nextLine());
            lines.add(line);
        }

        Map<Integer, Long> sequenceValues = new HashMap<>();
        for(int i = 0; i < lines.size(); i++){
            int diffHash = 0;
            long past = lines.get(i);
            Set<Integer> viewedHashes = new HashSet<>();
            for(int j = 0; j < 2000; j++){
                long future = oneIteration(past);
                diffHash <<= 5;
                diffHash += ( future%10 - past%10+ 9);
                if(j >= 3){
                    if(!viewedHashes.contains(diffHash)){
                        sequenceValues.merge(diffHash, future%10, Long::sum);
                        viewedHashes.add(diffHash);
                    }
                    diffHash %= 1<<15;
                }
                past = future;
            }
            answer1 += past;
        }
        for(Integer key: sequenceValues.keySet()){
            if(sequenceValues.get(key) > answer2){
                answer2 = sequenceValues.get(key);
            }
        }

        return new String[]{answer1 + "", answer2 + ""};
    }

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
            for(int i = 0; i < lines.size(); i++){
                int diffHash = 0;
                long past = lines.get(i);
                Set<Integer> viewedHashes = new HashSet<>();
                for(int j = 0; j < 2000; j++){
                    long future = oneIteration(past);
                    diffHash <<= 5;
                    diffHash += ( future%10 - past%10+ 9);
                    if(j >= 3){
                        if(!viewedHashes.contains(diffHash)){
                            sequenceValues.merge(diffHash, future%10, Long::sum);
                            viewedHashes.add(diffHash);
                        }
                        diffHash %= 1<<15;
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
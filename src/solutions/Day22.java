package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day22 extends DayTemplate {
    private static final int MASK = (1 << 24) - 1;
    private static final int SEQUENCE_COUNT = 1 << 20;

    public String solve(boolean part1, Scanner in) {
        long answer = 0;

        if(part1){
            while (in.hasNext()) {
                long l = Long.parseLong(in.nextLine());
                for(int i = 0; i < 2000; i++){
                    l = oneIteration(l);
                }
                answer += l;
            }
        }
        else{
            long[] lines = readLines(in);
            int[] sequenceValues = new int[SEQUENCE_COUNT];
            int[] viewedHashes = new int[SEQUENCE_COUNT];
            for(int i = 0; i < lines.length; i++){
                int diffHash = 0;
                long past = lines[i];
                int buyerId = i + 1;
                for(int j = 0; j < 2000; j++){
                    int future = oneIteration(past);
                    diffHash = ((diffHash << 5) | (int) (future % 10 - past % 10 + 9)) & (SEQUENCE_COUNT - 1);
                    if(j >= 3){
                        if(viewedHashes[diffHash] != buyerId){
                            int value = sequenceValues[diffHash] + future % 10;
                            sequenceValues[diffHash] = value;
                            viewedHashes[diffHash] = buyerId;
                            if(value > answer){
                                answer = value;
                            }
                        }
                    }
                    past = future;
                }
            }
        }

        return answer + "";
    }

    private long[] readLines(Scanner in) {
        long[] lines = new long[256];
        int size = 0;
        while (in.hasNext()) {
            if(size == lines.length){
                lines = Arrays.copyOf(lines, lines.length * 2);
            }
            lines[size++] = Long.parseLong(in.nextLine());
        }
        return Arrays.copyOf(lines, size);
    }

    private int oneIteration(long l){
        l ^= l<<6;
        l &= MASK;
        l ^= l>>5;
        l &= MASK;
        l ^= l<<11;
        return (int) (l & MASK);
    }
}

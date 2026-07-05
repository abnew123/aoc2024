package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day22 extends DayTemplate {
    private static final int MASK = (1 << 24) - 1;
    private static final int SEQUENCE_COUNT = 1 << 20;
    private static final int SEQUENCE_MASK = SEQUENCE_COUNT - 1;

    public String solve(boolean part1, Scanner in) {
        long answer = 0;

        if(part1){
            while (in.hasNext()) {
                int secret = (int) (Long.parseLong(in.nextLine()) & MASK);
                for(int i = 0; i < 2000; i++){
                    secret = oneIteration(secret);
                }
                answer += secret;
            }
        }
        else{
            int[] sequenceValues = new int[SEQUENCE_COUNT];
            int[] viewedHashes = new int[SEQUENCE_COUNT];
            int buyerId = 1;
            while (in.hasNext()) {
                int diffHash = 0;
                long initial = Long.parseLong(in.nextLine());
                int past = (int) (initial & MASK);
                int pastPrice = (int) (initial % 10);
                for(int j = 0; j < 2000; j++){
                    int future = oneIteration(past);
                    int futurePrice = future % 10;
                    diffHash = ((diffHash << 5) | (futurePrice - pastPrice + 9)) & SEQUENCE_MASK;
                    if(j >= 3){
                        if(viewedHashes[diffHash] != buyerId){
                            int value = sequenceValues[diffHash] + futurePrice;
                            sequenceValues[diffHash] = value;
                            viewedHashes[diffHash] = buyerId;
                            if(value > answer){
                                answer = value;
                            }
                        }
                    }
                    past = future;
                    pastPrice = futurePrice;
                }
                buyerId++;
            }
        }

        return answer + "";
    }

    private int oneIteration(int secret){
        secret = (secret ^ (secret << 6)) & MASK;
        secret = (secret ^ (secret >> 5)) & MASK;
        return (secret ^ (secret << 11)) & MASK;
    }
}

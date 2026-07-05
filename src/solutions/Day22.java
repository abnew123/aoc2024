package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day22 extends DayTemplate {
    static final int MASK = (1 << 24) - 1;
    static final int SEQUENCE_COUNT = 19 * 19 * 19 * 19;

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
                int a = 0;
                int b = 0;
                int c = 0;
                int d = 0;
                long initial = Long.parseLong(in.nextLine());
                int past = (int) (initial & MASK);
                int pastPrice = (int) (initial % 10);
                for(int j = 0; j < 2000; j++){
                    int future = oneIteration(past);
                    int futurePrice = future % 10;
                    a = b;
                    b = c;
                    c = d;
                    d = futurePrice - pastPrice + 9;
                    if(j >= 3){
                        int diffHash = ((a * 19 + b) * 19 + c) * 19 + d;
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

    int oneIteration(int secret){
        secret = (secret ^ (secret << 6)) & MASK;
        secret = (secret ^ (secret >> 5)) & MASK;
        return (secret ^ (secret << 11)) & MASK;
    }
}

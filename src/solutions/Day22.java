// Inspired by https://github.com/maneatingape/advent-of-code-rust/blob/main/src/year2024/day22.rs
// for encoding four price changes as base-19 digits instead of sparse bit-packed indices.
package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day22 extends DayTemplate {
    private static final int MASK = (1 << 24) - 1;
    private static final int SEQUENCE_COUNT = 19 * 19 * 19 * 19;

    @Override
    public String[] fullSolve(Scanner in) {
        long[] secrets = parseSecrets(in);
        long part1 = 0;
        int part2 = 0;
        int[] sequenceValues = new int[SEQUENCE_COUNT];
        int[] viewedHashes = new int[SEQUENCE_COUNT];
        int buyerId = 1;
        for (int index = 0; index < secrets.length; index++) {
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;
            long initial = secrets[index];
            int secret = (int) (initial & MASK);
            int pastPrice = (int) (initial % 10);
            for(int j = 0; j < 2000; j++){
                secret = oneIteration(secret);
                int futurePrice = secret % 10;
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
                        if(value > part2){
                            part2 = value;
                        }
                    }
                }
                pastPrice = futurePrice;
            }
            part1 += secret;
            buyerId++;
        }
        return new String[]{part1 + "", part2 + ""};
    }

    public String solve(boolean part1, Scanner in) {
        long[] secrets = parseSecrets(in);
        long answer = 0;

        if(part1){
            for (int index = 0; index < secrets.length; index++) {
                int secret = (int) (secrets[index] & MASK);
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
            for (int index = 0; index < secrets.length; index++) {
                int a = 0;
                int b = 0;
                int c = 0;
                int d = 0;
                long initial = secrets[index];
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

    private long[] parseSecrets(Scanner in) {
        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        int length = input.length();
        long[] secrets = new long[128];
        int count = 0;
        int position = 0;
        while (position < length) {
            char current = input.charAt(position);
            if (current == '\n' || current == '\r') {
                position++;
                continue;
            }
            long value = 0;
            int digits = 0;
            while (position < length && (current = input.charAt(position)) >= '0' && current <= '9') {
                value = value * 10 + (current - '0');
                position++;
                digits++;
            }
            if (digits == 0 || (position < length
                    && input.charAt(position) != '\n' && input.charAt(position) != '\r')) {
                throw new IllegalArgumentException("Invalid initial secret");
            }
            if (count == secrets.length) {
                secrets = Arrays.copyOf(secrets, count * 2);
            }
            secrets[count++] = value;
        }
        return Arrays.copyOf(secrets, count);
    }

    private int oneIteration(int secret){
        secret = (secret ^ (secret << 6)) & MASK;
        secret = (secret ^ (secret >> 5)) & MASK;
        return (secret ^ (secret << 11)) & MASK;
    }
}

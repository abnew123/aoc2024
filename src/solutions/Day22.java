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
            List<int[]> changes = new ArrayList<>();
            for(int i = 0; i < lines.size(); i++){
                changes.add(new int[2000]);
                long past = lines.get(i);
                for(int j = 0; j < 2000; j++){
                    long future = oneIteration(past);
                    changes.get(i)[j] = (int)(future % 10 - past%10);
                    past = future;
                }
            }
            for(int i = -9; i < 10; i++){
                for(int j = -9; j <10; j++){
                    for(int k = -9; k < 10; k++){
                        for(int a = -9; a < 10; a++){
                            long possible = checkSequence(i,j,k,a,changes, lines);
                            if( possible > answer){
                                answer = possible;
                            }
                        }
                    }
                }
            }
        }

        return answer + "";
    }

    private long checkSequence(int i, int j, int k, int a, List<int[]> changes, List<Long> lines){
        long result = 0;
        for(int c = 0; c < changes.size(); c++){
            int[] changeList = changes.get(c);
            long initial = lines.get(c) % 10;
            for(int b = 3; b < changeList.length; b++){
                if(changeList[b - 3] == i && changeList[b - 2] == j &&changeList[b - 1] == k &&changeList[b] == a){
                    result+= initial;
                    for(int d = 0; d <= b; d++){
                        result+= changeList[d];
                    }
                    break;
                }
            }
        }
        return result;
    }

    private long prune(long l){
        return l % 16777216L;
    }

    private long mix(long l, long factor, boolean mult){
        if(mult){
            long other = l * factor;
            return l ^ other;
        }
        else{
            long other = l / factor;
            return l ^ other;
        }

    }

    private long oneIteration(long l){
        l = mix(l, 64, true);
        l = prune(l);
        l = mix(l, 32, false);
        l = prune(l);
        l = mix(l, 2048, true);
        l = prune(l);
        return l;
    }
}
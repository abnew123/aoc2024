package src.solutions;
import src.meta.DayTemplate;

import java.util.*;

import static src.meta.Utils.*;

public class Day13 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        while (in.hasNext()) {
            String[] tmp = new String[3];
            for(int i = 0; i < 3; i++){
                tmp[i] = in.nextLine();
            }
            answer+=helper(tmp, part1);
            if(in.hasNext()){
                in.nextLine();
            }
        }
        return answer+"";
    }

    private long oneCycle(long x1, long y1, long x2, long y2, long x3, long y3){
        if(y2 * x1 == x2 * y1){
            return 0;
        }
        long firstDelta = -1;
        long cycles = Math.max(y3/y1, x3/x1);
        if(y3%gcd(y1,y2) != 0 || x3%gcd(x1,x2) !=0){
            return 0;
        }
        long increment = 1;
        for(long i = 1; i <= cycles; i+=increment){
            y3-=y1 * increment;
            x3-=x1 * increment;
            if(y3%y2 == 0){
                increment = lcm(y1,y2)/y1;
                long second = y3/y2;
                long delta = x3 - second * x2;
                if(firstDelta == -1){
                    firstDelta = delta;
                }
                else{
                    long deltaDifference = firstDelta - delta;
                    if(delta % deltaDifference != 0){
                        return 0;
                    }
                    increment *= (delta/deltaDifference);
                }
                if(delta == 0){
                    return second + (i * 3);
                }
            }

        }
        return 0;
    }

    private long helper(String[] lines, boolean part1){
        String aButton = lines[0];
        String bButton = lines[1];
        String goal = lines[2];
        String[] first = aButton.split("[,+]");
        String[] second = bButton.split("[,+]");
        String[] third = goal.split("[,=]");
        long x3 = 10000000000000L + Integer.parseInt(third[1]);
        long y3 = 10000000000000L + Integer.parseInt(third[3]);
        if(part1){
            x3 = Integer.parseInt(third[1]);
            y3 = Integer.parseInt(third[3]);
        }
        return oneCycle(Integer.parseInt(first[1]), Integer.parseInt(first[3]), Integer.parseInt(second[1]), Integer.parseInt(second[3]), x3, y3);
    }
}
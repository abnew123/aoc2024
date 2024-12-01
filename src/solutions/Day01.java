package src.solutions;

import src.meta.DayTemplate;
import static src.meta.Utils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day01 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {

        long answer = 0;
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        while (in.hasNext()) {
            String[] line = in.nextLine().split("   ");
            left.add(Integer.parseInt(line[0]));
            right.add(Integer.parseInt(line[1]));
        }
        if(part1){
            Collections.sort(left);
            Collections.sort(right);
            for(int i = 0; i < left.size(); i++){
                answer += Math.abs(left.get(i) - right.get(i));
            }
        }
        else{
            for(Integer l: left){
                for(Integer r: right){
                    if(r.equals(l)){
                        answer+=l;
                    }
                }
            }
        }
        return answer+ "";
    }
}

package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day01 extends DayTemplate {

    public String[] fullSolve(Scanner in) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        while (in.hasNext()) {
            String[] line = in.nextLine().split("   ");
            left.add(Integer.parseInt(line[0]));
            right.add(Integer.parseInt(line[1]));
        }
        // Part 2 first: it reads the lists exactly as parsed (the sort below would otherwise
        // reorder them). It performs no mutation, so part 1 is unaffected by running it first.
        long answer2 = 0;
        for (Integer l : left) {
            for (Integer r : right) {
                if (r.equals(l)) {
                    answer2 += l;
                }
            }
        }
        long answer1 = 0;
        Collections.sort(left);
        Collections.sort(right);
        for (int i = 0; i < left.size(); i++) {
            answer1 += Math.abs(left.get(i) - right.get(i));
        }
        return new String[]{answer1 + "", answer2 + ""};
    }

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

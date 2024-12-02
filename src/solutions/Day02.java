package src.solutions;

import src.meta.DayTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day02 extends DayTemplate {

    public String solve(boolean part1, Scanner in) {

        long answer = 0;
        List<String[]> tmp = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            tmp.add(line.split(" "));
        }

        if(part1){
            for(String[] arr: tmp){
                if(safe(arr)){
                    answer++;
                }
            }
        }
        else{
            for(String[] arr: tmp){
                boolean any = false;
                for(int i = 0; i < arr.length; i++){
                    String[] newarr = new String[arr.length - 1];
                    System.arraycopy(arr, 0, newarr, 0, i);
                    if (arr.length >= i + 1)
                        System.arraycopy(arr, i + 1, newarr, i + 1 - 1, arr.length - (i + 1));
                    if(safe(newarr)){
                        any = true;
                    }
                }
                if(any){
                    answer++;
                }
            }
        }
        return answer + "";
    }

    private boolean safe(String[] steps){
        int[] nums = new int[steps.length];
        for(int i = 0; i < nums.length; i++){
            nums[i] = Integer.parseInt(steps[i]);
        }
        boolean decreasing = nums[0] > nums[1];
        if(nums[0] == nums[1]){
            return false;
        }
        for(int i = 1; i < nums.length; i++){
            if(Math.abs(nums[i] - nums[i-1]) > 3){
                return false;
            }
            if(nums[i] > nums[i - 1] && decreasing){
                return false;
            }
            if(nums[i] < nums[i - 1] && !decreasing){
                return false;
            }
            if(nums[i] == nums[i-1]){
                return false;
            }
        }
        return true;

    }
}

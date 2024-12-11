package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day11 extends DayTemplate{

    public String solve(boolean part1, Scanner in) {
        Map<Long, Long> map = new HashMap<>();
        long answer = 0;
        String[] line = in.nextLine().split(" ");
        for(String s: line){
            Long num = map.getOrDefault(Long.parseLong(s), 0L);
            map.put(Long.parseLong(s), num + 1);
        }
        int counter = part1?25:75;
        while(counter-- > 0){
            map = oneCycle(map);
        }
        for(Long val: map.values()){
            answer += val;
        }
        return answer + "";
    }

    public Map<Long, Long> oneCycle(Map<Long, Long> map){
        Map<Long, Long> newStones = new HashMap<>();
        for(Map.Entry<Long,Long> entry: map.entrySet()){
            String str = entry.getKey() + "";
            if(entry.getKey() == 0L){
                newStones.merge(1L, entry.getValue(), Long::sum);
            }
            else{
                if(str.length() % 2 == 0){
                    Long num = newStones.getOrDefault(Long.parseLong(str.substring(0, (str.length() / 2))), 0L);
                    newStones.put(Long.parseLong(str.substring(0, (str.length() / 2))), num + entry.getValue());
                    Long num2 = newStones.getOrDefault(Long.parseLong(str.substring((str.length() / 2))), 0L);
                    newStones.put(Long.parseLong(str.substring((str.length() / 2))), num2 + entry.getValue());
                }
                else{
                    newStones.merge(entry.getKey() * 2024, entry.getValue(), Long::sum);
                }
            }
        }
        return newStones;
    }
}

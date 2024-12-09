package src.solutions;

import java.util.*;

public class Day09 {
    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        String line = in.nextLine();
        int[] parts = new int[line.length()];
        int length = 0;
        for(int i = 0; i < parts.length; i++){
            parts[i] = Integer.parseInt(line.substring(i, i+1));
            length += parts[i];
        }
        int[] filesystem = new int[length];
        int[] condensed = new int[length];
        int index = 0;
        for(int i = 0; i < parts.length; i++){
            int part = parts[i];
            for(int j = 0; j < part; j++){
                if(i % 2 == 1){
                    filesystem[index + j] = -1;
                }
                else{
                    filesystem[index + j] = i/2;
                }
            }
            index += part;
        }
        int backindex = length - 1;
        int b = 0;
        if(part1){
            for(int i = 0; i < length; i++){
                if(filesystem[i] != -1){
                    condensed[i] = filesystem[i];
//                System.out.println(i);
//                System.out.println(length);
                }

                else{
                    while(backindex >= 0 && filesystem[backindex] == -1){
                        backindex--;
                    }
                    if(backindex > i){
                        condensed[i] = filesystem[backindex];
                        System.out.println(backindex);
                        filesystem[backindex] = -1;
                        b = i;
                    }

                }
            }
            for(int i = 0; i < condensed.length; i++){
                if(condensed[i] != -1){
                    answer+= i * condensed[i];
                }

            }
        }
        else{
            Map<Integer, int[]> starting = new HashMap<>();
            Map<Integer, int[]> gaps = new HashMap<>();
            int index1 = 0;
            for(int i = 0; i < parts.length; i++) {
                if(i %2 == 0){
                    starting.put(i / 2, new int[]{index1, parts[i]});
                }
                if(i % 2 == 1){
                    gaps.put(i/2, new int[] {index1, parts[i]});
                }
                index1 += parts[i];
            }
            for(int key: starting.keySet()) {
                int[] value = starting.get(key);
                System.out.println(key + " " + Arrays.toString(value));
            }

            List<Integer> blocks = new ArrayList<>();
            for(int i = 0; i < parts.length; i+=2){
                blocks.add(parts[1]);
            }
            System.out.println(gaps);
            for(int i = blocks.size() - 1; i >= 0; i--){
                int size = starting.get(i)[1];
                for(int j = 0; j < i; j++){
                    int gap = gaps.get(j)[1];
                    if(gap >= size){
                        starting.put(i, new int[]{gaps.get(j)[0], size});
                        System.out.println("Moved number " + i +  " to location " + gaps.get(j)[0]);
                        gaps.put(j, new int[]{gaps.get(j)[0] + size, gaps.get(j)[1] - size});
                        break;
                    }
                }
                System.out.println(gaps);
            }

            for(int key: starting.keySet()){
                int[] value = starting.get(key);
                System.out.println(key + " " + Arrays.toString(value));
                for(int i = value[0]; i < value[0] + value[1]; i++){
                    answer+= i * key;
                }
            }
        }

//        System.out.println(Arrays.toString(condensed));
//        System.out.println(Arrays.toString(filesystem));

        return answer + "";
    }
}

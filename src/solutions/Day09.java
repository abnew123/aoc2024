package src.solutions;

import src.meta.DayTemplate;

import java.util.*;

public class Day09 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        long answer;
        String line = in.nextLine();
        int[] parts = new int[line.length()];
        int length = 0;
        for(int i = 0; i < parts.length; i++){
            parts[i] = line.charAt(i) - '0';
            length += parts[i];
        }
        if(part1){
            answer = part1(length, parts);
        }
        else{
            answer = part2(parts);
        }
        return answer + "";
    }

    private long part1(int length, int[] parts){
        long answer = 0;
        int[] filesystem = new int[length];
        int[] condensed = new int[length];
        int index = 0;
        for(int i = 0; i < parts.length; i++){
            int part = parts[i];
            for(int j = 0; j < part; j++){
                filesystem[index + j] = (i % 2 == 1)?-1:i/2;
            }
            index += part;
        }
        int backindex = length - 1;
        for(int i = 0; i < length; i++){
            if(filesystem[i] != -1){
                condensed[i] = filesystem[i];
            }
            else{
                while(backindex >= i && filesystem[backindex] == -1){
                    backindex--;
                }
                if(backindex > i){
                    condensed[i] = filesystem[backindex];
                    filesystem[backindex] = -1;
                }
            }
        }
        for(int i = 0; i < condensed.length; i++){
            if(condensed[i] != -1){
                answer+= (long) i * condensed[i];
            }
        }
        return answer;
    }

    private long part2(int[] parts){
        int fileCount = (parts.length + 1) / 2;
        int[] fileStarts = new int[fileCount];
        int[] fileSizes = new int[fileCount];
        @SuppressWarnings("unchecked")
        PriorityQueue<Gap>[] gapsBySize = new PriorityQueue[10];
        for (int i = 0; i < gapsBySize.length; i++) {
            gapsBySize[i] = new PriorityQueue<>(Comparator.comparingInt(gap -> gap.start));
        }

        int index = 0;
        for (int i = 0; i < parts.length; i++) {
            int size = parts[i];
            if (i % 2 == 0) {
                int file = i / 2;
                fileStarts[file] = index;
                fileSizes[file] = size;
            } else if (size > 0) {
                gapsBySize[size].add(new Gap(index, size));
            }
            index += size;
        }

        long answer = 0;
        for (int file = fileCount - 1; file >= 0; file--) {
            int fileSize = fileSizes[file];
            int fileStart = fileStarts[file];
            Gap bestGap = null;
            int bestSize = -1;
            for (int size = fileSize; size < gapsBySize.length; size++) {
                Gap gap = gapsBySize[size].peek();
                if (gap != null && gap.start < fileStart && (bestGap == null || gap.start < bestGap.start)) {
                    bestGap = gap;
                    bestSize = size;
                }
            }

            int finalStart = fileStart;
            if (bestGap != null) {
                gapsBySize[bestSize].poll();
                finalStart = bestGap.start;
                int remaining = bestGap.size - fileSize;
                if (remaining > 0) {
                    gapsBySize[remaining].add(new Gap(bestGap.start + fileSize, remaining));
                }
            }
            answer += checksum(file, finalStart, fileSize);
        }
        return answer;
    }

    private long checksum(int fileId, int start, int size) {
        return (long) fileId * size * (2L * start + size - 1) / 2;
    }

    private record Gap(int start, int size) {}
}

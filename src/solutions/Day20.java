package src.solutions;

import src.meta.DayTemplate;
import java.util.*;

public class Day20 extends DayTemplate {

    private boolean[] walls;
    private int rows;
    private int cols;

    @Override
    public String[] fullSolve(Scanner in) {
        ParsedInput input = parse(in);
        int[] distanceToEnd = bfs(input.end());
        long[] answers = countPathCheats(input.start(), input.end(), distanceToEnd);
        return new String[]{
                answers[0] + "",
                answers[1] + ""
        };
    }

    public String solve(boolean part1, Scanner in) {
        ParsedInput input = parse(in);
        int[] bfsFromEnd = bfs(input.end());
        int[] bfsFromStart = bfs(input.start());
        return countCheats(bfsFromStart, bfsFromEnd, bfsFromEnd[input.start()], part1 ? 2 : 20) + "";
    }

    private ParsedInput parse(Scanner in) {
        List<String> lines = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            lines.add(line);
        }
        rows = lines.size();
        cols = lines.get(0).length();
        walls = new boolean[rows * cols];
        int startX = -1;
        int startY = -1;
        int endX = -1;
        int endY = -1;
        for(int i = 0 ; i < rows; i++){
            for(int j = 0; j < cols; j++){
                char c = lines.get(i).charAt(j);
                if(c == 'S'){
                    startX = i;
                    startY = j;
                }
                if(c == 'E'){
                    endX = i;
                    endY = j;
                }
                if(c == '#'){
                    walls[toIndex(i, j)] = true;
                }
            }
        }

        int start = toIndex(startX, startY);
        int end = toIndex(endX, endY);
        return new ParsedInput(start, end);
    }

    private long countCheats(int[] bfsFromStart, int[] bfsFromEnd, int currentDistance, int maxCheat) {
        long answer = 0;
        int targetDistance = currentDistance - 100;
        for(int x = 0; x < rows; x++){
            int rowStart = x * cols;
            for(int y = 0; y < cols; y++){
                int startDistance = bfsFromStart[rowStart + y];
                if(startDistance < 0){
                    continue;
                }
                int minDx = Math.max(-maxCheat, -x);
                int maxDx = Math.min(maxCheat, rows - 1 - x);
                for(int dx = minDx; dx <= maxDx; dx++){
                    int nx = x + dx;
                    int absDx = Math.abs(dx);
                    int yRange = maxCheat - absDx;
                    int targetRowStart = nx * cols;
                    int minY = Math.max(0, y - yRange);
                    int maxY = Math.min(cols - 1, y + yRange);
                    for(int ny = minY; ny <= maxY; ny++){
                        int endDistance = bfsFromEnd[targetRowStart + ny];
                        if(endDistance >= 0 && startDistance + endDistance + absDx + Math.abs(ny - y) <= targetDistance){
                            answer++;
                        }
                    }
                }
            }
        }
        return answer;
    }

    private long[] countPathCheats(int start, int end, int[] distanceToEnd) {
        int[] path = new int[walls.length];
        int length = 0;
        int current = start;
        while (true) {
            path[length] = current;
            length++;
            if (current == end) {
                break;
            }
            int row = current / cols;
            int col = current - row * cols;
            int nextDistance = distanceToEnd[current] - 1;
            int next = -1;
            if (row > 0 && distanceToEnd[current - cols] == nextDistance) {
                next = current - cols;
            }
            if (row + 1 < rows && distanceToEnd[current + cols] == nextDistance) {
                if (next >= 0) {
                    throw new IllegalArgumentException("Racetrack must have one shortest path from S to E");
                }
                next = current + cols;
            }
            if (col > 0 && distanceToEnd[current - 1] == nextDistance) {
                if (next >= 0) {
                    throw new IllegalArgumentException("Racetrack must have one shortest path from S to E");
                }
                next = current - 1;
            }
            if (col + 1 < cols && distanceToEnd[current + 1] == nextDistance) {
                if (next >= 0) {
                    throw new IllegalArgumentException("Racetrack must have one shortest path from S to E");
                }
                next = current + 1;
            }
            if (next < 0) {
                throw new IllegalArgumentException("No path from S to E");
            }
            current = next;
        }

        long shortCheats = 0;
        long longCheats = 0;
        int routeLength = distanceToEnd[start];
        for (int position = 0; position < distanceToEnd.length; position++) {
            int distance = distanceToEnd[position];
            distanceToEnd[position] = distance < 0 ? -1_000_000_000 : routeLength - distance;
        }
        for (int step = 0; step < length; step++) {
            int position = path[step];
            int row = position / cols;
            int col = position - row * cols;
            int minDr = Math.max(-20, -row);
            int maxDr = Math.min(20, rows - 1 - row);
            for (int dr = minDr; dr <= maxDr; dr++) {
                int distanceInRows = Math.abs(dr);
                int columnRange = 20 - distanceInRows;
                int targetRow = row + dr;
                int targetRowStart = targetRow * cols;
                int minCol = Math.max(0, col - columnRange);
                int maxCol = Math.min(cols - 1, col + columnRange);
                for (int targetCol = minCol; targetCol <= maxCol; targetCol++) {
                    int distance = distanceInRows + Math.abs(targetCol - col);
                    int targetProgress = distanceToEnd[targetRowStart + targetCol];
                    if (targetProgress - step - distance >= 100) {
                        longCheats++;
                        if (distance <= 2) {
                            shortCheats++;
                        }
                    }
                }
            }
        }
        return new long[]{shortCheats, longCheats};
    }

    private int[] bfs(int start){
        int[] distances = new int[rows * cols];
        Arrays.fill(distances, -1);

        int[] queue = new int[rows * cols];
        int head = 0;
        int tail = 0;
        distances[start] = 0;
        queue[tail++] = start;

        while(head < tail){
            int current = queue[head++];
            int x = current / cols;
            int y = current - x * cols;
            int nextDistance = distances[current] + 1;

            int next = current - cols;
            if(x > 0 && !walls[next] && distances[next] < 0){
                distances[next] = nextDistance;
                queue[tail++] = next;
            }

            next = current + cols;
            if(x + 1 < rows && !walls[next] && distances[next] < 0){
                distances[next] = nextDistance;
                queue[tail++] = next;
            }

            next = current - 1;
            if(y > 0 && !walls[next] && distances[next] < 0){
                distances[next] = nextDistance;
                queue[tail++] = next;
            }

            next = current + 1;
            if(y + 1 < cols && !walls[next] && distances[next] < 0){
                distances[next] = nextDistance;
                queue[tail++] = next;
            }
        }
        return distances;
    }

    private int toIndex(int x, int y){
        return x * cols + y;
    }

    private record ParsedInput(int start, int end) {
    }
}

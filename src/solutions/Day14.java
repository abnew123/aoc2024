package src.solutions;
import src.meta.DayTemplate;

import java.util.*;

public class Day14 extends DayTemplate {
    public String solve(boolean part1, Scanner in) {
        long answer = 0;
        List<Robot> robots = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            String[] parts = line.split(" |,|=");
            robots.add(new Robot(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])));
        }

        if(part1){
            int xlimit = 101;
            int ylimit = 103;
            int counter = 100;
            while(counter-->0){
                for(Robot robot: robots){
                    robot.update(xlimit, ylimit);
                }
            }
            int[] quadrants = new int[]{0,0,0,0};
            for(Robot robot: robots){
                if(robot.x%xlimit > (xlimit - 1)/2){
                    if(robot.y%ylimit > (ylimit - 1)/2){
                        quadrants[0]++;
                    }
                    if(robot.y%ylimit < (ylimit - 1)/2){
                        quadrants[1]++;
                    }
                }
                if(robot.x%xlimit < (xlimit - 1)/2){
                    if(robot.y%ylimit > (ylimit - 1)/2){
                        quadrants[2]++;
                    }
                    if(robot.y%ylimit < (ylimit - 1)/2){
                        quadrants[3]++;
                    }
                }
            }
            answer = (long)quadrants[0] * quadrants[1]* quadrants[2] * quadrants[3];
        }
        else{
            int xlimit = 101;
            int ylimit = 103;
            int counter = 0;
            int[][] grid;
            while(counter++ < 10000){
                grid = new int[103][101];
                for(Robot robot: robots){
                    robot.update(xlimit, ylimit);
                    grid[robot.y][robot.x] = 1;
                }
                if(diagonal(robots) > 14000){
                    return counter + "";
                }

            }
        }
        return answer + "";
    }

    private int diagonal(List<Robot> robots){
        int answer = 0;
        for(Robot robot: robots){
            for(Robot robot2: robots){
                if(Math.abs(robot.x - robot2.x) == Math.abs(robot.y - robot2.y)){
                    answer++;
                    if(Math.abs(robot.x - robot2.x) == 1){
                        answer+=2;
                    }
                }
                if(robot.x == robot2.x && robot.y == robot2.y){
                    answer--;
                }
                if(robot.x == robot2.x && robot.y != robot2.y){
                    answer++;
                }
            }
        }
        return answer;
    }
}

class Robot{
    int x;
    int y;
    int vx;
    int vy;

    public Robot(int x, int y, int vx, int vy){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public void update(int xlimit, int ylimit){
        x += vx;
        y += vy;
        x = (x%xlimit + xlimit)%xlimit;
        y = (y%ylimit + ylimit)%ylimit;
    }
}
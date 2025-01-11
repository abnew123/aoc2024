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
            for(Robot robot: robots){
                robot.updateBatch(xlimit, ylimit, 100);
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
            int counter = 1;
            int increment = 1;
            while(counter < 103 * 101){
                for(Robot robot: robots){
                    robot.updateBatch(xlimit, ylimit, increment);
                }
                boolean boxX = boxX(robots);
                boolean boxY = boxY(robots);
                if(boxX && boxY){
                    return counter + "";
                }
                if(boxX(robots)){
                    increment = 101;
                }
                counter += increment;

            }
        }
        return answer + "";
    }

//    private boolean box(List<Robot> robots){
//        HashMap<Integer, Integer> mapX = new HashMap<>();
//        HashMap<Integer, Integer> mapY = new HashMap<>();
//        for(Robot robot: robots){
//            mapX.merge(robot.x, 1, Integer::sum);
//            mapY.merge(robot.y, 1, Integer::sum);
//        }
//
//        boolean xbox = false;
//        boolean ybox = false;
//
//        for(int key: mapX.keySet()){
//            if(mapX.get(key) > 30){
//                xbox = true;
//            }
//        }
//        for(int key: mapY.keySet()){
//            if(mapY.get(key) > 30){
//                ybox = true;
//            }
//        }
//        return xbox && ybox;
//    }

    private boolean boxX(List<Robot> robots){
        HashMap<Integer, Integer> mapX = new HashMap<>();
        for(Robot robot: robots){
            mapX.merge(robot.x, 1, Integer::sum);
        }
        for(int key: mapX.keySet()){
            if(mapX.get(key) > 30){
                return true;
            }
        }
        return false;
    }

    private boolean boxY(List<Robot> robots){
        HashMap<Integer, Integer> mapY = new HashMap<>();
        for(Robot robot: robots){
            mapY.merge(robot.y, 1, Integer::sum);
        }
        for(int key: mapY.keySet()){
            if(mapY.get(key) > 30){
                return true;
            }
        }
        return false;
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

    public void updateBatch(int xlimit, int ylimit, int numUpdate){
        x += vx * numUpdate;
        y += vy * numUpdate;
        x = (x%xlimit + xlimit)%xlimit;
        y = (y%ylimit + ylimit)%ylimit;
    }
}
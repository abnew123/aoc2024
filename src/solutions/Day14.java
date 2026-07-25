package src.solutions;
import src.meta.DayTemplate;

import java.util.*;

public class Day14 extends DayTemplate {

    /**
     * Single pass solver. Parsing is shared; each part gets its own freshly built robot list
     * because both parts simulate the robots from their initial positions and updateBatch()
     * mutates the Robot objects in place.
     *
     * @param in The solver will read data from this Scanner.
     * @return Returns answer as a string array, with part 1 as index 0 and part 2 as index 1
     */
    public String[] fullSolve(Scanner in) {
        List<int[]> specs = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            String[] parts = line.split(" |,|=");
            specs.add(new int[]{Integer.parseInt(parts[1]),Integer.parseInt(parts[2]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])});
        }
        return new String[]{solvePart1(build(specs)), solvePart2(build(specs))};
    }

    public String solve(boolean part1, Scanner in) {
        List<int[]> specs = new ArrayList<>();
        while(in.hasNext()){
            String line = in.nextLine();
            String[] parts = line.split(" |,|=");
            specs.add(new int[]{Integer.parseInt(parts[1]),Integer.parseInt(parts[2]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])});
        }
        List<Robot> robots = build(specs);
        return part1 ? solvePart1(robots) : solvePart2(robots);
    }

    private List<Robot> build(List<int[]> specs){
        List<Robot> robots = new ArrayList<>();
        for(int[] spec: specs){
            robots.add(new Robot(spec[0], spec[1], spec[2], spec[3]));
        }
        return robots;
    }

    private String solvePart1(List<Robot> robots){
        long answer;
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
        return answer + "";
    }

    private String solvePart2(List<Robot> robots){
        long answer = 0;
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
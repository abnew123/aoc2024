            package src.solutions;

            import src.meta.DayTemplate;
            import src.objects.Coordinate;

            import java.util.*;
            public class Day18 extends DayTemplate {

                public String solve(boolean part1, Scanner in) {

                    long answer = 0;
                    List<String[]> tmp = new ArrayList<>();
                    while (in.hasNext()) {
                        String line = in.nextLine();
                        tmp.add(line.split(""));
                    }
                    int[][] grid = new int[tmp.get(0).length][tmp.size()];
                }
            }

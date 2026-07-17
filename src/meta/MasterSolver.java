package src.meta;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MasterSolver {

    private static final String PATH_NAME_PREFIX = "src.solutions.Day";

    public static void main(String[] args) throws Exception {

        // inputs.
        int[] days = new int[]{};
        boolean[] parts = new boolean[] { true, false };

        // Do not change anything in the method below this comment

        for (int day : days) {
            String zeroFilledDay = (day < 10 ? "0" : "") + day;
            for (boolean part1 : parts) {
                File file = new File("./data/day" + zeroFilledDay + ".txt");
                try (Scanner in = new Scanner(file)) {
                    Class<?> cls = Class.forName(PATH_NAME_PREFIX + zeroFilledDay);
                    Method m = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
                    String answer = (String) m.invoke(cls.getDeclaredConstructor().newInstance(), part1, in);
                    System.out.println(
                            "Day " + zeroFilledDay + " part " + (part1 ? 1 : 2) + " solution: " + answer);
                }
            }
        }
    }
}

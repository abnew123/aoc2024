package src.meta;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class SingleDayRunner {
    private static final String PATH_NAME_PREFIX = "src.solutions.Day";

    public static void main(String[] args) throws Exception {
        String zeroFilledDay = args[0].length() == 1 ? "0" + args[0] : args[0];
        boolean part1 = args[1].equals("1");
        File file = new File("./data/day" + zeroFilledDay + ".txt");
        try (Scanner in = new Scanner(file)) {
            Class<?> cls = Class.forName(PATH_NAME_PREFIX + zeroFilledDay);
            Method solve = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
            String answer = (String) solve.invoke(cls.getDeclaredConstructor().newInstance(), part1, in);
            System.out.println(answer);
        }
    }
}

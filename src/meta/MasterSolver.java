package src.meta;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Scanner;

public class MasterSolver {

    private static final String PATH_NAME_PREFIX = "src.solutions.Day";
    private static final String[] GOLFED_DAYS =
            "A B C D E F G H I J K L M N O P Q R S T U V W X Y".split(" ");
    private static boolean useGolfed;

    public static void main(String[] args) throws Exception {

        // inputs.
        boolean runTimer = true;
        boolean totalTimer = false;
        boolean exclusionTimer = true;
        useGolfed = false;
        int[] days = new int[]{};
        boolean[] parts = new boolean[]{true, false};

        // Do not change anything in the method below this comment

        for (int day : days) {
            String zeroFilledDay = (day < 10 ? "0" : "") + day;
            for (boolean part1 : parts) {
                File file = new File("./data/day" + zeroFilledDay + ".txt");
                Class<?> cls = Class.forName(className(day, zeroFilledDay));
                String answer = solve(cls, part1, file);
                System.out.println(
                        "Day " + zeroFilledDay + " part " + (part1 ? 1 : 2) + " solution: " + answer);
            }
        }
        if (runTimer) {
            timer(totalTimer, exclusionTimer);
        }
    }

    /**
     * Times every selected implementation.
     *
     * @param total     print only the summed time when true
     * @param exclusion skip normal solvers whose {@code exclude()} method returns true
     * @throws Exception when a solver cannot be loaded or invoked
     */
    public static void timer(boolean total, boolean exclusion) throws Exception {
        if (useGolfed) {
            timerGolfed(total);
            return;
        }

        double totalTime = 0.0;
        for (int day = 1; day <= 25; day++) {
            String zeroFilledDay = (day < 10 ? "0" : "") + day;
            Class<?> cls = Class.forName(PATH_NAME_PREFIX + zeroFilledDay);
            Object solver = cls.getDeclaredConstructor().newInstance();
            if (exclusion && (boolean) cls.getMethod("exclude").invoke(solver)) {
                continue;
            }

            double time;
            try (Scanner scanner = new Scanner(new File("./data/day" + zeroFilledDay + ".txt"))) {
                time = (Double) cls.getMethod("dayTimer", Scanner.class).invoke(solver, scanner);
            }
            if (!total) {
                System.out.println("Day " + zeroFilledDay + " execution time: " + time);
            }
            totalTime += time;
        }
        System.out.println("Total execution time (ms): " + totalTime);
    }

    /**
     * Backwards-compatible normal-solver timer entry point.
     */
    public static void timer(boolean total) throws Exception {
        timer(total, true);
    }

    private static void timerGolfed(boolean total) throws Exception {
        double totalTime = 0.0;
        for (int day = 1; day <= 25; day++) {
            String zeroFilledDay = (day < 10 ? "0" : "") + day;
            Class<?> cls = Class.forName(className(day, zeroFilledDay));
            File file = new File("./data/day" + zeroFilledDay + ".txt");
            for (int part = 1; part <= 2; part++) {
                long start = System.nanoTime();
                solve(cls, part == 1, file);
                double time = (System.nanoTime() - start) / 1_000_000.0;
                if (!total) {
                    System.out.println("Day " + zeroFilledDay + " part " + part
                            + " execution time: " + time);
                }
                totalTime += time;
            }
        }
        System.out.println("Total execution time (ms): " + totalTime);
    }

    private static String className(int day, String zeroFilledDay) {
        return useGolfed ? GOLFED_DAYS[day - 1] : PATH_NAME_PREFIX + zeroFilledDay;
    }

    private static String solve(Class<?> cls, boolean part1, File file) throws Exception {
        Object solver = cls.getDeclaredConstructor().newInstance();
        if (useGolfed) {
            try {
                Method method = cls.getDeclaredMethod("s", boolean.class, String.class);
                method.setAccessible(true);
                return (String) method.invoke(solver, part1, Files.readString(file.toPath()));
            } catch (NoSuchMethodException exception) {
                Method method = cls.getDeclaredMethod("s", boolean.class, Scanner.class);
                method.setAccessible(true);
                try (Scanner scanner = new Scanner(file)) {
                    return (String) method.invoke(solver, part1, scanner);
                }
            }
        }
        try (Scanner scanner = new Scanner(file)) {
            Method method = cls.getDeclaredMethod("solve", boolean.class, Scanner.class);
            return (String) method.invoke(solver, part1, scanner);
        }
    }
}

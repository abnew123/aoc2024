package src.meta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;

public class TimerRunner {
    private static final String PATH_NAME_PREFIX = "src.solutions.Day";

    public static void main(String[] args) throws IOException {
        String outputFile = "timing-output.txt";
        try (FileWriter writer = new FileWriter(outputFile)) {
            Double totalTime = 0.0;
            for (int day = 1; day <= 25; day++) {
                String zeroFilledDay = (day < 10 ? "0" : "") + day;
                for (int part = 1; part <= 2; part++) {
                    File inputFile = new File("./data/day" + zeroFilledDay + ".txt");
                    if (!inputFile.exists()) {
                        writer.write("Day " + zeroFilledDay + " part " + part + ": Input file not found, skipping.\n");
                        continue;
                    }

                    try {
                        System.out.println("doing day " + day + " part " + part);
                        Double time = timeSolution(zeroFilledDay, part, inputFile);
                        writer.write("Day " + zeroFilledDay + " part " + part + " execution time: " + time + " ms\n");
                        totalTime += time;
                    } catch (TimeoutException e) {
                        writer.write("Day " + zeroFilledDay + " part " + part + ": Execution timed out after 10 seconds.\n");
                    } catch (Exception e) {
                        writer.write("Day " + zeroFilledDay + " part " + part + ": Error - " + e.getMessage() + "\n");
                    }
                }
            }
            writer.write("Total execution time: " + totalTime + " ms\n");
            System.out.println("Done!");
        }
    }

    private static Double timeSolution(String zeroFilledDay, int part, File inputFile) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Double> task = () -> (Double) Class.forName(PATH_NAME_PREFIX + zeroFilledDay)
                .getMethod("timer", boolean.class, Scanner.class)
                .invoke(Class.forName(PATH_NAME_PREFIX + zeroFilledDay)
                                .getDeclaredConstructor()
                                .newInstance(),
                        part == 1,
                        new Scanner(inputFile));

        Future<Double> future = executor.submit(task);
        try {
            double result = future.get(10, TimeUnit.SECONDS);
            executor.shutdownNow();
            return result;
        } finally {
            executor.shutdownNow();
        }
    }
}

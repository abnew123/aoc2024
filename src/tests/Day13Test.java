package src.tests;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import src.meta.BaseTest;
import src.solutions.Day13;

class Day13Test extends BaseTest {

    private static final String DAY = "day13";

    @Test
    void testDay13TestPart1() throws FileNotFoundException {
        Day13 day13 = new Day13();
        Scanner input = getInputScanner(DAY);
        String expectedPart1 = getExpectedSolutions(13)[0];

        String result = day13.solve(true, input);
        if (isUnimplementedSolve(result)) {
            fail("Part 1 solve() is not implemented");
        } else {
            assertEquals(expectedPart1, result, "Part 1 solution is incorrect");
        }
    }

    @Test
    void testDay13TestPart2() throws FileNotFoundException {
        Day13 day13 = new Day13();
        Scanner input = getInputScanner(DAY);
        String expectedPart2 = getExpectedSolutions(13)[1];

        String result = day13.solve(false, input);
        if (isUnimplementedSolve(result)) {
            fail("Part 2 solve() is not implemented");
        } else {
            assertEquals(expectedPart2, result, "Part 2 solution is incorrect");
        }
    }

}

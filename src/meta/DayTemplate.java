package src.meta;

import java.util.Scanner;

public abstract class DayTemplate {

    /**
     * Main solving method.
     *
     * @param part1 The solver will solve part 1 if param is set to true.
     *              The solver will solve part 2 if param is set to false.
     * @param in    The solver will read data from this Scanner.
     * @return Returns answer in string format.
     */
    public abstract String solve(boolean part1, Scanner in);

    /**
     * Solves both parts from one input snapshot.
     *
     * <p>Days may override this method when they can share parsing or other work.
     * The default preserves the independent {@link #solve(boolean, Scanner)}
     * semantics.</p>
     *
     * @param in The solver input.
     * @return Part 1 and part 2 answers, in that order.
     */
    public String[] fullSolve(Scanner in) {
        String input = in.useDelimiter("\\A").hasNext() ? in.next() : "";
        try (Scanner part1Input = new Scanner(input);
             Scanner part2Input = new Scanner(input)) {
            return new String[]{
                    freshSolver().solve(true, part1Input),
                    freshSolver().solve(false, part2Input)
            };
        }
    }

    private DayTemplate freshSolver() {
        try {
            return getClass().getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "Solver must have an accessible no-argument constructor", exception);
        }
    }

    /**
     * Some classes require additional, non code steps (e.g. judge an image
     * output). In those cases, we do not want to run the solver.
     *
     * @return By default, returns false.
     * Subclasses can override in exceptional cases.
     */
    public boolean exclude() {
        return false;
    }

}

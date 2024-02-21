package se.kth.dd2480;


import java.util.HashMap;
import java.util.Map;

/**
 * <h3>BranchCoverageTracker</h3>
 * This class is used to track code coverage for the program.
 */
public class BranchCoverageTracker {
    /**
     * A map to store the coverage of the program.
     */
    public static Map<String, Boolean> coverageMap = new HashMap<>();

    /**
     * Add line coverage to the coverage map. This method should be called every time a branch is taken.
     * <br><br>
     * <i>Usage :</i> Add the following line every time you want to track a branch coverage:
     * <br>
     * <code>BranchCoverageTracker.addCoverage();</code>
     */
    public static void addCoverage() {
        generateKey();
    }


    /**
     * Also add line coverage to the coverage map adding the possibility to return a value.
     * This can be used to add coverage to some constructs like ternary operators.
     * <br><br>
     * <i>Usage with ternary operator :</i>
     * <br>
     * <code>int result = condition ? BranchCoverageTracker.addCoverage(1) :
     * BranchCoverageTracker.addCoverage(0);</code>
     * <br><br>
     * @param object Any object to be returned
     * @return the object passed as parameter
     * @param <T> Any type of object
     */
    public static <T> T addCoverage(T object) {
        generateKey();
        return object;
    }

    /**
     * Generate a key for the coverage map. The key is generated based on the class name, method name and line number.
     */
    private static void generateKey() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        String className = caller.getClassName().split("\\.")[caller.getClassName().split("\\.").length - 1];
        String key =
                className + "." + caller.getMethodName() + "." + caller.getLineNumber();
        if (coverageMap.put(key, true) == null) {
            CoverageSaver.saveCoverage(coverageMap);
        }
    }

    /**
     * Print the coverage of the program.
     * This method will print the content of the coverage file to the console.
     */
    public static void printCoverage() {
        System.out.println("Code coverage: ");
        Map<String, Boolean> coverage = CoverageSaver.loadCoverage();
        for (Map.Entry<String, Boolean> entry : coverage.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

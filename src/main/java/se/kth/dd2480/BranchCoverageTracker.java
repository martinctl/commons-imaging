package se.kth.dd2480;


import java.util.HashMap;
import java.util.Map;

/**
 * <h3>BranchCoverageTracker</h3>
 * This class is used to track code coverage for the program.
 * <br>
 * <i>Usage :</i> Add the following line every time you want to track a branch coverage:
 * <br>
 * <code>BranchCoverageTracker.addCoverage();</code>
 */
public class BranchCoverageTracker {
    /**
     * A map to store the coverage of the program.
     */
    public static Map<String, Boolean> coverageMap = new HashMap<>();

    /**
     * Add line coverage to the coverage map. This method should be called every time a branch is taken.
     */
    public static void addCoverage() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
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

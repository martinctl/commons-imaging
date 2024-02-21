package se.kth.dd2480;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * <h3>CoverageSaver</h3>
 * This class is used to save and load the coverage of the program to a file.
 */
public class CoverageSaver {
    /**
     * The file to save the coverage to.
     */
    private static final String COVERAGE_FILE = "src/main/resources" +
            "/se/kth/dd2480/coverage.properties";

    /**
     * Save the coverage to a file.
     * @param coverageMap The coverage map to save.
     */
    public static void saveCoverage(Map<String, Boolean> coverageMap) {
        try {
            Properties properties = new Properties();
            for (Map.Entry<String, Boolean> entry : coverageMap.entrySet()) {
                properties.put(entry.getKey(), entry.getValue().toString());
            }
            Path path = Paths.get(COVERAGE_FILE);
            properties.store(Files.newOutputStream(path), null);
        } catch (Exception e) {
            System.err.println("Failed to save coverage to file: " + e.getMessage());
        }
    }

    /**
     * Clear the coverage file.
     */
    public static void clearCoverage(){
        System.out.println("Clearing coverage");
        try {
            Properties properties = new Properties();
            Path path = Paths.get(COVERAGE_FILE);
            properties.store(Files.newOutputStream(path), null);
        } catch (Exception e) {
            System.err.println("Failed to clear coverage: " + e.getMessage());
        }
    }

    /**
     * Load and return the coverage from a file.
     * @return The coverage map.
     */
    public static Map<String, Boolean> loadCoverage() {
        Map<String, Boolean> coverageMap = new HashMap<>();
        try {
            Properties properties = new Properties();
            properties.load(Files.newInputStream(Paths.get(COVERAGE_FILE)));
            for (String key : properties.stringPropertyNames()) {
                coverageMap.put(key, Boolean.parseBoolean(properties.getProperty(key)));
            }
        } catch (Exception e) {
            System.err.println("Failed to load coverage from file: " + e.getMessage());
        }
        return coverageMap;
    }
}

package report;

/*
 * Integration tests for CapitalCityReports.
 * Verifies capital city reports worldwide, by continent and by region,
 * including population ordering, invalid filters, null connections
 * and formatted console output using the database.
 */

import com.napier.sem.App;
import com.napier.sem.City;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CapitalCityReportsIntegrationTest {

    private static App app;
    private static CapitalCityReports reports;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("127.0.0.1:3307", 1000);
        reports = new CapitalCityReports();
    }

    @AfterAll
    static void cleanup() {
        if (app != null) {
            app.disconnect();
        }
    }

    // IT07; All capital cities worldwide ordered by population descending
    @Test
    void testCapitalCitiesInWorldByPopulation() {
        List<City> capitals = reports.getCapitalCitiesInWorldByPopulation(app.con);

        assertNotNull(capitals, "Capital cities list should not be null.");
        assertFalse(capitals.isEmpty(), "Capital cities list should not be empty.");
        assertTrue(capitals.get(0).toString().contains("Seoul"),
                "Most populated capital city worldwide should be Seoul.");
        assertTrue(capitals.stream().anyMatch(c -> c.toString().contains("London")),
                "Worldwide capital cities should include London.");
    }

    // IT07; Capital cities in a specific continent ordered by population descending
    @Test
    void testCapitalCitiesInContinentByPopulation() {
        List<City> capitals = reports.getCapitalCitiesInContinentByPopulation(app.con, "Asia");

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty(), "Asian capital cities list should not be empty.");
        assertTrue(capitals.get(0).toString().contains("Seoul"),
                "Most populated capital city in Asia should be Seoul.");
        assertTrue(capitals.stream().anyMatch(c -> c.toString().contains("Tokyo")),
                "Asian capital cities should include Tokyo.");
        assertFalse(capitals.stream().anyMatch(c -> c.toString().contains("London")),
                "Asian capital cities should not include European capitals like London.");
    }

    // IT07; Capital cities in a specific region ordered by population descending
    @Test
    void testCapitalCitiesInRegionByPopulation() {
        List<City> capitals = reports.getCapitalCitiesInRegionByPopulation(app.con, "Southeast Asia");

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty(), "Southeast Asian capital cities list should not be empty.");
        assertTrue(capitals.get(0).toString().contains("Jakarta"),
                "Most populated capital city in Southeast Asia should be Jakarta.");
        assertTrue(capitals.stream().anyMatch(c -> c.toString().contains("Bangkok")),
                "Southeast Asian capital cities should include Bangkok.");
        assertFalse(capitals.stream().anyMatch(c -> c.toString().contains("Tokyo")),
                "Southeast Asian capital cities should not include Eastern Asian capitals like Tokyo.");
    }

    // Edge cases; Nonexistent filters and null connection handling
    @Test
    void testInvalidFiltersAndNullConnection() {
        assertTrue(reports.getCapitalCitiesInContinentByPopulation(app.con, "Atlantis").isEmpty(),
                "Nonexistent continent should return an empty list.");
        assertTrue(reports.getCapitalCitiesInRegionByPopulation(app.con, "Nowhere").isEmpty(),
                "Nonexistent region should return an empty list.");

        assertTrue(reports.getCapitalCitiesInWorldByPopulation(null).isEmpty(),
                "Null connection should return an empty list for world capitals.");
        assertTrue(reports.getCapitalCitiesInContinentByPopulation(null, "Asia").isEmpty(),
                "Null connection should return an empty list for continent capitals.");
        assertTrue(reports.getCapitalCitiesInRegionByPopulation(null, "Southeast Asia").isEmpty(),
                "Null connection should return an empty list for region capitals.");
    }

    // Output formatting; printResults for populated, empty, and null lists
    @Test
    void testPrintResults() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            List<City> capitals = reports.getCapitalCitiesInRegionByPopulation(app.con, "Southeast Asia");
            reports.printResults(capitals);
            reports.printResults(Collections.emptyList());
            reports.printResults(null);
        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);
        assertTrue(consoleOutput.contains("Capital City"),
                "Output should include the report header.");
        assertTrue(consoleOutput.contains("Jakarta"),
                "Output should display capital city records.");
        assertTrue(consoleOutput.contains("No matching capital cities found."),
                "Empty or null list should print the no-matching-results message.");
    }
}
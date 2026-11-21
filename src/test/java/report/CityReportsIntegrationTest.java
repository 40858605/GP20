package report;

import com.napier.sem.App;
import com.napier.sem.City;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityReportsIntegrationTest {

    private static App app;
    private static CityReports reports;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("127.0.0.1:3307", 1000);
        reports = new CityReports();
    }

    @AfterAll
    static void cleanup() {
        if (app != null) {
            app.disconnect();
        }
    }

    // IT05; population ordered city reports
    @Test
    void testCityReportsIT05() {
        List<City> worldCities = reports.getAllCitiesInWorldByPopulation(app.con);
        assertNotNull(worldCities);
        assertFalse(worldCities.isEmpty(), "Worldwide city list should not be empty.");
        assertTrue(worldCities.get(0).toString().contains("Mumbai"),
                "Most populated city worldwide should be Mumbai.");
        assertTrue(worldCities.stream().anyMatch(c -> c.toString().contains("Tokyo")),
                "Worldwide city list should include Tokyo.");

        List<City> asianCities = reports.getCitiesInContinentByPopulation(app.con, "Asia");
        assertFalse(asianCities.isEmpty(), "Asian city list should not be empty.");
        assertTrue(asianCities.stream().anyMatch(c -> c.toString().contains("Seoul")),
                "Asian city list should include Seoul.");
        assertFalse(asianCities.stream().anyMatch(c -> c.toString().contains("London")),
                "Asian city list should not include European cities like London.");

        List<City> seaCities = reports.getCitiesInRegionByPopulation(app.con, "Southeast Asia");
        assertFalse(seaCities.isEmpty(), "Southeast Asian city list should not be empty.");
        assertTrue(seaCities.get(0).toString().contains("Jakarta"),
                "Most populated city in Southeast Asia should be Jakarta.");
        assertTrue(seaCities.stream().anyMatch(c -> c.toString().contains("Bangkok")),
                "Southeast Asian city list should include Bangkok.");
        assertFalse(seaCities.stream().anyMatch(c -> c.toString().contains("Tokyo")),
                "Southeast Asian city list should not include Tokyo.");

        List<City> myanmarCities = reports.getCitiesInCountryByPopulation(app.con, "Myanmar");
        assertFalse(myanmarCities.isEmpty(), "Myanmar city list should not be empty.");
        assertTrue(myanmarCities.stream().anyMatch(
                        c -> c.toString().contains("Yangon") || c.toString().contains("Mandalay")),
                "Myanmar city list should include Yangon or Mandalay.");

        List<City> californiaCities = reports.getCitiesInDistrictByPopulation(app.con, "California");
        assertFalse(californiaCities.isEmpty(), "California district city list should not be empty.");
        assertTrue(californiaCities.get(0).toString().contains("Los Angeles"),
                "Most populated city in California should be Los Angeles.");
        assertFalse(californiaCities.stream().anyMatch(c -> c.toString().contains("London")),
                "California district list should not include London.");
    }

    // IT06; Top N city reports return count of highest population cities
    @Test
    void testTopNCityReportsIT06() {
        List<City> topWorld = reports.getTopNCitiesWorldwide(app.con, 5);
        assertEquals(5, topWorld.size(), "Top 5 worldwide should return exactly 5 cities.");
        assertTrue(topWorld.get(0).toString().contains("Mumbai"),
                "Top 1 city worldwide should be Mumbai.");
        assertTrue(topWorld.get(1).toString().contains("Seoul"),
                "Top 2 city worldwide should be Seoul.");

        List<City> topAsia = reports.getTopNCitiesInContinentByPopulation(app.con, "Asia", 3);
        assertEquals(3, topAsia.size(), "Top 3 in Asia should return exactly 3 cities.");
        assertTrue(topAsia.get(0).toString().contains("Mumbai"),
                "Top 1 city in Asia should be Mumbai.");

        List<City> topSea = reports.getTopNCitiesInRegionByPopulation(app.con, "Southeast Asia", 3);
        assertEquals(3, topSea.size(), "Top 3 in Southeast Asia should return exactly 3 cities.");
        assertTrue(topSea.get(0).toString().contains("Jakarta"),
                "Top 1 city in Southeast Asia should be Jakarta.");
    }

    // IT06; Top N cities in a country and in district
    @Test
    void testPrintTopNCitiesInCountryAndDistrictIT06() throws SQLException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            CityReports.printTopNCitiesInCountry(app.con, "Myanmar", 3);
            CityReports.printTopNCitiesInDistrict(app.con, "California", 3);
        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);
        assertTrue(consoleOutput.contains("Top 3 populated cities in Myanmar"),
                "Should print the Top N country cities header.");
        assertTrue(consoleOutput.contains("Yangon") || consoleOutput.contains("Mandalay"),
                "Should display top Myanmar cities.");
        assertTrue(consoleOutput.contains("Top 3 populated cities in district California"),
                "Should print the Top N district cities header.");
        assertTrue(consoleOutput.contains("Los Angeles"),
                "Should display top California cities.");
    }

    // IT07; Top N capital cities worldwide in continent and in region
    @Test
    void testPrintTopNCapitalCitiesIT07() throws SQLException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            CityReports.printTopNCapitalCitiesInWorld(app.con, 5);
            CityReports.printTopNCapitalCitiesInContinent(app.con, "Asia", 3);
            CityReports.printTopNCapitalCitiesInRegion(app.con, "Southeast Asia", 3);
        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);
        assertTrue(consoleOutput.contains("Top 5 populated capital cities in the world"),
                "Should print Top N world capital cities header.");
        assertTrue(consoleOutput.contains("Seoul"),
                "Top world/Asian capital cities should include Seoul.");
        assertTrue(consoleOutput.contains("Top 3 populated capital cities in Asia"),
                "Should print Top N Asian capital cities header.");
        assertTrue(consoleOutput.contains("Top 3 populated capital cities in Southeast Asia"),
                "Should print Top N Southeast Asian capital cities header.");
        assertTrue(consoleOutput.contains("Jakarta"),
                "Top Southeast Asian capital cities should include Jakarta.");
    }

    // Population lookups; District total population and single city population
    @Test
    void testDistrictAndCityPopulation() {
        long californiaPop = reports.getTotalPopulationOfDistrict(app.con, "California");
        assertTrue(californiaPop > 0, "California district population should be greater than 0.");
        assertEquals(0, reports.getTotalPopulationOfDistrict(app.con, "NowhereDistrict123"),
                "Nonexistent district should return 0.");

        Long seoulPop = reports.getPopulationOfCity(app.con, "Seoul");
        assertNotNull(seoulPop, "Existing city Seoul should return a non-null population.");
        assertTrue(seoulPop > 0, "Seoul population should be greater than 0.");
        assertNull(reports.getPopulationOfCity(app.con, "DefinitelyNotACity123"),
                "Nonexistent city should return null.");
    }

    // Edge cases; Null connections, invalid filters, printResults formatting
    @Test
    void testNullConnectionsInvalidFiltersAndPrintResults() throws SQLException {
        assertTrue(reports.getAllCitiesInWorldByPopulation(null).isEmpty());
        assertTrue(reports.getCitiesInContinentByPopulation(null, "Asia").isEmpty());
        assertTrue(reports.getCitiesInRegionByPopulation(null, "Southeast Asia").isEmpty());
        assertTrue(reports.getCitiesInCountryByPopulation(null, "Myanmar").isEmpty());
        assertTrue(reports.getCitiesInDistrictByPopulation(null, "California").isEmpty());
        assertTrue(reports.getTopNCitiesWorldwide(null, 5).isEmpty());
        assertTrue(reports.getTopNCitiesInContinentByPopulation(null, "Asia", 5).isEmpty());
        assertTrue(reports.getTopNCitiesInRegionByPopulation(null, "Southeast Asia", 5).isEmpty());
        assertEquals(0, reports.getTotalPopulationOfDistrict(null, "California"));
        assertNull(reports.getPopulationOfCity(null, "Seoul"));

        assertTrue(reports.getCitiesInContinentByPopulation(app.con, "Atlantis").isEmpty());
        assertTrue(reports.getCitiesInRegionByPopulation(app.con, "Nowhere").isEmpty());
        assertTrue(reports.getCitiesInCountryByPopulation(app.con, "Atlantis").isEmpty());
        assertTrue(reports.getCitiesInDistrictByPopulation(app.con, "Nowhere").isEmpty());
        assertTrue(reports.getTopNCitiesInContinentByPopulation(app.con, "Atlantis", 5).isEmpty());
        assertTrue(reports.getTopNCitiesInRegionByPopulation(app.con, "Nowhere", 5).isEmpty());

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            CityReports.printTopNCitiesInCountry(null, "Myanmar", 3);
            CityReports.printTopNCitiesInDistrict(null, "California", 3);
            CityReports.printTopNCapitalCitiesInWorld(null, 3);
            CityReports.printTopNCapitalCitiesInContinent(null, "Asia", 3);
            CityReports.printTopNCapitalCitiesInRegion(null, "Southeast Asia", 3);

            CityReports.printResults(Collections.emptyList());
            CityReports.printResults(null);
        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);
        assertTrue(consoleOutput.contains("Database connection is not available."),
                "Null connection on static print methods should log unavailable message.");
        assertTrue(consoleOutput.contains("No matching cities found."),
                "Empty or null list should print no-matching-cities message.");
    }
}
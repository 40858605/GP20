package report;

import com.napier.sem.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CapitalCityReports class.
 * This test suite verifies the behavior of capital city report generation and printing methods
 * when handling null database connections, empty collections, and valid capital city data
 * without requiring an active database connection.
 */
public class CapitalCityReportsTest {
    static CapitalCityReports capitalCityReports;

    @BeforeAll
    static void init() {
        capitalCityReports = new CapitalCityReports();
    }

    @Test
    void printResultsTestNull() {
        capitalCityReports.printResults(null);
    }

    @Test
    void printResultsTestEmpty() {
        List<City> cities = new ArrayList<>();
        capitalCityReports.printResults(cities);
    }

    @Test
    void printResultsTestContainsNull() {
        List<City> cities = new ArrayList<>();
        cities.add(null);
        capitalCityReports.printResults(cities);
    }

    @Test
    void printResultsTestNormal() {
        List<City> cities = new ArrayList<>();
        City city = new City("Naypyidaw", "Myanmar", "Naypyidaw Union Territory", 1164299);
        cities.add(city);
        capitalCityReports.printResults(cities);
    }

    // Issue #22: Test all capital cities in the world with null connection (40858605)
    @Test
    void getCapitalCitiesInWorldByPopulationTestNullConnection() {
        List<City> cities =
                capitalCityReports.getCapitalCitiesInWorldByPopulation(null);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #23: Test capital cities in a continent with null connection (40858605)
    @Test
    void getCapitalCitiesInContinentByPopulationTestNullConnection() {
        List<City> cities =
                capitalCityReports.getCapitalCitiesInContinentByPopulation(null, "Asia");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #24: Test capital cities in a region with null connection (40858605)
    @Test
    void getCapitalCitiesInRegionByPopulationTestNullConnection() {
        List<City> cities =
                capitalCityReports.getCapitalCitiesInRegionByPopulation(null, "Southeast Asia");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }
}
package report;

import com.napier.sem.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CityReports class.
 * This test suite verifies the behavior of city report generation and printing methods
 * when handling null database connections, empty collections, and valid city data
 * without requiring an active database connection.
 */
public class CityReportsTest {
    static CityReports cityReports;

    @BeforeAll
    static void init() {
        cityReports = new CityReports();
    }

    @Test
    void printResultsTestNull() {
        // Will not crash because of the null check you just added
        CityReports.printResults(null);
    }

    @Test
    void printResultsTestEmpty() {
        List<City> cities = new ArrayList<>();
        CityReports.printResults(cities);
    }

    @Test
    void printResultsTestContainsNull() {
        List<City> cities = new ArrayList<>();
        cities.add(null);
        CityReports.printResults(cities);
    }

    @Test
    void printResultsTestNormal() {
        List<City> cities = new ArrayList<>();
        City city = new City("Mandalay", "Myanmar", "Mandalay", 1225553);
        cities.add(city);
        CityReports.printResults(cities);
    }

    // Issue #15: Test cities in a country with null connection
    @Test
    void getCitiesInCountryByPopulationTestNullConnection() {
        List<City> cities =
                cityReports.getCitiesInCountryByPopulation(null, "Myanmar");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #16: Test cities in a district with null connection
    @Test
    void getCitiesInDistrictByPopulationTestNullConnection() {
        List<City> cities =
                cityReports.getCitiesInDistrictByPopulation(null, "Mandalay");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #17: Test top N cities worldwide with null connection
    @Test
    void getTopNCitiesWorldwideTestNullConnection() {
        List<City> cities =
                cityReports.getTopNCitiesWorldwide(null, 10);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #18: Test top N cities in a continent with null connection
    @Test
    void getTopNCitiesInContinentByPopulationTestNullConnection() {
        List<City> cities =
                cityReports.getTopNCitiesInContinentByPopulation(
                        null, "Asia", 10);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #19: Test top N cities in a region with null connection
    @Test
    void getTopNCitiesInRegionByPopulationTestNullConnection() {
        List<City> cities =
                cityReports.getTopNCitiesInRegionByPopulation(
                        null, "Southeast Asia", 10);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #33: Test population of a specific city with null connection
    @Test
    void getPopulationOfCityTestNullConnection() {
        Long population =
                cityReports.getPopulationOfCity(null, "Mandalay");

        assertNull(population);
    }

    // Issue #12: Test all cities in the world with null connection (40858605)
    @Test
    void getAllCitiesInWorldByPopulationTestNullConnection() {
        List<City> cities =
                cityReports.getAllCitiesInWorldByPopulation(null);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #13: Test cities in a continent with null connection (40858605)
    @Test
    void getCitiesInContinentByPopulationTestNullConnection() {
        List<City> cities =
                cityReports.getCitiesInContinentByPopulation(null, "Asia");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #14: Test cities in a region with null connection (40858605)
    @Test
    void getCitiesInRegionByPopulationTestNullConnection() {
        List<City> cities =
                cityReports.getCitiesInRegionByPopulation(null, "Southeast Asia");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Issue #20: Test printing top N cities in a country with null connection (40858605)
    @Test
    void printTopNCitiesInCountryTestNullConnection() throws SQLException {
        CityReports.printTopNCitiesInCountry(null, "Myanmar", 10);
    }

    // Issue #21: Test printing top N cities in a district with null connection (40858605)
    @Test
    void printTopNCitiesInDistrictTestNullConnection() throws SQLException {
        CityReports.printTopNCitiesInDistrict(null, "Mandalay", 10);
    }

    // Issue #25: Test printing top N capital cities in the world with null connection (40858605)
    @Test
    void printTopNCapitalCitiesInWorldTestNullConnection() throws SQLException {
        CityReports.printTopNCapitalCitiesInWorld(null, 10);
    }

    // Issue #26: Test printing top N capital cities in a continent with null connection (40858605)
    @Test
    void printTopNCapitalCitiesInContinentTestNullConnection() throws SQLException {
        CityReports.printTopNCapitalCitiesInContinent(null, "Asia", 10);
    }

    // Issue #27: Test printing top N capital cities in a region with null connection (40858605)
    @Test
    void printTopNCapitalCitiesInRegionTestNullConnection() throws SQLException {
        CityReports.printTopNCapitalCitiesInRegion(null, "Southeast Asia", 10);
    }

    // Issue #32: Test total population of a specific district with null connection (40858605)
    @Test
    void getTotalPopulationOfDistrictTestNullConnection() {
        long population =
                cityReports.getTotalPopulationOfDistrict(null, "Mandalay");

        assertEquals(0L, population);
    }

}
package report;

import com.napier.sem.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

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

}
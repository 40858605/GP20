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
}
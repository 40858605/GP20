package report;

import com.napier.sem.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

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
}
package report;

import com.napier.sem.App;
import com.napier.sem.Country;
import com.napier.sem.City;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseIntegrationTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:3307", 30000);
    }

    @AfterAll
    static void cleanup() {
        if (app != null) {
            app.disconnect();
        }
    }

    @Test // IT01; Java connects successfully to MySQL
    void testDatabaseConnectionIT01() {
        assertNotNull(app.con, "Database connection should not be null after connecting.");
    }

    @Test // IT02; Retrieves country records
    void testRetrieveCountryRecordsIT02() {
        CountryReports countryReports = new CountryReports();
        List<Country> countries = countryReports.getAllCountriesByPopulation(app.con);

        assertNotNull(countries, "Country list should not be null.");
        assertFalse(countries.isEmpty(), "Country list should contain records from the database.");
    }

    @Test // IT03; Retrieves city records
    void testRetrieveCityRecordsIT03() {
        CityReports cityReports = new CityReports();
        List<City> cities = cityReports.getAllCitiesInWorldByPopulation(app.con);

        assertNotNull(cities, "City list should not be null.");
        assertFalse(cities.isEmpty(), "City list should contain records from the database.");
    }
}
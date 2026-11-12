package report;

import com.napier.sem.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CountryReportsTest {
    static CountryReports countryReports;

    @BeforeAll
    static void init() {
        countryReports = new CountryReports();
    }

    @Test
    void testGetAllCountriesByPopulationNullConnection() {
        List<Country> countries = countryReports.getAllCountriesByPopulation(null);
        assertNotNull(countries, "The returned list should not be null, it should be empty.");
        assertTrue(countries.isEmpty(), "The list should be empty when connection is null.");
    }

    @Test
    void testGetTotalPopulationOfCountryNullConnection() {
        long pop = countryReports.getTotalPopulationOfCountry(null, "Myanmar");
        assertEquals(0, pop, "Population should return 0 if connection is null.");
    }
}
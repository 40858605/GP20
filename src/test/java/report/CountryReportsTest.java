package report;

import com.napier.sem.Country;
import com.napier.sem.PopulationBreakdown;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CountryReportsTest {
    static CountryReports countryReports;

    @BeforeAll
    static void init() {
        countryReports = new CountryReports();
    }

    // DATABASE METHODS: NULL CONNECTION TESTS

    @Test //Issue #3 phone-myint-myat-307
    void testGetAllCountriesByPopulationNullConnection() {
        List<Country> countries = countryReports.getAllCountriesByPopulation(null);
        assertNotNull(countries, "List should not be null when connection is null.");
        assertTrue(countries.isEmpty(), "List should be empty when connection is null.");
    }

    @Test //Issue #4 phone-myint-myat-307
    void testGetCountriesInContinentByPopulationNullConnection() {
        List<Country> countries = countryReports.getCountriesInContinentByPopulation(null, "Asia");
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test //Issue#9: 40858605
    void testGetTopNCountriesByPopulationNullConnection() {
        List<Country> countries = countryReports.getTopNCountriesByPopulation(null, 10);
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test //Issue#10: 40858605
    void testGetTopNCountriesInContinentByPopulationNullConnection() {
        List<Country> countries = countryReports.getTopNCountriesInContinentByPopulation(null, "Europe", 5);
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test //Issue#11: 40858605
    void testGetTopNCountriesInRegionByPopulationNullConnection() {
        List<Country> countries = countryReports.getTopNCountriesInRegionByPopulation(null, "Southeast Asia", 5);
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test //Issue #5 phone-myint-myat-307
    void testGetCountriesInRegionByPopulationNullConnection() {
        List<Country> countries = countryReports.getCountriesInRegionByPopulation(null, "Middle East");
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test //Issue #6 phone-myint-myat-307
    void testGetUrbanRuralByContinentNullConnection() {
        List<PopulationBreakdown> breakdown = countryReports.getUrbanRuralByContinent(null);
        assertNotNull(breakdown);
        assertTrue(breakdown.isEmpty());
    }

    @Test //Issue #7 phone-myint-myat-307
    void testGetUrbanRuralByRegionNullConnection() {
        List<PopulationBreakdown> breakdown = countryReports.getUrbanRuralByRegion(null);
        assertNotNull(breakdown);
        assertTrue(breakdown.isEmpty());
    }

    @Test //Issue #8    phone-myint-myat-307
    void testGetUrbanRuralByCountryNullConnection() {
        List<PopulationBreakdown> breakdown = countryReports.getUrbanRuralByCountry(null);
        assertNotNull(breakdown);
        assertTrue(breakdown.isEmpty());
    }

    @Test //Issue #30 phone-myint-myat-307
    void testGetTotalPopulationOfRegionNullConnection() {
        long pop = countryReports.getTotalPopulationOfRegion(null, "Southeast Asia");
        assertEquals(0, pop, "Population should return 0 if connection is null.");
    }

    @Test //Issue#31: 40858605
    void testGetTotalPopulationOfCountryNullConnection() {
        long pop = countryReports.getTotalPopulationOfCountry(null, "Myanmar");
        assertEquals(0, pop, "Population should return 0 if connection is null.");
    }

    @Test // Issue #28 winhponeemyat202104-del
    void testGetTotalWorldPopulationNullConnection() {
        long pop = countryReports.getTotalWorldPopulation(null);
        assertEquals(0, pop, "Population should return 0 if connection is null.");
    }

    @Test //Issue #29 winhponeemyat202104-del
    void testGetTotalPopulationOfContinentNullConnection() {
        long pop = countryReports.getTotalPopulationOfContinent(null, "Asia");
        assertEquals(0, pop, "Population should return 0 if connection is null.");
    }

    // PRINT METHODS: EDGE CASE TESTS
    @Test
    void printResultsTestNull() {
        // Tests if method correctly catches a null list without crashing
        CountryReports.printResults(null);
    }

    @Test
    void printResultsTestEmpty() {
        // Tests if method correctly handles an empty list
        List<Country> countries = new ArrayList<>();
        CountryReports.printResults(countries);
    }

    @Test
    void printResultsTestContainsNull() {
        // Tests if method correctly skips a null object inside the list
        List<Country> countries = new ArrayList<>();
        countries.add(null);
        CountryReports.printResults(countries);
    }

    @Test
    void printResultsTestNormal() {
        // Tests normal printing functionality
        List<Country> countries = new ArrayList<>();
        Country country = new Country("MMR", "Myanmar", "Asia", "Southeast Asia", 54409800, "Naypyidaw");
        countries.add(country);
        CountryReports.printResults(countries);
    }
}
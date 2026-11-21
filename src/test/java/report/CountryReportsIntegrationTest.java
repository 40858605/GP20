package report;

import com.napier.sem.App;
import com.napier.sem.Country;
import com.napier.sem.PopulationBreakdown;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CountryReportsIntegrationTest {

    private static App app;
    private static CountryReports reports;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("127.0.0.1:3307", 1000);
        reports = new CountryReports();
    }

    @AfterAll
    static void cleanup() {
        if (app != null) {
            app.disconnect();
        }
    }

    // IT04; Country reports return records ordered through population
    @Test
    void testCountryReportsIT04() {
        List<Country> allCountries = reports.getAllCountriesByPopulation(app.con);
        assertNotNull(allCountries);
        assertFalse(allCountries.isEmpty(), "Worldwide country list should not be empty.");
        assertTrue(allCountries.get(0).toString().contains("China"),
                "Most populated country worldwide should be China.");
        assertTrue(allCountries.stream().anyMatch(c -> c.toString().contains("Myanmar")),
                "Worldwide country list should include Myanmar.");

        List<Country> asianCountries = reports.getCountriesInContinentByPopulation(app.con, "Asia");
        assertFalse(asianCountries.isEmpty(), "Asian country list should not be empty.");
        assertTrue(asianCountries.get(0).toString().contains("China"),
                "Most populated country in Asia should be China.");
        assertFalse(asianCountries.stream().anyMatch(c -> c.toString().contains("France")),
                "Asian country list should not include European countries.");

        List<Country> seaCountries = reports.getCountriesInRegionByPopulation(app.con, "Southeast Asia");
        assertFalse(seaCountries.isEmpty(), "Southeast Asian country list should not be empty.");
        assertTrue(seaCountries.get(0).toString().contains("Indonesia"),
                "Most populated country in Southeast Asia should be Indonesia.");
        assertTrue(seaCountries.stream().anyMatch(c -> c.toString().contains("Myanmar")),
                "Southeast Asian country list should include Myanmar.");
        assertFalse(seaCountries.stream().anyMatch(c -> c.toString().contains("Japan")),
                "Southeast Asian country list should not include Eastern Asian countries.");
    }

    // IT04; top N countryreports return count, ordered through population
    @Test
    void testTopNCountryReportsIT04() {
        List<Country> topWorld = reports.getTopNCountriesByPopulation(app.con, 5);
        assertEquals(5, topWorld.size(), "Top 5 worldwide should return exactly 5 countries.");
        assertTrue(topWorld.get(0).toString().contains("China"),
                "Top 1 worldwide should be China.");
        assertTrue(topWorld.get(1).toString().contains("India"),
                "Top 2 worldwide should be India.");

        List<Country> topAsia = reports.getTopNCountriesInContinentByPopulation(app.con, "Asia", 3);
        assertEquals(3, topAsia.size(), "Top 3 in Asia should return exactly 3 countries.");
        assertTrue(topAsia.get(0).toString().contains("China"),
                "Top 1 in Asia should be China.");

        List<Country> topSea = reports.getTopNCountriesInRegionByPopulation(app.con, "Southeast Asia", 3);
        assertEquals(3, topSea.size(), "Top 3 in Southeast Asia should return exactly 3 countries.");
        assertTrue(topSea.get(0).toString().contains("Indonesia"),
                "Top 1 in Southeast Asia should be Indonesia.");
    }

    // IT08; Grouped urban/rural population breakdowns by continent, region, country
    @Test
    void testUrbanRuralBreakdownsIT08() {
        List<PopulationBreakdown> byContinent = reports.getUrbanRuralByContinent(app.con);
        assertNotNull(byContinent);
        assertFalse(byContinent.isEmpty(), "Continent population breakdown should not be empty.");
        assertTrue(byContinent.get(0).toString().contains("Asia"),
                "Asia should rank first in continent population breakdown.");
        assertTrue(byContinent.stream().anyMatch(b -> b.toString().contains("Europe")),
                "Continent breakdown should include Europe.");

        List<PopulationBreakdown> byRegion = reports.getUrbanRuralByRegion(app.con);
        assertNotNull(byRegion);
        assertFalse(byRegion.isEmpty(), "Region population breakdown should not be empty.");
        assertTrue(byRegion.stream().anyMatch(b -> b.toString().contains("Southeast Asia")),
                "Region breakdown should include Southeast Asia.");

        List<PopulationBreakdown> byCountry = reports.getUrbanRuralByCountry(app.con);
        assertNotNull(byCountry);
        assertFalse(byCountry.isEmpty(), "Country population breakdown should not be empty.");
        assertTrue(byCountry.get(0).toString().contains("China"),
                "China should rank first in country population breakdown.");
        assertTrue(byCountry.stream().anyMatch(b -> b.toString().contains("Myanmar")),
                "Country breakdown should include Myanmar.");
    }

    // IT08;Calculated total populations across world, continent, region, country
    @Test
    void testTotalPopulationCalculationsIT08() {
        long worldPop = reports.getTotalWorldPopulation(app.con);
        long asiaPop = reports.getTotalPopulationOfContinent(app.con, "Asia");
        long seaPop = reports.getTotalPopulationOfRegion(app.con, "Southeast Asia");
        long myanmarPop = reports.getTotalPopulationOfCountry(app.con, "Myanmar");

        assertTrue(myanmarPop > 0, "Myanmar population should be greater than 0.");
        assertTrue(seaPop > myanmarPop, "Southeast Asia population should exceed Myanmar's.");
        assertTrue(asiaPop > seaPop, "Asia population should exceed Southeast Asia's.");
        assertTrue(worldPop > asiaPop, "World population should exceed Asia's.");

        assertEquals(0, reports.getTotalPopulationOfContinent(app.con, "Atlantis"),
                "Nonexistent continent should return 0.");
        assertEquals(0, reports.getTotalPopulationOfRegion(app.con, "Nowhere"),
                "Nonexistent region should return 0.");
        assertEquals(0, reports.getTotalPopulationOfCountry(app.con, "Atlantis"),
                "Nonexistent country should return 0.");
    }

    // Edge cases; null connection handling n invalid filters on all reports
    @Test
    void testNullConnectionsAndInvalidFilters() {
        assertTrue(reports.getAllCountriesByPopulation(null).isEmpty());
        assertTrue(reports.getCountriesInContinentByPopulation(null, "Asia").isEmpty());
        assertTrue(reports.getCountriesInRegionByPopulation(null, "Southeast Asia").isEmpty());
        assertTrue(reports.getUrbanRuralByContinent(null).isEmpty());
        assertTrue(reports.getUrbanRuralByRegion(null).isEmpty());
        assertTrue(reports.getUrbanRuralByCountry(null).isEmpty());
        assertTrue(reports.getTopNCountriesByPopulation(null, 5).isEmpty());
        assertTrue(reports.getTopNCountriesInContinentByPopulation(null, "Asia", 5).isEmpty());
        assertTrue(reports.getTopNCountriesInRegionByPopulation(null, "Southeast Asia", 5).isEmpty());

        assertEquals(0, reports.getTotalWorldPopulation(null));
        assertEquals(0, reports.getTotalPopulationOfContinent(null, "Asia"));
        assertEquals(0, reports.getTotalPopulationOfRegion(null, "Southeast Asia"));
        assertEquals(0, reports.getTotalPopulationOfCountry(null, "Myanmar"));

        assertTrue(reports.getCountriesInContinentByPopulation(app.con, "Atlantis").isEmpty());
        assertTrue(reports.getCountriesInRegionByPopulation(app.con, "Nowhere").isEmpty());
        assertTrue(reports.getTopNCountriesInContinentByPopulation(app.con, "Atlantis", 5).isEmpty());
        assertTrue(reports.getTopNCountriesInRegionByPopulation(app.con, "Nowhere", 5).isEmpty());
    }

    // Output formatting: printResults with valid list (including null item), empty list, and null list
    @Test
    void testPrintResults() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            List<Country> topOne = reports.getTopNCountriesByPopulation(app.con, 1);
            CountryReports.printResults(Arrays.asList(topOne.get(0), null));
            CountryReports.printResults(Collections.emptyList());
            CountryReports.printResults(null);
        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);
        assertTrue(consoleOutput.contains("Continent"),
                "Output should display the country table header.");
        assertTrue(consoleOutput.contains("China"),
                "Output should display country records.");
        assertTrue(consoleOutput.contains("No matching countries found."),
                "Empty list should print no-matching-countries message.");
        assertTrue(consoleOutput.contains("No countries provided."),
                "Null list should print no-countries-provided message.");
    }
}
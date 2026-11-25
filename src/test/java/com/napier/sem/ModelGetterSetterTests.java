package com.napier.sem;

/*
 * Unit tests for model class getters and setters.
 * Verifies that Country, City, PopulationBreakdown and Language
 * correctly store, retrieve and update their field values.
 * Also checks the recalculation of urban and rural population percentages.
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelGetterSetterTests {

    // Tests getters and setters for Country fields.
    @Test
    void testCountryGettersAndSetters() {
        Country country = new Country(
                "MMR", "Myanmar", "Asia",
                "Southeast Asia", 54409800, "Naypyidaw"
        );

        // Verify the initial field values.
        assertEquals("MMR", country.getCode());
        assertEquals("Myanmar", country.getName());
        assertEquals("Asia", country.getContinent());
        assertEquals("Southeast Asia", country.getRegion());
        assertEquals(54409800, country.getPopulation());
        assertEquals("Naypyidaw", country.getCapital());

        // Update the country fields.
        country.setCode("THA");
        country.setName("Thailand");
        country.setContinent("Asia");
        country.setRegion("Southeast Asia");
        country.setPopulation(70000000);
        country.setCapital("Bangkok");

        // Verify the updated field values.
        assertEquals("THA", country.getCode());
        assertEquals("Thailand", country.getName());
        assertEquals("Asia", country.getContinent());
        assertEquals("Southeast Asia", country.getRegion());
        assertEquals(70000000, country.getPopulation());
        assertEquals("Bangkok", country.getCapital());
    }

    // Tests getters and setters for City fields.
    @Test
    void testCityGettersAndSetters() {
        City city = new City(
                "Yangon", "Myanmar", "Yangon", 5209541
        );

        // Verify the initial field values.
        assertEquals("Yangon", city.getName());
        assertEquals("Myanmar", city.getCountry());
        assertEquals("Yangon", city.getDistrict());
        assertEquals(5209541, city.getPopulation());

        // Update the city fields.
        city.setName("Mandalay");
        city.setCountry("Myanmar");
        city.setDistrict("Mandalay");
        city.setPopulation(1500000);

        // Verify the updated field values.
        assertEquals("Mandalay", city.getName());
        assertEquals("Myanmar", city.getCountry());
        assertEquals("Mandalay", city.getDistrict());
        assertEquals(1500000, city.getPopulation());
    }

    // Tests getters and setters for PopulationBreakdown fields.
    @Test
    void testPopulationBreakdownGettersAndSetters() {
        PopulationBreakdown breakdown = new PopulationBreakdown(
                "Asia", 1000000, 600000, 400000
        );

        // Verify the initial field values and calculated percentages.
        assertEquals("Asia", breakdown.getName());
        assertEquals(1000000, breakdown.getTotalPopulation());
        assertEquals(600000, breakdown.getCityPopulation());
        assertEquals(60.0, breakdown.getCityPercentage(), 0.01);
        assertEquals(400000, breakdown.getRuralPopulation());
        assertEquals(40.0, breakdown.getRuralPercentage(), 0.01);

        // Update the population fields.
        breakdown.setName("Europe");
        breakdown.setTotalPopulation(2000000);
        breakdown.setCityPopulation(1500000);
        breakdown.setRuralPopulation(500000);

        // Verify the updated values and recalculated percentages.
        assertEquals("Europe", breakdown.getName());
        assertEquals(2000000, breakdown.getTotalPopulation());
        assertEquals(1500000, breakdown.getCityPopulation());
        assertEquals(75.0, breakdown.getCityPercentage(), 0.01);
        assertEquals(500000, breakdown.getRuralPopulation());
        assertEquals(25.0, breakdown.getRuralPercentage(), 0.01);
    }

    // Tests getters and setters for Language fields.
    @Test
    void testLanguageGettersAndSetters() {
        Language language = new Language(
                "English", 1500000000L, 18.0
        );

        // Verify the initial field values.
        assertEquals("English", language.getName());
        assertEquals(1500000000L, language.getTotalSpeakers());
        assertEquals(18.0, language.getWorldPercentage(), 0.01);

        // Update the language fields.
        language.setName("Chinese");
        language.setTotalSpeakers(1200000000L);
        language.setWorldPercentage(15.0);

        // Verify the updated field values.
        assertEquals("Chinese", language.getName());
        assertEquals(1200000000L, language.getTotalSpeakers());
        assertEquals(15.0, language.getWorldPercentage(), 0.01);
    }

    @Test
    void testPopulationBreakdownZeroTotalPopulation() {

        PopulationBreakdown breakdown = new PopulationBreakdown(
                "Antarctica", 0, 0, 0
        );

        // Verify zero population percentage calculations
        assertEquals(0.0, breakdown.getCityPercentage(), 0.01);
        assertEquals(0.0, breakdown.getRuralPercentage(), 0.01);
    }
}

package com.napier.sem;

/*
 * Represents a capital city record retrieved from the database.
 * Stores the city name, country and population.
 */

public class CapitalCity {
    private String name;
    private String country;
    private long population;

    public CapitalCity(String name, String country, long population) {
        this.name = name;
        this.country = country;
        this.population = population;
    }

    // Returns capital city details in a formatted table row.
    @Override
    public String toString() {
        return String.format("%-35s %-35s %-12d", name, country, population);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }
}
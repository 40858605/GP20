package com.napier.sem;

/*
 * Represents a city record retrieved from the database.
 * Stores the city name, country, district and population.
 */

public class City {
    private String name;
    private String country;
    private String district;
    private long population;

    //this is for testing CI
    public City(String name, String country, String district, long population) {
        this.name = name;
        this.country = country;
        this.district = district;
        this.population = population;
    }

    @Override
    public String toString() {
        return String.format("%-35s %-35s %-25s %-12d", name, country, district, population);
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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
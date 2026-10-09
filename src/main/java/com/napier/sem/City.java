package com.napier.sem;

public class City {
    private String name;
    private String country;
    private String district;
    private long population;

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
}
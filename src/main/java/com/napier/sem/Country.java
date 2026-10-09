package com.napier.sem;

public class Country {
    private String code;
    private String name;
    private String continent;
    private String region;
    private long population;
    private String capital;

    public Country(String code, String name, String continent, String region, long population, String capital) {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.capital = capital;
    }

    @Override
    public String toString() {
        return String.format("%-5s %-45s %-20s %-25s %-12d %-20s",
                code, name, continent, region, population, capital != null ? capital : "N/A");
    }
}
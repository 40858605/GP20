package com.napier.sem;

public class CapitalCity {
    private String name;
    private String country;
    private long population;

    public CapitalCity(String name, String country, long population) {
        this.name = name;
        this.country = country;
        this.population = population;
    }

    @Override
    public String toString() {
        return String.format("%-35s %-35s %-12d", name, country, population);
    }
}
package com.napier.sem;

/*
 * Represents population statistics for a continent, region or country.
 * Stores total population, urban population, rural population
 * and their respective percentages.
 */

public class PopulationBreakdown {
    private String name;
    private long totalPopulation;
    private long cityPopulation;
    private double cityPercentage;
    private long ruralPopulation;
    private double ruralPercentage;

    public PopulationBreakdown(String name, long totalPopulation, long cityPopulation, long ruralPopulation) {
        this.name = name;
        this.totalPopulation = totalPopulation;
        this.cityPopulation = cityPopulation;
        this.ruralPopulation = ruralPopulation;
        this.cityPercentage = totalPopulation > 0 ? ((double) cityPopulation / totalPopulation) * 100 : 0.0;
        this.ruralPercentage = totalPopulation > 0 ? ((double) ruralPopulation / totalPopulation) * 100 : 0.0;
    }

    @Override
    public String toString() {
        return String.format("%-30s %-15d %-15d (%-5.2f%%) %-15d (%-5.2f%%)",
                name, totalPopulation, cityPopulation, cityPercentage, ruralPopulation, ruralPercentage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(long totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public long getCityPopulation() {
        return cityPopulation;
    }

    public void setCityPopulation(long cityPopulation) {
        this.cityPopulation = cityPopulation;
    }

    // Calculates the city population percentage.
    public double getCityPercentage() {
        return totalPopulation > 0
                ? ((double) cityPopulation / totalPopulation) * 100
                : 0.0;
    }

    public long getRuralPopulation() {
        return ruralPopulation;
    }

    public void setRuralPopulation(long ruralPopulation) {
        this.ruralPopulation = ruralPopulation;
    }

    // Calculates the rural population percentage.
    public double getRuralPercentage() {
        return totalPopulation > 0
                ? ((double) ruralPopulation / totalPopulation) * 100
                : 0.0;
    }
}
package com.napier.sem;

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
}
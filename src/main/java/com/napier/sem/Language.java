package com.napier.sem;

public class Language {
    private String name;
    private long totalSpeakers;
    private double worldPercentage;

    public Language(String name, long totalSpeakers, double worldPercentage) {
        this.name = name;
        this.totalSpeakers = totalSpeakers;
        this.worldPercentage = worldPercentage;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15d (%-5.2f%%)", name, totalSpeakers, worldPercentage);
    }
}
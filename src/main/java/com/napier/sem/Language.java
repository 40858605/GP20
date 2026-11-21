package com.napier.sem;

/*
 * Represents a language record retrieved from the database.
 * Stores the language name, total speakers and percentage
 * of the world's population who speak the language.
 */

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalSpeakers() {
        return totalSpeakers;
    }

    public void setTotalSpeakers(long totalSpeakers) {
        this.totalSpeakers = totalSpeakers;
    }

    public double getWorldPercentage() {
        return worldPercentage;
    }

    public void setWorldPercentage(double worldPercentage) {
        this.worldPercentage = worldPercentage;
    }
}
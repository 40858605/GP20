package com.napier.sem;

import report.CountryReports;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // Instantiate Database Handler
        DatabaseHandler db = new DatabaseHandler();
        db.connect();

        if (db.getConnection() == null) {
            System.out.println("Could not establish a database connection. Exiting...");
            return;
        }

        CountryReports countryReports = new CountryReports();
        //put ur own objects for reports here <---
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=================== SPRINT 1 REPORTS MENU ===================");
            System.out.println("1. All countries in world by population (No input)");
            System.out.println("2. All countries in a continent by population (Requires Continent)");
            //enter your reports name here, number 3 to 10 in order <---
            System.out.println("0. Exit");
            System.out.print("Select an option (0-10): ");

            String input = scanner.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (0-10).");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\nExecuting: All countries in world by population...\n");
                    List<Country> worldCountries = countryReports.getAllCountriesByPopulation(db.getConnection());
                    if (worldCountries.isEmpty()) {
                        System.out.println("No countries found.");
                    } else {
                        System.out.println(String.format("%-5s %-45s %-20s %-25s %-12s %-20s",
                                "Code", "Name", "Continent", "Region", "Population", "Capital"));
                        System.out.println("------------------------------------------------------------------------------------------------------------------------");
                        for (Country c : worldCountries) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 2:
                    System.out.print("Enter Continent (e.g., Europe, Asia): ");
                    String continent1 = scanner.nextLine().trim();
                    System.out.println("\nExecuting: All countries in " + continent1 + "...\n");
                    List<Country> continentCountries = countryReports.getCountriesInContinentByPopulation(db.getConnection(), continent1);
                    if (continentCountries.isEmpty()) {
                        System.out.println("No countries found for continent: " + continent1);
                    } else {
                        System.out.println(String.format("%-5s %-45s %-20s %-25s %-12s %-20s",
                                "Code", "Name", "Continent", "Region", "Population", "Capital"));
                        System.out.println("------------------------------------------------------------------------------------------------------------------------");
                        for (Country c : continentCountries) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid option or feature not yet implemented.");
            }
        }

        scanner.close();
        db.disconnect();
    }
}

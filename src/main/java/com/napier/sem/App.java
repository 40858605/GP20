package com.napier.sem;

import com.napier.sem.reports.PopulationReports;
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

        PopulationReports popReports = new PopulationReports();

        // ================= TEMPORARY FEATURE BRANCH TEST =================
        System.out.println("=== TEST 1: All Countries in World ===");
        List<Country> allCountries = popReports.getAllCountriesByPopulation(db.getConnection());
        for (int i = 0; i < Math.min(5, allCountries.size()); i++) {
            System.out.println(allCountries.get(i));
        }

        System.out.println("\n=== TEST 2: Countries in Europe ===");
        List<Country> europeCountries = popReports.getCountriesInContinentByPopulation(db.getConnection(), "Europe");
        for (int i = 0; i < Math.min(5, europeCountries.size()); i++) {
            System.out.println(europeCountries.get(i));
        }
        // =================================================================

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=================== SPRINT 1 REPORTS MENU ===================");
            System.out.println("1. All countries in world by population (No input)");
            System.out.println("2. All countries in a continent by population (Requires Continent)");
            System.out.println("3. Top N populated countries in world (Requires N)");
            System.out.println("4. Top N populated countries in a continent (Requires Continent & N)");
            System.out.println("5. All cities in a country by population (Requires Country)");
            System.out.println("6. All cities in a district by population (Requires District)");
            System.out.println("7. Top N populated cities in a country (Requires Country & N)");
            System.out.println("8. Top N populated cities in a district (Requires District & N)");
            System.out.println("9. Top N populated capital cities in world (Requires N)");
            System.out.println("10. Top N populated capital cities in a continent (Requires Continent & N)");
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
                    List<Country> worldCountries = popReports.getAllCountriesByPopulation(db.getConnection());
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
                    List<Country> continentCountries = popReports.getCountriesInContinentByPopulation(db.getConnection(), continent1);
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
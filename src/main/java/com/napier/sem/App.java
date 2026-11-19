package com.napier.sem;

import report.CountryReports;
import report.CityReports;
import report.CapitalCityReports;
import report.LanguageReports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {

    // Public connection exposed for Integration Testing
    public Connection con = null;

    private static final String BORDER = "=".repeat(65);
    private static final String DIVIDER = "-".repeat(65);

    // Dynamic Connect Method
    public void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(delay);
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    // Disconnect Method
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public static void main(String[] args) {

        App a = new App();

        // Connect to database using command line arguments or default to localhost
        if(args.length < 1){
            a.connect("localhost:3307", 30000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        if (a.con == null) {
            System.out.println("Could not establish a database connection. Exiting...");
            return;
        }

        CountryReports countryReports = new CountryReports();
        CityReports cityReports = new CityReports();
        CapitalCityReports capitalReports = new CapitalCityReports();
        LanguageReports languageReports = new LanguageReports();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            printHeader("WORLD DATABASE SYSTEM");

            System.out.println("\n  Please select a report category:\n");
            printOption(1, "Country Information");
            printOption(2, "City Information");
            printOption(3, "Capital City Information");
            printOption(4, "Population Statistics");
            printOption(5, "Language Statistics");

            printFooter();
            System.out.println("  [0]  Exit Application");
            System.out.println(BORDER);
            System.out.print("  Enter your choice: ");

            Integer choice = readChoice(scanner);

            if (choice == null) {
                System.out.println("\nNo interactive input detected. Exiting...");
                break;
            }

            switch (choice) {
                case 1:
                    countryMenu(scanner, a.con, countryReports);
                    break;
                case 2:
                    cityMenu(scanner, a.con, cityReports);
                    break;
                case 3:
                    capitalMenu(scanner, a.con, cityReports, capitalReports);
                    break;
                case 4:
                    populationMenu(scanner, a.con, countryReports, cityReports);
                    break;
                case 5:
                    languageMenu(scanner, a.con, languageReports);
                    break;
                case 0:
                    running = false;
                    System.out.println("\nThank you for using World Database System!");
                    break;
                default:
                    System.out.println("\nInvalid option. Please select 0-5.");
            }
        }

        scanner.close();
        a.disconnect();
    }

    // COUNTRY INFORMATION
    // ---------------------------------------------------------

    private static void countryMenu(
            Scanner scanner, Connection con, CountryReports reports) {

        while (true) {

            printHeader("COUNTRY INFORMATION");

            printSection("GENERAL COUNTRY REPORTS");
            printOption(1, "View All Countries Worldwide by Population");
            printOption(2, "View Countries by Continent");
            printOption(3, "View Countries by Region");

            printSection("TOP POPULATION REPORTS");
            printOption(4, "Top N Most Populated Countries Worldwide");
            printOption(5, "Top N Most Populated Countries in a Continent");
            printOption(6, "Top N Most Populated Countries in a Region");

            printFooter();
            printBackOption();

            Integer choice = readChoice(scanner);
            if (choice == null) return;

            switch (choice) {

                case 1:
                    printCountryResults(reports.getAllCountriesByPopulation(con));
                    break;

                case 2: {
                    String continent = readText(scanner, "Enter continent name: ");
                    if (continent == null) return;

                    if (continent.isEmpty()) {
                        System.out.println("Continent cannot be empty.");
                        break;
                    }

                    printCountryResults(reports.getCountriesInContinentByPopulation(con, continent));
                    break;
                }

                case 3: {
                    String region = readText(scanner, "Enter region name: ");
                    if (region == null) return;

                    if (region.isEmpty()) {
                        System.out.println("Region cannot be empty.");
                        break;
                    }

                    printCountryResults(reports.getCountriesInRegionByPopulation(con, region));
                    break;
                }

                case 4: {
                    Integer n = readPositiveInt(scanner, "Enter the number of countries: ");
                    if (n == null) return;
                    if (n < 1) break;

                    printCountryResults(reports.getTopNCountriesByPopulation(con, n));
                    break;
                }

                case 5: {
                    String continent = readText(scanner, "Enter continent name: ");
                    if (continent == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of countries: ");
                    if (n == null) return;

                    if (continent.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    printCountryResults(reports.getTopNCountriesInContinentByPopulation(con, continent, n));
                    break;
                }

                case 6: {
                    String region = readText(scanner, "Enter region name: ");
                    if (region == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of countries: ");
                    if (n == null) return;

                    if (region.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    printCountryResults(reports.getTopNCountriesInRegionByPopulation(con, region, n));
                    break;
                }

                case 0:
                    return;

                default:
                    invalidOption();
            }
        }
    }

    // CITY INFORMATION
    // -------------------------------------------------------

    private static void cityMenu(
            Scanner scanner, Connection con, CityReports reports) {

        while (true) {

            printHeader("CITY INFORMATION");

            printSection("GENERAL CITY REPORTS");
            printOption(1, "View All Cities Worldwide by Population");
            printOption(2, "View Cities by Continent");
            printOption(3, "View Cities by Region");
            printOption(4, "View Cities by Country");
            printOption(5, "View Cities by District");

            printSection("TOP POPULATION REPORTS");
            printOption(6, "Top N Most Populated Cities Worldwide");
            printOption(7, "Top N Most Populated Cities in a Continent");
            printOption(8, "Top N Most Populated Cities in a Region");
            printOption(9, "Top N Most Populated Cities in a Country");
            printOption(10, "Top N Most Populated Cities in a District");

            printFooter();
            printBackOption();

            Integer choice = readChoice(scanner);
            if (choice == null) return;

            switch (choice) {

                case 1:
                    printCityResults(reports.getAllCitiesInWorldByPopulation(con));
                    break;

                case 2: {
                    String continent = readText(scanner, "Enter continent name: ");
                    if (continent == null) return;

                    printCityResults(reports.getCitiesInContinentByPopulation(con, continent));
                    break;
                }

                case 3: {
                    String region = readText(scanner, "Enter region name: ");
                    if (region == null) return;

                    printCityResults(reports.getCitiesInRegionByPopulation(con, region));
                    break;
                }

                case 4: {
                    String country = readText(scanner, "Enter country name: ");
                    if (country == null) return;

                    printCityResults(reports.getCitiesInCountryByPopulation(con, country));
                    break;
                }

                case 5: {
                    String district = readText(scanner, "Enter district name: ");
                    if (district == null) return;

                    printCityResults(reports.getCitiesInDistrictByPopulation(con, district));
                    break;
                }

                case 6: {
                    Integer n = readPositiveInt(scanner, "Enter the number of cities: ");
                    if (n == null) return;
                    if (n < 1) break;

                    printCityResults(reports.getTopNCitiesWorldwide(con, n));
                    break;
                }

                case 7: {
                    String continent = readText(scanner, "Enter continent name: ");
                    if (continent == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of cities: ");
                    if (n == null) return;

                    if (continent.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    printCityResults(reports.getTopNCitiesInContinentByPopulation(con, continent, n));
                    break;
                }

                case 8: {
                    String region = readText(scanner, "Enter region name: ");
                    if (region == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of cities: ");
                    if (n == null) return;

                    if (region.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    printCityResults(reports.getTopNCitiesInRegionByPopulation(con, region, n));
                    break;
                }

                case 9: {
                    String country = readText(scanner, "Enter country name: ");
                    if (country == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of cities: ");
                    if (n == null) return;

                    if (country.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    try {
                        CityReports.printTopNCitiesInCountry(con, country, n);
                    } catch (SQLException e) {
                        databaseError(e);
                    }
                    break;
                }

                case 10: {
                    String district = readText(scanner, "Enter district name: ");
                    if (district == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of cities: ");
                    if (n == null) return;

                    if (district.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    try {
                        CityReports.printTopNCitiesInDistrict(con, district, n);
                    } catch (SQLException e) {
                        databaseError(e);
                    }
                    break;
                }

                case 0:
                    return;

                default:
                    invalidOption();
            }
        }
    }

    // CAPITAL CITY INFORMATION
    // -------------------------------------------------------

    private static void capitalMenu(
            Scanner scanner,
            Connection con,
            CityReports cityReports,
            CapitalCityReports reports) {

        while (true) {

            printHeader("CAPITAL CITY INFORMATION");

            printSection("GENERAL CAPITAL CITY REPORTS");
            printOption(1, "View All Capital Cities Worldwide");
            printOption(2, "View Capital Cities by Continent");
            printOption(3, "View Capital Cities by Region");

            printSection("TOP POPULATION REPORTS");
            printOption(4, "Top N Most Populated Capital Cities Worldwide");
            printOption(5, "Top N Most Populated Capital Cities in a Continent");
            printOption(6, "Top N Most Populated Capital Cities in a Region");

            printFooter();
            printBackOption();

            Integer choice = readChoice(scanner);
            if (choice == null) return;

            switch (choice) {

                case 1:
                    reports.printResults(reports.getCapitalCitiesInWorldByPopulation(con));
                    break;

                case 2: {
                    String continent = readText(scanner, "Enter continent name: ");
                    if (continent == null) return;

                    reports.printResults(reports.getCapitalCitiesInContinentByPopulation(con, continent));
                    break;
                }

                case 3: {
                    String region = readText(scanner, "Enter region name: ");
                    if (region == null) return;

                    reports.printResults(reports.getCapitalCitiesInRegionByPopulation(con, region));
                    break;
                }

                case 4: {
                    Integer n = readPositiveInt(scanner, "Enter the number of capital cities: ");
                    if (n == null) return;
                    if (n < 1) break;

                    try {
                        CityReports.printTopNCapitalCitiesInWorld(con, n);
                    } catch (SQLException e) {
                        databaseError(e);
                    }
                    break;
                }

                case 5: {
                    String continent = readText(scanner, "Enter continent name: ");
                    if (continent == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of capital cities: ");
                    if (n == null) return;

                    if (continent.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    try {
                        CityReports.printTopNCapitalCitiesInContinent(con, continent, n);
                    } catch (SQLException e) {
                        databaseError(e);
                    }
                    break;
                }

                case 6: {
                    String region = readText(scanner, "Enter region name: ");
                    if (region == null) return;

                    Integer n = readPositiveInt(scanner, "Enter the number of capital cities: ");
                    if (n == null) return;

                    if (region.isEmpty() || n < 1) {
                        System.out.println("Please enter valid details.");
                        break;
                    }

                    try {
                        CityReports.printTopNCapitalCitiesInRegion(con, region, n);
                    } catch (SQLException e) {
                        databaseError(e);
                    }
                    break;
                }

                case 0:
                    return;

                default:
                    invalidOption();
            }
        }
    }

    // POPULATION STATISTICS
    // -------------------------------------------------------

    private static void populationMenu(
            Scanner scanner,
            Connection con,
            CountryReports countryReports,
            CityReports cityReports) {

        while (true) {

            printHeader("POPULATION STATISTICS");

            printSection("URBAN AND RURAL POPULATION");
            printOption(1, "Population Breakdown by Continent");
            printOption(2, "Population Breakdown by Region");
            printOption(3, "Population Breakdown by Country");

            printSection("TOTAL POPULATION REPORTS");
            printOption(4, "Total Population Worldwide");
            printOption(5, "Total Population of a Continent");
            printOption(6, "Total Population of a Region");
            printOption(7, "Total Population of a Country");
            printOption(8, "Total Population of a District");
            printOption(9, "Total Population of a City");

            printFooter();
            printBackOption();

            Integer choice = readChoice(scanner);
            if (choice == null) return;

            switch (choice) {

                case 1:
                    printBreakdownResults(countryReports.getUrbanRuralByContinent(con));
                    break;

                case 2:
                    printBreakdownResults(countryReports.getUrbanRuralByRegion(con));
                    break;

                case 3:
                    printBreakdownResults(countryReports.getUrbanRuralByCountry(con));
                    break;

                case 4:
                    System.out.println("\nWORLD POPULATION");
                    System.out.printf("Total Population: %,d%n", countryReports.getTotalWorldPopulation(con));
                    break;

                case 5: {
                    String continent = readText(scanner, "Enter continent name: ");
                    if (continent == null) return;

                    if (continent.isEmpty()) {
                        System.out.println("Continent cannot be empty.");
                        break;
                    }

                    System.out.printf("\nTotal Population of %s: %,d%n",
                            continent,
                            countryReports.getTotalPopulationOfContinent(con, continent));
                    break;
                }

                case 6: {
                    String region = readText(scanner, "Enter region name: ");
                    if (region == null) return;

                    if (region.isEmpty()) {
                        System.out.println("Region cannot be empty.");
                        break;
                    }

                    System.out.printf("\nTotal Population of %s: %,d%n",
                            region,
                            countryReports.getTotalPopulationOfRegion(con, region));
                    break;
                }

                case 7: {
                    String country = readText(scanner, "Enter country name: ");
                    if (country == null) return;

                    if (country.isEmpty()) {
                        System.out.println("Country cannot be empty.");
                        break;
                    }

                    System.out.printf("\nTotal Population of %s: %,d%n",
                            country,
                            countryReports.getTotalPopulationOfCountry(con, country));
                    break;
                }

                case 8: {
                    String district = readText(scanner, "Enter district name: ");
                    if (district == null) return;

                    if (district.isEmpty()) {
                        System.out.println("District cannot be empty.");
                        break;
                    }

                    System.out.printf("\nTotal Population of %s: %,d%n",
                            district,
                            cityReports.getTotalPopulationOfDistrict(con, district));
                    break;
                }

                case 9: {
                    String city = readText(scanner, "Enter city name: ");
                    if (city == null) return;

                    if (city.isEmpty()) {
                        System.out.println("City cannot be empty.");
                        break;
                    }

                    Long population = cityReports.getPopulationOfCity(con, city);

                    if (population == null) {
                        System.out.println("City not found.");
                    } else {
                        System.out.printf("\nTotal Population of %s: %,d%n",
                                city, population);
                    }
                    break;
                }

                case 0:
                    return;

                default:
                    invalidOption();
            }
        }
    }

    // LANGUAGE STATISTICS
    // -------------------------------------------------------

    private static void languageMenu(
            Scanner scanner, Connection con, LanguageReports reports) {

        while (true) {

            printHeader("LANGUAGE STATISTICS");

            printSection("GLOBAL LANGUAGE REPORTS");
            printOption(1, "Major Global Languages");

            System.out.println("\n  Includes Chinese, English, Hindi, Spanish and Arabic.");

            printFooter();
            printBackOption();

            Integer choice = readChoice(scanner);
            if (choice == null) return;

            switch (choice) {

                case 1:
                    reports.printMajorGlobalLanguages(con);
                    break;

                case 0:
                    return;

                default:
                    invalidOption();
            }
        }
    }

    // REUSABLE MENU DISPLAY METHODS
    // ---------------------------------------------------------

    private static void printHeader(String title) {

        System.out.println("\n" + BORDER);
        System.out.printf("%" + ((65 + title.length()) / 2) + "s%n", title);
        System.out.println(BORDER);
    }

    private static void printSection(String title) {

        System.out.println("\n  " + title);
        System.out.println("  " + "-".repeat(title.length()));
    }

    private static void printOption(int number, String description) {

        System.out.printf("  [%-2d]  %s%n", number, description);
    }

    private static void printFooter() {

        System.out.println("\n" + DIVIDER);
    }

    private static void printBackOption() {

        System.out.println("  [0]   Return to Main Menu");
        System.out.println(BORDER);
        System.out.print("  Enter your choice: ");
    }

    private static void invalidOption() {

        System.out.println("\nInvalid option. Please try again.");
    }

    // REUSABLE REPORT DISPLAY METHODS
    // ------------------------------------------------------

    private static void printCountryResults(List<Country> countries) {

        if (countries == null || countries.isEmpty()) {
            System.out.println("No matching countries found.");
            return;
        }

        System.out.println("\nCOUNTRY REPORT RESULTS\n");

        System.out.printf("%-5s %-45s %-20s %-25s %-12s %-20s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");

        System.out.println("------------------------------------------------------------------------------------------------------------------------");

        for (Country country : countries) {
            System.out.println(country);
        }
    }

    private static void printCityResults(List<City> cities) {

        if (cities == null || cities.isEmpty()) {
            System.out.println("No matching cities found.");
            return;
        }

        System.out.println("\nCITY REPORT RESULTS\n");

        System.out.printf("%-35s %-35s %-25s %-12s%n",
                "City", "Country", "District", "Population");

        System.out.println("----------------------------------------------------------------------------------------------");

        for (City city : cities) {
            System.out.println(city);
        }
    }

    private static void printBreakdownResults(
            List<PopulationBreakdown> breakdownList) {

        if (breakdownList == null || breakdownList.isEmpty()) {
            System.out.println("No population breakdown data found.");
            return;
        }

        System.out.println("\nPOPULATION BREAKDOWN RESULTS\n");

        System.out.printf("%-40s %-15s %-15s %-15s%n",
                "Name", "Total Pop", "City Pop", "Rural Pop");

        System.out.println("--------------------------------------------------------------------------------");

        for (PopulationBreakdown pb : breakdownList) {
            System.out.println(pb);
        }
    }

    // INPUT VALIDATION METHODS
    // ------------------------------------------------------

    private static Integer readChoice(Scanner scanner) {

        if (!scanner.hasNextLine()) {
            return null;
        }

        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String readText(Scanner scanner, String message) {

        System.out.print("\n" + message);

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine().trim();
    }

    private static Integer readPositiveInt(
            Scanner scanner, String message) {

        System.out.print("\n" + message);

        Integer number = readChoice(scanner);

        if (number == null) {
            return null;
        }

        if (number <= 0) {
            System.out.println("Please enter a number greater than zero.");
            return -1;
        }

        return number;
    }

    private static void databaseError(SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}
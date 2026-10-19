package com.napier.sem;

import report.CapitalCityReports;
import report.CountryReports;
import report.CityReports;
import report.TopNCitiesReport;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Instantiate Database Handler
        DatabaseHandler db = new DatabaseHandler();
        db.connect();

        if (db.getConnection() == null) {
            System.out.println(
                    "Could not establish a database connection. Exiting...");
            db.disconnect();
            return;
        }

        CountryReports countryReports = new CountryReports();
        CityReports cityReports = new CityReports();
        TopNCitiesReport topNCitiesReport = new TopNCitiesReport();
        CapitalCityReports capitalCityReports = new CapitalCityReports();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=================== REPORTS MENU ===================");
            System.out.println("1. All countries in world by population");
            System.out.println("2. All countries in a continent by population");
            System.out.println("3. Top N populated countries in the world");
            System.out.println("4. Top N populated countries in a continent");
            System.out.println("5. All cities in a country by population");
            System.out.println("6. All cities in a district by population");
            System.out.println("7. Top N cities in the world by population");
            System.out.println("8. Top N cities in a continent by population");
            System.out.println("9. Top N populated cities in a country");
            System.out.println("10. Top N populated cities in a district");
            System.out.println("11. Top N populated capital cities in the world");
            System.out.println("12. Top N populated capital cities in a continent");
            System.out.println("0. Exit");
            System.out.print("Select an option (0-12): ");

            String input = scanner.nextLine().trim();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Please enter a valid number (0-12).");
                continue;
            }

            switch (choice) {

                case 1: {
                    System.out.println(
                            "\nExecuting: All countries in world by population...\n");

                    List<Country> countries =
                            countryReports.getAllCountriesByPopulation(
                                    db.getConnection());

                    if (countries.isEmpty()) {
                        System.out.println("No countries found.");
                    } else {
                        System.out.printf(
                                "%-5s %-45s %-20s %-25s %-12s %-20s%n",
                                "Code", "Name", "Continent", "Region",
                                "Population", "Capital");

                        System.out.println(
                                "------------------------------------------------------------------------------------------------------------------------");

                        for (Country country : countries) {
                            System.out.println(country);
                        }
                    }
                    break;
                }

                case 2: {
                    System.out.print(
                            "Enter Continent (e.g., Europe, Asia): ");
                    String continent = scanner.nextLine().trim();

                    System.out.println(
                            "\nExecuting: All countries in " +
                                    continent + "...\n");

                    List<Country> countries =
                            countryReports.getCountriesInContinentByPopulation(
                                    db.getConnection(), continent);

                    if (countries.isEmpty()) {
                        System.out.println(
                                "No countries found for continent: " + continent);
                    } else {
                        System.out.printf(
                                "%-5s %-45s %-20s %-25s %-12s %-20s%n",
                                "Code", "Name", "Continent", "Region",
                                "Population", "Capital");

                        System.out.println(
                                "------------------------------------------------------------------------------------------------------------------------");

                        for (Country country : countries) {
                            System.out.println(country);
                        }
                    }
                    break;
                }

                case 3: {
                    System.out.print("Enter the number of top countries: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (n <= 0) {
                            System.out.println(
                                    "Number must be greater than zero.");
                            break;
                        }

                        List<Country> countries =
                                countryReports.getTopNCountriesByPopulation(
                                        db.getConnection(), n);

                        if (countries.isEmpty()) {
                            System.out.println("No countries found.");
                        } else {
                            for (Country country : countries) {
                                System.out.println(country);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    }
                    break;
                }

                case 4: {
                    System.out.print(
                            "Enter continent (e.g., Europe, Asia): ");
                    String continent = scanner.nextLine().trim();

                    System.out.print("Enter the number of top countries: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (continent.isEmpty() || n <= 0) {
                            System.out.println(
                                    "Enter a valid continent and number greater than zero.");
                            break;
                        }

                        List<Country> countries =
                                countryReports
                                        .getTopNCountriesInContinentByPopulation(
                                                db.getConnection(), continent, n);

                        if (countries.isEmpty()) {
                            System.out.println(
                                    "No countries found for continent: " +
                                            continent);
                        } else {
                            for (Country country : countries) {
                                System.out.println(country);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    }
                    break;
                }

                case 5: {
                    System.out.print("Enter country name: ");
                    String countryName = scanner.nextLine().trim();

                    List<City> cities =
                            cityReports.getCitiesInCountryByPopulation(
                                    db.getConnection(), countryName);

                    if (cities.isEmpty()) {
                        System.out.println(
                                "No cities found for country: " + countryName);
                    } else {
                        System.out.printf(
                                "%-35s %-35s %-25s %-12s%n",
                                "City", "Country", "District", "Population");

                        for (City city : cities) {
                            System.out.println(city);
                        }
                    }
                    break;
                }

                case 6: {
                    System.out.print("Enter district name: ");
                    String districtName = scanner.nextLine().trim();

                    List<City> cities =
                            cityReports.getCitiesInDistrictByPopulation(
                                    db.getConnection(), districtName);

                    if (cities.isEmpty()) {
                        System.out.println(
                                "No cities found for district: " + districtName);
                    } else {
                        System.out.printf(
                                "%-35s %-35s %-25s %-12s%n",
                                "City", "Country", "District", "Population");

                        for (City city : cities) {
                            System.out.println(city);
                        }
                    }
                    break;
                }

                case 7: {
                    System.out.print("Enter the number of top cities: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (n <= 0) {
                            System.out.println(
                                    "Number must be greater than zero.");
                            break;
                        }

                        List<City> cities =
                                cityReports.getTopNCitiesByPopulation(
                                        db.getConnection(), n);

                        if (cities.isEmpty()) {
                            System.out.println("No cities found.");
                        } else {
                            System.out.printf(
                                    "%-35s %-35s %-25s %-12s%n",
                                    "City", "Country", "District", "Population");

                            for (City city : cities) {
                                System.out.println(city);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    }
                    break;
                }

                case 8: {
                    System.out.print(
                            "Enter continent (e.g., Europe, Asia): ");
                    String continent = scanner.nextLine().trim();

                    System.out.print("Enter the number of top cities: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (continent.isEmpty() || n <= 0) {
                            System.out.println(
                                    "Enter a valid continent and a number greater than zero.");
                            break;
                        }

                        List<City> cities =
                                cityReports.getTopNCitiesInContinentByPopulation(
                                        db.getConnection(), continent, n);

                        if (cities.isEmpty()) {
                            System.out.println(
                                    "No cities found for continent: " + continent);
                        } else {
                            System.out.printf(
                                    "%-35s %-35s %-25s %-12s%n",
                                    "City", "Country", "District", "Population");

                            for (City city : cities) {
                                System.out.println(city);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    }
                    break;
                }

                // Report #20: Top N populated cities in a country
                case 9: {
                    System.out.print("Enter country name: ");
                    String countryName = scanner.nextLine().trim();

                    System.out.print("Enter the number of top cities: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (countryName.isEmpty() || n <= 0) {
                            System.out.println(
                                    "Enter a valid country and a number greater than zero.");
                            break;
                        }

                        List<City> cities =
                                topNCitiesReport.getTopNCitiesInCountry(
                                        db.getConnection(), countryName, n);

                        System.out.println(
                                "\nTop " + n +
                                        " populated cities in " + countryName);

                        topNCitiesReport.printResults(cities);

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    }
                    break;
                }

                // Report #21: Top N populated cities in a district
                case 10: {
                    System.out.print("Enter district name: ");
                    String districtName = scanner.nextLine().trim();

                    System.out.print("Enter the number of top cities: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (districtName.isEmpty() || n <= 0) {
                            System.out.println(
                                    "Enter a valid district and a number greater than zero.");
                            break;
                        }

                        List<City> cities =
                                topNCitiesReport.getTopNCitiesInDistrict(
                                        db.getConnection(), districtName, n);

                        System.out.println(
                                "\nTop " + n +
                                        " populated cities in district " +
                                        districtName);

                        topNCitiesReport.printResults(cities);

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    }
                    break;
                }

                case 11: {
                    System.out.print(
                            "Enter the number of top capital cities: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (n <= 0) {
                            System.out.println(
                                    "Number must be greater than zero.");
                            break;
                        }

                        CityReports.printTopNCapitalCitiesInWorld(
                                db.getConnection(), n);

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    } catch (java.sql.SQLException e) {
                        System.out.println(
                                "Database error: " + e.getMessage());
                    }
                    break;
                }

                case 12: {
                    System.out.print(
                            "Enter continent (e.g., Europe, Asia): ");
                    String continent = scanner.nextLine().trim();

                    System.out.print(
                            "Enter the number of top capital cities: ");

                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (continent.isEmpty() || n <= 0) {
                            System.out.println(
                                    "Enter a valid continent and a number greater than zero.");
                            break;
                        }

                        CityReports.printTopNCapitalCitiesInContinent(
                                db.getConnection(), continent, n);

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Invalid input. Please enter a valid number.");
                    } catch (java.sql.SQLException e) {
                        System.out.println(
                                "Database error: " + e.getMessage());
                    }
                    break;
                }

                case 0:
                    running = false;
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose a number between 0 and 12.");
            }
        }

        scanner.close();
        db.disconnect();
    }
}

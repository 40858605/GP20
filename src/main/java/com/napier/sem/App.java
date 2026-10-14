package com.napier.sem;

import report.CountryReports;
import report.CityReports;
import java.sql.SQLException;
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
        CityReports cityReports = new CityReports();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=================== REPORTS MENU ===================");
            System.out.println("1. All countries in the world by population (Issue #3)");
            System.out.println("2. All countries in a continent by population (Issue #4)");
            System.out.println("3. Top N populated countries in the world (Issue #9)");
            System.out.println("4. Top N populated countries in a continent (Issue #10)");
            System.out.println("5. All cities in a country by population (Issue #15)");
            System.out.println("6. All cities in a district by population (Issue #16)");
            System.out.println("7. Top N populated cities in a country (Issue #20)");
            System.out.println("8. Top N populated cities in a district (Issue #21)");
            System.out.println("9. Top N populated capital cities in the world (Issue #25)");
            System.out.println("10. Top N populated capital cities in a continent (Issue #26)");
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
                case 1: // Issue #3
                    System.out.println("\nExecuting: All countries in world by population...\n");
                    List<Country> worldCountries = countryReports.getAllCountriesByPopulation(db.getConnection());
                    if (worldCountries.isEmpty()) {
                        System.out.println("No countries found.");
                    } else {
                        System.out.printf("%-5s %-45s %-20s %-25s %-12s %-20s%n",
                                "Code", "Name", "Continent", "Region", "Population", "Capital");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------");
                        for (Country c : worldCountries) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 2: // Issue #4
                    System.out.print("Enter Continent (e.g., Europe, Asia): ");
                    String continent1 = scanner.nextLine().trim();
                    System.out.println("\nExecuting: All countries in " + continent1 + "...\n");
                    List<Country> continentCountries = countryReports.getCountriesInContinentByPopulation(db.getConnection(), continent1);
                    if (continentCountries.isEmpty()) {
                        System.out.println("No countries found for continent: " + continent1);
                    } else {
                        System.out.printf("%-5s %-45s %-20s %-25s %-12s %-20s%n",
                                "Code", "Name", "Continent", "Region", "Population", "Capital");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------");
                        for (Country c : continentCountries) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 3: // Issue #9
                    System.out.print("Enter the number of top countries: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (n <= 0) {
                            System.out.println("Number must be greater than zero.");
                            break;
                        }

                        List<Country> countries = countryReports.getTopNCountriesByPopulation(db.getConnection(), n);

                        if (countries.isEmpty()) {
                            System.out.println("No countries found.");
                        } else {
                            for (Country c : countries) {
                                System.out.println(c);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                    break;

                case 4: // Issue #10
                    System.out.print("Enter continent (e.g., Europe, Asia): ");
                    String continent = scanner.nextLine().trim();

                    System.out.print("Enter the number of top countries: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (continent.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid continent and number greater than zero.");
                            break;
                        }

                        List<Country> countries = countryReports.getTopNCountriesInContinentByPopulation(db.getConnection(), continent, n);

                        if (countries.isEmpty()) {
                            System.out.println("No countries found for continent: " + continent);
                        } else {
                            for (Country c : countries) {
                                System.out.println(c);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                    break;

                case 5: // Issue #15
                    System.out.print("Enter country name: ");
                    String countryName = scanner.nextLine().trim();

                    List<City> citiesInCountry = cityReports.getCitiesInCountryByPopulation(db.getConnection(), countryName);

                    if (citiesInCountry.isEmpty()) {
                        System.out.println("No cities found for country: " + countryName);
                    } else {
                        System.out.printf("%-35s %-35s %-25s %-12s%n", "City", "Country", "District", "Population");
                        for (City city : citiesInCountry) {
                            System.out.println(city);
                        }
                    }
                    break;

                case 6: // Issue #16
                    System.out.print("Enter district name: ");
                    String districtName = scanner.nextLine().trim();

                    List<City> citiesInDistrict = cityReports.getCitiesInDistrictByPopulation(db.getConnection(), districtName);

                    if (citiesInDistrict.isEmpty()) {
                        System.out.println("No cities found for district: " + districtName);
                    } else {
                        System.out.printf("%-35s %-35s %-25s %-12s%n", "City", "Country", "District", "Population");
                        for (City city : citiesInDistrict) {
                            System.out.println(city);
                        }
                    }
                    break;

                case 7: // Issue #20
                    System.out.print("Enter country name: ");
                    String countryName7 = scanner.nextLine().trim();
                    System.out.print("Enter the number of top cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (countryName7.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid country and a number greater than zero.");
                            break;
                        }
                        CityReports.printTopNCitiesInCountry(db.getConnection(), countryName7, n);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                case 8: // Issue #21
                    System.out.print("Enter district name: ");
                    String districtName8 = scanner.nextLine().trim();
                    System.out.print("Enter the number of top cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (districtName8.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid district and a number greater than zero.");
                            break;
                        }
                        CityReports.printTopNCitiesInDistrict(db.getConnection(), districtName8, n);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                case 9: // Issue #25
                    System.out.print("Enter the number of top capital cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (n <= 0) {
                            System.out.println("Number must be greater than zero.");
                            break;
                        }
                        CityReports.printTopNCapitalCitiesInWorld(db.getConnection(), n);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                case 10: // Issue #26
                    System.out.print("Enter continent (e.g., Europe, Asia): ");
                    String continent10 = scanner.nextLine().trim();
                    System.out.print("Enter the number of top capital cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (continent10.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid continent and a number greater than zero.");
                            break;
                        }
                        CityReports.printTopNCapitalCitiesInContinent(db.getConnection(), continent10, n);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid option. Please choose a number between 0 and 10.");
            }
        }

        scanner.close();
        db.disconnect();
    }
}

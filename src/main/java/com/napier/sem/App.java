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
                System.out.println("Invalid input. Please enter a valid number (0-12).");
                continue;
            }

            switch (choice) {
                case 1:
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

                case 2:
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

                case 3:
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

                case 4:
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

                case 5:
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

                case 6:
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

                case 7:
                    System.out.print("Enter the number of top cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (n <= 0) {
                            System.out.println("Number must be greater than zero.");
                            break;
                        }

                        List<City> topCities = cityReports.getTopNCitiesByPopulation(db.getConnection(), n);

                        if (topCities.isEmpty()) {
                            System.out.println("No cities found.");
                        } else {
                            System.out.printf("%-35s %-35s %-25s %-12s%n", "City", "Country", "District", "Population");
                            for (City city : topCities) {
                                System.out.println(city);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                    break;

                case 8:
                    System.out.print("Enter continent (e.g., Europe, Asia): ");
                    String continent8 = scanner.nextLine().trim();

                    System.out.print("Enter the number of top cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());

                        if (continent8.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid continent and a number greater than zero.");
                            break;
                        }

                        List<City> cities = cityReports.getTopNCitiesInContinentByPopulation(db.getConnection(), continent8, n);

                        if (cities.isEmpty()) {
                            System.out.println("No cities found for continent: " + continent8);
                        } else {
                            System.out.printf("%-35s %-35s %-25s %-12s%n", "City", "Country", "District", "Population");
                            for (City city : cities) {
                                System.out.println(city);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                    break;

                case 9:
                    System.out.print("Enter country name: ");
                    String countryName9 = scanner.nextLine().trim();
                    System.out.print("Enter the number of top cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (countryName9.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid country and a number greater than zero.");
                            break;
                        }
                        CityReports.printTopNCitiesInCountry(db.getConnection(), countryName9, n);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                case 10:
                    System.out.print("Enter district name: ");
                    String districtName10 = scanner.nextLine().trim();
                    System.out.print("Enter the number of top cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (districtName10.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid district and a number greater than zero.");
                            break;
                        }
                        CityReports.printTopNCitiesInDistrict(db.getConnection(), districtName10, n);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                case 11:
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

                case 12:
                    System.out.print("Enter continent (e.g., Europe, Asia): ");
                    String continent12 = scanner.nextLine().trim();
                    System.out.print("Enter the number of top capital cities: ");
                    try {
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (continent12.isEmpty() || n <= 0) {
                            System.out.println("Enter a valid continent and a number greater than zero.");
                            break;
                        }
                        CityReports.printTopNCapitalCitiesInContinent(db.getConnection(), continent12, n);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    } catch (SQLException e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    break;

                // Issue #4: Urban vs. Rural population breakdown for each continent
                case 13: {
                    System.out.println("\nExecuting: Urban vs. Rural population breakdown by continent...\n");

                    List<PopulationBreakdown> breakdownList = cityReports.getUrbanRuralByContinent(db.getConnection());

                    if (breakdownList.isEmpty()) {
                        System.out.println("No population breakdown data found.");
                    } else {
                        System.out.printf("%-20s %-15s %-18s %-18s %-12s %-12s%n",
                                "Continent", "Total Pop", "City Pop", "Rural Pop", "City %", "Rural %");
                        System.out.println("------------------------------------------------------------------------------------------------");

                        for (PopulationBreakdown pb : breakdownList) {
                            System.out.println(pb);
                        }
                    }
                    break;
                }

                case 0:
                    running = false;
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid option. Please choose a number between 0 and 12.");
            }
        }

        scanner.close();
        db.disconnect();
    }
}
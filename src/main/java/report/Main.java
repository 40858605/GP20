package report;

import com.napier.sem.App;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.connect();

        if (app.getConnection() == null) {
            System.out.println("Could not establish a database connection. Exiting...");
            return;
        }

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
                    System.out.println("\nExecuting: All countries in world by population...");
                    // CountryReport.printAllCountriesByPopulation(app.getConnection());
                    break;

                case 2:
                    System.out.print("Enter Continent (e.g., Europe, Asia): ");
                    String continent1 = scanner.nextLine();
                    System.out.println("Executing: All countries in " + continent1 + "...");
                    // CountryReport.printCountriesInContinent(app.getConnection(), continent1);
                    break;

                case 3:
                    System.out.print("Enter N (number of top countries): ");
                    int topN1 = Integer.parseInt(scanner.nextLine().trim());
                    // CountryReport.printTopNCountriesInWorld(app.getConnection(), topN1);
                    break;

                case 4:
                    System.out.print("Enter Continent: ");
                    String continent2 = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int topN2 = Integer.parseInt(scanner.nextLine().trim());
                    // CountryReport.printTopNCountriesInContinent(app.getConnection(), continent2, topN2);
                    break;

                case 5:
                    System.out.print("Enter Country name: ");
                    String countryName1 = scanner.nextLine();
                    // CityReport.printCitiesInCountry(app.getConnection(), countryName1);
                    break;

                case 6:
                    System.out.print("Enter District name: ");
                    String districtName1 = scanner.nextLine();
                    // CityReport.printCitiesInDistrict(app.getConnection(), districtName1);
                    break;

                case 7:
                    System.out.print("Enter Country name: ");
                    String countryName2 = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int topN3 = Integer.parseInt(scanner.nextLine().trim());
                    // CityReport.printTopNCitiesInCountry(app.getConnection(), countryName2, topN3);
                    break;

                case 8:
                    System.out.print("Enter District name: ");
                    String districtName2 = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int topN4 = Integer.parseInt(scanner.nextLine().trim());
                    // CityReport.printTopNCitiesInDistrict(app.getConnection(), districtName2, topN4);
                    break;

                case 9:
                    System.out.print("Enter N: ");
                    int topN5 = Integer.parseInt(scanner.nextLine().trim());
                    // CapitalCityReport.printTopNCapitalCitiesInWorld(app.getConnection(), topN5);
                    break;

                case 10:
                    System.out.print("Enter Continent: ");
                    String continent3 = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int topN6 = Integer.parseInt(scanner.nextLine().trim());
                    // CapitalCityReport.printTopNCapitalCitiesInContinent(app.getConnection(), continent3, topN6);
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid option. Choose 0-10.");
            }
        }

        scanner.close();
        app.disconnect();
    }
}

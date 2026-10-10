package report;

import com.napier.sem.App;
import java.sql.SQLException;
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
            System.out.println("\n===== SPRINT 1 REPORTS MENU =====");
            System.out.println("7. Top N populated cities in a country");
            System.out.println("8. Top N populated cities in a district");
            System.out.println("0. Exit");
            System.out.print("Select an option (0, 7, or 8): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "7":
                        System.out.print("Enter Country name: ");
                        String countryName = scanner.nextLine();

                        System.out.print("Enter N: ");
                        int countryN = Integer.parseInt(scanner.nextLine().trim());

                        TopNCitiesReport.printTopNCitiesInCountry(
                                app.getConnection(), countryName, countryN);
                        break;

                    case "8":
                        System.out.print("Enter District name: ");
                        String districtName = scanner.nextLine();

                        System.out.print("Enter N: ");
                        int districtN = Integer.parseInt(scanner.nextLine().trim());

                        TopNCitiesReport.printTopNCitiesInDistrict(
                                app.getConnection(), districtName, districtN);
                        break;

                    case "0":
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please select 7, 8, or 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number for N.");
            } catch (SQLException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        app.disconnect();
        System.out.println("Application closed.");
    }
}

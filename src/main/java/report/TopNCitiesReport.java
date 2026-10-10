
package report;

import com.napier.sem.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopNCitiesReport {

    // Report #20: Top N populated cities in a country
    public static void printTopNCitiesInCountry(
            Connection conn, String countryName, int n)
            throws SQLException {

        validateInput(conn, countryName, n);

        String sql = """
                SELECT ci.Name,
                       co.Name AS Country,
                       ci.District,
                       ci.Population
                FROM city ci
                JOIN country co
                    ON ci.CountryCode = co.Code
                WHERE co.Name = ?
                ORDER BY ci.Population DESC, ci.Name ASC
                LIMIT ?
                """;

        List<City> cities = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, countryName);
            stmt.setInt(2, n);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(new City(
                            rs.getString("Name"),
                            rs.getString("Country"),
                            rs.getString("District"),
                            rs.getLong("Population")
                    ));
                }
            }
        }

        System.out.println(
                "\nTop " + n + " populated cities in " + countryName);
        printResults(cities);
    }

    // Report #21: Top N populated cities in a district
    public static void printTopNCitiesInDistrict(
            Connection conn, String districtName, int n)
            throws SQLException {

        validateInput(conn, districtName, n);

        String sql = """
                SELECT ci.Name,
                       co.Name AS Country,
                       ci.District,
                       ci.Population
                FROM city ci
                JOIN country co
                    ON ci.CountryCode = co.Code
                WHERE ci.District = ?
                ORDER BY ci.Population DESC, ci.Name ASC
                LIMIT ?
                """;

        List<City> cities = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, districtName);
            stmt.setInt(2, n);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(new City(
                            rs.getString("Name"),
                            rs.getString("Country"),
                            rs.getString("District"),
                            rs.getLong("Population")
                    ));
                }
            }
        }

        System.out.println(
                "\nTop " + n + " populated cities in district "
                        + districtName);
        printResults(cities);
    }

    private static void validateInput(
            Connection conn, String location, int n) {

        if (conn == null) {
            throw new IllegalArgumentException(
                    "Database connection is not available.");
        }

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException(
                    "Country or district name cannot be empty.");
        }

        if (n <= 0) {
            throw new IllegalArgumentException(
                    "N must be greater than zero.");
        }
    }

    private static void printResults(List<City> cities) {
        if (cities.isEmpty()) {
            System.out.println("No matching cities found.");
            return;
        }

        System.out.printf(
                "%-35s %-35s %-25s %-12s%n",
                "City", "Country", "District", "Population");

        System.out.println(
                "----------------------------------------------------------------------------------------------");

        for (City city : cities) {
            System.out.println(city);
        }
    }
}

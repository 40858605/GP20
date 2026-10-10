package report;

import com.napier.sem.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityReports {

    // Task #15: All cities in a country by population
    public List<City> getCitiesInCountryByPopulation(
            Connection con, String countryName) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Name = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, countryName);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error running Task #15: " + e.getMessage());
        }

        return cities;
    }

    // Task #16: All cities in a district by population
    public List<City> getCitiesInDistrictByPopulation(
            Connection con, String districtName) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE ci.District = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, districtName);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error running Task #16: " + e.getMessage());
        }

        return cities;
    }

    // Report #20: Top N populated cities in a country
    public static void printTopNCitiesInCountry(
            Connection conn, String countryName, int n)
            throws SQLException {

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

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
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

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        }

        System.out.println(
                "\nTop " + n + " populated cities in district "
                        + districtName);

        printResults(cities);
    }

    // Convert a database row into a City object
    private static City mapCity(ResultSet rset) throws SQLException {
        return new City(
                rset.getString("Name"),
                rset.getString("Country"),
                rset.getString("District"),
                rset.getLong("Population")
        );
    }

    // Print a list of cities
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

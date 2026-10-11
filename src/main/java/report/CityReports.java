
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

    // Report #20: Top N populated cities in the world
    public static void printTopNCitiesInWorld(
            Connection conn, int n) throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, null,
                "Top " + n + " populated cities in the world");
    }

    // Report #21: Top N populated cities in a continent
    public static void printTopNCitiesInContinent(
            Connection conn, String continent, int n)
            throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, continent,
                "Top " + n + " populated cities in " + continent);
    }

    // Report #22: Top N populated cities in a region
    public static void printTopNCitiesInRegion(
            Connection conn, String region, int n)
            throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Region = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, region,
                "Top " + n + " populated cities in region " + region);
    }

    // Report #23: Top N populated cities in a country
    public static void printTopNCitiesInCountry(
            Connection conn, String countryName, int n)
            throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Name = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, countryName,
                "Top " + n + " populated cities in " + countryName);
    }

    // Report #24: Top N populated cities in a district
    public static void printTopNCitiesInDistrict(
            Connection conn, String districtName, int n)
            throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE ci.District = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, districtName,
                "Top " + n + " populated cities in district " + districtName);
    }

    // Report #25: Top N populated capital cities in the world
    public static void printTopNCapitalCitiesInWorld(
            Connection conn, int n) throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Capital = ci.ID " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, null,
                "Top " + n + " populated capital cities in the world");
    }

    // Report #26: Top N populated capital cities in a continent
    public static void printTopNCapitalCitiesInContinent(
            Connection conn, String continent, int n)
            throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Capital = ci.ID " +
                        "AND co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, continent,
                "Top " + n + " populated capital cities in " + continent);
    }

    // Report #27: Top N populated capital cities in a region
    public static void printTopNCapitalCitiesInRegion(
            Connection conn, String region, int n)
            throws SQLException {

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Capital = ci.ID " +
                        "AND co.Region = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        printQueryResults(
                conn, sql, n, region,
                "Top " + n + " populated capital cities in region " + region);
    }

    // Shared query method for all Top N reports
    private static void printQueryResults(
            Connection conn,
            String sql,
            int n,
            String filterValue,
            String heading) throws SQLException {

        if (n <= 0) {
            System.out.println("N must be greater than zero.");
            return;
        }

        List<City> cities = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (filterValue == null) {
                stmt.setInt(1, n);
            } else {
                stmt.setString(1, filterValue);
                stmt.setInt(2, n);
            }

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        }

        System.out.println("\n" + heading);
        printResults(cities);
    }

    // Convert database row into City object
    private static City mapCity(ResultSet rset) throws SQLException {
        return new City(
                rset.getString("Name"),
                rset.getString("Country"),
                rset.getString("District"),
                rset.getLong("Population")
        );
    }

    // Display query results
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

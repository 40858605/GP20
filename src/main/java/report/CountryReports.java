package report;

import com.napier.sem.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CountryReports {

    // Report 1: All countries in the world by population
    public List<Country> getAllCountriesByPopulation(Connection con) {
        List<Country> countries = new ArrayList<>();

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "ORDER BY c.Population DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(sql)) {
            while (rset.next()) {
                countries.add(mapCountry(rset));
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 1: " + e.getMessage());
        }

        return countries;
    }

    // Report 2: All countries in a continent by population
    public List<Country> getCountriesInContinentByPopulation(
            Connection con, String continent) {
        List<Country> countries = new ArrayList<>();

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Continent = ? ORDER BY c.Population DESC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    countries.add(mapCountry(rset));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 2: " + e.getMessage());
        }

        return countries;
    }

    // Report 3: Top N populated countries in the world
    public List<Country> getTopNCountriesByPopulation(Connection con, int n) {
        List<Country> countries = new ArrayList<>();

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "ORDER BY c.Population DESC LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, n);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    countries.add(mapCountry(rset));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 3: " + e.getMessage());
        }

        return countries;
    }

    // Report 4: Top N populated countries in a continent
    public List<Country> getTopNCountriesInContinentByPopulation(
            Connection con, String continent, int n) {
        List<Country> countries = new ArrayList<>();

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Continent = ? ORDER BY c.Population DESC LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);
            stmt.setInt(2, n);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    countries.add(mapCountry(rset));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 4: " + e.getMessage());
        }

        return countries;
    }

    // Report 28: Total population of the world
    public long getTotalWorldPopulation(Connection con) throws SQLException {
        String sql =
                "SELECT COALESCE(SUM(Population), 0) AS TotalPopulation FROM country";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rset = stmt.executeQuery()) {
            if (rset.next()) {
                return rset.getLong("TotalPopulation");
            }
        }

        return 0L;
    }

    // Report 29: Total population of a specific continent
    public long getTotalContinentPopulation(
            Connection con, String continent) throws SQLException {
        String sql =
                "SELECT COALESCE(SUM(Population), 0) AS TotalPopulation " +
                        "FROM country WHERE Continent = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);

            try (ResultSet rset = stmt.executeQuery()) {
                if (rset.next()) {
                    return rset.getLong("TotalPopulation");
                }
            }
        }

        return 0L;
    }

    // Report 30: Major language speakers and percentage of world population
    // Uses countrylanguage.Percentage to estimate speakers in each country.
    public static void printMajorLanguageStatistics(Connection con)
            throws SQLException {

        String sql =
                "SELECT cl.Language, " +
                        "CAST(SUM(c.Population * cl.Percentage / 100.0) AS UNSIGNED) " +
                        "AS TotalSpeakers, " +
                        "100.0 * SUM(c.Population * cl.Percentage / 100.0) / " +
                        "(SELECT SUM(Population) FROM country) AS WorldPercentage " +
                        "FROM countrylanguage cl " +
                        "JOIN country c ON c.Code = cl.CountryCode " +
                        "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                        "GROUP BY cl.Language " +
                        "ORDER BY TotalSpeakers DESC";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rset = stmt.executeQuery()) {

            System.out.println("\n===== MAJOR LANGUAGE SPEAKERS =====");
            System.out.printf("%-15s %-20s %-20s%n",
                    "Language", "Total Speakers", "World Population %");
            System.out.println("----------------------------------------------------------");

            boolean found = false;
            while (rset.next()) {
                found = true;
                System.out.printf("%-15s %-20s %.2f%%%n",
                        rset.getString("Language"),
                        String.format("%,d", rset.getLong("TotalSpeakers")),
                        rset.getDouble("WorldPercentage"));
            }

            if (!found) {
                System.out.println("No language statistics found.");
            }
        }
    }

    // Convert a database row into a Country object.
    private Country mapCountry(ResultSet rset) throws SQLException {
        return new Country(
                rset.getString("Code"),
                rset.getString("Name"),
                rset.getString("Continent"),
                rset.getString("Region"),
                rset.getLong("Population"),
                rset.getString("Capital")
        );
    }
}


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

    // 1. All countries in the world by population
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

    // 2. All countries in a continent by population
    public List<Country> getCountriesInContinentByPopulation(
            Connection con, String continent) {

        List<Country> countries = new ArrayList<>();

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Continent = ? " +
                "ORDER BY c.Population DESC";

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

    // 3. Top N populated countries in the world
    public List<Country> getTopNCountriesByPopulation(
            Connection con, int n) {

        List<Country> countries = new ArrayList<>();

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "ORDER BY c.Population DESC " +
                "LIMIT ?";

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

    // 4. Top N populated countries in a continent
    public List<Country> getTopNCountriesInContinentByPopulation(
            Connection con, String continent, int n) {

        List<Country> countries = new ArrayList<>();

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Continent = ? " +
                "ORDER BY c.Population DESC " +
                "LIMIT ?";

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

    // Convert a database row into a Country object
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

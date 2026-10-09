package report;

import com.napier.sem.Country;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryReports {

    // 1. All countries in the world by population (largest to smallest) [Sprint 1]
    public List<Country> getAllCountriesByPopulation(Connection con) {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "ORDER BY c.Population DESC";

        try (Statement stmt = con.createStatement(); ResultSet rset = stmt.executeQuery(sql)) {
            while (rset.next()) {
                countries.add(new Country(
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getLong("Population"),
                        rset.getString("Capital")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 1: " + e.getMessage());
        }
        return countries;
    }

    // 2. All countries in a continent by population (largest to smallest) [Sprint 1]
    public List<Country> getCountriesInContinentByPopulation(Connection con, String continent) {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Continent = ? ORDER BY c.Population DESC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    countries.add(new Country(
                            rset.getString("Code"),
                            rset.getString("Name"),
                            rset.getString("Continent"),
                            rset.getString("Region"),
                            rset.getLong("Population"),
                            rset.getString("Capital")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 2: " + e.getMessage());
        }
        return countries;
    }
}

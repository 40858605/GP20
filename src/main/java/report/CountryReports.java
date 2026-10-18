
package report;

import com.napier.sem.Country;
import com.napier.sem.PopulationBreakdown;

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

    // Issue#5 Generate a report of all countries in a region by population (Sprint 2)
    public List<Country> getCountriesInRegionByPopulation(Connection con, String region) {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT Code, Name, Continent, Region, Population, Capital FROM country WHERE Region = ? ORDER BY Population DESC";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, region);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    countries.add(new Country(
                        rset.getString("Code"),
                        rset.getString("Name"),
                        rset.getString("Continent"),
                        rset.getString("Region"),
                        rset.getLong("Population"),
                        rset.getInt("Capital")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 13: " + e.getMessage());
        }
        return countries;
    }
    
    // Issue#7 Urban vs. Rural population breakdown for each region (sprint 3)
    public List<PopulationBreakdown> getUrbanRuralByRegion(Connection con) {
        List<PopulationBreakdown> report = new ArrayList<>();
        String sql = "SELECT c.Region AS Name, " +
                "SUM(c.Population) AS TotalPop, " +
                "SUM(COALESCE(city_pop.TotalCityPop, 0)) AS CityPop, " +
                "(SUM(c.Population) - SUM(COALESCE(city_pop.TotalCityPop, 0))) AS RuralPop " +
                "FROM country c " +
                "LEFT JOIN (SELECT CountryCode, SUM(Population) AS TotalCityPop FROM city GROUP BY CountryCode) city_pop " +
                "ON c.Code = city_pop.CountryCode " +
                "GROUP BY c.Region " +
                "ORDER BY TotalPop DESC";

        try (Statement stmt = con.createStatement(); ResultSet rset = stmt.executeQuery(sql)) {
            while (rset.next()) {
                report.add(new PopulationBreakdown(
                        rset.getString("Name"),
                        rset.getLong("TotalPop"),
                        rset.getLong("CityPop"),
                        rset.getLong("RuralPop")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 5: " + e.getMessage());
        }
        return report;
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

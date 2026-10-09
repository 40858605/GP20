package report;

import com.napier.sem.Country;
import com.napier.sem.PopulationBreakdown;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PopulationReports {

    // 1. All countries in the world by population (largest to smallest)
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

    // 2. All countries in a continent by population (largest to smallest)
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

    // 3. All countries in a region by population (largest to smallest)
    public List<Country> getCountriesInRegionByPopulation(Connection con, String region) {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Region = ? ORDER BY c.Population DESC";

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
                            rset.getString("Capital")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 3: " + e.getMessage());
        }
        return countries;
    }

    // 4. Urban vs. Rural population breakdown for each continent
    public List<PopulationBreakdown> getUrbanRuralByContinent(Connection con) {
        List<PopulationBreakdown> report = new ArrayList<>();
        String sql = "SELECT c.Continent AS Name, " +
                "SUM(c.Population) AS TotalPop, " +
                "SUM(COALESCE(city_pop.TotalCityPop, 0)) AS CityPop, " +
                "(SUM(c.Population) - SUM(COALESCE(city_pop.TotalCityPop, 0))) AS RuralPop " +
                "FROM country c " +
                "LEFT JOIN (SELECT CountryCode, SUM(Population) AS TotalCityPop FROM city GROUP BY CountryCode) city_pop " +
                "ON c.Code = city_pop.CountryCode " +
                "GROUP BY c.Continent " +
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
            System.out.println("Error running Report 4: " + e.getMessage());
        }
        return report;
    }

    // 5. Urban vs. Rural population breakdown for each region
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

    // 6. Urban vs. Rural population breakdown for each country
    public List<PopulationBreakdown> getUrbanRuralByCountry(Connection con) {
        List<PopulationBreakdown> report = new ArrayList<>();
        String sql = "SELECT c.Name AS Name, " +
                "c.Population AS TotalPop, " +
                "COALESCE(city_pop.TotalCityPop, 0) AS CityPop, " +
                "(c.Population - COALESCE(city_pop.TotalCityPop, 0)) AS RuralPop " +
                "FROM country c " +
                "LEFT JOIN (SELECT CountryCode, SUM(Population) AS TotalCityPop FROM city GROUP BY CountryCode) city_pop " +
                "ON c.Code = city_pop.CountryCode " +
                "ORDER BY c.Population DESC";

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
            System.out.println("Error running Report 6: " + e.getMessage());
        }
        return report;
    }

    // 7. Access the total population of a specific region
    public long getTotalPopulationOfRegion(Connection con, String region) {
        String sql = "SELECT SUM(Population) AS TotalPopulation FROM country WHERE Region = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, region);
            try (ResultSet rset = stmt.executeQuery()) {
                if (rset.next()) {
                    return rset.getLong("TotalPopulation");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Report 7: " + e.getMessage());
        }
        return 0;
    }
}
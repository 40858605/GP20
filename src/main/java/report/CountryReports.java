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

    // Issue #3: All countries in the world by population (phone-myint-myat-307)
    public List<Country> getAllCountriesByPopulation(Connection con) {
        List<Country> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return countries;
        }

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

    // Issue #4: All countries in a continent by population (phone-myint-myat-307)
    public List<Country> getCountriesInContinentByPopulation(
            Connection con, String continent) {

        List<Country> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return countries;
        }

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

    // Issue #5: All countries in a region by population (largest to smallest) (phone-myint-myat-307)
    public List<Country> getCountriesInRegionByPopulation(Connection con, String region) {
        List<Country> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return countries;
        }

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Region = ? ORDER BY c.Population DESC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, region);
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

    // Issue #6: Urban vs. Rural population breakdown for each continent (sprint 2) (phone-myint-myat-307)
    public List<PopulationBreakdown> getUrbanRuralByContinent(Connection con) {
        List<PopulationBreakdown> report = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return report;
        }

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

    // Issue #7: Urban vs. Rural population breakdown for each region (sprint 3) (phone-myint-myat-307)
    public List<PopulationBreakdown> getUrbanRuralByRegion(Connection con) {
        List<PopulationBreakdown> report = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return report;
        }

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

    // Issue #8: Urban vs. Rural population breakdown for each country (sprint 3) (phone-myint-myat-307)
    public List<PopulationBreakdown> getUrbanRuralByCountry(Connection con) {
        List<PopulationBreakdown> report = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return report;
        }

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

    // Issue #9: Top N populated countries in the world (40858605)
    public List<Country> getTopNCountriesByPopulation(
            Connection con, int n) {

        List<Country> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return countries;
        }

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
            System.out.println("Error running Report 9: " + e.getMessage());
        }

        return countries;
    }

    // Issue #10: Top N populated countries in a continent (40858605)
    public List<Country> getTopNCountriesInContinentByPopulation(
            Connection con, String continent, int n) {

        List<Country> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return countries;
        }

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
            System.out.println("Error running Report 10: " + e.getMessage());
        }

        return countries;
    }

    // Issue #11: Top N populated countries in a region (40858605)
    public List<Country> getTopNCountriesInRegionByPopulation(
            Connection con, String region, int n) {

        List<Country> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Database connection is not available.");
            return countries;
        }

        String sql = "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                "c.Population, ci.Name AS Capital " +
                "FROM country c LEFT JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Region = ? " +
                "ORDER BY c.Population DESC " +
                "LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, region);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    countries.add(mapCountry(rset));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error running Report 11: " + e.getMessage());
        }

        return countries;
    }

    // Issue #28: Access the total world population count (winhponeemyat202104-del)
    public long getTotalWorldPopulation(Connection con) {
        if (con == null) {
            System.out.println("Database connection is not available.");
            return 0;
        }

        String sql = "SELECT SUM(Population) AS TotalPopulation FROM country";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(sql)) {
            if (rset.next()) {
                return rset.getLong("TotalPopulation");
            }
        } catch (SQLException e) {
            System.out.println("Error running Task #28: " + e.getMessage());
        }
        return 0;
    }

    // Issue #29: Access the total population of a specific continent (winhponeemyat202104-del)
    public long getTotalPopulationOfContinent(Connection con, String continent) {
        if (con == null) {
            System.out.println("Database connection is not available.");
            return 0;
        }

        String sql = "SELECT SUM(Population) AS TotalPopulation FROM country WHERE Continent = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);
            try (ResultSet rset = stmt.executeQuery()) {
                if (rset.next()) {
                    return rset.getLong("TotalPopulation");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Task #29: " + e.getMessage());
        }
        return 0;
    }

    // Issue #30: Access the total population of a specific region (sprint 3) (phone-myint-myat-307)
    public long getTotalPopulationOfRegion(Connection con, String region) {
        if (con == null) {
            System.out.println("Database connection is not available.");
            return 0;
        }

        String sql = "SELECT SUM(Population) AS TotalPopulation FROM country WHERE Region = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, region);
            try (ResultSet rset = stmt.executeQuery()) {
                if (rset.next()) {
                    return rset.getLong("TotalPopulation");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Task #30: " + e.getMessage());
        }
        return 0;
    }

    // Issue #31: Access the total population of a specific country (40858605)
    public long getTotalPopulationOfCountry(Connection con, String countryName) {
        if (con == null) {
            System.out.println("Database connection is not available.");
            return 0;
        }

        String sql = "SELECT Population FROM country WHERE Name = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, countryName);

            try (ResultSet rset = stmt.executeQuery()) {
                if (rset.next()) {
                    return rset.getLong("Population");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running Task #31: " + e.getMessage());
        }

        return 0;
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
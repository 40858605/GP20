package report;

import com.napier.sem.City;
import com.napier.sem.PopulationBreakdown;

import java.sql.*;
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

    // Task #17: Top N cities in the world by population
    public List<City> getTopNCitiesByPopulation(
            Connection con, int n) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error running Task #17: " + e.getMessage());
        }

        return cities;
    }

    // Task #18: Top N cities in a continent by population
    public List<City> getTopNCitiesInContinentByPopulation(
            Connection con, String continent, int n) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error running Task #18: " + e.getMessage());
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

    // Report #25: Top N populated capital cities in the world
    public static void printTopNCapitalCitiesInWorld(
            Connection conn, int n) throws SQLException {

        String sql = """
                SELECT ci.Name,
                       co.Name AS Country,
                       ci.District,
                       ci.Population
                FROM city ci
                JOIN country co
                    ON ci.CountryCode = co.Code
                WHERE co.Capital = ci.ID
                ORDER BY ci.Population DESC, ci.Name ASC
                LIMIT ?
                """;

        List<City> cities = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        }

        System.out.println(
                "\nTop " + n + " populated capital cities in the world");

        printResults(cities);
    }

    // Report #26: Top N populated capital cities in a continent
    public static void printTopNCapitalCitiesInContinent(
            Connection conn, String continent, int n)
            throws SQLException {

        String sql = """
                SELECT ci.Name,
                       co.Name AS Country,
                       ci.District,
                       ci.Population
                FROM city ci
                JOIN country co
                    ON ci.CountryCode = co.Code
                WHERE co.Capital = ci.ID
                  AND co.Continent = ?
                ORDER BY ci.Population DESC, ci.Name ASC
                LIMIT ?
                """;

        List<City> cities = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, continent);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        }

        System.out.println(
                "\nTop " + n + " populated capital cities in " + continent);

        printResults(cities);
    }

    // Issue #4 Urban vs. Rural population breakdown for each continent
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
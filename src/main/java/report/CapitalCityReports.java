package report;

import com.napier.sem.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CapitalCityReports {

    // Report #22: All capital cities in the world by population
    public List<City> getCapitalCitiesInWorldByPopulation(
            Connection con) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON co.Capital = ci.ID " +
                        "ORDER BY ci.Population DESC, ci.Name ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rset = stmt.executeQuery()) {

            while (rset.next()) {
                cities.add(mapCity(rset));
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error running Report #22: " + e.getMessage());
        }

        return cities;
    }

    // Report #23: All capital cities in a continent by population
    public List<City> getCapitalCitiesInContinentByPopulation(
            Connection con, String continent) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON co.Capital = ci.ID " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error running Report #23: " + e.getMessage());
        }

        return cities;
    }

    // Convert a database row into a City object
    private City mapCity(ResultSet rset) throws SQLException {
        return new City(
                rset.getString("Name"),
                rset.getString("Country"),
                rset.getString("District"),
                rset.getLong("Population")
        );
    }

    // Display report results
    public void printResults(List<City> cities) {
        if (cities.isEmpty()) {
            System.out.println("No matching capital cities found.");
            return;
        }

        System.out.printf(
                "%-35s %-35s %-25s %-12s%n",
                "Capital City", "Country", "District", "Population");

        System.out.println(
                "----------------------------------------------------------------------------------------------");

        for (City city : cities) {
            System.out.println(city);
        }
    }
}

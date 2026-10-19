
package report;

import com.napier.sem.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopNCitiesReport {

    // issue:28 top N populated cities in a country
    public List<City> getTopNCitiesWorldwide(
            Connection connection, int n) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving top cities worldwide: "
                            + e.getMessage());
        }

        return cities;
    }

    // issue:29 top N populated cities in a district
    public List<City> getTopNCitiesInContinent(
            Connection connection, String continent, int n) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, continent);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "Error retrieving top cities in continent: "
                            + e.getMessage());
        }

        return cities;
    }

    // Report #20: Top N populated cities in a country
    public List<City> getTopNCitiesInCountry(
            Connection connection, String countryName, int n) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Name = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, countryName);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "Error running Report #20: " + e.getMessage());
        }

        return cities;
    }

    // Report #21: Top N populated cities in a district
    public List<City> getTopNCitiesInDistrict(
            Connection connection, String districtName, int n) {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE ci.District = ? " +
                        "ORDER BY ci.Population DESC, ci.Name ASC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, districtName);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    cities.add(mapCity(rset));
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "Error running Report #21: " + e.getMessage());
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

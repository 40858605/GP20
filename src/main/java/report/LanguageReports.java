package report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LanguageReports {

    // Issue #34: Report on major global languages (Chinese, English, Hindi, Spanish, Arabic) (winhponeemyat202104-del)
    public void printMajorGlobalLanguages(Connection con) {
        if (con == null) {
            System.out.println("Database connection is not available.");
            return;
        }

        String sql = """
                SELECT cl.Language,
                       ROUND(SUM(c.Population * (cl.Percentage / 100.0))) AS Speakers,
                       (SUM(c.Population * (cl.Percentage / 100.0)) /
                           (SELECT SUM(Population) FROM country)) * 100.0 AS WorldPercentage
                FROM countrylanguage cl
                JOIN country c ON cl.CountryCode = c.Code
                WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic')
                GROUP BY cl.Language
                ORDER BY Speakers DESC
                """;

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(sql)) {

            System.out.printf("%-15s %-20s %-20s%n",
                    "Language", "Speakers", "% of World Population");
            System.out.println("---------------------------------------------------------");

            while (rset.next()) {
                String language = rset.getString("Language");
                long speakers = rset.getLong("Speakers");
                double worldPercentage = rset.getDouble("WorldPercentage");

                System.out.printf("%-15s %,-20d %.2f%%%n",
                        language, speakers, worldPercentage);
            }

        } catch (SQLException e) {
            System.out.println("Error running Task #34: " + e.getMessage());
        }
    }
}
package report;

import com.napier.sem.Language;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses the standard MySQL world schema.
 * Speaker counts are estimates from countrylanguage.Percentage, not exact
 * unique-speaker counts. Multilingual people may be counted for more than one language.
 */
public class LanguageReports {
    private static final String[] LANGUAGES = {
            "Chinese", "English", "Hindi", "Spanish", "Arabic"
    };

    public List<Language> getMajorGlobalLanguages(Connection con) throws SQLException {
        List<Language> results = new ArrayList<>();

        String sql = """
            SELECT cl.Language AS LanguageName,
                   SUM(c.Population * cl.Percentage / 100.0) AS EstimatedSpeakers,
                   SUM(c.Population * cl.Percentage / 100.0) * 100.0 /
                       NULLIF((SELECT SUM(Population) FROM country), 0) AS WorldPercentage
            FROM countrylanguage cl
            JOIN country c ON c.Code = cl.CountryCode
            WHERE cl.Language IN (?, ?, ?, ?, ?)
            GROUP BY cl.Language
            ORDER BY EstimatedSpeakers DESC
            """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            for (int i = 0; i < LANGUAGES.length; i++) {
                stmt.setString(i + 1, LANGUAGES[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Language(
                            rs.getString("LanguageName"),
                            Math.round(rs.getDouble("EstimatedSpeakers")),
                            rs.getDouble("WorldPercentage")
                    ));
                }
            }
        }
        return results;
    }

    public void printMajorGlobalLanguages(Connection con) {
        try {
            List<Language> languages = getMajorGlobalLanguages(con);
            System.out.println("\\n========== MAJOR GLOBAL LANGUAGES ==========");
            System.out.printf("%-15s %-22s %-22s%n",
                    "Language", "Estimated Speakers", "World Population %");
            System.out.println("---------------------------------------------------------------");

            if (languages.isEmpty()) {
                System.out.println("No matching language records found.");
                System.out.println("Check the language names and the countrylanguage table.");
                return;
            }
            for (Language language : languages) {
                System.out.println(language);
            }
            System.out.println("\\nSpeaker counts are estimates based on country-level language percentages.");
        } catch (SQLException e) {
            System.out.println("Error running language report: " + e.getMessage());
        }
    }
}

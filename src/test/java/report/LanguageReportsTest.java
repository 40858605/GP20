package report;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LanguageReports class.
 * This test suite verifies the behavior of the language report generation method
 * when handling a null database connection without requiring an active database connection.
 */
public class LanguageReportsTest {
    static LanguageReports languageReports;

    @BeforeAll
    static void init() {
        languageReports = new LanguageReports();
    }

    // Issue #34: Test printing major global languages with null connection (40858605)
    @Test
    void printMajorGlobalLanguagesTestNullConnection() {
        assertDoesNotThrow(() -> languageReports.printMajorGlobalLanguages(null));
    }
}
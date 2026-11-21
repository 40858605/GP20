package report;

/*
 * Integration tests for LanguageReports.
 * Verifies the major global languages report, including language names,
 * speaker statistics, world population percentages and descending speaker order.
 * Also tests null database connection handling and the expected console output.
 */

import com.napier.sem.App;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageReportsIntegrationTest {

    private static App app;
    private static LanguageReports reports;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("127.0.0.1:3307", 1000);
        reports = new LanguageReports();
    }

    @AfterAll
    static void cleanup() {
        if (app != null) {
            app.disconnect();
        }
    }

    // Report #34: Major global languages ordered by speakers descending
    @Test
    void testPrintMajorGlobalLanguages() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            reports.printMajorGlobalLanguages(app.con);
        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);

        assertTrue(consoleOutput.contains("Language"),
                "Output should display the Language column header.");
        assertTrue(consoleOutput.contains("Speakers"),
                "Output should display the Speakers column header.");
        assertTrue(consoleOutput.contains("% of World Population"),
                "Output should display the World Population percentage header.");

        assertTrue(consoleOutput.contains("Chinese"), "Report should include Chinese.");
        assertTrue(consoleOutput.contains("English"), "Report should include English.");
        assertTrue(consoleOutput.contains("Hindi"), "Report should include Hindi.");
        assertTrue(consoleOutput.contains("Spanish"), "Report should include Spanish.");
        assertTrue(consoleOutput.contains("Arabic"), "Report should include Arabic.");

        assertTrue(consoleOutput.indexOf("Chinese") < consoleOutput.indexOf("English"),
                "Languages should be ordered from most to least speakers (Chinese before English).");
    }

    // Edge case: Null connection handling
    @Test
    void testPrintMajorGlobalLanguagesWithNullConnection() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            reports.printMajorGlobalLanguages(null);
        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);
        assertTrue(consoleOutput.contains("Database connection is not available."),
                "Null connection should print unavailable message.");
    }
}
package com.napier.sem;

/*
 * AppIntegrationTest
 *
 * Purpose:
 * This class performs integration testing of the World Database System
 * by executing the actual App.main() method and interacting with the
 * application through simulated console input.
 *
 * Testing approach:
 * - Verifies database connection and disconnection behaviour.
 * - Tests all five main report categories and their menu options.
 * - Checks report results retrieved from the MySQL World database.
 * - Tests invalid inputs, empty values and non-existent records.
 * - Tests unexpected end-of-input (EOF) in menus and prompts.
 * - Verifies that the application exits gracefully when errors occur.
 *
 * Requirements:
 * A MySQL World database must be running at 127.0.0.1:3307.
 * The database should contain the expected World dataset.
 *
 * These are integration tests because they test the interaction between
 * the application, report classes, JDBC connection and database rather
 * than testing individual methods in isolation.
 */

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppIntegrationTest {

    private static final String DB_HOST = "127.0.0.1:3307";

    // Helper: run the application with simulated console input
    // and capture everything printed to the console.
    private String runApplication(String simulatedInput) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(
                    simulatedInput.getBytes(StandardCharsets.UTF_8)));

            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8));

            App.main(new String[]{DB_HOST, "0"});

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        return output.toString(StandardCharsets.UTF_8);
    }

    // IT09: Application handles database connection failure appropriately
    @Test
    void testDatabaseConnectionFailure() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8));

            App.main(new String[]{"localhost:9999", "0"});

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        String consoleOutput =
                output.toString(StandardCharsets.UTF_8);

        assertTrue(consoleOutput.contains("Failed to connect to database"),
                "Application should log failed database connection attempts.");

        assertTrue(consoleOutput.contains(
                        "Could not establish a database connection. Exiting..."),
                "Application should exit gracefully when database connection fails.");
    }

    // IT10: Selecting a report from the menu displays the correct results
    @Test
    void testCityReportMenuWorkflow() {

        String simulatedInput =
                "2\n" +       // City Information
                        "4\n" +       // Cities by Country
                        "Myanmar\n" + // Country name
                        "0\n" +       // Return to main menu
                        "0\n";        // Exit application

        String consoleOutput = runApplication(simulatedInput);

        assertTrue(consoleOutput.contains("CITY INFORMATION"),
                "City Information menu should be displayed.");

        assertTrue(consoleOutput.contains("CITY REPORT RESULTS"),
                "City report results should be displayed.");

        assertTrue(consoleOutput.contains("Yangon")
                        || consoleOutput.contains("Mandalay"),
                "Report should display Myanmar cities from the database.");

        assertTrue(consoleOutput.contains(
                        "Thank you for using World Database System!"),
                "Application should exit normally.");
    }

    // Additional Test 1: All Country Information menu options
    @Test
    void testAllCountryMenuOptions() {

        String simulatedInput =
                "1\n" +
                        "1\n" +
                        "2\nAsia\n" +
                        "2\nAtlantis\n" +
                        "3\nSoutheast Asia\n" +
                        "4\n5\n" +
                        "5\nAsia\n5\n" +
                        "6\nSoutheast Asia\n5\n" +
                        "0\n" +
                        "0\n";

        String output = runApplication(simulatedInput);

        assertTrue(output.contains("COUNTRY INFORMATION"),
                "Country menu should be displayed.");

        assertTrue(output.contains("COUNTRY REPORT RESULTS"),
                "Country reports should display results.");

        assertTrue(output.contains("No matching countries found."),
                "Nonexistent continent should produce an empty-result message.");
    }

    // Additional Test 2: Remaining City Information menu options
    @Test
    void testRemainingCityMenuOptions() {

        String simulatedInput =
                "2\n" +
                        "1\n" +
                        "2\nAsia\n" +
                        "3\nSoutheast Asia\n" +
                        "5\nYangon\n" +
                        "6\n5\n" +
                        "7\nAsia\n5\n" +
                        "8\nSoutheast Asia\n5\n" +
                        "9\nMyanmar\n5\n" +
                        "10\nYangon\n5\n" +
                        "0\n" +
                        "0\n";

        String output = runApplication(simulatedInput);

        assertTrue(output.contains("CITY INFORMATION"),
                "City menu should be displayed.");

        assertTrue(output.contains("CITY REPORT RESULTS"),
                "City reports should display results.");

        assertTrue(output.contains("Top 5 populated cities in Myanmar"));
    }

    // Additional Test 3: All Capital City Information menu options
    @Test
    void testAllCapitalMenuOptions() {

        String simulatedInput =
                "3\n" +
                        "1\n" +
                        "2\nAsia\n" +
                        "3\nSoutheast Asia\n" +
                        "4\n5\n" +
                        "5\nAsia\n5\n" +
                        "6\nSoutheast Asia\n5\n" +
                        "0\n" +
                        "0\n";

        String output = runApplication(simulatedInput);

        assertTrue(output.contains("CAPITAL CITY INFORMATION"),
                "Capital City menu should be displayed.");

        assertTrue(output.contains("Capital City"),
                "Capital city report header should be displayed.");

        assertTrue(output.contains("Seoul") || output.contains("Jakarta"),
                "Capital city records should be displayed.");

        assertTrue(output.contains("Top 5 populated capital cities in the world"),
                "Top N capital cities report should be displayed.");
    }

    // Additional Test 4: All Population Statistics menu options
    @Test
    void testAllPopulationMenuOptions() {

        String simulatedInput =
                "4\n" +
                        "1\n" +
                        "2\n" +
                        "3\n" +
                        "4\n" +
                        "5\nAsia\n" +
                        "6\nSoutheast Asia\n" +
                        "7\nMyanmar\n" +
                        "8\nYangon\n" +
                        "9\nYangon\n" +
                        "9\nDefinitelyNotACity123\n" +
                        "0\n" +
                        "0\n";

        String output = runApplication(simulatedInput);

        assertTrue(output.contains("POPULATION STATISTICS"),
                "Population Statistics menu should be displayed.");

        assertTrue(output.contains("POPULATION BREAKDOWN RESULTS"),
                "Population breakdown results should be displayed.");

        assertTrue(output.contains("WORLD POPULATION"),
                "Worldwide population should be displayed.");

        assertTrue(output.contains("Total Population of Yangon"),
                "Existing city population should be displayed.");

        assertTrue(output.contains("City not found."),
                "Nonexistent city should trigger the City not found message.");
    }

    // Additional Test 5: Language Statistics menu
    @Test
    void testLanguageMenuOption() {

        String simulatedInput =
                "5\n" +
                        "1\n" +
                        "0\n" +
                        "0\n";

        String output = runApplication(simulatedInput);

        assertTrue(output.contains("LANGUAGE STATISTICS"),
                "Language Statistics menu should be displayed.");

        assertTrue(output.contains("Major Global Languages"),
                "Major Global Languages option should be displayed.");

        assertTrue(output.contains("% of World Population"),
                "Language report table header should be displayed.");

        assertTrue(output.contains("Chinese") && output.contains("English"),
                "Language report should display major global languages from the database.");
    }

    // Additional Test 6: Invalid input, empty input, no results and EOF
    @Test
    void testInvalidInputsAndEdgeCases() {

        String simulatedInput =
                "99\n" +
                        "abc\n" +

                        "1\n" +
                        "99\n" +
                        "2\n\n" +
                        "3\n\n" +
                        "4\n0\n" +
                        "4\n-1\n" +
                        "5\n\n5\n" +
                        "6\n\n5\n" +
                        "0\n" +

                        "2\n" +
                        "99\n" +
                        "2\n\n" +
                        "3\n\n" +
                        "4\nDefinitelyNotACountry123\n" +
                        "5\n\n" +
                        "6\n0\n" +
                        "6\n-1\n" +
                        "7\n\n5\n" +
                        "8\n\n5\n" +
                        "9\n\n5\n" +
                        "10\n\n5\n" +
                        "0\n" +

                        "3\n" +
                        "99\n" +
                        "2\n\n" +
                        "3\n\n" +
                        "4\n0\n" +
                        "4\n-1\n" +
                        "5\n\n5\n" +
                        "6\n\n5\n" +
                        "0\n" +

                        "4\n" +
                        "99\n" +
                        "5\n\n" +
                        "6\n\n" +
                        "7\n\n" +
                        "8\n\n" +
                        "9\n\n" +
                        "9\nDefinitelyNotACity123\n" +
                        "0\n" +

                        "5\n" +
                        "99\n" +
                        "0\n" +

                        "0\n";

        String output = runApplication(simulatedInput);

        assertTrue(output.contains("Invalid option."),
                "Invalid submenu selections should be handled.");

        assertTrue(output.contains("Invalid option. Please select 0-5."),
                "Invalid main menu selections should be handled.");

        assertTrue(output.contains("Continent cannot be empty."),
                "Empty continent input should be rejected.");

        assertTrue(output.contains("Region cannot be empty."),
                "Empty region input should be rejected.");

        assertTrue(output.contains("Please enter a number greater than zero."),
                "Zero and negative Top N values should be rejected.");

        assertTrue(output.contains("Please enter valid details."),
                "Invalid Top N details should be rejected.");

        assertTrue(output.contains("No matching cities found."),
                "A nonexistent country should produce no matching cities.");

        assertTrue(output.contains("City not found."),
                "A nonexistent city should produce the City not found message.");

        // Test unexpected end of input separately.
        String eofOutput = runApplication("");

        assertTrue(eofOutput.contains(
                        "No interactive input detected. Exiting..."),
                "Application should handle unexpected end of input.");
    }

    // Additional Test 7: EOF inside each submenu
    // Explanation:
    // Checks whether the application handles unexpected end-of-input
    // after entering each report category without crashing.
    @Test
    void testEOFInsideSubmenus() {

        String[] simulatedInputs = {
                "1\n", // Country menu
                "2\n", // City menu
                "3\n", // Capital menu
                "4\n", // Population menu
                "5\n"  // Language menu
        };

        for (String input : simulatedInputs) {

            String output = runApplication(input);

            assertTrue(output.contains(
                            "No interactive input detected. Exiting..."),
                    "Application should handle EOF inside every submenu.");
        }
    }

    // Additional Test 8: EOF while requesting additional information
    // Explanation:
    // Checks that the application returns safely when the user stops
    // providing input after selecting a report that requires more details.
    @Test
    void testEOFWhileRequestingDetails() {

        String[] simulatedInputs = {
                "1\n2\n", // Country: continent
                "1\n4\n", // Country: Top N
                "1\n5\n", // Country: continent and Top N
                "1\n6\n", // Country: region and Top N

                "2\n2\n", // City: continent
                "2\n4\n", // City: country
                "2\n6\n", // City: Top N
                "2\n9\n", // City: country and Top N

                "3\n2\n", // Capital: continent
                "3\n4\n", // Capital: Top N
                "3\n5\n", // Capital: continent and Top N

                "4\n5\n", // Population: continent
                "4\n9\n"  // Population: city
        };

        for (String input : simulatedInputs) {

            String output = runApplication(input);

            assertTrue(output.contains(
                            "No interactive input detected. Exiting..."),
                    "Application should handle EOF while requesting report details.");
        }
    }

    // Additional Test 9: Non-numeric Top N input
    // Explanation:
    // Checks that entering text instead of a positive integer does not
    // crash the application and that the invalid value is rejected.
    @Test
    void testNonNumericTopNInput() {

        String simulatedInput =
                "1\n" +
                        "4\n" +
                        "abc\n" +
                        "0\n" +
                        "0\n";

        String output = runApplication(simulatedInput);

        assertTrue(output.contains("Please enter a number greater than zero."),
                "Non-numeric Top N input should be rejected.");

        assertTrue(output.contains("Thank you for using World Database System!"),
                "Application should continue and exit normally after invalid Top N input.");
    }
}

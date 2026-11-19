
package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    // IT10: Selecting a report from the menu displays the correct results
    @Test
    void testCityReportMenuWorkflow() {

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        // Simulate user menu selections:
        // 2 = City Information
        // 4 = View Cities by Country
        // Myanmar = Country name
        // 0 = Return to main menu
        // 0 = Exit application
        String simulatedInput = "2\n4\nMyanmar\n0\n0\n";

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(
                    simulatedInput.getBytes(StandardCharsets.UTF_8)));

            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            // Run the application using the Docker database connection
            App.main(new String[]{"localhost:3307", "0"});

        } finally {
            // Restore the original console streams
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        String consoleOutput = output.toString(StandardCharsets.UTF_8);


        // Verify the correct menu and report were displayed
        assertTrue(consoleOutput.contains("CITY INFORMATION"),
                "City Information menu should be displayed.");

        assertTrue(consoleOutput.contains("CITY REPORT RESULTS"),
                "City report results should be displayed.");

        assertTrue(consoleOutput.contains("Thank you for using World Database System!"),
                "Application should exit normally.");
    }
}
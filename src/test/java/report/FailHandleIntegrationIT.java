package report;

/*
 * Integration tests for application failure handling.
 * Verifies that the application handles database connection failures
 * correctly when the MySQL database is unavailable.
 * Ensures that an unsuccessful connection attempt leaves the
 * database connection null.
 */

import com.napier.sem.App;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class FailHandleIntegrationIT {

    // IT09: Application handles database connection failure
    @Test
    void testDatabaseConnectionFailure() {

        App app = new App();

        // Attempt to connect using an unavailable database port
        app.connect("127.0.0.1:1", 0);

        // Verify that the connection was not established
        assertNull(app.con,
                "Application should not establish a connection when the database is unavailable.");
    }
}
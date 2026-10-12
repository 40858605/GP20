package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
    private Connection con = null;

    public Connection getConnection() {
        return con;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Determine which hosts/ports to try
        String envHost = System.getenv("DB_HOST");
        String[] hostsToTry;

        if (envHost != null && !envHost.isEmpty()) {
            hostsToTry = new String[]{ envHost };
        } else {
            hostsToTry = new String[]{ "localhost:3306", "localhost:3307", "db:3306" };
        }

        for (String dbHost : hostsToTry) {
            int retries = 6; // Quick retries per host
            for (int i = 0; i < retries; ++i) {
                System.out.println("Connecting to database (" + dbHost + ") Attempt " + (i + 1) + "...");
                try {
                    Thread.sleep(1500);
                    con = DriverManager.getConnection(
                            "jdbc:mysql://" + dbHost + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                            "root",
                            "example"
                    );
                    System.out.println("Successfully connected to 'world' database at " + dbHost + "!");
                    return; // Exit as soon as a connection succeeds
                } catch (SQLException sqle) {
                    System.out.println("Failed to connect to " + dbHost + " (Attempt " + (i + 1) + "): " + sqle.getMessage());
                } catch (InterruptedException ie) {
                    System.out.println("Thread interrupted.");
                }
            }
        }

        System.out.println("Could not establish a database connection on any port. Exiting...");
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected successfully.");
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
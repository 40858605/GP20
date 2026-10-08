package com.napier.sem;

import java.sql.*;

public class App {
    private Connection con = null;

    // Connect to the MySQL database //
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 30;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database (Attempt " + (i + 1) + ")...");
            try {
                Thread.sleep(2000);
                // Connect to MySQL server inside Docker container network 'db'
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );
                System.out.println("Successfully connected to 'world' database!");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect attempt " + (i + 1) + ": " + sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
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

    public static void main(String[] args) {
        App app = new App();
        app.connect();
        app.disconnect();
    }
}
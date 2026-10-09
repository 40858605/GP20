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

        String dbHost = System.getenv("DB_HOST");
        if (dbHost == null || dbHost.isEmpty()) {
            dbHost = "localhost:3307"; //change this back to 3306 if you're using it
        }

        int retries = 30;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database (" + dbHost + ") Attempt " + (i + 1) + "...");
            try {
                Thread.sleep(2000);
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + dbHost + "/world?allowPublicKeyRetrieval=true&useSSL=false",
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
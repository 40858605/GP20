
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

        String dbHost = System.getenv("DB_HOST");

        if (dbHost == null || dbHost.isEmpty()) {
            dbHost = "localhost:3307";
        }

        String url = "jdbc:mysql://" + dbHost
                + "/world?allowPublicKeyRetrieval=true&useSSL=false";

        int retries = 30;

        for (int i = 1; i <= retries; i++) {

            System.out.println(
                    "Connecting to database (" + dbHost
                            + ") Attempt " + i + "/" + retries
            );

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                con = DriverManager.getConnection(
                        url,
                        "root",
                        "example"
                );

                System.out.println(
                        "Successfully connected to 'world' database!"
                );

                return;

            } catch (ClassNotFoundException e) {

                System.out.println("Could not load SQL driver");
                return;

            } catch (SQLException e) {

                System.out.println(
                        "Failed to connect: " + e.getMessage()
                );

                if (i < retries) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.out.println("Database connection interrupted.");
                        return;
                    }
                }
            }
        }

        System.out.println("Database connection is not available.");
    }

    public void disconnect() {

        if (con != null) {
            try {
                con.close();
                con = null;
                System.out.println("Disconnected successfully.");

            } catch (SQLException e) {
                System.out.println(
                        "Error closing connection: " + e.getMessage()
                );
            }
        }
    }
}

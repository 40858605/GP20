package com.napier.sem;

import report.LanguageReports;

public class App {

    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();
        db.connect();

        try {
            if (db.getConnection() == null) {
                System.out.println(
                        "Could not establish a database connection. Exiting...");
                return;
            }

            LanguageReports languageReports = new LanguageReports();
            languageReports.printMajorGlobalLanguages(db.getConnection());
        } finally {
            db.disconnect();
        }
    }
}
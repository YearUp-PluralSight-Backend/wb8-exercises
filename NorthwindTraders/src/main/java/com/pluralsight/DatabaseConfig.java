package com.pluralsight;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    public static void main(String[] args) {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("dbconfig.properties")) {
            // Load properties from file
            properties.load(input);

            // Resolve environment variables
            String dbUser = System.getenv(properties.getProperty("db.user"));
            String dbPassword = System.getenv(properties.getProperty("db.password"));

            // Update the properties with resolved values
            properties.setProperty("db.user", dbUser);
            properties.setProperty("db.password", dbPassword);

            // Print or use the properties
            System.out.println("Database URL: " + properties.getProperty("db.url"));
            System.out.println("Database User: " + properties.getProperty("db.user"));
            // Avoid printing sensitive information like passwords in production
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
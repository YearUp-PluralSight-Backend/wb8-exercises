package com.pluralsight;

import com.pluralsight.util.Utility;

import java.sql.*;

public class DemoMySQLThree {

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/northwind";
    private static final String USER = System.getenv("MYSQL_ROOT_USER");
    private static final String PASSWORD = System.getenv("MYSQL_ROOT_PASSWORD");


    public static void main(String[] args) {

        homeScreen();
    }


    public static void homeScreen() {

        boolean exit = false;

        while (!exit) {
            printMenu();
            switch (Utility.getInt("Enter an option: ")) {
                case 1 -> {
                        System.out.println("Display all products");
                        displayAllProducts();
                    }
                case 2 -> {
                    System.out.println("Display all customers");
                    displayAllCustomers();
                }
                case 0 -> {
                    System.out.println("Exiting...");
                    exit = true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }


    public static void printMenu() {

        String menu  =
                """
                What do you want to do?
                    1) Display all products
                    2) Display all customers
                    0) Exit
                """;

        System.out.println(menu);

    }


    public static void displayAllProducts() {
        String query = "SELECT * FROM products ORDER BY ProductID";
        try (Connection connection = getConnection()) {
            assert connection != null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                if (resultSet == null) {
                    System.out.println("Result set is null");
                    return;
                }

                printHeaderProducts();
                while (resultSet.next()) {
                    int productID = resultSet.getInt("ProductID");
                    String productName = resultSet.getString("ProductName");
                    double unitPrice = resultSet.getDouble("UnitPrice");
                    double unitsInStock = resultSet.getDouble("UnitsInStock");

                    String info = String.format("%-5d %-35s %-8.2f %-8.2f", productID, productName, unitPrice, unitsInStock);
                    System.out.println(info);
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing ResultSet: " + e.getMessage());
                    }
                }
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing PreparedStatement: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void displayAllCustomers() {
        String query = "SELECT * FROM customers ORDER BY country";
        try (Connection connection = getConnection()) {
            assert connection != null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                if (resultSet == null) {
                    System.out.println("Result set is null");
                    return;
                }

                printHeaderCustomers();
                while (resultSet.next()) {
                    String contactName = resultSet.getString("ContactName");
                    String companyName = resultSet.getString("CompanyName");
                    String City = resultSet.getString("City");
                    String Country = resultSet.getString("Country");
                    String PhoneNumber = resultSet.getString("phone");


                    String info = String.format("%-20s %-35s %-20s %-8s", contactName, companyName, City, Country, PhoneNumber);
                    System.out.println(info);
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing ResultSet: " + e.getMessage());
                    }
                }
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing PreparedStatement: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printHeaderCustomers() {
        String header = String.format("%-20s %-35s %20s %-8s", "ContactName", "CompanyName", "City", "Country", "Phone Number");
        System.out.println(header);
        System.out.println("--".repeat(45));
    }


    public static void printHeaderProducts() {
        String header = String.format("%-5s %-35s %-8s %-8s", "ID", "Name", "Price", "Stock");
        System.out.println(header);
        System.out.println("--".repeat(45));
    }

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
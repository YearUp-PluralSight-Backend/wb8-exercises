package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class DemoMySQLFive {

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = System.getenv("MYSQL_ROOT_USER");
    private static final String PASSWORD = System.getenv("MYSQL_ROOT_PASSWORD");


    public static DataSource getDataSource(String database, String user, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setUrl(URL + database);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
    public static void main(String[] args) {

        String query = "SELECT * FROM products";
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet == null) {
                    System.out.println("Result set is null");
                    return;
                }

                printHeader();
                while (resultSet.next()) {
                    int productID = resultSet.getInt("ProductID");
                    String productName = resultSet.getString("ProductName");
                    double unitPrice = resultSet.getDouble("UnitPrice");
                    double unitsInStock = resultSet.getDouble("UnitsInStock");

                    String info = String.format("%-5d %-35s %-8.2f %-8.2f", productID, productName, unitPrice, unitsInStock);
                    System.out.println(info);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void printHeader() {
        String header = String.format("%-5s %-35s %-8s %-8s", "ID", "Name", "Price", "Stock");
        System.out.println(header);
        System.out.println("--".repeat(30));
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
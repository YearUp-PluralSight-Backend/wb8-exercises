package com.pluralsight;

import java.sql.*;

public class DemoMySQL {

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "northwind";
    private static final String USER = System.getenv("MYSQL_ROOT_USER");
    private static final String PASSWORD = System.getenv("MYSQL_ROOT_PASSWORD");

    public static void main(String[] args)  {

        Connection connection = getConnection(args[0], args[1]);

        if (connection == null) {
            System.out.println("Connection is null");
            return;
        }

        String query = "SELECT * FROM products";
        ResultSet resultSet = createPreparedStatement(connection, query);
        try {
            if (resultSet == null) {
                System.out.println("Result set is null");
                return;
            }
            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                System.out.println(productName);

            }
        } catch (SQLException e) {
            System.out.println("Error iterating over result set: " + e.getMessage());
        }


    }

    public static ResultSet createPreparedStatement(Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error creating prepared statement: " + e.getMessage());
        }

        return null;
    }


    public static Connection getConnection(String user, String password) {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error connecting to DB: " + e.getMessage());
        }

        return null;
    }

    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public static void closeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error closing statement: " + e.getMessage());
        }
    }

    public static void closeConnectionAndStatement(Connection connection, Statement statement) {
        closeStatement(statement);
        closeConnection(connection);
    }
}

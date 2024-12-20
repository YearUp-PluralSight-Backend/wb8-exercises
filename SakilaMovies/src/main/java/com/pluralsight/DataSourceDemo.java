package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

import static com.pluralsight.utils.Utility.getInput;

public class DataSourceDemo {

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
        String sql = "select * from actor where last_name = '" + getInput("Last name of your favorite actor: ") + "'";
        query(sql);
    }

    public static void query(String sql) {
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                printHeaderActors();
                int id = 0;
                while (resultSet.next()) {
                    id = resultSet.getInt("actor_id");
                    String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                    LocalDateTime lastUpdate = resultSet.getTimestamp("last_update").toLocalDateTime();
                    String output = String.format("%-5d %-15s %-8s", id, name, lastUpdate);
                    System.out.println(output);

                }
                String query =
                        """
                        select * from film where film_id in (
                            select film_id from film_actor
                            where actor_id = %d
                        )
                        """.formatted(id);
                System.out.println("\nFilms:");
                try (Statement statement2 = connection.createStatement();
                     ResultSet resultSet2 = statement2.executeQuery(query)) {
                    printHeaderFilms();
                    while (resultSet2.next()) {
                        int filmId = resultSet2.getInt("film_id");
                        String title = resultSet2.getString("title");
                        String description = resultSet2.getString("description");
                        LocalDateTime lastUpdate = resultSet2.getTimestamp("last_update").toLocalDateTime();
                        String output = String.format("%-5d %-25s %-120s %-10s", filmId, title, description, lastUpdate);
                        System.out.println(output);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void printHeaderActors() {
        String header = String.format("%-15s %-15s %-8s", "ID", "Name", "Last Update");
        System.out.println(header);
        System.out.println("--".repeat(30));
    }

    public static void printHeaderFilms() {
        String header = String.format("%-5s %-25s %-120s %-10s", "ID", "Title", "Description", "Last Update");
        System.out.println(header);
        System.out.println("---".repeat(60));
    }

    public static Connection getConnection() {
        DataSource dataSource = getDataSource("sakila", USER, PASSWORD);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
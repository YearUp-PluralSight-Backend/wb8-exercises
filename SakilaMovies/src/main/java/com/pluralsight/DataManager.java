package com.pluralsight;

import com.pluralsight.model.Actor;
import com.pluralsight.utils.Utility;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pluralsight.utils.Utility.getInput;

public class DataManager {

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
        getActorsById(sql);
    }

    public static List<Actor> getActorsById(String sql) {
        List<Actor> actors = new ArrayList<>();
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
                    String output = String.format("%-5d %-20s %-8s", id, name, lastUpdate);
                    Actor actor = new Actor(id, resultSet.getString("first_name"), resultSet.getString("last_name"));
                    actors.add(actor);
                    System.out.println(output);

                }
                System.out.println();

                String query =
                        """
                                select * from film where film_id in (
                                    select film_id from film_actor
                                    where actor_id = %d
                                )
                                """.formatted(id);
                getFilmsByActorId(query);

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return actors;
    }

    private static List<Film> getFilmsByActorId(String query) {
        if (query.isBlank()) {
            query =
                    """
                            select * from film where film_id in (
                                select film_id from film_actor
                                where actor_id = %s
                            )
                            """.formatted(Utility.getInput("Enter the actor ID: "));
        }
        List<Film> films = new ArrayList<>();
        try (Connection connection = getConnection()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                printHeaderFilms();
                while (resultSet.next()) {
                    int filmId = resultSet.getInt("film_id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    int releaseYear = resultSet.getInt("release_year");
                    int length = resultSet.getInt("length");
                    String output = String.format("%-5d %-25s %-130s %-10d %-10d", filmId, title, description, releaseYear, length);
                    Film film = new Film(filmId, title, description, releaseYear, length);
                    films.add(film);
                    System.out.println(output);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return films;
    }

    public static void printHeaderActors() {
        String header = String.format("%-5s %-20s %-8s", "ID", "Name", "Last Update");
        System.out.println(header);
        System.out.println("--".repeat(30));
    }

    public static void printHeaderFilms() {
        String header = String.format("%-5s %-25s %-130s %-10s %-10s", "ID", "Title", "Description", "Release Year", "Length");
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
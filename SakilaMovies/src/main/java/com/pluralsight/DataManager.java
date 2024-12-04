package com.pluralsight;

import com.pluralsight.model.Actor;
import com.pluralsight.model.Film;
import com.pluralsight.utils.Utility;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pluralsight.DataSourceDemo.printHeaderActors;
import static com.pluralsight.DataSourceDemo.printHeaderFilms;

public class DataManager {

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = System.getenv("MYSQL_ROOT_USER");
    private static final String PASSWORD = System.getenv("MYSQL_ROOT_PASSWORD");

    public DataSource getDataSource(String database, String user, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setUrl(URL + database);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    public List<Actor> getActorsByLastName(String lastName) {
        String query = "SELECT * FROM actor WHERE last_name = ?";
        List<Actor> actors = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastNameFromDb = resultSet.getString("last_name");
                LocalDateTime lastUpdate = resultSet.getTimestamp("last_update").toLocalDateTime();
                actors.add(new Actor(id, firstName, lastNameFromDb, lastUpdate));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return actors;
    }

    public List<Film> getFilmsByActorId(int actorId) {
        String query = """
        SELECT * FROM film WHERE film_id IN (
            SELECT film_id FROM film_actor WHERE actor_id = ?
        )
    """;

        List<Film> films = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, actorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int filmId = resultSet.getInt("film_id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    int releaseYear = resultSet.getInt("release_year");
                    int length = resultSet.getInt("length");
                    Film film = new Film(filmId, title, description, releaseYear, length);
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return films;
    }

    public Connection getConnection() {
        DataSource dataSource = getDataSource("sakila", USER, PASSWORD);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
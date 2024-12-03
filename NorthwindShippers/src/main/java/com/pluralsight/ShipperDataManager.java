package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShipperDataManager {

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = System.getenv("MYSQL_ROOT_USER");
    private static final String PASSWORD = System.getenv("MYSQL_ROOT_PASSWORD");
    private final DataSource dataSource;

    public ShipperDataManager(String database) {
        this.dataSource = getDataSource(database, USER, PASSWORD);
    }

    private DataSource getDataSource(String database, String user, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setUrl(URL + database);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Boolean insertShipper(String companyName, String phone) {
        String sql = "INSERT INTO shippers (CompanyName, Phone) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, companyName);
            preparedStatement.setString(2, phone);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public Boolean updateShipper(int shipperId, String phone) {
        String sql = "UPDATE shippers SET Phone = ? WHERE ShipperID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, phone);
            preparedStatement.setInt(2, shipperId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public Boolean deleteShipper(int shipperId) {
        String sql = "DELETE FROM shippers WHERE ShipperID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, shipperId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public void displayShippers() {
        String sql = "SELECT * FROM shippers";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("ShipperID: " + resultSet.getInt("ShipperID") +
                        ", CompanyName: " + resultSet.getString("CompanyName") +
                        ", Phone: " + resultSet.getString("Phone"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
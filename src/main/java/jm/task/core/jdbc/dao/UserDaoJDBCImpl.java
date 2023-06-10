package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.nio.charset.CoderResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(45), lastName VARCHAR(45), age TINYINT)";
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String CLEAR_TABLE_SQL = "DROP TABLE users";
    private static final String INSERT_SQL = "INSERT INTO users(name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM users WHERE id=?";
    private static final String GET_ALL_SQL = "SELECT * FROM users";

    private Connection connection;


    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User с именем "+ name +" добавлен в базу данных");
    }

    public void removeUserById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_SQL);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAR_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

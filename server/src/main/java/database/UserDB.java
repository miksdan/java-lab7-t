package database;

import messages.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDB {

    public static User getUserById(int id) {
        User user = new User();
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from \"user\" where id = ?")) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String createUser(User user) {

        if (getUserId(user) != -1) {
            return "User with this login already exists!";
        }

        Connection connection = Database.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \"user\" (name, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "User created successfully";
    }

    public static int getUserId(User user) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM \"user\" WHERE name = ?")) {
            preparedStatement.setString(1, user.getName());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static User getUser(User user) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from \"user\" where name = ?")) {

            preparedStatement.setString(1, user.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}

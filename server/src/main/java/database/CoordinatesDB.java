package database;

import model.Coordinates;

import java.sql.*;

public class CoordinatesDB {
    public static Coordinates getCoordinatesById(int id) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("select * from coordinates where id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return new Coordinates(resultSet.getInt("x"), resultSet.getLong("y"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public static int createCoordinates(Coordinates coordinates) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO coordinates (x, y) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, coordinates.getX());
            preparedStatement.setLong(2, coordinates.getY());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void updateCoordinates(Coordinates coordinates, int coordinatesId) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE coordinates set x = ?, y = ? WHERE id = ?");
            preparedStatement.setInt(1, coordinates.getX());
            preparedStatement.setLong(2, coordinates.getY());
            preparedStatement.setInt(3, coordinatesId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCoordinates(int coordinatesId) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM coordinates WHERE id = ?");
            preparedStatement.setInt(1, coordinatesId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getCoordinatesIdByMovieId(int movieId) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();

        int id = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select id from coordinates where id = ?")) {
            connection.prepareStatement("select id from coordinates where id = ?");
            preparedStatement.setLong(1, movieId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
}

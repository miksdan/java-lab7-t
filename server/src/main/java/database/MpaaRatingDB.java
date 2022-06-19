package database;

import model.MpaaRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaaRatingDB {
    public static MpaaRating getMpaaRatingById(int id) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement("select * from \"mpaaRating\" where id = ?")) {
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return MpaaRating.valueOf(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getMpaaRatingIdByName(String name) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select id from \"mpaaRating\" where name = ?")) {
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("MpaaRating was not found!");
    }
}

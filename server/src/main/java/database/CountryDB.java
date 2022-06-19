package database;

import model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDB {
    public static Country getCountryById(int id) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from country where id = ?")) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Country.valueOf(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Country was not found!");
    }

    public static int getCountryIdByName(String name) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select id from country where name = ?")) {
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Country was not found!");
    }
}

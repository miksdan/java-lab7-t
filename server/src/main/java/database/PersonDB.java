package database;

import model.Person;

import java.sql.*;

import static database.ColorDB.getColorById;
import static database.ColorDB.getColorIdByName;
import static database.CountryDB.getCountryById;
import static database.CountryDB.getCountryIdByName;

public class PersonDB {
    public static Person getPersonById(int id) {
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from person where id = ?")) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return new Person(
                        resultSet.getString("name"),
                        resultSet.getInt("weight"),
                        getColorById(resultSet.getInt("eye_id")),
                        getColorById(resultSet.getInt("hair_id")),
                        getCountryById(resultSet.getInt("country_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    public static int createPerson(Person person) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO person (name, weight, eye_id, hair_id, country_id) VALUES (?, ?, ?, ?, ?)"
                , Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getWeight());
            preparedStatement.setInt(3, getColorIdByName(person.getEyeColor().name()));
            preparedStatement.setInt(4, getColorIdByName(person.getHairColor().name()));
            preparedStatement.setInt(5, getCountryIdByName(person.getNationality().name()));
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

    public static void updatePerson(Person person, int personId) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE person set name = ?, weight = ?, eye_id = ?, hair_id = ?, country_id = ? WHERE id = ?")) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getWeight());
            preparedStatement.setInt(3, getColorIdByName(person.getEyeColor().name()));
            preparedStatement.setInt(4, getColorIdByName(person.getHairColor().name()));
            preparedStatement.setInt(5, getCountryIdByName(person.getNationality().name()));
            preparedStatement.setInt(6, personId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removePerson(int personId) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person WHERE id = ?")) {
            preparedStatement.setInt(1, personId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getPersonIdByMovieId(long movie) {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        int id = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select person_id from movie where id = ?")) {
            preparedStatement.setLong(1, movie);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("person_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
}

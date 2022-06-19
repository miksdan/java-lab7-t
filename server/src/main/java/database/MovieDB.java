package database;

import model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.CoordinatesDB.*;
import static database.MpaaRatingDB.getMpaaRatingById;
import static database.MpaaRatingDB.getMpaaRatingIdByName;
import static database.PersonDB.*;
import static database.UserDB.getUserById;

public class MovieDB {
    public static List<Movie> getAllMoviesFromDB() {
        List<Movie> movies = new ArrayList<>();
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from movie")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    Movie movie = new Movie(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            getCoordinatesById(resultSet.getInt("coordinates_id")),
                            resultSet.getTimestamp("creationDate").toLocalDateTime().toLocalDate(),
                            resultSet.getInt("oscarsCount"),
                            resultSet.getInt("goldenPalmCount"),
                            resultSet.getLong("length"),
                            getMpaaRatingById(resultSet.getInt("mpaaRating_id")),
                            getPersonById(resultSet.getInt("person_id")));
                    movie.setUser(getUserById(resultSet.getInt("user_id")));
                    movies.add(movie);
                } catch (Exception e) {
                    System.out.format("Movie with id: %d - skipped.\n", resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static Movie getMovieById(int id) {
        Movie movie = null;
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement= connection.prepareStatement("select * from movie where id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                movie = new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        getCoordinatesById(resultSet.getInt("coordinates_id")),
                        resultSet.getTimestamp("creationDate").toLocalDateTime().toLocalDate(),
                        resultSet.getInt("oscarsCount"),
                        resultSet.getInt("goldenPalmCount"),
                        resultSet.getLong("length"),
                        getMpaaRatingById(resultSet.getInt("mpaaRating_id")),
                        getPersonById(resultSet.getInt("person_id")));
                movie.setUser(getUserById(resultSet.getInt("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public static void createMovie(Movie movie, int userId) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO movie" +
                " (name, coordinates_id, \"creationDate\", \"oscarsCount\", \"goldenPalmCount\", length, \"mpaaRating_id\", person_id, user_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setInt(2, createCoordinates(movie.getCoordinates()));
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(4, movie.getOscarsCount());
            preparedStatement.setInt(5, movie.getGoldenPalmCount());
            preparedStatement.setLong(6, movie.getLength());
            preparedStatement.setInt(7, movie.getMpaaRating() != null ? getMpaaRatingIdByName(movie.getMpaaRating().name()) : -1);
            preparedStatement.setInt(8, movie.getScreenwriter() != null ? createPerson(movie.getScreenwriter()) : -1);
            preparedStatement.setInt(9, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Movie movie, long movieId) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE movie " +
                "SET name = ?, \"oscarsCount\" = ?, \"goldenPalmCount\" = ?, length = ?, \"mpaaRating_id\" = ?, WHERE id = ?")) {
            preparedStatement.setString(1, movie.getName());
            preparedStatement.setInt(2, movie.getOscarsCount());
            preparedStatement.setInt(3, movie.getGoldenPalmCount());
            preparedStatement.setLong(4, movie.getLength());
            preparedStatement.setInt(5, getMpaaRatingIdByName(movie.getMpaaRating().name()));
            preparedStatement.setLong(6, movieId);

            updateCoordinates(movie.getCoordinates(), getCoordinatesIdByMovieId(movie.getId()));
            updatePerson(movie.getScreenwriter(), getPersonIdByMovieId(movie.getId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeById(int movieId) {
        Connection connection = Database.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM movie WHERE id = ?")) {
            preparedStatement.setLong(1, movieId);

            removeCoordinates(getCoordinatesIdByMovieId(movieId));
            removePerson(getPersonIdByMovieId(movieId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

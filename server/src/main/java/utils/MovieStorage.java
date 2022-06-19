package utils;

import database.MovieDB;
import model.Movie;
import model.MpaaRating;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Manipulate with stored movies
 */
public class MovieStorage {
    private static final CopyOnWriteArrayList<Movie> STORAGE = new CopyOnWriteArrayList<>();
    private static final Date initDate = new Date();
    private static int currentId = 0;

    public static Date getInitDate() {
        return initDate;
    }

    /**
     * add movie
     *
     * @param movie
     */
    public static void add(Movie movie) {
        STORAGE.add(movie);
    }

    /**
     * cleans the collection
     */
    public static ArrayList<Integer> getIdForClear() {
        return STORAGE.stream()
                .map(Movie::getId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * obtaining storage iterator
     *
     * @return Storage.iterator()
     */
    public static Iterator<Movie> getIterator() {
        return STORAGE.iterator();
    }

    /**
     * obtaining storage size
     *
     * @return storage size
     */
    public static int size() {
        return STORAGE.size();
    }

    /**
     * updates movie
     *
     * @param id
     * @param movie
     */
    public static void update(int id, Movie movie) {
        STORAGE.stream().sequential().filter(m -> m.getId() == id).forEach(m -> m.update(movie.getName(),
                movie.getCoordinates(),
                movie.getOscarsCount(),
                movie.getGoldenPalmCount(),
                movie.getLength(),
                movie.getMpaaRating(),
                movie.getScreenwriter()));
    }

    /**
     * remove movie by id
     *
     * @param id
     */
    public static void removeById(int id) {
        STORAGE.removeIf(m -> m.getId() == id);
    }

    /**
     * remove greater movie
     *
     * @param movie
     */
    public static ArrayList<Integer> getIdForRemoveGreater(Movie movie) {
        return STORAGE.stream()
                .filter(m -> movie.compareTo(m) < 0)
                .map(Movie::getId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * remove lower movie
     *
     * @param movie
     */
    public static ArrayList<Integer> getIdForRemoveLower(Movie movie) {
        return STORAGE.stream()
                .filter(m -> movie.compareTo(m) > 0)
                .map(Movie::getId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * obtaining movie with max date
     *
     * @return movie with max date
     */
    public static Movie getMaxCreationDate() {
        return STORAGE.stream()
                .max(Comparator.comparing(Movie::getCreationDate))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * count by Mpaa Rating
     *
     * @param rating
     * @return count
     */
    public static int countByMpaaRating(MpaaRating rating) {
        return (int) STORAGE.stream()
                .filter(movie -> movie.getMpaaRating() != null)
                .filter(movie -> movie.getMpaaRating().equals(rating))
                .count();
    }

    /**
     * obtaining filter by Mpaa Rating
     *
     * @param rating
     * @return movies
     */
    public static List<Movie> filterByMpaaRating(MpaaRating rating) {
        return STORAGE.stream()
                .filter(movie -> movie.getMpaaRating() != null)
                .filter(movie -> movie.getMpaaRating().equals(rating))
                .collect(Collectors.toList());
    }

    /**
     * method to help with work with a queue
     *
     * @return list with movies
     */
    public static List<Movie> getStorageAsList() {
        return new ArrayList<>(STORAGE);
    }

    /**
     * obtaining sorted list
     *
     * @return movies
     */
    public static List<Movie> getSortedListByName() {
        List<Movie> movies = getStorageAsList();
        movies.sort(Movie::compareTo);
        return movies;
    }

    public static void loadMoviesFromDB() {
        STORAGE.clear();
        List<Movie> movies = MovieDB.getAllMoviesFromDB();
        movies.forEach(MovieStorage::add);
    }
}
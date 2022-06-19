package database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "4444";

    private static Database database;
    private final Connection connection;

    private Database() {
        this.connection = connectToDB();
        initDB();
    }

    private void initDB() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists \"user\"\n" +
                    "(\n" +
                    "    id       serial\n" +
                    "        constraint user_pk\n" +
                    "            primary key,\n" +
                    "    name     varchar not null,\n" +
                    "    password varchar not null\n" +
                    ");\n" +
                    "\n" +
                    "create table if not exists movie\n" +
                    "(\n" +
                    "    id                serial\n" +
                    "        constraint movie_pk\n" +
                    "            primary key,\n" +
                    "    name              varchar   not null,\n" +
                    "    coordinates_id    int       not null,\n" +
                    "    \"creationDate\"    timestamp not null,\n" +
                    "    \"oscarsCount\"     int       not null,\n" +
                    "    \"goldenPalmCount\" int       not null,\n" +
                    "    length            int       not null,\n" +
                    "    \"mpaaRating_id\"   int,\n" +
                    "    person_id         int,\n" +
                    "    user_id           int       not null\n" +
                    ");\n" +
                    "\n" +
                    "create unique index if not exists movie_id_uindex\n" +
                    "    on movie (id);\n" +
                    "\n" +
                    "create table if not exists coordinates\n" +
                    "(\n" +
                    "    id serial\n" +
                    "        constraint coordinates_pk\n" +
                    "            primary key,\n" +
                    "    x  int not null,\n" +
                    "    y  int not null\n" +
                    ");\n" +
                    "\n" +
                    "create unique index if not exists coordinates_id_uindex\n" +
                    "    on coordinates (id);\n" +
                    "\n" +
                    "drop table if exists \"mpaaRating\";\n" +
                    "\n" +
                    "create table if not exists \"mpaaRating\"\n" +
                    "(\n" +
                    "    id   serial\n" +
                    "        constraint mpaaRating_pk\n" +
                    "            primary key,\n" +
                    "    name varchar not null\n" +
                    ");\n" +
                    "\n" +
                    "create table if not exists person\n" +
                    "(\n" +
                    "    id         serial\n" +
                    "        constraint person_pk\n" +
                    "            primary key,\n" +
                    "    name       varchar not null,\n" +
                    "    weight     int     not null,\n" +
                    "    eye_id     int     not null,\n" +
                    "    hair_id    int     not null,\n" +
                    "    country_id int\n" +
                    ");\n" +
                    "\n" +
                    "create unique index if not exists person_id_uindex\n" +
                    "    on person (id);\n" +
                    "\n" +
                    "drop table if exists  color;\n" +
                    "\n" +
                    "create table if not exists color\n" +
                    "(\n" +
                    "    id   serial\n" +
                    "        constraint color_pk\n" +
                    "            primary key,\n" +
                    "    name varchar not null\n" +
                    ");\n" +
                    "\n" +
                    "drop table if exists  country;\n" +
                    "\n" +
                    "create table if not exists country\n" +
                    "(\n" +
                    "    id   serial\n" +
                    "        constraint country_pk\n" +
                    "            primary key,\n" +
                    "    name varchar not null\n" +
                    ");");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO \"mpaaRating\" (id, name) VALUES (DEFAULT, 'R');\n" +
                    "INSERT INTO \"mpaaRating\" (id, name) VALUES (DEFAULT, 'G');\n" +
                    "INSERT INTO \"mpaaRating\" (id, name) VALUES (DEFAULT, 'NC_17');\n" +
                    "\n" +
                    "INSERT INTO \"country\" (id, name) VALUES (DEFAULT, 'GERMANY');\n" +
                    "INSERT INTO \"country\" (id, name) VALUES (DEFAULT, 'FRANCE');\n" +
                    "INSERT INTO \"country\" (id, name) VALUES (DEFAULT, 'VATICAN');\n" +
                    "INSERT INTO \"country\" (id, name) VALUES (DEFAULT, 'NORTH_KOREA');\n" +
                    "\n" +
                    "INSERT INTO \"color\" (id, name) VALUES (DEFAULT, 'GREEN');\n" +
                    "INSERT INTO \"color\" (id, name) VALUES (DEFAULT, 'RED');\n" +
                    "INSERT INTO \"color\" (id, name) VALUES (DEFAULT, 'BLACK');\n" +
                    "INSERT INTO \"color\" (id, name) VALUES (DEFAULT, 'ORANGE');\n" +
                    "INSERT INTO \"color\" (id, name) VALUES (DEFAULT, 'WHITE');\n" +
                    "INSERT INTO \"color\" (id, name) VALUES (DEFAULT, 'BROWN');");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    private Connection connectToDB() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warn(e.getMessage());
        }
        throw new RuntimeException("Problems with creating connection!");
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new RuntimeException("No connection to database!");
        }
        return connection;
    }
}

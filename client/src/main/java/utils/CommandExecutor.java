package utils;

import messages.Request;
import messages.Response;
import messages.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;

import static utils.Encryption.encryptPassword;
import static utils.MovieUtil.createMovie;

/**
 * Execute commands from console or from file
 */
public class CommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    /**
     * defines the mapping of command names to its calls
     */
    private static final Map<String, BiConsumer<String, Scanner>> COMMAND_FUNCTION_MAP;

    static {
        Map<String, BiConsumer<String, Scanner>> stringBiConsumerHashMap = new HashMap<>();
        stringBiConsumerHashMap.put("help", CommandExecutor::help);
        stringBiConsumerHashMap.put("info", CommandExecutor::info);
        stringBiConsumerHashMap.put("show", CommandExecutor::show);
        stringBiConsumerHashMap.put("add", CommandExecutor::add);
        stringBiConsumerHashMap.put("update", CommandExecutor::update);
        stringBiConsumerHashMap.put("remove_by_id", CommandExecutor::removeById);
        stringBiConsumerHashMap.put("clear", CommandExecutor::clear);
        stringBiConsumerHashMap.put("execute_script", CommandExecutor::executeScript);
        stringBiConsumerHashMap.put("exit", CommandExecutor::exit);
        stringBiConsumerHashMap.put("head", CommandExecutor::head);
        stringBiConsumerHashMap.put("remove_greater", CommandExecutor::removeGreater);
        stringBiConsumerHashMap.put("remove_lower", CommandExecutor::removeLower);
        stringBiConsumerHashMap.put("max_by_creation_date", CommandExecutor::maxByCreationDate);
        stringBiConsumerHashMap.put("count_by_mpaa_rating", CommandExecutor::countByMpaaRating);
        stringBiConsumerHashMap.put("filter_by_mpaa_rating", CommandExecutor::filterByMpaaRating);
        COMMAND_FUNCTION_MAP = Collections.unmodifiableMap(stringBiConsumerHashMap);
    }

    /**
     * Starts an endless loop of receiving commands from the console
     *
     * @param scan console scanner
     */
    public static void startExecution(Scanner scan) {
        do {
            if (scan.hasNext()) {
                executeCommand(scan);
            } else {
                logger.info("Client is stopped.");
                System.exit(0);
            }
        } while (scan.hasNext());
    }

    /**
     * execute script from file
     *
     * @param scan script file scanner
     */
    public static void executeScriptCommands(Scanner scan) {
        while (scan.hasNext()) {
            executeCommand(scan);
        }
    }

    /**
     * execute script from file
     *
     * @param scan script file scanner
     */
    private static void executeScript(String params, Scanner scan) {
        if (UniqueValuesUtil.isScriptAlreadyRunning(params)) {
            System.out.println();
            throw new IllegalArgumentException("Infinite loop detected, command 'execute_script " + params + "' skipped" + "\n");
        }
        FileAccessor.readScript(params);
    }

    /**
     * execute one command from scanner
     *
     * @param scan script file scanner
     * @throws IllegalArgumentException for invalid command name
     */
    private static void executeCommand(Scanner scan) {

        String[] currentCommand = (scan.nextLine().trim() + " ").split(" ", 2);
        try {
            Optional.ofNullable(COMMAND_FUNCTION_MAP.get(currentCommand[0]))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid command, type \"help\" for information about commands"))
                    .accept(currentCommand[1].trim(), scan);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * checks if additional parameters were on the same line with the command
     *
     * @param params should be empty
     * @throws IllegalArgumentException for invalid command name
     */
    private static void isAdditionalParamsEmpty(String params) {
        if (!params.isEmpty()) {
            throw new IllegalArgumentException("This command doesn't need parameters");
        }
    }

    /**
     * help command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void help(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(new Request("help", null, null, LoginHandler.getUser()));
    }

    /**
     * info command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void info(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("info", null, null, LoginHandler.getUser()));
    }

    /**
     * show command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void show(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("show", null, null, LoginHandler.getUser()));
    }

    /**
     * add command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void add(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("add", null, createMovie(scan), LoginHandler.getUser()));
//        System.out.println("The film is added to the collection!");
    }

    /**
     * update command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void update(String params, Scanner scan) {
        int id = Integer.parseInt(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("update", String.valueOf(id), createMovie(scan), LoginHandler.getUser()));
//        System.out.println("The fields of the film updated!");
    }

    /**
     * remove_by_id command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void removeById(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("remove_by_id", String.valueOf(params), null, LoginHandler.getUser()));
//        System.out.println("Removed by ID!");
    }

    /**
     * clear command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void clear(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("clear", null, null, LoginHandler.getUser()));
//        System.out.println("The collection is cleaned of elements!");
    }

    /**
     * exit command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void exit(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        System.out.println("The program terminated.");
        System.exit(0);
    }

    /**
     * head command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void head(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("head", null, null, LoginHandler.getUser()));
    }

    /**
     * remove_greater command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void removeGreater(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("remove_greater", null, createMovie(scan), LoginHandler.getUser()));
    }

    /**
     * remove_lower command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void removeLower(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("remove_lower", null, createMovie(scan), LoginHandler.getUser()));
    }

    /**
     * max_by_creationDate command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void maxByCreationDate(String params, Scanner scan) {
        isAdditionalParamsEmpty(params);
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("max_by_creation_date", null, null, LoginHandler.getUser()));
    }

    /**
     * count_by_mpaa_rating command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void countByMpaaRating(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("count_by_mpaa_rating", params, null, LoginHandler.getUser()));
    }

    /**
     * filter_by_mpaa_rating command
     *
     * @param params command additional params (id, filename etc.)
     * @param scan   helps to get the params of an object (for example for 'add' command)
     */
    private static void filterByMpaaRating(String params, Scanner scan) {
        ConnectionKeeper.sendMessageAndGetAnswer(
                new Request("filter_by_mpaa_rating", params, null, LoginHandler.getUser()));
    }

    public static void authorize(String params, boolean log) {
        String[] loginAndPassword = params.split(" ");
        if (loginAndPassword.length < 2) {
            logger.info("Invalid Login and(or) Password.");
            return;
        }
        User user = new User(loginAndPassword[0], encryptPassword(loginAndPassword[1]));
        Request request = new Request(log ? "login" : "register", params, null, user);
        Response messageAnswer = ConnectionKeeper.sendMessageAndGetAnswer(request);
        String[] answer = messageAnswer.getAnswer().split("=");
        if (answer.length == 2) {
            user.setId(Integer.parseInt(answer[1]));
            LoginHandler.setUser(user);
        }
    }
}

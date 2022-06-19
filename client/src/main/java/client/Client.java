package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CommandExecutor;
import utils.ConnectionKeeper;
import utils.LoginHandler;

import java.io.Console;
import java.util.Scanner;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final Scanner consoleScanner;

    /**
     * Constructor for creating a client object
     *
     * @param inetSocketAddress
     */
    public Client(Integer inetSocketAddress) {
        this.consoleScanner = new Scanner(System.in);
        logger.info("Client is started.");
        connectToServer(inetSocketAddress);
        startClient();
    }

    private void connectToServer(Integer inetSocketAddress) {
        try {
            ConnectionKeeper.connectToServer(inetSocketAddress);
            logger.info("Client connected to server.");
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            logger.info("Client is turned off.");
            System.exit(0);
        }
    }

    private void startClient() {
        authUser();
        try (Scanner scan = new Scanner(System.in)) {
            CommandExecutor.startExecution(scan);
        } catch (Exception e) {

        }
    }

    private void authUser() {
        while (LoginHandler.getUser() == null) {
            String password = "";
            System.out.println("Confirm to login [Y/N]");
            String answer = consoleScanner.nextLine();
            System.out.println("Enter your login:");
            String login = consoleScanner.nextLine();
            System.out.println("Enter your password:");
            Console console = System.console();
            if (console != null) {
                char[] symbols = console.readPassword();
                if (symbols == null) continue;
                password = String.valueOf(symbols);
            } else password = consoleScanner.nextLine();
//                    String password = consoleScanner.nextLine();
            CommandExecutor.authorize(login + " " + password, answer.equalsIgnoreCase("y"));
        }
    }
}

package server;

import database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utils.ConnectionAcceptThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.channels.ServerSocketChannel;
import java.util.Scanner;

import static utils.MovieStorage.loadMoviesFromDB;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final ServerSocketChannel server;

    /**
     * Constructor for creating a server object
     *
     * @param inetSocketAddress
     * @throws IOException
     * @throws URISyntaxException
     */
    public Server(Integer inetSocketAddress) throws IOException, URISyntaxException, InterruptedException {
        this.server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress(inetSocketAddress));
        logger.info("Server started.");
        Database.getInstance();
        loadMoviesFromDB();
        run();
        throw new RuntimeException("Something wrong with Thread-block.");
    }

    /**
     * server command execution thread
     */
    private void run() throws InterruptedException {

        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String command = "";
            while (true) {
                if (scanner.hasNext()) {
                    command = scanner.nextLine();
                    if (command.equals("exit")) {
                        logger.info("Server is stopped.");
                        System.exit(0);
                    }
                }
                else {
                    logger.warn("Server is stopped.");
                    System.exit(0);
                }
            }
        });
        thread.start();

        ConnectionAcceptThread connectionAcceptThread = new ConnectionAcceptThread(server);
        connectionAcceptThread.start();
        connectionAcceptThread.join();
    }
}

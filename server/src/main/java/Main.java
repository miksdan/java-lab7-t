import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            logger.error("The command line argument was expected.\n");
            logger.info("The program terminated.");
            System.exit(0);
        }

        try {
            final Integer inetSocketAddress = Integer.valueOf(args[0]);
            new Server(inetSocketAddress);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.info(e.getMessage());
        }
    }
}

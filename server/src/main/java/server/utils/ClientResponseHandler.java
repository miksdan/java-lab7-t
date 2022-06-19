package server.utils;

import messages.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.ConvertUtil.convertObjectToByteArray;

public class ClientResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void sendAnswer(Response response, ClientConnection client) {
        executorService.submit(() -> {
            try {
                logger.info("Sending Response to client.");
                client.getSocket().write(ByteBuffer.wrap(convertObjectToByteArray(response)));
                logger.info("Response send to client.");
            } catch (IOException e) {
                logger.warn("Error while sending response to client. " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}

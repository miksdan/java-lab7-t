package server.utils;

import messages.Request;
import messages.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CommandExecutor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ForkJoinPool;

import static server.utils.ClientResponseHandler.sendAnswer;
import static utils.ConvertUtil.convertByteArrayToObject;

public class ClientRequestHandler extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final ClientConnection clientConnection;
    private boolean alive;

    public ClientRequestHandler(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        alive = true;
        this.start();
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(10000);
        while (true) {
            try {
                buffer.clear();
                int countOfBytes = clientConnection.getSocket().read(buffer);
                if (countOfBytes < 1) {
                    continue;
                }
                forkJoinPool.submit(() -> {
                    try {
                        Request request = (Request) convertByteArrayToObject(buffer.array());
                        logger.info("Received Request from client " + clientConnection.getSocket().getRemoteAddress() + " with command: " + request.getCommand() + ".");
                        Response response = CommandExecutor.startExecution(request);
                        sendAnswer(response, clientConnection);
                    } catch (IOException e) {
                        logger.warn("Error in handling request from client.");
                        throw new RuntimeException(e);
                    }

                });
            } catch (IOException e) {
                logger.warn("Client disconnected.");
                alive = false;
                break;
            }
        }
    }

    public boolean isActive() {
        return alive;
    }
}

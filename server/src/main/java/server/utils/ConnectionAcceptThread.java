package server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ConnectionAcceptThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionAcceptThread.class);
    private final List<ClientRequestHandler> clients = new ArrayList<>();

    private ServerSocketChannel server;

    public ConnectionAcceptThread(ServerSocketChannel server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            clients.removeIf(c -> !c.isActive());
            try {
                SocketChannel client = server.accept();
                if (client != null) {
                    client.configureBlocking(false);
                    clients.add(new ClientRequestHandler(new ClientConnection(client)));
                    logger.info("Client connected.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

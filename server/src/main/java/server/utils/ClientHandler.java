package server.utils;

import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler {
    private static final CopyOnWriteArrayList<ClientConnection> clients = new CopyOnWriteArrayList<>();

    public static void addNewClient(ClientConnection clientConnection) {
        clients.add(clientConnection);
    }

    public static CopyOnWriteArrayList<ClientConnection> getClients() {
        return clients;
    }

    public static void removeClient(ClientConnection clientConnection) {
        clients.remove(clientConnection);
    }
}

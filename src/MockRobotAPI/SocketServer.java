package MockRobotAPI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * A class that creates a socket server that can be used by referencing it
 */
public class SocketServer implements AutoCloseable {

    private final int PORT = 1000;
    private ServerSocket serverSocket;
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    //Creating socket server on port 1000
    public SocketServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    //Start the server socket and initiate it
    public void runSeverSocket(){
        try {
            System.out.println("Initializing the server socket...");
            System.out.println("Waiting for new connections...");
            while (true){
                //Accept sockets and add socket to clients
                Socket socket = serverSocket.accept();
                acceptNewConnection(socket);
            }
        } catch (Exception e) {
            System.out.println("Unhandled exception while initializing " +
                    "the socket server. ERROR MESSAGE: " + e.getMessage());
        }
    }

    //Accepts new connections from new clients and create new instances with new threads
    private void acceptNewConnection(Socket socket) throws Exception {
        ClientHandler client = new ClientHandler(socket, clients);
        clients.add(client);
        new Thread(client).start();
    }

    //Close the server's socket
    @Override
    public void close() throws Exception {
        serverSocket.close();
    }
}

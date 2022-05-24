package SchedulerProgram;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * A class that creates a client's socket
 */
public class SocketClient implements AutoCloseable {
    final int SERVER_PORT = 1000;

    private final PrintWriter writer;

    private static SocketClient socketClient;

    int latestPID = 0;

    //Creates a socket cline that is connected to a server on SERVER_IP and SERVER_PORT by instantiating socket client
    private SocketClient(String SERVER_IP) throws Exception {

        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        ServerHandler serverHandler = new ServerHandler(socket, this);
        writer = new PrintWriter(socket.getOutputStream(), true);

        new Thread(serverHandler).start();
    }

    public static SocketClient getInstance(String SERVER_IP) throws Exception {
        if(socketClient == null){
            socketClient = new SocketClient(SERVER_IP);
        }

        return socketClient;
    }

    //Sends to the server using writer
    public void sendToServer(Object data) {
        writer.println(data);
    }

    //Closes the data being written to the server port (API)
    @Override
    public void close(){
        writer.close();
    }
}

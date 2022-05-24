package MockRobotAPI;

import java.io.IOException;


public class ServerProgram {

    //Server's main running method
    public static void main(String[] args) throws IOException {
        SocketServer socketServer = new SocketServer();
        socketServer.runSeverSocket();
    }
}

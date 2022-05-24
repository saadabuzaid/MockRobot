package SchedulerProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * A class that handles the server's(API) responses
 */
public class ServerHandler implements Runnable {
    private final SocketClient client;
    private final BufferedReader reader;

    //Instantiate the server handler using socket and client
    public ServerHandler(Socket socket, SocketClient client) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    //The Device Driver thread that handles the responses while communicating with the API
    @Override
    public void run() {
        String response;

        try {
            while (true) {
                response = reader.readLine();


                if (response.equals("SUCCESS"))
                    System.out.println("Connection has been made successfully to the Program");
                else if (response.equals("HOMING")){
                    client.latestPID = Integer.parseInt(reader.readLine());
                    System.out.println("Received the homing state from the server with PID:" + client.latestPID);
                }
                else if (response.equals("PICKING")){
                    client.latestPID = Integer.parseInt(reader.readLine());
                    System.out.println("Received the picking state from the server with PID:" + client.latestPID);
                }
                else if (response.equals("PLACING")){
                    client.latestPID = Integer.parseInt(reader.readLine());
                    System.out.println("Received the placing state from the server with PID:" + client.latestPID);
                }
                else
                    System.out.println("[SERVER] says: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

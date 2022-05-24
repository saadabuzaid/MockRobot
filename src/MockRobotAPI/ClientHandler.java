package MockRobotAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that handles the client responses
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter writer;
    int PID;
    Status status;
    String [] operation;
    private final ArrayList<ClientHandler> clients;
    MockRobotAPI mockRobotAPI;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) throws Exception {
        this.socket = socket;
        this.clients = clients;
        this.mockRobotAPI = new MockRobotAPI(this);
    }

    @Override
    public void run() {

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            try {
                this.writer = writer;

                setupNewConnection();

                while (true) {
                    String response = reader.readLine();
                    operation = response.split("%");
                    System.out.println("Client says: " + response);

                    if (response.contains("HOMED"))
                    {
                        PID = mockRobotAPI.home();
                        sendToClient("HOMING");
                        sendToClient(PID);
                    }
                    else if(response.toLowerCase().contains("pick"))
                    {
                        System.out.println(Arrays.toString(operation));
                        if(operation.length > 2)
                            System.out.println("Check Parameter inputted");

                        else {
                            PID = mockRobotAPI.pick(Integer.parseInt(operation[1]));
                            sendToClient("PICKING");
                            sendToClient(PID);
                        }
                    }
                    else if(response.toLowerCase().contains("place"))
                    {
                        System.out.println(Arrays.toString(operation));
                        if(operation.length > 2)
                            System.out.println("Check Parameter inputted");

                        else
                            PID = mockRobotAPI.place(Integer.parseInt(operation[1]));
                            sendToClient("PLACING");
                            sendToClient(PID);
                    }
                    else if(response.toLowerCase().contains("status")){
                        if(operation.length > 2)
                            System.out.println("Check Parameter Inputted");
                        else
                            sendToClient("Status of PID "+operation[1]+": "+mockRobotAPI.status(Integer.parseInt(operation[1])));

                    }
                    else
                        sendToClient("UNKNOWN_COMMAND");

                }
            } catch (Exception e) {
                System.out.println("(ClientHandler) ERROR: " + e.getMessage());
                e.printStackTrace();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                removeClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupNewConnection() {
        System.out.println("[NEW] client has been connected.");

        sendToClient("SUCCESS");
    }

    private void removeClient() throws IOException {
        this.clients.remove(this);
        System.out.println("Client has been disconnected.");
    }

    public void sendToClient(Object data) {
        writer.println(data);
    }


}

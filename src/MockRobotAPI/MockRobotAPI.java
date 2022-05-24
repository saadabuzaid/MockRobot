package MockRobotAPI;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class that runs MockRobotAPI functions
 */
public class MockRobotAPI {

    int processID = 0;

    boolean isInProgress = false;
    int processId;

    ClientHandler clientHandler;

    Random rand = new Random();

    HashMap<Integer, Status> processTracking = new HashMap<Integer, Status>();

    //Reference the ClientHandler instance here in the MockRobotAPI
    public MockRobotAPI(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }

    //A function that randomly generates PIDs
    private int generateProcessID()
    {
        int min = 100000, max = 999999;
        return rand.nextInt(max - min) + min;
    }

    //Home function that waits for 2500ms simulating its running period
    int home() {

        System.out.println("home command received.");
        processId = ExecuteProcess();
        return processId;
    }

    //Pick function that gets a PID and waits for 2500ms simulating its running period
    int pick(int sourceLocation) {
        System.out.println("pick command received.");
        processId = ExecuteProcess();
        return processId;
    }


    //Place function that gets a PID and waits for 2500ms simulating its running period
    int place(int destinationLocation){

        System.out.println("place command received.");
        processId = ExecuteProcess();
        return processId;
    }

    //Executes a process, assigns PIDs according to the process states protocol and updates their status
    private int ExecuteProcess(){
        processID = generateProcessID();

        if(isInProgress)
            return -1;

        processTracking.put(processID,Status.InProgress);
        isInProgress = true;

        ProcessTimer();

        processTracking.put(processID,Status.FinishedSuccessfully);
        return processID;
    }

    //ProcessTimer simulates a process function period
    private void ProcessTimer(){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        isInProgress = false;
                    }
                },
                2500
        );
    }


    //Status function returns a PID status using hashmap <PID,Status> processTracking
    String status(int processID) {
        String status = String.valueOf(processTracking.get(processID));
        return status;
    }
}

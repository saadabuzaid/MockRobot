package SchedulerProgram;

/**
 * Running device driver functions in Main function
 */
public class ClientProgram {

    public static void main(String[] args){
        DeviceDriver deviceDriver = new DeviceDriver();
        String[] paramValue = {"100"};

        try {
            //Testing DeviceDriver & MockRobotAPI functions
            //OpenConnection between client and server
            deviceDriver.openConnection("localhost");
            //Send to tne API to start the home operation so that the robot be ready for automation
            deviceDriver.initialize();
            deviceDriver.executeOperation("Pick", new String[] {"Source Location"}, paramValue);
            Thread.sleep(3000);
            deviceDriver.executeOperation("Place",new String[] {"Destination Location"}, paramValue);
            Thread.sleep(3000);
            deviceDriver.executeOperation("status",new String[] {"Process ID"}, new String[] { Integer.toString(deviceDriver.socketClient.latestPID)});
            Thread.sleep(3000);
            //Closes the connection from the client's side
            deviceDriver.abort();


            while (true) { //While loop to thread the terminal and the client in different threads, so that you get the response from the server immediately without waiting for the terminal
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            } catch(Exception e){
                e.printStackTrace();
            }


        }
}

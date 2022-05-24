package SchedulerProgram;

/**
 * A class that implements SchedulerProgram in DeviceDriver
 */
public class DeviceDriver implements SchedulerProgram {

    SocketClient socketClient;

    String errorCode = null;
    String error = null;

    //Opens a connection to the server with IP address passed as a parameter on port 1000 and creates a new instance of SocketClient
    @Override
    public String openConnection(String ipAddress){
       try {
           socketClient = SocketClient.getInstance(ipAddress);
       } catch (Exception e) {
          errorCode = String.valueOf(e);
       }
        return errorCode;
    }

    //Initialize function that sends to the API to run home function as an initialization from the device driver side
    @Override
    public String initialize() {
        try {
            socketClient.sendToServer("HOMED");
        } catch (Exception e) {
            errorCode = String.valueOf(e);
        }
        return errorCode;
    }

    //calls an operation on the API in this format: executeOperation(operation,[paramNames],[paramValues]) and sends
    //it to the API in the format: operation%paramValues
    @Override
    public String executeOperation(String operation, String[] parameterNames, String[] parameterValues) throws Exception {

        errorCode = executeOperationHandler(operation,parameterNames,parameterValues);

        socketClient.sendToServer(operation +"%"+ String.join(",", parameterValues) );
        return errorCode;
    }

    // Handles how executeOperation is used, and checks the operation and parameters passed
    // Throws an error telling the user what was wrong with using executeOperation function
    private String executeOperationHandler(String operation, String[] paramNames, String[] paramValues) throws Exception {
        if(paramNames.length != paramValues.length) {
            error = "Parameter names and parameter do not correspond";

        }
        else if(operation.toLowerCase().equals("pick")) {
            if (!paramNames[0].toLowerCase().equals("source location"))
                error = "Check inputted parameter names as pick has only source location";
            else if (paramNames.length != 1)
                error = "Check inputted parameter names or values as pick has only 1 input";
        }
        else if(operation.toLowerCase().equals("place")){
            if (!paramNames[0].toLowerCase().equals("destination location"))
                error = "Check inputted parameter names as pick has only destination location";
            else if (paramNames.length != 1)
                error = "Check inputted parameter names or values as place has only 1 input";
        }

        else if(operation.toLowerCase().equals("status")){
            if (!paramNames[0].toLowerCase().equals("process id"))
                error = "Check inputted parameter names as pick has only process id";
            else if (paramNames.length != 1)
                error = "Check inputted parameter names or values as status has only 1 input";
        }

        if (error != null){
            throw new Exception(error);
        }

        return null;
    }

    // Aborts the connection by closing the client socket
    @Override
    public String abort() {
        try {
            System.out.println("Closing the Socket and aborting the connection");
           socketClient.close();
        } catch (Exception e) {
            errorCode = String.valueOf(e);
        }
        return errorCode;
    }
}

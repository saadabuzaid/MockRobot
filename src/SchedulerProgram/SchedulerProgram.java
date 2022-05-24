package SchedulerProgram;

/**
 * An interface that is being used by DeviceDriver class
 */
public interface SchedulerProgram {

    String openConnection(String ipAddress) throws Exception;

    String initialize();

    String executeOperation(String operation, String[] parameterNames, String[] parameterValues) throws Exception;

    String abort() throws Exception;

}

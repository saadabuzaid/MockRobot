# MockRobot
## MockRobot Device Driver and API connection using java websockets
### Device Driver Interface Details


SchedulerProgram controls a MockRobot through the interface described in this document, implemented by a Device Driver.  The interface requires that each function returns either an empty string if the operation completed successfully, or a string with a description of an error that occurred during the function call.
Interface Methods:
1.	OpenConnection(string IPAddress)
a.	When SchedulerProgram calls this function, it expects the Device Driver to establish a connection with the MockRobot onboard software.
b.	The parameter IPAddress is the address at which the MockRobot software is running.
2.	Initialize()
a.	When SchedulerProgram calls this function, it expects that the Device Driver will put the MockRobot into an automation-ready (homed) state.
3.	ExecuteOperation(string operation, string[] parameterNames, string[] parameterValues)
a.	When SchedulerProgram calls this function, it expects that the Device Driver will perform an operation determined by the parameter operation.
i.	For this challenge, valid operations include Pick, Place, and Transfer (a pick followed immediately by a place in a single operation).
b.	parameterNames is an array that contains the name of each parameter to be used for the given operation.
c.	parameterValues is an array that contains the value of each parameter to be used for the given operation.
d.	The parameters parameterNames and parameterValues are parallel, meaning that the name of a parameter and it’s value will be found at the same index of the two arrays.
i.	For this challenge, you can expect “Source Location” and “Destination Location” to be the parameters sent from SchedulerProgram, as needed by the MockRobot API.
ii.	Examples of ExecuteOperation Calls:
1.	ExecuteOperation(“Pick”, [Source Location], [10])
2.	ExecuteOperation(“Transfer”, [Destination Location, Source Location], [5, 12]) 
3.	ExecuteOperation(“Transfer”, [ Source Location, Destination Location], [12,5])
a.	Note that 2 and 3 should result in the same behavior.
4.	Abort()
a.	When SchedulerProgram calls this function, it expects that the Device Driver will terminate communication with the MockRobot. 

During normal operation, Scheduler program would call Open Connection once, Initialize once, then ExecuteOperation any number of times, and abort could be called in between any of these calls.  Be aware though that SchedulerProgram may act abnormally and make these calls in an incorrect order.  Your driver design should have a way to handle this gracefully.
It is also possible that SchedulerProgram will send an invalid operation, or similarly parameterNames/Values may not contain the entries required for the MockRobot.  Your driver design should be able to catch these situations as well.

### MockRobot Device API
The MockRobot is a robotic arm that moves objects to and from various locations around a system.  The robot can be controlled remotely by sending the commands supplied in this document to the robot’s onboard software.  The software will open a socket on port 1000, which will accept commands sent over TCP/IP.
Commands sent to the robot should be in this format:  a string with the command name first, followed by a “%” symbol, then any parameters required for the command.  Note that the MockRobot can only perform one process at a time, and will return a negative processID for any commands to begin a process while another process is in progress, rejecting the new command.

The MockRobot’s onboard software will accept the following commands:
•	“home” – Takes no parameters.  Returns int, processID.  This command tells the robot to begin its homing process, which needs to complete before the robot can move samples, and may take up to two minutes.  The onboard software will immediately return a unique ID that can be used to track the status of the running Home process. </br>
•	“pick” – Takes an int, sourceLocation.  Returns int, processID.  This command tells the robot to begin a process to pick up a sample from the provided sourceLocation.  The onboard software will immediately return a unique ID that can be used to track the status of the running Pick process.  The process may take up to five minutes. </br>
•	 “place” – Takes an int, destinationLocation.  Returns int, processID.  This command tells the robot to begin a process to place a sample to the provided destinationLocation.  The onboard software will immediately return a unique ID that can be used to track the status of the running Pick process.  The process may take up to five minutes. </br> 
•	“status” – Takes an int, processID.  Returns a string, processStatus.  This command retrieves the status of the process specified by processID.  The returned processStatus will be a message from the following list:
o	In Progress
o	Finished Successfully
o	Terminated With Error

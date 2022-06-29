import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        RequestTable[] processQueue = new RequestTable[5];
        Elevator[] elevators = new Elevator[5];
        ElevatorFactory elevatorFactory = new ElevatorFactory();

        for (int i = 0; i < 5; i++) {
            processQueue[i] = new RequestTable();
            elevators[i] = elevatorFactory.lookElevator(i + 1, processQueue[i]);
        }

        InputHandler input = new InputHandler(processQueue);

        new Thread(input).start();
        for (int i = 0; i < 5; i++) {
            new Thread(elevators[i]).start();
        }
    }
}

import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.ElevatorInput;

import java.io.IOException;

public class Input extends Thread {
    private PassengerQueue waitQueue;

    public Input(PassengerQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                waitQueue.setEnd();
                break;
            } else {
                Passenger passenger = new Passenger(request);
                waitQueue.addPassenger(passenger);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
}

import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;

    public InputThread(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest personRequest = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (personRequest == null) {
                waitQueue.setEnd(true);
                break;
            } else {
                int id = personRequest.getPersonId();
                int balcony = personRequest.getFromBuilding() - 'A';
                int beginFloor = personRequest.getFromFloor();
                int endFloor = personRequest.getToFloor();
                Request request = new Request(id, balcony, beginFloor, endFloor);
                waitQueue.addRequest(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


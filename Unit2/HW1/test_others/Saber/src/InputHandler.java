import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputHandler implements Runnable {
    private RequestTable[] processQueue;

    public InputHandler(RequestTable[] processQueue) {
        this.processQueue = processQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            } else {
                char building = request.getFromBuilding();
                // FromBuilding == ToBuilding
                processQueue[building - 'A'].addRequest(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (Exception e) { /*TODO*/ }
        for (int i = 0; i < 5; i++) {
            processQueue[i].sendOver();
        }
    }
}

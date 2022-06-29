import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.ArrayList;

public class InputHandler extends Thread {
    private final ArrayList<RequestQueue> requestQueues;

    public InputHandler(ArrayList<RequestQueue> requestQueues) {
        this.requestQueues = requestQueues;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                for (int i = 0; i < 5; i++) {
                    requestQueues.get(i).setEnd(true);
                }
                break;
            } else {
                requestQueues.get(request.getFromBuilding() - 'A').addRequest(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

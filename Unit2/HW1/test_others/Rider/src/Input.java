import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Input extends Thread {
    private RequestControl requestControl;

    public Input(RequestControl requestControl) {
        this.requestControl = requestControl;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                requestControl.noNewRequest();
                break;
            } else {
                Request newRequest = new Request(request.getPersonId(), request.getFromBuilding(),
                        request.getFromFloor(), request.getToBuilding(), request.getToFloor());
                requestControl.putRequest(newRequest);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

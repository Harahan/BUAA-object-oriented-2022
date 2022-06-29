import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;
import java.util.Objects;

public class InputHandler extends Thread {
    private final Dispatcher[] longitudinalDispatcher = new Dispatcher[5];
    private final Dispatcher[] crosswiseDispatcher = new Dispatcher[10];

    public void initDispatcher() {
        int i = 0;
        for (; i < 5; i++) {
            longitudinalDispatcher[i] = new Dispatcher(i, 2);
        }
        for (; i < 15; i++) {
            crosswiseDispatcher[i - 5] = new Dispatcher(i, 1);
        }
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        initDispatcher();
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                setEnd(true);
                break;
            } else if (request instanceof PersonRequest) {
                addPersonRequest((PersonRequest) request);
            } else {
                addElevatorRequest((ElevatorRequest) request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPersonRequest(PersonRequest request) {
        if (request.getFromBuilding() == request.getToBuilding()) {
            longitudinalDispatcher[request.getToBuilding() - 'A'].addPersonRequest(request);
        } else {
            crosswiseDispatcher[request.getToFloor() - 1].addPersonRequest(request);
        }
    }

    public void addElevatorRequest(ElevatorRequest request) {
        if (Objects.equals(request.getType(), "building")) {
            longitudinalDispatcher[request.getBuilding() - 'A'].addElevatorRequest(request);
        } else {
            crosswiseDispatcher[request.getFloor() - 1].addElevatorRequest(request);
        }
    }

    public void setEnd(boolean end) {
        for (int i = 0; i < 10; i++) {
            crosswiseDispatcher[i].setEnd(end);
        }
        for (int i = 0; i < 5; i++) {
            longitudinalDispatcher[i].setEnd(end);
        }
    }
}

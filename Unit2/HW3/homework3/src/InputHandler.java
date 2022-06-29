import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import control.Controller;
import java.io.IOException;
import java.util.Objects;
import elevator.MyPersonRequest;

public class InputHandler extends Thread {

    @Override
    public void run() {
        currentThread().setName("InputHandler");
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Controller.getInstance().start();
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                Controller.getInstance().setInputEnd(true);
                break;
            } else if (request instanceof PersonRequest) {
                Controller.getInstance().addPersonNum();
                Controller.getInstance().getWaitingQueue()
                        .addRequest(new MyPersonRequest((PersonRequest) request));
            } else {
                ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                if (Objects.equals(elevatorRequest.getType(), "floor")) {
                    Controller.getInstance().getCrosswiseDispatcher()
                            [elevatorRequest.getFloor() - 1].addElevatorRequest(elevatorRequest);
                } else {
                    Controller.getInstance().getLongitudinalDispatcher()
                            [elevatorRequest.getBuilding() - 'A'].
                            addElevatorRequest(elevatorRequest);
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

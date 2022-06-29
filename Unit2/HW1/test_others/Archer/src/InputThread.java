import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputThread extends Thread {
    private final RequestQueue waitQueue;

    public InputThread(RequestQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest personRequest = elevatorInput.nextPersonRequest();
            if (personRequest == null) {
                waitQueue.setEnd(true);
                //是否需要打印输入结束的信息
                try {
                    elevatorInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            else {
                Request request = new Request(personRequest.getFromFloor(),
                        personRequest.getToFloor(),
                        personRequest.getFromBuilding(), personRequest.getToBuilding(),
                        personRequest.getPersonId());
                waitQueue.addRequest(request);
            }
        }
    }
}

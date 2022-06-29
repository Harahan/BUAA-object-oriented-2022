package homeworkv.thread;

import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import homeworkv.data.RequestQueue;

public class RequestInputDevice extends Thread {
    private final RequestQueue requestQueue;

    public RequestInputDevice(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void run() {
        try {
            ElevatorInput elevatorInput = new ElevatorInput(System.in);
            while (true) {
                PersonRequest request = elevatorInput.nextPersonRequest();
                // when request == null
                // it means there are no more lines in stdin
                if (request == null) {
                    requestQueue.setInputEnd(true);
                    break;
                } else {
                    // a new valid request
                    //System.out.println(request);
                    requestQueue.putRequest(request);
                    // I think it seems don't need to sleep
                }
            }
            elevatorInput.close();
        } catch (Exception e) {
            // print something else
            e.printStackTrace();
        }

    }
}

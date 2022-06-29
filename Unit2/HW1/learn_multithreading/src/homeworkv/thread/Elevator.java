package homeworkv.thread;

//import com.hansbug.debug.DebugHelper;
import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;
import homeworkv.data.RequestQueue;

public class Elevator extends Thread {
    public enum States {
        OPEN, CLOSE, UP, DOWN;
    }

    private static final long OSDOOR = 500;
    private final RequestQueue requestQueue;
    private int currentFloor;
    private int originFloor;
    private int destFloor;
    private long pickupTime;
    private long dropoffTime;
    private States state; // for now
    private int id;

    public Elevator(RequestQueue requestQueue) {
        super("ElevatorThread");
        this.requestQueue = requestQueue;
        this.currentFloor = 1;
    }

    public void run() {
        // please MUST initialize start timestamp at the beginning
        TimableOutput.initStartTimestamp();
        try {
            while (true) {
                if (requestQueue.InputEnd()) {
                    break;
                }

                PersonRequest request = requestQueue.getRequest();
                //System.out.println(
                //        "The Elevator is handling this request: " + request);
                resolveRequest(request);
                pickupSb();
                dropoffSb();
                //sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resolveRequest(PersonRequest request) {
        this.originFloor = request.getFromFloor();
        this.destFloor = request.getToFloor();
        // temp
        this.id = request.getPersonId();

        if (this.currentFloor < this.originFloor) {
            this.state = States.UP;
            this.pickupTime = (this.originFloor - this.currentFloor) * 500;
        } else if (this.currentFloor > this.originFloor) {
            this.state = States.DOWN;
            this.pickupTime = (this.currentFloor - this.originFloor) * 500;
        } else {    // ==
            this.state = States.OPEN;
            this.pickupTime = 0;
        }

        if (this.originFloor < this.destFloor) {
            //this.state = States.UP;
            dropoffTime = (this.destFloor - this.originFloor) * 500;
        } else if (this.originFloor > this.destFloor) {
            //this.state = States.DOWN;
            dropoffTime = (this.originFloor - this.destFloor) * 500;
        } else {    // it won't get here theoretically
            TimableOutput.println("DuplicatedFromToFloorException");
            System.exit(0);
        }
    }

    private void pickupSb() {
        try {
            sleep(pickupTime);   // up or down to pick up sb.
            this.currentFloor = this.originFloor;
            this.state = States.OPEN;
            printEleState();    // open
            // output : IN-id-floor
            printPersonState("IN");
            sleep(OSDOOR);
            this.state = States.CLOSE;
            printEleState();    // close
            //DebugHelper.debugPrintln(2, String.format("PickupTime detected : \"%s\"", pickupTime));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dropoffSb() {
        try {
            sleep(dropoffTime); // up or down to drop off sb.
            this.currentFloor = this.destFloor;
            this.state = States.OPEN;
            printEleState();    // open
            // output : OUT-id-floor
            printPersonState("OUT");
            sleep(OSDOOR);
            this.state = States.CLOSE;
            printEleState();    // close
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printEleState() {
        TimableOutput.println(this.state.toString() + "-" + this.currentFloor);
    }

    private void printPersonState(String string) {
        TimableOutput.println(string + "-" + this.id + "-" + this.currentFloor);
    }
}

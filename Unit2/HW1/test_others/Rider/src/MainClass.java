import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Object obj = new Object();
        Object outputLock = new Object();
        RequestControl requestControl = new RequestControl(obj);

        Input input = new Input(requestControl);
        input.start();

        for (int i = 0; i < 5; i++) {
            Elevator elevator = new Elevator((char) ('A' + i), i + 1,
                    requestControl, obj, outputLock);
            elevator.start();
        }
    }
}

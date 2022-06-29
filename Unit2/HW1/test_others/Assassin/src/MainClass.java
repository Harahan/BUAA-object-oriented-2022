import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        WaitQueue waitQueue = new WaitQueue();
        ArrayList<WaitTable> waitTables = new ArrayList<>();

        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();

        for (int i = 0; i < 5; i++) {
            WaitTable waitTable = new WaitTable();
            waitTables.add(waitTable);
            Elevator elevator = new Elevator(i, waitTable);
            elevator.start();
        }

        Dispatcher dispatcher = new Dispatcher(waitQueue, waitTables);
        dispatcher.start();
    }
}

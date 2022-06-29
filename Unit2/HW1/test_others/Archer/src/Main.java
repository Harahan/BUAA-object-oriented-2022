import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestQueue waitQueue = new RequestQueue();

        ArrayList<RequestQueue> elevatorQueues = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            RequestQueue parallelQueue = new RequestQueue();
            elevatorQueues.add(parallelQueue);
            Elevator elevator = new Elevator(parallelQueue,i + 1);
            elevator.start();
        }

        Schedule schedule = new Schedule(waitQueue,elevatorQueues);
        schedule.start();

        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();
    }
}

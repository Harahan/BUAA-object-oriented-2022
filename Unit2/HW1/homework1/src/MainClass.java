import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ArrayList<RequestQueue> requestQueues = new ArrayList<>();
        ArrayList<Elevator> elevators = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            requestQueues.add(new RequestQueue());
            elevators.add(new Elevator(requestQueues.get(i),
                    i + 1, (char) ('A' + i), new LookStrategy(requestQueues.get(i))));
            elevators.get(i).start();
        }
        new InputHandler(requestQueues).start();
    }
}


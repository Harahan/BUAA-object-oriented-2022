import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Mainclass {
    public static void main(String[] args) {
        Requestlist waitQueue = new Requestlist();
        TimableOutput.initStartTimestamp();
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (char i = 'A'; i <= 'E'; i++) {
            Requestlist requestlist = new Requestlist();
            Elevator elevator = new Elevator(i, requestlist);
            elevators.add(elevator);
            elevator.start();
        }
        Schedule schedule = new Schedule(waitQueue, elevators);
        schedule.start();

        Input input = new Input(waitQueue);
        input.start();
    }

}

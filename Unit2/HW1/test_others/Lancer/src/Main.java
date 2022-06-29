import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Output output = new Output();
        PassengerQueue waitQueue = new PassengerQueue();
        ArrayList<BuildingQueue> buildingQueues = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            HashMap<Integer, PassengerQueue> buildingQueue = new HashMap<>();
            for (int j = 1; j <= 10; j++) {
                buildingQueue.put(j, new PassengerQueue());
            }
            BuildingQueue buildingQueue1 = new BuildingQueue(buildingQueue, i);
            buildingQueues.add(buildingQueue1);
            Elevator elevator = new Elevator(buildingQueue1, i, output);
            elevator.start();
        }
        Scheduler scheduler = new Scheduler(waitQueue, buildingQueues);
        scheduler.start();
        Input input = new Input(waitQueue);
        input.start();
    }
}
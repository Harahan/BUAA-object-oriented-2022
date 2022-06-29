import java.util.ArrayList;

public class Scheduler extends Thread {
    private PassengerQueue waitQueue;
    private ArrayList<BuildingQueue> buildingQueues;

    public Scheduler(PassengerQueue passengerQueue,
                     ArrayList<BuildingQueue> buildingQueues) {
        this.waitQueue = passengerQueue;
        this.buildingQueues = buildingQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEnd()) {
                for (int i = 0; i < 5; i++) {
                    buildingQueues.get(i).setEnd();
                }
                return;
            } else {
                Passenger passenger = waitQueue.getPassenger();
                if (passenger == null) {
                    continue;
                } else {
                    buildingQueues.get(passenger.getFromBuilding() - 'A').addPassenger(passenger);
                }
            }
        }
    }
}

import java.util.HashMap;

public class BuildingQueue {
    private volatile HashMap<Integer, PassengerQueue> buildingQueue;
    private volatile int id;
    private volatile boolean end;

    public BuildingQueue(HashMap<Integer, PassengerQueue> buildingQueue, int id) {
        this.buildingQueue = buildingQueue;
        this.id = id;
        end = false;
    }

    public synchronized void setEnd() {
        end = true;
        for (PassengerQueue item : buildingQueue.values()) {
            item.setEnd();
        }
        notifyAll();
    }

    public synchronized void addPassenger(Passenger passenger) {
        buildingQueue.get(passenger.getFromFloor()).addPassenger(passenger);
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return end;
    }

    public synchronized PassengerQueue get(int i) {
        notifyAll();
        return buildingQueue.get(i);
    }

    public synchronized boolean isEmpty() {
        for (PassengerQueue item : buildingQueue.values()) {
            if (item.isEmpty()) {
                notifyAll();
                return true;
            }
        }
        notifyAll();
        return false;
    }

    public synchronized void waiting() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyAll();
    }
}

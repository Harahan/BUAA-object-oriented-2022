import java.util.ArrayList;

public class PassengerQueue {
    private volatile ArrayList<Passenger> passengers;
    private volatile boolean end;

    public PassengerQueue() {
        this.passengers = new ArrayList<>();
        this.end = false;
    }

    public synchronized void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        notifyAll();
    }

    public synchronized Passenger getPassenger() {
        if (passengers.isEmpty() && !end) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (passengers.isEmpty()) {
            notifyAll();
            return null;
        } else {
            Passenger passenger = passengers.get(0);
            passengers.remove(0);
            notifyAll();
            return passenger;
        }
    }

    public synchronized void setEnd() {
        this.end = true;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return end;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return passengers.isEmpty();
    }

    public synchronized ArrayList<Passenger> getPassengers() {
        notifyAll();
        return passengers;
    }

}

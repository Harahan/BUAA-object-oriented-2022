import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {
    private BuildingQueue buildingQueue;
    private int id;
    private int direction;
    private int floor;
    private ArrayList<Passenger> passengers;
    private boolean end;
    private Output output;
    private char building;

    public Elevator(BuildingQueue buildingQueue, int id, Output output) {
        this.buildingQueue = buildingQueue;
        this.id = id;
        direction = 0;
        passengers = new ArrayList<>();
        end = false;
        floor = 1;
        this.output = output;
        building = (char) (id + 'A' - 1);
    }

    @Override
    public void run() {
        while (!(buildingQueue.isEnd() && passengers.isEmpty() && direction == 0)) {
            if (buildingQueue.isEmpty() && passengers.isEmpty() && direction == 0) {
                buildingQueue.waiting();
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int flag = 0;
            if (direction != 0) {
                output.println("ARRIVE-" + building + "-" + floor + "-" + id);
            }
            if (judgeOut() || judgeIn()) {
                flag = 1;
                output.println("OPEN-" + building + "-" + floor + "-" + id);
                off();
                on();
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (judgeIn()) {
                on();
            }
            if (flag == 1) {
                output.println("CLOSE-" + building + "-" + floor + "-" + id);
            }
            change();
            try {
                move();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public void change() {
        if (passengers.size() == 0) {
            if (direction == 1) {
                if (!checkUp()) {
                    if (!buildingQueue.get(floor).isEmpty()) {
                        direction = -1;
                        onWhenChange();
                    } else {
                        direction = checkDown() ? -1 : 0;
                    }
                }
            } else if (direction == -1) {
                if (!checkDown()) {
                    if (!buildingQueue.get(floor).isEmpty()) {
                        direction = 1;
                        onWhenChange();
                    } else {
                        direction = checkUp() ? 1 : 0;
                    }
                }
            } else {
                search();
            }
        }
    }

    public boolean checkUp() {
        for (int i = floor + 1; i <= 10; i++) {
            if (!buildingQueue.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDown() {
        for (int i = floor - 1; i >= 1; i--) {
            if (!buildingQueue.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void on() {
        PassengerQueue floorQueue = buildingQueue.get(floor);
        synchronized (floorQueue) {
            Iterator iterator = floorQueue.getPassengers().iterator();
            while (iterator.hasNext() && passengers.size() < 6) {
                Passenger passenger = (Passenger) iterator.next();
                if (passenger.getDirection() == direction) {
                    output.println("IN-" + passenger.getPersonId() + "-" + building
                            + "-" + floor + "-" + id);
                    passengers.add(passenger);
                    iterator.remove();
                }
            }
        }
    }

    public void off() {
        Iterator iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = (Passenger) iterator.next();
            if (passenger.getToFloor() == floor) {
                output.println("OUT-" + passenger.getPersonId() + "-"
                        + building + "-" + floor + "-" + id);
                iterator.remove();
            }
        }
    }

    public boolean judgeOut() {
        for (Passenger item : passengers) {
            if (item.getToFloor() == floor) {
                return true;
            }
        }
        return false;
    }

    public boolean judgeIn() {
        if (passengers.size() == 6) {
            return false;
        } else {
            for (Passenger passenger : buildingQueue.get(floor).getPassengers()) {
                if (passenger.getDirection() == direction) {
                    return true;
                }
            }
            return false;
        }
    }

    public void move() throws InterruptedException {
        if (direction == 1) {
            sleep(400);
            floor++;
        } else if (direction == -1) {
            sleep(400);
            floor--;
        }
    }

    public void search() {
        int abs = 10;
        int flag = 0;
        for (int i = 1; i <= 10; i++) {
            if (!buildingQueue.get(i).isEmpty()) {
                if (abs > Math.abs(i - floor)) {
                    abs = Math.abs(i - floor);
                    flag = i;
                }
            }
        }
        if (flag == 0) {
            direction = 0;
        } else if (flag > floor) {
            direction = 1;
        } else if (flag < floor) {
            direction = -1;
        } else {
            Passenger passenger = buildingQueue.get(flag).getPassenger();
            direction = passenger.getDirection();
            output.println("OPEN-" + building + "-" + floor + "-" + id);
            output.println("IN-" + passenger.getPersonId() + "-" +
                    building + "-" + floor + "-" + id);
            passengers.add(passenger);
            on();
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            output.println("CLOSE-" + building + "-" + floor + "-" + id);
        }
    }

    public void onWhenChange() {
        output.println("OPEN-" + building + "-" + floor + "-" + id);
        on();
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        output.println("CLOSE-" + building + "-" + floor + "-" + id);
    }
}

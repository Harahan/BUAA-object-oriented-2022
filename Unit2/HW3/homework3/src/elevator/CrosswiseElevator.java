package elevator;

import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class CrosswiseElevator extends Thread implements Elevator {
    private final int floorId;
    private final int elevatorId;
    private final RequestQueue requests;
    private ArrayList<PersonRequest> inElevator;
    private int curBuilding = 0; /*default value: 0 (building id: A)*/
    private int dir = 1;
    private long lastOpenTime;
    private boolean isOpen = false;
    private final long velocity;
    private final int capacity;
    private final Strategy strategy;

    public CrosswiseElevator(RequestQueue requests, int elevatorId, int floorId,
                             int capacity, double velocity) {
        this.floorId = floorId;
        this.elevatorId = elevatorId;
        this.capacity = capacity;
        this.velocity = (long) (velocity * 1000);
        this.requests = requests;
        this.inElevator = new ArrayList<>();

        this.strategy = new CrosswiseLookStrategy(requests, capacity);
    }

    @Override
    public void run() {
        currentThread().setName("Crosswise" + elevatorId + ":" + floorId);
        while (!(requests.isEnd() && requests.isEmpty() && inElevator.isEmpty())) {
            synchronized (requests) {
                while (requests.isEmpty() && inElevator.isEmpty() && !requests.isEnd()) {
                    try {
                        requests.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (requests.isEnd() && inElevator.isEmpty() && requests.isEmpty()) {
                    break;
                }
            }
            execute();
            moveTo();
        }
    }

    @Override
    public void execute() {
        synchronized (requests) {
            if (strategy.whetherToOpen(inElevator, dir, curBuilding)) {
                executeOpen();
            }
        }
        if (isOpen) {
            executeClose();
        }
    }

    @Override
    public void executeClose() {
        while (System.currentTimeMillis() - lastOpenTime < 400) {
            synchronized (requests) {
                pickUp();
                long tmpTime = System.currentTimeMillis() - lastOpenTime;
                if (tmpTime >= 400) {
                    break;
                }
                try {
                    requests.wait(400 - tmpTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        close();
    }

    @Override
    public void executeOpen() {
        open();
        dropOff();
        pickUp();
    }

    @Override
    public void pickUp() {
        synchronized (requests) {
            ArrayList<PersonRequest> curBuildingRequests =
                    strategy.whoToPickUp(inElevator, dir, curBuilding);
            while (inElevator.size() < capacity && !curBuildingRequests.isEmpty()) {
                PersonRequest request = curBuildingRequests.get(0);
                inElevator.add(request);
                in(request);
                requests.removeRequest(request);
                curBuildingRequests.remove(0);
            }
            dir = strategy.whichDir(inElevator, dir, curBuilding);
            /*  if the elevator was empty and now is picking
                up some people whose target is opposite,
                the elevator must refresh its direction.
            */
        }
    }

    @Override
    public void dropOff() {
        ArrayList<PersonRequest> res = new ArrayList<>();
        for (PersonRequest request : inElevator) {
            if (request.getToBuilding() - 'A' == curBuilding) {
                out(request);
                transfer(request);
            } else {
                res.add(request);
            }
        }
        inElevator = res;
    }

    @Override
    public void moveTo() {
        dir = strategy.whichDir(inElevator, dir, curBuilding);
        if (strategy.whetherToMove(inElevator)) {
            try {
                sleep(velocity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            curBuilding = (dir + curBuilding + 5) % 5;
            arrive();
        }
    }

    @Override
    public void open() {
        if (!isOpen) {
            isOpen = true;
            lastOpenTime = MyOutPut.myPrintln("OPEN-" + (char)(curBuilding + 'A') +
                    "-" + floorId + "-" + elevatorId);
        }
    }

    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            MyOutPut.myPrintln("CLOSE-" +
                    (char)(curBuilding + 'A') + "-" + floorId + "-" + elevatorId);
        }
    }

    @Override
    public void arrive() {
        MyOutPut.myPrintln("ARRIVE-" +
                (char)(curBuilding + 'A') + "-" + floorId + "-" + elevatorId);
    }

    @Override
    public void in(PersonRequest request) {
        MyOutPut.myPrintln("IN-" + request.getPersonId() +
                "-" + (char)(curBuilding + 'A') + "-" + floorId + "-" + elevatorId);
    }

    @Override
    public void out(PersonRequest request) {
        MyOutPut.myPrintln("OUT-" + request.getPersonId() +
                "-" + (char)(curBuilding + 'A') + "-" + floorId + "-" + elevatorId);
    }
}

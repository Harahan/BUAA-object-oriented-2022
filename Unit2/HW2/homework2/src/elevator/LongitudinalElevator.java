package elevator;

import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class LongitudinalElevator extends Thread implements Elevator {
    /**
     * buildingId ---> char
     * curFloor ---> int
     */
    private final char buildingId;
    private final int elevatorId;

    private final RequestQueue requests;
    private ArrayList<PersonRequest> inElevator;

    private int curFloor = 1;
    private int dir = 1;
    private long lastOpenTime;
    private boolean isOpen = false;

    private final Strategy strategy;

    public LongitudinalElevator(RequestQueue requests, int elevatorId, char buildingId) {
        this.buildingId = buildingId;
        this.elevatorId = elevatorId;

        this.requests = requests;
        this.inElevator = new ArrayList<>();

        this.strategy = new LongitudinalLookStrategy(requests);
    }

    @Override
    public void run() {
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
            if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
                executeOpen();
            }
            dir = strategy.whichDir(inElevator, dir, curFloor);
            if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
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
            ArrayList<PersonRequest> curFloorRequests =
                    strategy.whoToPickUp(null, dir, curFloor);
            while (inElevator.size() < 6 && !curFloorRequests.isEmpty()) {
                PersonRequest request = curFloorRequests.get(0);
                inElevator.add(request);
                in(request);
                requests.removeRequest(request);
                curFloorRequests.remove(0);
            }
        }
    }

    @Override
    public void dropOff() {
        ArrayList<PersonRequest> res = new ArrayList<>();
        for (PersonRequest request : inElevator) {
            if (request.getToFloor() == curFloor) {
                out(request);
            } else {
                res.add(request);
            }
        }
        inElevator = res;
    }

    @Override
    public void moveTo() {
        dir = strategy.whichDir(inElevator, dir, curFloor);
        if (strategy.whetherToMove(inElevator)) {
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            curFloor += dir;
            arrive();
        }
    }

    @Override
    public void open() {
        if (!isOpen) {
            isOpen = true;
            lastOpenTime = MyOutPut.myPrintln("OPEN-" + buildingId +
                    "-" + curFloor + "-" + elevatorId);
        }
    }

    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            MyOutPut.myPrintln("CLOSE-" + buildingId + "-" + curFloor + "-" + elevatorId);
        }
    }

    @Override
    public void arrive() {
        MyOutPut.myPrintln("ARRIVE-" + buildingId + "-" + curFloor + "-" + elevatorId);
    }

    @Override
    public void in(PersonRequest request) {
        MyOutPut.myPrintln("IN-" + request.getPersonId() +
                "-" + buildingId + "-" + curFloor + "-" + elevatorId);
    }

    @Override
    public void out(PersonRequest request) {
        MyOutPut.myPrintln("OUT-" + request.getPersonId() +
                "-" + buildingId + "-" + curFloor + "-" + elevatorId);
    }
}

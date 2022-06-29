import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Elevator extends Thread {
    private final char buildingId;
    private final int elevatorId;

    private final RequestQueue requests;
    private ArrayList<PersonRequest> inElevator;

    private int curFloor = 1;
    private int dir = 1;
    private long lastOpenTime;
    private boolean isOpen = false;

    private final Strategy strategy;

    public Elevator(RequestQueue requests, int elevatorId, char buildingId, Strategy strategy) {
        this.buildingId = buildingId;
        this.elevatorId = elevatorId;

        this.requests = requests;
        this.inElevator = new ArrayList<>();

        this.strategy = strategy;
    }

    @Override
    public void run() {
        while (!(requests.isEnd() && requests.isEmpty() && inElevator.isEmpty())) {
            synchronized (requests) {
                if (requests.isEmpty() && inElevator.isEmpty() && !requests.isEnd()) {
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

    public void execute() {
        if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
            executeOpen();
        }
        dir = strategy.whichDir(inElevator, dir, curFloor);
        if (strategy.whetherToOpen(inElevator, dir, curFloor)) {
            executeOpen();
        }
        if (isOpen) {
            executeClose();
        }
    }

    public void executeClose() {
        while (System.currentTimeMillis() - lastOpenTime < 400) {
            synchronized (requests) {
                if (System.currentTimeMillis() - lastOpenTime >= 400) {
                    break;
                }
                try {
                    requests.wait(400 - (System.currentTimeMillis() - lastOpenTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pickUp();
            }
        }
        close();
    }

    public void executeOpen() {
        open();
        dropOff();
        pickUp();
    }

    public void pickUp() {
        ArrayList<PersonRequest> curFloorRequests =
                strategy.whoToPickUp(dir, curFloor);
        while (inElevator.size() < 6 && !curFloorRequests.isEmpty()) {
            PersonRequest request = curFloorRequests.get(0);
            inElevator.add(request);
            in(request);
            requests.removeRequest(request);
            curFloorRequests.remove(0);
        }
    }

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

    public void moveTo() {
        if (strategy.whetherToMove(inElevator)) {
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dir = strategy.whichDir(inElevator, dir, curFloor);
            curFloor += dir;
            arrive();
        }
    }

    public void open() {
        if (!isOpen) {
            isOpen = true;
            lastOpenTime = MyOutPut.myPrintln("OPEN-" + buildingId +
                    "-" + curFloor + "-" + elevatorId);
        }
    }

    public void close() {
        if (isOpen) {
            isOpen = false;
            MyOutPut.myPrintln("CLOSE-" + buildingId + "-" + curFloor + "-" + elevatorId);
        }
    }

    public void arrive() {
        MyOutPut.myPrintln("ARRIVE-" + buildingId + "-" + curFloor + "-" + elevatorId);
    }

    public void in(PersonRequest request) {
        MyOutPut.myPrintln("IN-" + request.getPersonId() +
                "-" + buildingId + "-" + curFloor + "-" + elevatorId);
    }

    public void out(PersonRequest request) {
        MyOutPut.myPrintln("OUT-" + request.getPersonId() +
                "-" + buildingId + "-" + curFloor + "-" + elevatorId);
    }
}

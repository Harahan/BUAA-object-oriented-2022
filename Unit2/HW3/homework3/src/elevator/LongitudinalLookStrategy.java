package elevator;

import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class LongitudinalLookStrategy implements Strategy {
    private final RequestQueue requests;

    private final int capacity;

    public LongitudinalLookStrategy(RequestQueue requests, int capacity) {
        this.requests = requests;
        this.capacity = capacity;
    }

    private int getDir(int x, int dir, int curFloor) {
        return (x - curFloor) * dir;
    }

    private int compare(PersonRequest a, PersonRequest b, int dir, int curFloor) {
        int dirA = getDir(a.getToFloor(), dir, curFloor);
        int dirB = getDir(b.getToFloor(), dir, curFloor);
        return Math.abs(dirB) - Math.abs(dirA);
    }

    private boolean hasTheSameFromDir(ArrayList<PersonRequest> requests,
                                    int dir, int curFloor) {
        for (PersonRequest request : requests) {
            if (getDir(request.getFromFloor(), dir, curFloor) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<PersonRequest> whoToPickUp(ArrayList<PersonRequest> inElevator,
                                                int dir, int curFloor) {
        ArrayList<PersonRequest> curFloorRequests = new ArrayList<>();
        synchronized (requests) {
            for (PersonRequest request : requests.getRequests()) {
                if (request.getFromFloor() == curFloor
                        && getDir(request.getToFloor(), dir, curFloor) > 0) {
                    curFloorRequests.add(request);
                }
            }
        }
        curFloorRequests.sort((a, b) -> compare(a, b, dir, curFloor));
        return curFloorRequests;
    }

    @Override
    public int whichDir(ArrayList<PersonRequest> inElevator, int dir, int curFloor) {
        if (curFloor == 1) {
            return 1;
        } else if (curFloor == 10) {
            return -1;
        }
        synchronized (requests) {
            if (inElevator.isEmpty() &&
                    !hasTheSameFromDir(requests.getRequests(), dir, curFloor)) {
                return -dir;
            }
        }
        return dir;
    }

    @Override
    public boolean whetherToMove(ArrayList<PersonRequest> inElevator) {
        return !(requests.isEmpty() && inElevator.isEmpty());
    }

    @Override
    public boolean whetherToOpen(ArrayList<PersonRequest> inElevator, int dir, int curFloor) {
        for (PersonRequest request : inElevator) {
            if (request.getToFloor() == curFloor) {
                return true;
            }
        }
        if (inElevator.size() == capacity) {
            return false;
        }
        synchronized (requests) {
            for (PersonRequest request : requests.getRequests()) {
                if (request.getFromFloor() == curFloor &&
                        getDir(request.getToFloor(), dir, curFloor) > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}

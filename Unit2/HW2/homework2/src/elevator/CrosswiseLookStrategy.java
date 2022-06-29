package elevator;

import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class CrosswiseLookStrategy implements Strategy {
    private final RequestQueue requests;

    public CrosswiseLookStrategy(RequestQueue requests) {
        this.requests = requests;
    }

    private int getDir(int x, int dir, int curBuilding) {
        int length = Math.abs(curBuilding - x);
        int p = (5 - length) * dir;
        int q = length * dir;
        return (length >= 3) ? ((curBuilding > x) ? p : -p) : ((curBuilding > x) ? -q : q);
    }

    private int compare(PersonRequest a, PersonRequest b, int dir, int curBuilding) {
        int dirA = getDir(a.getToBuilding() - 'A', dir, curBuilding);
        int dirB = getDir(b.getToBuilding() - 'A', dir, curBuilding);
        return Math.abs(dirB) - Math.abs(dirA);
    }

    private boolean hasTheSameFromBuilding(ArrayList<PersonRequest> requests,
                                           int dir, int curBuilding) {
        for (PersonRequest request : requests) {
            if (getDir(request.getFromBuilding() - 'A', dir, curBuilding) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<PersonRequest> whoToPickUp(ArrayList<PersonRequest> inElevator,
                                                int dir, int curBuilding) {
        ArrayList<PersonRequest> curBuildingRequests = new ArrayList<>();
        synchronized (requests) {
            for (PersonRequest request : requests.getRequests()) {
                if (request.getFromBuilding() - 'A' == curBuilding
                        && getDir(request.getToBuilding() - 'A', dir, curBuilding) > 0) {
                    curBuildingRequests.add(request);
                }
            }
            if (inElevator.isEmpty() && curBuildingRequests.isEmpty()) {
                for (PersonRequest request : requests.getRequests()) {
                    if (request.getFromBuilding() - 'A' == curBuilding) {
                        curBuildingRequests.add(request);
                    }
                }
            }
        }
        curBuildingRequests.sort((a, b) -> compare(a, b, dir, curBuilding));
        return curBuildingRequests;
    }

    @Override
    public int whichDir(ArrayList<PersonRequest> inElevator, int dir, int curBuilding) {
        synchronized (requests) {
            if (inElevator.isEmpty() &&
                    !hasTheSameFromBuilding(requests.getRequests(), dir, curBuilding)) {
                return -dir;
            }
        }
        if (!inElevator.isEmpty()) {
            return (getDir(inElevator.get(0).getToBuilding() - 'A',
                    dir, curBuilding) > 0) ? dir : -dir;
        }
        return dir;
    }

    @Override
    public boolean whetherToMove(ArrayList<PersonRequest> inElevator) {
        return !(requests.isEmpty() && inElevator.isEmpty());
    }

    @Override
    public boolean whetherToOpen(ArrayList<PersonRequest> inElevator, int dir, int curBuilding) {
        for (PersonRequest request : inElevator) {
            if (request.getToBuilding() - 'A' == curBuilding) {
                return true;
            }
        }
        if (inElevator.size() == 6) {
            return false;
        }
        synchronized (requests) {
            for (PersonRequest request : requests.getRequests()) {
                if (request.getFromBuilding() - 'A' == curBuilding &&
                        getDir(request.getToBuilding() - 'A', dir, curBuilding) > 0) {
                    return true;
                }
            }
            if (inElevator.isEmpty()) {
                for (PersonRequest request : requests.getRequests()) {
                    if (request.getFromBuilding() - 'A' == curBuilding) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

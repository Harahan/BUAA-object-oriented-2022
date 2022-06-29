import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Scheduler {
    private final HashMap<Character, ArrayList<PersonRequest>> waitDownQueue =  new HashMap<>();
    private final HashMap<Character, ArrayList<PersonRequest>> waitUpQueue = new HashMap<>();
    private boolean readEnd;

    public Scheduler() {
        this.readEnd = false;
        for (int i = 0; i < 5; i++) {
            waitDownQueue.put((char) ('A' + i), new ArrayList<>());
            waitUpQueue.put((char) ('A' + i), new ArrayList<>());
        }
    }

    public synchronized PersonRequest getForwardReq(Character building, int nowFloor) {
        if (waitDownQueue.get(building).isEmpty()) {
            return null;
        }
        PersonRequest minDownReq = null;
        // 先扫描所在楼层之上
        for (PersonRequest personRequest : waitDownQueue.get(building)) {
            if (personRequest.getFromFloor() < nowFloor) {
                continue;
            }
            if (minDownReq == null || personRequest.getFromFloor() < minDownReq.getFromFloor()) {
                minDownReq = personRequest;
            }
        }
        // 所在楼层已没有请求， 扫描所在楼层之下
        if (minDownReq == null) {
            for (PersonRequest personRequest : waitDownQueue.get(building)) {
                if (personRequest.getFromFloor() < nowFloor) {
                    if (minDownReq == null ||
                            personRequest.getFromFloor() > minDownReq.getFromFloor()) {
                        minDownReq = personRequest;
                    }
                }
            }
        }
        return minDownReq;
    }

    public synchronized PersonRequest getBackwardReq(Character building, int nowFloor) {
        if (waitUpQueue.get(building).isEmpty()) {
            return null;
        }
        PersonRequest maxUpReq = null;
        for (PersonRequest personRequest : waitUpQueue.get(building)) {
            if (personRequest.getFromFloor() > nowFloor) {
                continue;
            }
            if (maxUpReq == null || personRequest.getFromFloor() > maxUpReq.getFromFloor()) {
                maxUpReq = personRequest;
            }
        }
        if (maxUpReq == null) {
            for (PersonRequest personRequest : waitUpQueue.get(building)) {
                if (personRequest.getFromFloor() > nowFloor) {
                    if (maxUpReq == null
                            || personRequest.getFromFloor() < maxUpReq.getFromFloor()) {
                        maxUpReq = personRequest;
                    }
                }
            }
        }
        return maxUpReq;
    }

    public synchronized ArrayList<PersonRequest> getInRequest(Character building,
                                                              int nowFloor, int size) {
        ArrayList<PersonRequest> passengersCanIn = new ArrayList<>();
        int nowSize = size;
        for (PersonRequest personRequest : waitDownQueue.get(building)) {
            if (nowSize >= 6) {
                break;
            }
            if (personRequest.getFromFloor() == nowFloor) {
                passengersCanIn.add(personRequest);
                nowSize++;
            }
        }
        for (PersonRequest personRequest : passengersCanIn) {
            waitDownQueue.get(building).remove(personRequest);
        }
        for (PersonRequest personRequest : waitUpQueue.get(building)) {
            if (nowSize >= 6) {
                break;
            }
            if (personRequest.getFromFloor() == nowFloor) {
                passengersCanIn.add(personRequest);
                nowSize++;
            }
        }
        for (PersonRequest personRequest : passengersCanIn) {
            waitUpQueue.get(building).remove(personRequest);
        }
        return passengersCanIn;
    }

    public synchronized boolean waitForReq(Character building, int nowSize) {
        boolean flag = false;
        if (waitUpQueue.get(building).size() + waitDownQueue.get(building).size() <= 1
                && nowSize == 0 && !isReadEnd()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!waitUpQueue.get(building).isEmpty()
                || !waitDownQueue.get(building).isEmpty() || nowSize != 0) {
            flag = true;
        }
        return flag;
    }

    public synchronized void addRequest(PersonRequest personRequest) {
        if (personRequest.getFromFloor() > personRequest.getToFloor()) {
            waitDownQueue.get(personRequest.getFromBuilding()).add(personRequest);
        } else {
            waitUpQueue.get(personRequest.getFromBuilding()).add(personRequest);
        }
        notifyAll();
    }

    public synchronized void setReadEnd(boolean readEnd) {
        notifyAll();
        this.readEnd = readEnd;
    }

    public synchronized boolean isEmpty(Character building) {
        return waitDownQueue.get(building).isEmpty() && waitUpQueue.get(building).isEmpty();
    }

    public synchronized boolean isReadEnd() {
        return readEnd;
    }
}

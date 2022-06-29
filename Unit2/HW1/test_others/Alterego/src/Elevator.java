import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Elevator extends Thread {
    private final char building;
    private final int number;
    private final Scheduler scheduler;
    private final int id;
    private final ArrayList<PersonRequest> passengers = new ArrayList<>();
    private int nowFloor;
    private int patten;
    private final int maxPassengers;
    private final Output outputMsg;

    public Elevator(char building, int number, Output outputMsg, Scheduler scheduler) {
        this.building = building;
        this.number = number;
        this.outputMsg = outputMsg;
        this.scheduler = scheduler;
        this.patten = 0;
        this.maxPassengers = 6;
        this.id = building - 'A' + 1;
        setNowFloor(1);
    }

    @Override
    public void run() {
        while (true) {
            if (scheduler.isReadEnd() && scheduler.isEmpty(building) && passengers.isEmpty()) {
                return;
            }
            if (!scheduler.isReadEnd()
                    && !scheduler.waitForReq(building, getNowSize())) {
                continue;
            }
            PersonRequest nextDown = scheduler.getForwardReq(building, nowFloor);
            PersonRequest nextUp = scheduler.getBackwardReq(building, nowFloor);
            // 处理乘客
            if (((patten == 0 && nextDown == null) || (patten == 1 && nextUp == null))
                    && !passengers.isEmpty() || getNowSize() == maxPassengers) {
                dealHavePassengers();
                if (passengers.size() == 0) {
                    patten = (patten == 1) ? 0 : 1;
                }
                continue;
            }
            // 处理
            if (patten == 0) {
                if (nextDown == null) {     // 同方向没有请求了
                    if (passengers.isEmpty()) {
                        patten = 1;
                    }
                } else {
                    getOnePassenger(nextDown);
                }
            }
            else if (patten == 1) {
                if (nextUp == null) {     // 同方向没有请求了
                    if (passengers.isEmpty()) {
                        patten = 0;
                    }
                } else {
                    getOnePassenger(nextUp);
                }
            }
        }
    }

    public void getOnePassenger(PersonRequest passenger) {
        int fromFloor = passenger.getFromFloor();
        int nextFloor = fromFloor > nowFloor ? nowFloor + 1 : nowFloor - 1;
        if (nowFloor != fromFloor) {
            addArriveMsg(nextFloor);
            nowFloor = nextFloor;
        }
        ArrayList<PersonRequest> passengerOut = outPassenger();
        ArrayList<PersonRequest> passengerIn =
                scheduler.getInRequest(building, nowFloor, getNowSize());
        if (passengerIn.isEmpty() && passengerOut.isEmpty()) {
            return;
        }
        addOpenAndCloseMsg(passengerIn, passengerOut);
        passengers.addAll(passengerIn);
    }

    public void dealHavePassengers() {
        PersonRequest mainReq = getMinTime();
        int des = mainReq.getToFloor();
        int nextFloor = des > nowFloor ? nowFloor + 1 : nowFloor - 1;
        if (nowFloor != des) {
            addArriveMsg(nextFloor);
            nowFloor = nextFloor;
        }
        ArrayList<PersonRequest> passengerOut = outPassenger();
        ArrayList<PersonRequest> passengerIn =
                scheduler.getInRequest(building, nowFloor, getNowSize());
        if (passengerIn.isEmpty() && passengerOut.isEmpty()) {
            return;
        }
        addOpenAndCloseMsg(passengerIn, passengerOut);
        passengers.addAll(passengerIn);
    }

    public ArrayList<PersonRequest> outPassenger() {
        ArrayList<PersonRequest> out = new ArrayList<>();
        for (PersonRequest personRequest : passengers) {
            if (personRequest.getToFloor() == nowFloor) {
                out.add(personRequest);
            }
        }
        for (PersonRequest personRequest : out) {
            passengers.remove(personRequest);
        }
        return out;
    }

    public void addArriveMsg(int nextFloor) {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputMsg.output("ARRIVE-" + building + "-" + nextFloor + "-" + id);
    }

    public void addOpenAndCloseMsg(ArrayList<PersonRequest> passengersIn,
                                   ArrayList<PersonRequest> passengersOut) {
        outputMsg.output("OPEN-" + building + "-" + nowFloor + "-" + id);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputMsg.outputInAndOut(nowFloor, building, passengersIn, passengersOut);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputMsg.output("CLOSE-" + building + "-" + nowFloor + "-" + id);
    }

    public PersonRequest getMinTime() {
        if (passengers.size() < 1) {
            return null;
        }
        PersonRequest mainRequest = passengers.get(0);
        for (PersonRequest request : passengers) {
            int begin = request.getFromFloor();
            int end = request.getToFloor();
            if (Math.abs(begin - end) <
                    Math.abs(mainRequest.getFromFloor() - mainRequest.getToFloor())) {
                mainRequest = request;
            }
        }
        return mainRequest;
    }

    public void setNowFloor(int nowFloor) {
        this.nowFloor = nowFloor;
    }

    public int getNowSize() {
        return this.passengers.size();
    }
}

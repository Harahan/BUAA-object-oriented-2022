import com.oocourse.TimableOutput;

import java.util.Vector;

public class Elevator extends Thread {
    private char building;
    private int id;
    private int floor;
    private RequestControl requestControl;
    private Vector<Request> requestsInControl;
    private Vector<Request> requestsInElevator;
    private Request mainRequest;
    private int personNum;
    private Object obj;
    private Object outputLock;

    public Elevator(char building, int id, RequestControl requestControl,
                    Object obj, Object outputLock) {
        this.building = building;
        this.id = id;
        this.requestControl = requestControl;
        this.floor = 1;
        this.requestsInElevator = new Vector<>();
        this.mainRequest = null;
        this.personNum = 0;
        this.obj = obj;
        this.outputLock = outputLock;
        if (this.building == 'A') {
            this.requestsInControl = requestControl.getRequestsForA();
        }
        else if (this.building == 'B') {
            this.requestsInControl = requestControl.getRequestsForB();
        }
        else if (this.building == 'C') {
            this.requestsInControl = requestControl.getRequestsForC();
        }
        else if (this.building == 'D') {
            this.requestsInControl = requestControl.getRequestsForD();
        }
        else if (this.building == 'E') {
            this.requestsInControl = requestControl.getRequestsForE();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (noRequest() && !requestControl.isHasNewRequest()) {
                return;
            }
            else {
                mainRequest = findMainRequest();
                if (mainRequest == null) {
                    continue;
                }
                else if (mainRequest.getStatus() == 0) {
                    if (mainRequest.getFromFloor() != floor) {
                        moveOneFloor(mainRequest.getFromFloor());
                    }
                    else {
                        letPersonIn();
                    }
                }
                else if (mainRequest.getStatus() == 1) {
                    if (mainRequest.getToFloor() != floor) {
                        moveOneFloor(mainRequest.getToFloor());
                    }
                    else {
                        letPersonOut();
                    }
                }
            }
        }
    }

    public boolean noRequest() {
        synchronized (requestsInControl) {
            for (Request request : requestsInControl) {
                if (request.getStatus() == 0) {
                    return false;
                }
            }
        }

        synchronized (requestsInElevator) {
            for (Request request : requestsInElevator) {
                if (request.getStatus() == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public Request findMainRequest() {
        synchronized (obj) {
            if (noRequest()) {
                if (requestControl.isHasNewRequest()) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }
        int requestFloor = 999;
        Request bestRequest = mainRequest;
        if (bestRequest == null || bestRequest.getStatus() == 2) {
            requestFloor = 21;
        }
        else if (bestRequest.getStatus() == 0) {
            requestFloor = Math.abs(bestRequest.getFromFloor() - floor);
        }
        else if (bestRequest.getStatus() == 1) {
            requestFloor = Math.abs(bestRequest.getToFloor() - floor);
        }

        if (personNum < 6) {
            synchronized (requestsInControl) {
                for (Request request : requestsInControl) {
                    if (request.getStatus() == 0 &&
                            Math.abs(request.getFromFloor() - floor) < requestFloor) {
                        requestFloor = Math.abs(request.getFromFloor() - floor);
                        bestRequest = request;
                    }
                }
            }
        }
        synchronized (requestsInElevator) {
            for (Request request : requestsInElevator) {
                if (request.getStatus() == 1 &&
                        Math.abs(request.getToFloor() - floor) < requestFloor) {
                    requestFloor = Math.abs(request.getToFloor() - floor);
                    bestRequest = request;
                }
            }
        }

        return bestRequest;
    }

    public void moveOneFloor(int targetFloor) {
        if (targetFloor > floor) {
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            floor++;
            synchronized (outputLock) {
                TimableOutput.println("ARRIVE-" + building + "-" + floor + "-" + id);
            }
        }
        else if (targetFloor < floor) {
            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            floor--;
            synchronized (outputLock) {
                TimableOutput.println("ARRIVE-" + building + "-" + floor + "-" + id);
            }
        }
    }

    public void letPersonIn() {
        synchronized (outputLock) {
            TimableOutput.println("OPEN-" + building + "-" + floor + "-" + id);
        }

        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (outputLock) {
            TimableOutput.println("IN-" + mainRequest.getId() + "-" +
                    building + "-" + floor + "-" + id);
        }

        personNum++;
        mainRequest.setStatus(1);
        requestsInElevator.add(mainRequest);

        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (outputLock) {
            TimableOutput.println("CLOSE-" + building + "-" + floor + "-" + id);
        }
    }

    public void letPersonOut() {
        synchronized (outputLock) {
            TimableOutput.println("OPEN-" + building + "-" + floor + "-" + id);
        }

        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (outputLock) {
            TimableOutput.println("OUT-" + mainRequest.getId() + "-" +
                    building + "-" + floor + "-" + id);
        }

        personNum--;
        mainRequest.setStatus(2);

        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (outputLock) {
            TimableOutput.println("CLOSE-" + building + "-" + floor + "-" + id);
        }
    }
}

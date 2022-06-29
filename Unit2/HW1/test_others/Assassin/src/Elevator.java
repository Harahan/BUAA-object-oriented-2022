import java.util.ArrayList;
import java.util.Iterator;

public class Elevator extends Thread {
    private final int building;
    private int status;
    private int num;
    private int floor;
    private final WaitTable waitTable;
    private final ArrayList<Request> guestQueue;
    private boolean open;//judge the door if open now

    public Elevator(int building, WaitTable waitTable) {
        this.building = building;
        status = 0;
        num = 0;
        floor = 1;
        this.waitTable = waitTable;
        guestQueue = new ArrayList<>();
        open = false;
    }

    @Override
    public void run() {
        while (true) {
            if (waitTable.isEmpty() && waitTable.isEnd() && guestQueue.isEmpty()) {
                break;
            } else {
                if (status != 0) {
                    arrive();
                }
                ArrayList<Request> floorRequest = waitTable.getRequests(floor);
                guestOut();
                guestOn(floorRequest);
                move();
            }
        }
    }

    private void arrive() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OutputThread.getInstance().println("ARRIVE-" + (char) (building + 'A') + "-"
                + floor + "-" + (building + 1));
    }

    private void openDoor() {
        OutputThread.getInstance().println("OPEN-" + (char) (building + 'A') + "-"
                + floor + "-" + (building + 1));
        open = true;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void closeDoor() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OutputThread.getInstance().println("CLOSE-" + (char) (building + 'A') + "-"
                + floor + "-" + (building + 1));
        open = false;
    }

    private void personIn(Request request) {
        OutputThread.getInstance().println("IN-" + request.getId() + "-"
                + (char) (building + 'A') + "-"
                + floor + "-" + (building + 1));
    }

    private void personOut(Request request) {
        OutputThread.getInstance().println("OUT-" + request.getId() + "-"
                + (char) (building + 'A') + "-"
                + floor + "-" + (building + 1));
    }

    private void guestOn(ArrayList<Request> floorRequest) {
        if (!floorRequest.isEmpty()) {
            Iterator<Request> iterator = floorRequest.iterator();
            while (iterator.hasNext()) {
                Request request = iterator.next();
                if (status == 1) {
                    if (request.isUp() && num < 6) {
                        if (!open) {
                            openDoor();
                        }
                        personIn(request);
                        guestQueue.add(request);
                        synchronized (waitTable) {
                            iterator.remove();
                            waitTable.notifyAll();
                        }
                        num++;
                    }
                } else if (status == -1) {
                    if ((!request.isUp()) && num < 6) {
                        if (!open) {
                            openDoor();
                        }
                        personIn(request);
                        guestQueue.add(request);
                        synchronized (waitTable) {
                            iterator.remove();
                            waitTable.notifyAll();
                        }
                        num++;
                    }
                } else if (num < 6 && (findUp() || findDown())) {
                    if (findUp()) {
                        status = 1;
                    } else {
                        status = -1;
                    }
                    if (!open) {
                        openDoor();
                    }
                    personIn(request);
                    guestQueue.add(request);
                    synchronized (waitTable) {
                        iterator.remove();
                        waitTable.notifyAll();
                    }
                    num++;
                }
            }
        }
    }

    private void guestOut() {
        if (!guestQueue.isEmpty()) {
            Iterator<Request> iterator = guestQueue.iterator();
            while (iterator.hasNext()) {
                Request request = iterator.next();
                if (request.getEndFloor() == floor) {
                    if (!open) {
                        openDoor();
                    }
                    personOut(request);
                    iterator.remove();
                    num--;
                }
            }
        }
    }

    private void move() {
        if (open) {
            closeDoor();
        }
        if (status == 1) {
            if (guestQueue.isEmpty() && (!findUp())) {
                status = 0;
            }
        } else if (status == -1) {
            if (guestQueue.isEmpty() && (!findDown())) {
                status = 0;
            }
        } else {
            if (waitTable.isEmpty() && !waitTable.isEnd()) {
                synchronized (waitTable) {
                    try {
                        waitTable.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (findUp()) {
                status = 1;
            } else if (findDown()) {
                status = -1;
            }
        }
        floor = floor + status;
    }

    private boolean findUp() {
        for (int i = floor; i <= 10; i++) {
            ArrayList<Request> nextRequests = waitTable.getRequests(i);
            if (i == floor) {
                for (Request request : nextRequests) {
                    if (request.isUp()) {
                        return true;
                    }
                }
            } else if (!nextRequests.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean findDown() {
        for (int i = floor; i >= 1; i--) {
            ArrayList<Request> nextRequests = waitTable.getRequests(i);
            if (i == floor) {
                for (Request request : nextRequests) {
                    if (!request.isUp()) {
                        return true;
                    }
                }
            } else if (!nextRequests.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}

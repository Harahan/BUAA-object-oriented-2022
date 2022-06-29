package control;

import com.oocourse.elevator3.PersonRequest;
import elevator.MyPersonRequest;
import elevator.RequestQueue;

public class Controller extends Thread {
    private int personNum = 0;
    private int finishNum = 0;
    private boolean inputEnd = false;

    private final LongitudinalDispatcher[] longitudinalDispatcher = new LongitudinalDispatcher[5];
    private final CrosswiseDispatcher[] crosswiseDispatcher = new CrosswiseDispatcher[10];
    private static final RequestQueue WAITING_QUEUE = new RequestQueue();
    private static final Controller INSTANCE = new Controller();

    private Controller() {
        int i = 0;
        for (; i < 5; i++) {
            longitudinalDispatcher[i] = new LongitudinalDispatcher(i);
        }
        for (; i < 15; i++) {
            crosswiseDispatcher[i - 5] = new CrosswiseDispatcher(i - 4);
        }
    }

    public static Controller getInstance() {
        return INSTANCE;
    }

    private int getTag(PersonRequest request, int floor) {
        return Math.abs(request.getFromFloor() - floor) + Math.abs(request.getToFloor() - floor);
    }

    public int getTransferFloor(PersonRequest request) {
        int tag = 2147483647;
        int tmpTag;
        int floor = 1;
        for (int i = 0; i < 10; i++) {
            if (!crosswiseDispatcher[i].getValidQueues(request).isEmpty() &&
                    (tmpTag = getTag(request, i + 1)) < tag) {
                tag = tmpTag;
                floor = i + 1;
            }
        }
        return floor;
    }

    public boolean whetherToTransfer(PersonRequest request) {
        return request.getFromBuilding() != request.getToBuilding()
                && (request.getFromFloor() != request.getToFloor()
                || crosswiseDispatcher[request.getToFloor() - 1].
                getValidQueues(request).isEmpty());
    }

    @Override
    public void run() {
        currentThread().setName("Controller");
        while (!(WAITING_QUEUE.isEnd() && WAITING_QUEUE.isEmpty())) {
            synchronized (WAITING_QUEUE) {
                while (WAITING_QUEUE.isEmpty() && !WAITING_QUEUE.isEnd()) {
                    try {
                        WAITING_QUEUE.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (WAITING_QUEUE.isEnd() && WAITING_QUEUE.isEmpty()) {
                    break;
                }
            }
            MyPersonRequest personRequest = (MyPersonRequest) WAITING_QUEUE.getRequests().get(0);
            if (whetherToTransfer(personRequest)) {
                personRequest.setRealPath(getTransferFloor(personRequest));
            }
            allocatePerson(personRequest);
            WAITING_QUEUE.removeRequest(personRequest);
        }
        setDispatcherEnd(true);
    }

    public void allocatePerson(MyPersonRequest request) {
        if (request.getFromBuilding() == request.getToBuilding()) {
            longitudinalDispatcher[request.getToBuilding() - 'A'].addPersonRequest(request);
        } else {
            crosswiseDispatcher[request.getToFloor() - 1].addPersonRequest(request);
        }
    }

    public void setInputEnd(boolean end) {
        inputEnd = end;
        waitingQueueSetEnd(true);
    }

    public void setDispatcherEnd(boolean end) {
        for (int i = 0; i < 10; i++) {
            crosswiseDispatcher[i].setEnd(end);
        }
        for (int i = 0; i < 5; i++) {
            longitudinalDispatcher[i].setEnd(end);
        }
    }

    public void addPersonNum() {
        personNum += 1;
    }

    public RequestQueue getWaitingQueue() {
        return WAITING_QUEUE;
    }

    public synchronized void addFinishNum() {
        finishNum += 1;
        waitingQueueSetEnd(true);
    }

    public void waitingQueueSetEnd(boolean end) {
        if (inputEnd && finishNum == personNum) {
            WAITING_QUEUE.setEnd(end);
        }
    }

    public CrosswiseDispatcher[] getCrosswiseDispatcher() {
        return crosswiseDispatcher;
    }

    public LongitudinalDispatcher[] getLongitudinalDispatcher() {
        return longitudinalDispatcher;
    }
}

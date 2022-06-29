import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import elevator.CrosswiseElevator;

import elevator.LongitudinalElevator;
import elevator.RequestQueue;

import java.util.ArrayList;

public class Dispatcher {
    /**
     * type:
     *      0 ~ 4 ---> longitudinal dispatcher
     *      5 ~ 14 ---> crosswise dispatcher
     *
     * strategy:
     *      1 ---> average allocation
     *      2 ---> free competition
     */
    private final int type;
    private final int strategyType;
    private final ArrayList<RequestQueue> requestQueues = new ArrayList<>();
    private final RequestQueue requestQueue = new RequestQueue();
    private int allocatorRecorder = 0;

    public Dispatcher(int type, int strategyType) {
        this.type = type;
        this.strategyType = strategyType;
        if (type < 5) {
            LongitudinalElevator tmpElevator;
            if (strategyType == 1) {
                RequestQueue tmpQueue = new RequestQueue();
                tmpElevator =
                        new LongitudinalElevator(tmpQueue, type + 1, (char) (type + 'A'));
                requestQueues.add(tmpQueue);
            } else {
                tmpElevator =
                        new LongitudinalElevator(requestQueue, type + 1, (char) (type + 'A'));
            }
            tmpElevator.start();
        }
    }

    public void averageStrategy(PersonRequest request) {
        requestQueues.get(allocatorRecorder).addRequest(request);
        allocatorRecorder = (allocatorRecorder + 1) % requestQueues.size();
    }

    public void addPersonRequest(PersonRequest request) {
        if (strategyType == 1) {
            averageStrategy(request);
        } else {
            requestQueue.addRequest(request);
        }
    }

    public void addElevatorRequest1(ElevatorRequest request) {
        RequestQueue tmpQueue = new RequestQueue();
        int id = request.getElevatorId();
        if (type < 5) {
            LongitudinalElevator tmpElevator =
                    new LongitudinalElevator(tmpQueue, id, (char) (type + 'A'));
            requestQueues.add(tmpQueue);
            tmpElevator.start();
        } else {
            CrosswiseElevator tmpElevator =
                    new CrosswiseElevator(tmpQueue, id, type - 4);
            requestQueues.add(tmpQueue);
            tmpElevator.start();
        }
    }

    public void addElevatorRequest2(ElevatorRequest request) {
        int id = request.getElevatorId();
        if (type < 5) {
            LongitudinalElevator tmpElevator =
                    new LongitudinalElevator(requestQueue, id, (char) (type + 'A'));
            tmpElevator.start();
        } else {
            CrosswiseElevator tmpElevator =
                    new CrosswiseElevator(requestQueue, id, type - 4);
            tmpElevator.start();
        }
    }

    public void addElevatorRequest(ElevatorRequest request) {
        if (strategyType == 1) {
            addElevatorRequest1(request);
        } else {
            addElevatorRequest2(request);
        }
    }

    public void setEnd(boolean end) {
        if (strategyType == 1) {
            for (RequestQueue requestQueue : requestQueues) {
                requestQueue.setEnd(end);
            }
        } else {
            requestQueue.setEnd(end);
        }
    }
}
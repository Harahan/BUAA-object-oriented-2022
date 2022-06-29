package control;

import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import elevator.LongitudinalElevator;
import elevator.RequestQueue;

public class LongitudinalDispatcher implements Dispatcher {
    private final int type;
    private final RequestQueue requestQueue = new RequestQueue();

    /**
     * @param type 0~4 ---> A, B, C, D, E
     */
    public LongitudinalDispatcher(int type) {
        this.type = type;
        LongitudinalElevator tmpElevator = new LongitudinalElevator(requestQueue,
                type + 1, (char) (type + 'A'), 8, 0.6);
        tmpElevator.start();
    }

    @Override
    public void addPersonRequest(PersonRequest request) {
        requestQueue.addRequest(request);
    }

    @Override
    public void addElevatorRequest(ElevatorRequest request) {
        LongitudinalElevator tmpElevator =
                new LongitudinalElevator(requestQueue, request.getElevatorId(),
                        (char) (type + 'A'), request.getCapacity(), request.getSpeed());
        tmpElevator.start();

    }

    @Override
    public void setEnd(boolean end) {
        requestQueue.setEnd(end);
    }
}

package elevator;

import com.oocourse.elevator3.PersonRequest;
import control.Controller;

public interface Elevator {

    void execute();

    void executeClose();

    void executeOpen();

    void pickUp();

    void dropOff();

    void moveTo();

    default void transfer(PersonRequest request) {
        MyPersonRequest person = (MyPersonRequest) request;
        person.addCnt();
        if (!person.hasArrived()) {
            // System.out.println(person);
            Controller.getInstance().getWaitingQueue().addRequest(person);
        } else {
            Controller.getInstance().addFinishNum();
        }
    }

    /**
     * OUTPUT:
     *      open, close, arrive, in, out
     */
    void open();

    void close();

    void arrive();

    void in(PersonRequest request);

    void out(PersonRequest request);
}

package elevator;

import com.oocourse.elevator2.PersonRequest;

public interface Elevator {

    void execute();

    void executeClose();

    void executeOpen();

    void pickUp();

    void dropOff();

    void moveTo();

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

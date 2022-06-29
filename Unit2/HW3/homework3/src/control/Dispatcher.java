package control;

import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;

public interface Dispatcher {
    void addPersonRequest(PersonRequest request);

    void addElevatorRequest(ElevatorRequest request);

    void setEnd(boolean end);
}

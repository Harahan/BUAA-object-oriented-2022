package elevator;

import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public interface Strategy {
    ArrayList<PersonRequest> whoToPickUp(ArrayList<PersonRequest> inElevator, int dir, int cur);

    int whichDir(ArrayList<PersonRequest> inElevator, int dir, int cur);

    boolean whetherToOpen(ArrayList<PersonRequest> inElevator, int dir, int cur);

    boolean whetherToMove(ArrayList<PersonRequest> inElevator);
}

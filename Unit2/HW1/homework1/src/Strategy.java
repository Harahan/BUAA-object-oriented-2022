import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public interface Strategy {
    ArrayList<PersonRequest> whoToPickUp(int dir, int curFloor);

    int whichDir(ArrayList<PersonRequest> inElevator, int dir, int curFloor);

    boolean whetherToOpen(ArrayList<PersonRequest> inElevator, int dir, int curFloor);

    boolean whetherToMove(ArrayList<PersonRequest> inElevator);
}

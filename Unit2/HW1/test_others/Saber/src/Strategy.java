import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public interface Strategy {
    public int updateDestination(int position, int direction,
                                 ArrayList<PersonRequest> insidePerson,
                                 RequestTable processQueue);

    public int updateDirection(int position, int direction,
                               ArrayList<PersonRequest> insidePerson,
                               RequestTable processQueue);

    public boolean toPick(int position, int direction, int num, PersonRequest pr,
                          RequestTable processQueue);
}

import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Output {

    public Output() {
    }

    public synchronized void output(String msg) {
        TimableOutput.println(msg);
    }

    public synchronized void outputInAndOut(int nowFloor, Character building,
                                            ArrayList<PersonRequest> in,
                                            ArrayList<PersonRequest> out) {
        int id = building - 'A' + 1;
        for (PersonRequest item : out) {
            TimableOutput.println("OUT-" + item.getPersonId()
                    + "-" + building + "-" + nowFloor + "-" + id);
        }
        for (PersonRequest item : in) {
            TimableOutput.println("IN-" + item.getPersonId() +
                    "-" + building + "-" + nowFloor + "-" + id);
        }

    }
}

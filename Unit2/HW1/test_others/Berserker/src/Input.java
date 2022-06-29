import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class Input extends Thread {
    private Requestlist waitQueue;

    public Input(Requestlist requestlist) {
        this.waitQueue = requestlist;
    }

    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                waitQueue.setEnd(true);
                return;
            } else {
                Person person = new Person(request.getFromFloor(), request.getToFloor(),
                        request.getPersonId(), request.getFromBuilding() - 'A');
                waitQueue.addRequest(person);
            }
        }
    }

}

import java.util.ArrayList;

public class Schedule extends Thread {
    private ArrayList<Elevator> elevators;
    private Requestlist requestlist;

    public void run() {
        while (true) {
            if (requestlist.isEmpty() && requestlist.isEnd()) {
                for (int i = 0; i < elevators.size(); i++) {
                    elevators.get(i).getRequestlist().setEnd(true);
                }
                return;
            }
            Person person = requestlist.getPerson();
            if (person == null) continue;
            int i = person.getEleId();
            elevators.get(i).addRequest(person);
        }
    }

    public Schedule(Requestlist requestlist, ArrayList<Elevator> elevators) {
        this.requestlist = requestlist;
        this.elevators = elevators;
    }
}

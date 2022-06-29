import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class RequestTable {
    private ArrayList<PersonRequest> requests;
    private boolean over;

    public RequestTable() {
        requests = new ArrayList<>();
        over = false;
    }

    public synchronized void addRequest(PersonRequest pr) {
        requests.add(pr);
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized ArrayList<PersonRequest> getRequestTable() {
        return requests;
    }

    public synchronized void sendOver() {
        this.over = true;
        notifyAll();
    }

    public synchronized boolean getOver() {
        return this.over;
    }

}

package elevator;

import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class RequestQueue {
    private final ArrayList<PersonRequest> requests;
    private boolean isEnd;

    public RequestQueue() {
        this.requests = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void setEnd(boolean end) {
        isEnd = end;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized void addRequest(PersonRequest request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized ArrayList<PersonRequest> getRequests() {
        return requests;
    }

    public synchronized void removeRequest(PersonRequest request) {
        requests.removeIf(item -> item.equals(request));
    }

}

import java.util.concurrent.CopyOnWriteArrayList;

public class RequestQueue {
    private final CopyOnWriteArrayList<Request> requests;
    private boolean isEnd;

    public RequestQueue() {
        requests = new CopyOnWriteArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(Request request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized Request getOneRequest() {
        if (!isEnd && this.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        Request request = requests.get(0);
        requests.remove(0);
        notifyAll();
        return request;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean getIsEnd() {
        notifyAll();
        return this.isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }

    public synchronized CopyOnWriteArrayList<Request> getRequests() {
        notifyAll();
        return requests;
    }

    public synchronized void waitElevator() {
        if (!isEnd && this.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
    }
}

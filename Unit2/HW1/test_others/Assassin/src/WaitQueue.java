import java.util.ArrayList;

public class WaitQueue {
    private final ArrayList<Request> waitQueue;
    private boolean isEnd;

    public WaitQueue() {
        waitQueue = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(Request request) {
        this.waitQueue.add(request);
        notifyAll();
    }

    public synchronized Request getRequest() {
        if (waitQueue.isEmpty() && !isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (waitQueue.isEmpty()) {
            return null;
        }
        Request request = waitQueue.get(0);
        waitQueue.remove(0);
        notifyAll();
        return request;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return waitQueue.isEmpty();
    }
}

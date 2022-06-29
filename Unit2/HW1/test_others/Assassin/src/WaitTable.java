import java.util.ArrayList;
import java.util.HashMap;

public class WaitTable {
    private HashMap<Integer, ArrayList<Request>> waitRequests;
    private boolean isEnd;

    public WaitTable() {
        HashMap<Integer, ArrayList<Request>> waitRequests = new HashMap<>();
        for (int i = 0; i <= 10; i++) {
            ArrayList<Request> floorRequests = new ArrayList<>();
            waitRequests.put(i, floorRequests);
        }
        this.waitRequests = waitRequests;
        this.isEnd = false;
    }

    public synchronized void addRequest(Request request) {
        ArrayList<Request> floorRequests = waitRequests.get(request.getBeginFloor());
        floorRequests.add(request);
        notifyAll();
    }

    public synchronized ArrayList<Request> getRequests(int floor) {
        notifyAll();
        return waitRequests.get(floor);
    }

    public synchronized boolean isEmpty() {
        for (int i = 1; i <= 10; i++) {
            if (!waitRequests.get(i).isEmpty()) {
                notifyAll();
                return false;
            }
        }
        notifyAll();
        return true;
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return this.isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }
}

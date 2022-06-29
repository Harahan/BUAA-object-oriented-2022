import java.util.ArrayList;

public class Dispatcher extends Thread {
    private final WaitQueue waitQueue;
    private final ArrayList<WaitTable> waitTables;

    public Dispatcher(WaitQueue waitQueue, ArrayList<WaitTable> waitTables) {
        this.waitQueue = waitQueue;
        this.waitTables = waitTables;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEnd() && waitQueue.isEmpty()) {
                for (int i = 0; i < waitTables.size(); i++) {
                    waitTables.get(i).setEnd(true);
                }
                return;
            } else {
                Request request = waitQueue.getRequest();
                if (request == null) {
                    continue;
                }
                waitTables.get(request.getBalcony()).addRequest(request);
            }
        }
    }
}

import java.util.ArrayList;

public class Schedule extends Thread {
    private final RequestQueue waitQueue;
    private final ArrayList<RequestQueue> elevatorQueues;

    public Schedule(RequestQueue waitQueue, ArrayList<RequestQueue> elevatorQueues) {
        this.waitQueue = waitQueue;
        this.elevatorQueues = elevatorQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.getIsEnd()) {
                for (int i = 0; i < elevatorQueues.size(); i++) {
                    elevatorQueues.get(i).setEnd(true);
                }
                //是否需要打印调度结束信息
                return;
            }
            Request request = waitQueue.getOneRequest();
            if (request == null) { continue; }
            switch (request.getFromBuilding())
            {
                case 'A':
                    elevatorQueues.get(0).addRequest(request);
                    break;
                case 'B':
                    elevatorQueues.get(1).addRequest(request);
                    break;
                case 'C':
                    elevatorQueues.get(2).addRequest(request);
                    break;
                case 'D':
                    elevatorQueues.get(3).addRequest(request);
                    break;
                case 'E':
                    elevatorQueues.get(4).addRequest(request);
                    break;
                default:
            }
        }
    }
}

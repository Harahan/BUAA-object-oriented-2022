package control;

import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import elevator.CrosswiseElevator;
import elevator.RequestQueue;

import java.util.ArrayList;
import java.util.Comparator;

public class CrosswiseDispatcher implements Dispatcher {
    private final int type;
    private final ArrayList<Integer> switchInfos = new ArrayList<>();
    private final ArrayList<MyPair> requestQueues = new ArrayList<>();

    /**
     * @param type 1~10
     */
    public CrosswiseDispatcher(int type) {
        this.type = type;
        if (type == 1) {
            RequestQueue tmpQueue = new RequestQueue();
            CrosswiseElevator tmpElevator =
                    new CrosswiseElevator(tmpQueue, 6, type, 8, 0.6);
            requestQueues.add(new MyPair(tmpQueue));
            switchInfos.add(31);
            tmpElevator.start();
        }
    }

    @Override
    public void addElevatorRequest(ElevatorRequest request) {
        RequestQueue tmpQueue = new RequestQueue();
        CrosswiseElevator tmpElevator = new CrosswiseElevator(tmpQueue,
                request.getElevatorId(), type, request.getCapacity(), request.getSpeed());
        requestQueues.add(new MyPair(tmpQueue));
        switchInfos.add(request.getSwitchInfo());
        tmpElevator.start();
    }

    public ArrayList<MyPair> getValidQueues(PersonRequest request) {
        ArrayList<MyPair> ret = new ArrayList<>();
        for (int i = 0; i < switchInfos.size(); i++) {
            if (((switchInfos.get(i) >> (request.getToBuilding() - 'A')) & 1) +
                    ((switchInfos.get(i) >> (request.getFromBuilding() - 'A')) & 1) == 2) {
                ret.add(requestQueues.get(i));
            }
        }
        return ret;
    }

    @Override
    public void addPersonRequest(PersonRequest request) {
        ArrayList<MyPair> validQueues = getValidQueues(request);
        validQueues.sort(Comparator.comparingInt(MyPair::getCnt));
        validQueues.get(0).addCnt();
        validQueues.get(0).getRequestQueue().addRequest(request);
    }

    @Override
    public void setEnd(boolean end) {
        for (MyPair myPair : requestQueues) {
            myPair.getRequestQueue().setEnd(end);
        }
    }

    static class MyPair {
        private final RequestQueue requestQueue;
        private int cnt = 0;

        public MyPair(RequestQueue requestQueue) {
            this.requestQueue = requestQueue;
        }

        public int getCnt() {
            return cnt;
        }

        public RequestQueue getRequestQueue() {
            return requestQueue;
        }

        public void addCnt() {
            cnt += 1;
        }
    }

}

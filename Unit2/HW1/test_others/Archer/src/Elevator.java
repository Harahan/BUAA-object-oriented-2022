import java.util.ArrayList;

import com.oocourse.TimableOutput;

public class Elevator extends Thread {
    private final RequestQueue elevatorQueue;//存储在当前电梯中的未进行处理的请求
    private final int serialNumber;
    private int curFloor;//当前所处楼层
    private int direction;//运行方向 1为上行，-1为下行, 0为停止
    private int curCapacity;//当前容量
    private Strategy strategy;//策略
    private ArrayList<Request> curQueue;//目前电梯中的请求
    private boolean isOpen;//是否在当前楼层开门

    public Elevator(RequestQueue elevatorQueue, int serialNumber) {
        this.elevatorQueue = elevatorQueue;
        this.serialNumber = serialNumber;
        this.curFloor = 1;
        this.direction = 0;
        this.curCapacity = 0;
        this.curQueue = new ArrayList<>();
        this.strategy = new Strategy(this.elevatorQueue, this.curQueue);
    }

    @Override
    public void run() {
        while (true) {
            if (elevatorQueue.getIsEnd() && elevatorQueue.isEmpty() && curQueue.isEmpty()) {
                //是否需要输出该电梯进程结束的信息
                return;
            }
            strategy.updateStrategy(curFloor, direction, curCapacity);

            isOpen = strategy.getIsOpen();//是否开门。同时获取出入信息

            if (isOpen) {
                TimableOutput.println("OPEN-" + (char)('A' + serialNumber - 1) +
                        "-" + curFloor + "-" + serialNumber);
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outRequest();
                inRequest();
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TimableOutput.println("CLOSE-" + (char)('A' + serialNumber - 1) +
                        "-" + curFloor + "-" + serialNumber);
            }

            curCapacity = strategy.getNextCapacity();
            direction = strategy.getNextDirection();
            curFloor = strategy.getNextFloor();
            if (direction != 0) {
                try {
                    sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TimableOutput.println("ARRIVE-" + (char)('A' + serialNumber - 1) +
                        "-" + curFloor + "-" + serialNumber);
            }
        }
    }

    public void outRequest() {
        for (Request request : strategy.getOutQueue()) {
            TimableOutput.println("OUT-" + request.getPersonId() + "-" + (char)('A' +
                    serialNumber - 1) + "-" + curFloor + "-" + serialNumber);
            curQueue.remove(request);
        }
    }

    public void inRequest() {
        for (Request request : strategy.getInQueue()) {
            elevatorQueue.getRequests().remove(request);
            TimableOutput.println("IN-" + request.getPersonId() + "-" +
                    (char)('A' + serialNumber - 1) + "-" + curFloor + "-" + serialNumber);
            curQueue.add(request);
        }
    }

}

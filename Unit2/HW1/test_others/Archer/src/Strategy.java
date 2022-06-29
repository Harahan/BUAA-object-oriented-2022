import java.util.ArrayList;

public class Strategy {
    //用来生成策略的参数
    private final RequestQueue elevatorQueue;//存储在当前电梯中的所有请求
    private int curFloor;//当前所处楼层
    private int direction;//运行方向 1为上行，-1为下行
    private int curCapacity;//当前容量
    private final ArrayList<Request> curQueue;//当前电梯中的人
    //返回的策略参数
    private int nextFloor;
    private int nextDirection;
    private int nextCapacity;
    private boolean isOpen;
    private ArrayList<Request> inQueue;//要进电梯的
    private ArrayList<Request> outQueue;//要出电梯的

    public Strategy(RequestQueue elevatorQueue, ArrayList<Request> curQueue) {
        this.elevatorQueue = elevatorQueue;
        this.curQueue = curQueue;
    }

    public void updateStrategy(int curFloor, int direction, int curCapacity) {
        this.curFloor = curFloor;
        this.direction = direction;
        this.curCapacity = curCapacity;
        this.inQueue = new ArrayList<>();
        this.outQueue = new ArrayList<>();
        this.isOpen = false; //默认不开门
    }

    public boolean getIsOpen() {
        //若内部有请求在此楼层下电梯，则将其加入outQueue中
        for (Request request:curQueue) {
            if (request.getToFloor() == curFloor) {
                outQueue.add(request);
                isOpen = true;
                curCapacity = curCapacity - 1;//请求量减一
            }
        }
        //若容量未满且外部请求方向与当前方向相同或当前方向为0，则将其加入
        int judgeDirection = 0;
        for (Request request:elevatorQueue.getRequests()) {
            if (curCapacity < 6) {
                if (curCapacity == 0) {
                    if (judgeDirection == 0 && request.getFromFloor() == curFloor) {
                        judgeDirection = request.getDirection();
                    }
                    if (request.getFromFloor() == curFloor &&
                            request.getDirection() == judgeDirection) {
                        inQueue.add(request);
                        isOpen = true;
                        curCapacity = curCapacity + 1;
                    }
                } else {
                    if (request.getFromFloor() == curFloor &&
                            (direction == 0 || direction == request.getDirection())) {
                        inQueue.add(request);
                        isOpen = true;
                        curCapacity = curCapacity + 1;
                    }
                }
            } else {
                break;
            }
        }
        nextCapacity = curCapacity;
        return isOpen;
    }

    public ArrayList<Request> getOutQueue() { return this.outQueue; }

    public ArrayList<Request> getInQueue() { return this.inQueue; }

    public int getNextDirection() {
        int judge; //0：啥也没有 1：有上方请求 -1：有下方请求
        if (!curQueue.isEmpty()) {
            nextDirection = curQueue.get(0).getDirection();
        } else {
            switch (direction)
            {
                case 0:
                    checkStyle();
                    break;
                case 1:
                    judge = 0;
                    for (Request request:elevatorQueue.getRequests()) {
                        if (request.getFromFloor() > curFloor) {
                            nextDirection = 1;
                            judge = 1;
                            break;
                        }
                    }
                    if (judge == 1) { break; }
                    for (Request request:elevatorQueue.getRequests()) {
                        if (request.getFromFloor() < curFloor) {
                            nextDirection = -1;
                            judge = -1;
                            break;
                        }
                    }
                    if (judge == -1) { break; }
                    nextDirection = 0;
                    break;
                case -1:
                    judge = 0;
                    for (Request request:elevatorQueue.getRequests()) {
                        if (request.getFromFloor() < curFloor) {
                            nextDirection = -1;
                            judge = -1;
                            break;
                        }
                    }
                    if (judge == -1) { break; }
                    for (Request request:elevatorQueue.getRequests()) {
                        if (request.getFromFloor() > curFloor) {
                            nextDirection = 1;
                            judge = 1;
                            break;
                        }
                    }
                    if (judge == 1) { break; }
                    nextDirection = 0;
                    break;
                default:
            }
        }
        if (nextDirection == 0) {
            elevatorQueue.waitElevator();
        }
        nextFloor = curFloor + nextDirection;
        return nextDirection;
    }

    public int getNextFloor() {
        return this.nextFloor;
    }

    public int getNextCapacity() { return this.nextCapacity; }

    public void checkStyle() {
        if (!elevatorQueue.getRequests().isEmpty()) {
            if (elevatorQueue.getRequests().get(0).getFromFloor() > curFloor) {
                nextDirection = 1;
            } else if (elevatorQueue.getRequests().get(0).getFromFloor() < curFloor) {
                nextDirection = -1;
            }
        } else { nextDirection = 0; }
    }
}

import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Elevator implements Runnable {
    private int id;
    private int capacity;
    private int position;
    private int destination;
    private int direction;
    // 初始化的目的地不可能达到
    private ArrayList<PersonRequest> insidePerson;
    private RequestTable processQueue;
    private Strategy strategy;

    public Elevator(int id, RequestTable processQueue, Strategy s) {
        capacity = 6;
        destination = -1;
        position = 1;
        direction = 0;
        // 初始化没有方向
        insidePerson = new ArrayList<>();
        this.id = id;
        this.processQueue = processQueue;
        this.strategy = s;
        //        haveGone = false;
    }

    public void stop() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(400);
    }

    public void putDownPerson() {
        ArrayList<PersonRequest> renew = new ArrayList<>();
        for (PersonRequest pr : insidePerson) {
            if (pr.getToFloor() == position) {
                SafeOutput.println(
                        String.format("OUT-%d-%c-%d-%d", pr.getPersonId(),
                                id + 'A' - 1, position, id));
            } else {
                renew.add(pr);
            }
        }
        insidePerson.clear();
        insidePerson.addAll(renew);
    }

    public void pickUpPerson() {
        ArrayList<PersonRequest> renew = new ArrayList<>();
        synchronized (processQueue) {
            ArrayList<PersonRequest> requestTable = processQueue.getRequestTable();
            for (PersonRequest pr : requestTable) {
                if (pr.getFromFloor() == position && insidePerson.size() <= capacity &&
                        strategy.toPick(position, direction, insidePerson.size(), pr,
                                processQueue)) {
                    insidePerson.add(pr);
                    SafeOutput.println(
                            String.format("IN-%d-%c-%d-%d", pr.getPersonId(),
                                    id + 'A' - 1, position, id));
                } else {
                    renew.add(pr);
                }
            }
            requestTable.clear();
            requestTable.addAll(renew);
        }
    }

    public void move() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(400);
        position += direction;
    }

    public boolean needToMove() {
        if (direction == 0) {
            return false;
            // 待机状态不走
        } else if (position == destination) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmpty() {
        return (processQueue.isEmpty() && insidePerson.isEmpty());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                boolean needtomove;

                synchronized (processQueue) {
                    while (isEmpty()) {
                        processQueue.wait();
                        if (processQueue.getOver()) {
                            break;
                        }
                    }
                }

                if (destination == position && direction != 0) {
                    // 待机状态不执行
                    SafeOutput.println(
                            String.format("OPEN-%c-%d-%d", id + 'A' - 1, position, id));
                    if (!insidePerson.isEmpty()) {
                        putDownPerson();
                    }
                    stop();
                    pickUpPerson();
                    SafeOutput.println(
                            String.format("CLOSE-%c-%d-%d", id + 'A' - 1, position, id));
                }

                synchronized (processQueue) {
                    direction = strategy.updateDirection(position, direction, insidePerson,
                            processQueue);
                    if (direction != 0) {
                        destination = strategy.updateDestination(position, direction,
                                insidePerson, processQueue);
                    }
                    needtomove = needToMove();
                }

                if (needtomove) {
                    move();
                    SafeOutput.println(
                            String.format("ARRIVE-%c-%d-%d", id + 'A' - 1, position, id));
                }
                if (processQueue.getOver() && isEmpty()) {
                    break;
                }

            }
        } catch (InterruptedException e) { /*TODO*/ }
    }
}

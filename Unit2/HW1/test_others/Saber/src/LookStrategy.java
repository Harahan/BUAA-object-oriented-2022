import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class LookStrategy implements Strategy {
    public int updateDestination(int position, int direction,
                                 ArrayList<PersonRequest> insidePerson,
                                 RequestTable processQueue) {
        int rmin = 30;
        int rfromFloor = 0;
        int itoFloor = 0;
        int imin = 30;
        int rmax = -1;
        int distance;
        int myDirection = direction;
        ArrayList<PersonRequest> requestTable = processQueue.getRequestTable();

        if (myDirection == 0) { return -1; }
        // 如果方向为0，说明没起步无所谓
        if (insidePerson.size() != 0) {
            // 如果内部有人，找该方向上的，走同方向的请求上的人和电梯上的请求下的人的最近
            for (PersonRequest pr : requestTable) {
                if ((pr.getFromFloor() - position) * myDirection >= 0 &&
                        (pr.getToFloor() - pr.getFromFloor()) * myDirection > 0) {
                    distance = Math.abs(pr.getFromFloor() - position);
                    if (distance < rmin) {
                        rfromFloor = pr.getFromFloor();
                        rmin = distance;
                    }
                }
            }
            for (PersonRequest pr : insidePerson) {
                if ((pr.getToFloor() - position) * myDirection >= 0) {
                    distance = Math.abs(pr.getToFloor() - position);
                    if (distance < imin) {
                        itoFloor = pr.getToFloor();
                        imin = distance;
                    }
                }
            }
            if (rmin < imin) { return rfromFloor; }
            else { return itoFloor; }
        } else if (insidePerson.size() == 0 && requestTable.size() != 0) {
            // 如果内部没人，请求单有人
            for (int i = 0; i < 2; i++) {
                for (PersonRequest pr : requestTable) {
                    if ((pr.getFromFloor() - position) * myDirection >= 0 &&
                            (pr.getToFloor() - pr.getFromFloor()) * myDirection > 0) {
                        // 先找该方向上，走同方向的想上的最近的人
                        distance = Math.abs(pr.getFromFloor() - position);
                        if (distance < rmin) {
                            rfromFloor = pr.getFromFloor();
                            rmin = distance;
                        }
                    }
                }
                if (rmin != 30) { return rfromFloor; }
                for (PersonRequest pr : requestTable) {
                    // 再找该方向上，走反方向的向上的最远的人
                    if ((pr.getFromFloor() - position) * myDirection >= 0 &&
                            (pr.getToFloor() - pr.getFromFloor()) * myDirection < 0) {
                        distance = Math.abs(pr.getFromFloor() - position);
                        if (distance > rmax) {
                            rfromFloor = pr.getFromFloor();
                            rmax = distance;
                        }
                    }
                }
                if (i == 0) { if (rmax != -1) { return rfromFloor; } }
                else if (i == 1) { return rfromFloor; }
                myDirection = -myDirection; } }

        return -1;
        // Impossible condition;

    }

    public boolean toPick(int position, int direction, int num, PersonRequest pr,
                          RequestTable processQueue) {
        ArrayList<PersonRequest> requestTable = processQueue.getRequestTable();
        if (direction * (pr.getToFloor()  - pr.getFromFloor()) > 0) {
            return true;
            // 如果请求与电梯运行方向相同，则响应
        }

        if (num == 0) {
            // 如果里面没人(且方向相反)
            for (PersonRequest p : requestTable) {
                if (position <= p.getFromFloor() &&
                        direction * (p.getToFloor()  - p.getFromFloor()) > 0) {
                    // 如果申请表里有方向相同的且也高等于这一楼，那么就不能响应这个相反的请求
                    return false;
                }
            }
            // 如果发现表中没有在这一楼且方向与电梯相同的了，那么就响应相反请求
            return true;
        } else {
            return false;
        }
    }

    public int updateDirection(int position, int direction,
                               ArrayList<PersonRequest> insidePerson,
                               RequestTable processQueue) {
        ArrayList<PersonRequest> requestTable = processQueue.getRequestTable();
        int fromFloor;
        int toFloor;
        if (insidePerson.isEmpty() && requestTable.isEmpty()) {
            // 如果两表都空，那么进入待机状态，direction = 0
            return 0;
        }
        if (insidePerson.size() != 0) {
            fromFloor = insidePerson.get(0).getFromFloor();
            toFloor = insidePerson.get(0).getToFloor();
            return (toFloor - fromFloor) / Math.abs(toFloor - fromFloor);
        } else {
            if (direction == 0) {
                // 待机状态的启动点是申请表里开始有人，那么响应最先申请的人
                fromFloor = requestTable.get(0).getFromFloor();
                if (fromFloor == position) {
                    toFloor = requestTable.get(0).getToFloor();
                    return (toFloor - fromFloor) / Math.abs(toFloor - fromFloor);
                }
                return (fromFloor - position) / Math.abs(fromFloor - position);
            }
            for (PersonRequest pr : requestTable) {
                if ((pr.getFromFloor() - position) * direction > 0) {
                    // 申请表中有人在行驶方向上，那么去找他
                    return direction;
                }
            }
            // 没人就反向了。
            return -direction;
        }
    }
}

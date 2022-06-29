package elevator;

import com.oocourse.elevator3.PersonRequest;

public class MyPersonRequest extends PersonRequest {
    private int transferNum;
    private int cnt = 0;
    private int transferFloor;

    public MyPersonRequest(PersonRequest request) {
        super(request.getFromFloor(), request.getToFloor(),
                request.getFromBuilding(), request.getToBuilding(), request.getPersonId());
        transferNum = 1;
    }

    public void setRealPath(int transferFloor) {
        this.transferFloor = transferFloor;
        if (transferFloor != super.getFromFloor() && transferFloor != super.getToFloor()) {
            transferNum = 3;
        } else {
            transferNum = 2;
        }
    }

    public void addCnt() {
        cnt += 1;
    }

    public boolean hasArrived() {
        return cnt == transferNum;
    }

    @Override
    public int getFromFloor() {
        if (cnt == 0) {
            return super.getFromFloor();
        }
        return transferFloor;
    }

    @Override
    public int getToFloor() {
        if (transferNum == cnt + 1) {
            return super.getToFloor();
        }
        return transferFloor;
    }

    @Override
    public char getFromBuilding() {
        if ((transferNum == 2 && transferFloor == super.getFromFloor() && cnt == 1)
                || (transferNum == 3 && cnt == 2)) {
            return super.getToBuilding();
        }
        return super.getFromBuilding();
    }

    @Override
    public char getToBuilding() {
        if ((transferNum == 2 && transferFloor != super.getFromFloor() && cnt == 0)
                || (transferNum == 3 && cnt == 0)) {
            return super.getFromBuilding();
        }
        return super.getToBuilding();
    }

    @Override
    public String toString() {
        return String.format("%d-FROM-%c-%d-TO-%c-%d",
                this.getPersonId(), this.getFromBuilding(),
                this.getFromFloor(), this.getToBuilding(), this.getToFloor());
    }
}

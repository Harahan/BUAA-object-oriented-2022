public class Request {
    private int id;
    private char fromBuilding;
    private int fromFloor;
    private char toBuilding;
    private int toFloor;
    private int status; //0是没进电梯，1是在电梯里，2是已经到达目的地下电梯

    public Request(int id, char fromBuilding, int fromFloor, char toBuilding, int toFloor) {
        this.id = id;
        this.fromBuilding = fromBuilding;
        this.fromFloor = fromFloor;
        this.toBuilding = toBuilding;
        this.toFloor = toFloor;
        this.status = 0;
    }

    public int getId() {
        return id;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public char getToBuilding() {
        return toBuilding;
    }

    public int getToFloor() {
        return toFloor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

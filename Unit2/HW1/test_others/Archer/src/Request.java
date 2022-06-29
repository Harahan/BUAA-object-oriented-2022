public class Request {
    private final int fromFloor;
    private final int toFloor;
    private final char fromBuilding;
    private final char toBuilding;
    private final int personId;
    private final int direction;//1为上行，2为下行
    //private int isSettled; //1表示请求已经被处理，0表示请求未被处理，2表示请求正在被处理（即将进入电梯），进入完毕后该状态变成1

    public Request(int fromFloor, int toFloor, char fromBuilding, char toBuilding, int personId) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.fromBuilding = fromBuilding;
        this.toBuilding = toBuilding;
        this.personId = personId;
        if (toFloor - fromFloor > 0) {
            this.direction = 1;
        } else {
            this.direction = -1;
        }
    }

    public int getFromFloor() { return fromFloor; }

    public int getToFloor() { return toFloor; }

    public char getFromBuilding() { return fromBuilding; }

    public char getToBuilding() { return toBuilding; }

    public int getPersonId() { return personId; }

    public int getDirection() { return direction; }
}

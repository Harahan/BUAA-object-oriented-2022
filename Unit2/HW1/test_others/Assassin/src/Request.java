public class Request {
    private final int id;
    private final int balcony;
    private final int beginFloor;
    private final int endFloor;
    private final boolean up;

    public Request(int id, int balcony, int beginFloor, int endFloor) {
        this.id = id;
        this.balcony = balcony;
        this.beginFloor = beginFloor;
        this.endFloor = endFloor;
        this.up = endFloor > beginFloor;
    }

    public int getId() {
        return id;
    }

    public int getBalcony() {
        return balcony;
    }

    public int getBeginFloor() {
        return beginFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    public boolean isUp() {
        return up;
    }
}

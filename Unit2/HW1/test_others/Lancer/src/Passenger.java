import com.oocourse.elevator1.PersonRequest;

public class Passenger {
    private int fromFloor;
    private int toFloor;
    private char fromBuilding;
    private char toBuilding;
    private int personId;
    private int direction;

    public Passenger(PersonRequest personRequest) {
        this.fromFloor = personRequest.getFromFloor();
        this.toFloor = personRequest.getToFloor();
        this.personId = personRequest.getPersonId();
        this.fromBuilding = personRequest.getFromBuilding();
        this.toBuilding = personRequest.getToBuilding();
        this.direction = (toFloor > fromFloor) ? 1 : -1;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public int getDirection() {
        return direction;
    }

    public int getPersonId() {
        return personId;
    }
}

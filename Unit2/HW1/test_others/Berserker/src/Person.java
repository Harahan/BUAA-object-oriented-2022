public class Person {
    private int leavefloor;
    private int desfloor;
    private int id;
    private int eleId;

    public int getDesfloor() {
        return desfloor;
    }

    public int getEleId() {
        return eleId;
    }

    public void setEleId(int eleId) {
        this.eleId = eleId;
    }

    public int getLeavefloor() {
        return leavefloor;
    }

    public int getId() {
        return id;
    }

    public Person(int leavefloor, int desfloor, int id,int eleId) {
        this.leavefloor = leavefloor;
        this.desfloor = desfloor;
        this.id = id;
        this.eleId = eleId;
    }

    public Person(){

    }

    public boolean up() {
        return (leavefloor - desfloor) < 0;
    }
}

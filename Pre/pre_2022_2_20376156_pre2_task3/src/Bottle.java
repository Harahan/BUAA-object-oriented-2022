public class Bottle extends Equipment {
    private double capacity = 0.0;
    private boolean filled = true;

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public double getCapacity() {
        return capacity;
    }

    public boolean getFilled() {
        return filled;
    }

    public void print() {
        System.out.println(
                "The bottle's id is " + this.getId() +
                ", name is " + this.getName() +
                ", capacity is " + this.getCapacity() +
                ", filled is " + this.getFilled() + "."
        );
    }
}






public class ExpBottle extends Bottle {
    private double expRatio = 0.0;

    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }

    public double getExpRatio() {
        return expRatio;
    }

    public void print() {
        System.out.println(
                "The expBottle's id is " + getId() +
                ", name is " + this.getName() +
                ", capacity is " + this.getCapacity() +
                ", filled is " + getFilled() +
                ", expRatio is " + getExpRatio() + "."
        );
    }
}

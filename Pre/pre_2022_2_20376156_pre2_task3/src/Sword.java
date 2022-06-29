public class Sword extends Equipment {
    private double sharpness = 0.0;

    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }

    public double getSharpness() {
        return sharpness;
    }

    public void print() {
        System.out.println(
                "The sword's id is " + this.getId() +
                ", name is " + this.getName() +
                ", sharpness is " + getSharpness() + "."
        );
    }
}

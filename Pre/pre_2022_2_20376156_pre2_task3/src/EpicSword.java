public class EpicSword extends Sword {
    private double evolveRatio = 0.0;

    public void setEvolveRatio(double evolveRatio) {
        this.evolveRatio = evolveRatio;
    }

    public double getEvolveRatio() {
        return evolveRatio;
    }

    public void print() {
        System.out.println(
                "The epicSword's id is " + this.getId() +
                ", name is " + this.getName() +
                ", sharpness is " + this.getSharpness() +
                ", evolveRatio is " + this.getEvolveRatio() + "."
        );
    }
}

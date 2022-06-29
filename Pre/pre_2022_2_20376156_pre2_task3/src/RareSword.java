public class RareSword extends Sword {
    private double extraExpBonus = 0.0;

    public void setExtraExpBonus(double extraExpBonus) {
        this.extraExpBonus = extraExpBonus;
    }

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    public void print() {
        System.out.println(
                "The rareSword's id is " + this.getId() +
                ", name is " + this.getName() +
                ", sharpness is " + this.getSharpness() +
                ", extraExpBonus is " + getExtraExpBonus() + "."
        );
    }
}

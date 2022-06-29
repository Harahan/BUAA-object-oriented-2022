public class HealingPotion extends Bottle {
    private double efficiency = 0.0;

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void print() {
        System.out.println(
                "The healingPotion's id is " + this.getId() +
                ", name is " + this.getName() +
                ", capacity is " + this.getCapacity() +
                ", filled is " + this.getFilled() +
                ", efficiency is " + this.getEfficiency() + "."
        );
    }
}

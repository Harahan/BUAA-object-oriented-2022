public class HealingPotion extends Bottle {
    private double efficiency = 0.0;

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public double getEfficiency() {
        return efficiency;
    }

    @Override
    public void print() {
        System.out.println(
                "The healingPotion's id is " + this.getId() +
                ", name is " + this.getName() +
                ", capacity is " + this.getCapacity() +
                ", filled is " + this.getFilled() +
                ", efficiency is " + this.getEfficiency() + "."
        );
    }

    @Override
    public void use(Adventurer user) throws Exception {
        if (!getFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        user.setHealth(user.getHealth() + getCapacity() / 10);
        setFilled(false);
        setPrice(getPrice() / 10);
        user.setHealth(user.getHealth() + getCapacity() * getEfficiency());
        System.out.println(user.getName() +
                " drank " + getName() +
                " and recovered " + getCapacity() / 10 +
                ".\n" + user.getName() + " recovered extra " +
                getCapacity() * getEfficiency() + ".");
    }
}

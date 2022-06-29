public class ExpBottle extends Bottle {
    private double expRatio = 0.0;

    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }

    public double getExpRatio() {
        return expRatio;
    }

    @Override
    public void print() {
        System.out.println(
                "The expBottle's id is " + getId() +
                ", name is " + this.getName() +
                ", capacity is " + this.getCapacity() +
                ", filled is " + getFilled() +
                ", expRatio is " + getExpRatio() + "."
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
        user.setExp(user.getExp() * getExpRatio());
        System.out.println(user.getName() +
                " drank " + getName() +
                " and recovered " + getCapacity() / 10 +
                ".\n" + user.getName() + "'s exp became " + user.getExp() + ".");
    }
}

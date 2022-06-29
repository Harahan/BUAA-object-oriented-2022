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

    @Override
    public void print() {
        System.out.println(
                "The bottle's id is " + this.getId() +
                ", name is " + this.getName() +
                ", capacity is " + this.getCapacity() +
                ", filled is " + this.getFilled() + "."
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
        System.out.println(user.getName() +
                " drank " + getName() +
                " and recovered " + getCapacity() / 10 + ".");
    }
}






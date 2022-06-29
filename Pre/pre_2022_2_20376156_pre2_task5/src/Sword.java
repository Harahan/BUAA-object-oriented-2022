public class Sword extends Equipment {
    private double sharpness = 0.0;

    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }

    public double getSharpness() {
        return sharpness;
    }

    @Override
    public void print() {
        System.out.println(
                "The sword's id is " + this.getId() +
                ", name is " + this.getName() +
                ", sharpness is " + getSharpness() + "."
        );
    }

    @Override
    public void use(Adventurer user) {
        user.setHealth(user.getHealth() - 10.0);
        user.setExp(user.getExp() + 10.0);
        user.setMoney(user.getMoney() + getSharpness());
        System.out.println(
                user.getName() + " used " +
                getName() + " and earned " + getSharpness() + "."
        );
    }
}

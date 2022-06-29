public class EpicSword extends Sword {
    private double evolveRatio = 0.0;

    public void setEvolveRatio(double evolveRatio) {
        this.evolveRatio = evolveRatio;
    }

    public double getEvolveRatio() {
        return evolveRatio;
    }

    @Override
    public void print() {
        System.out.println(
                "The epicSword's id is " + this.getId() +
                ", name is " + this.getName() +
                ", sharpness is " + this.getSharpness() +
                ", evolveRatio is " + this.getEvolveRatio() + "."
        );
    }

    @Override
    public void use(Adventurer user) {
        user.setHealth(user.getHealth() - 10.0);
        user.setExp(user.getExp() + 10.0);
        user.setMoney(user.getMoney() + getSharpness());
        setSharpness(getSharpness() * getEvolveRatio());
        System.out.println(
                user.getName() + " used " +
                        getName() + " and earned " + getSharpness() / getEvolveRatio() +
                        ".\n" + getName() + "'s sharpness became " +
                        getSharpness() + "."
        );
    }
}

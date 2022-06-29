public class RareSword extends Sword {
    private double extraExpBonus = 0.0;

    public void setExtraExpBonus(double extraExpBonus) {
        this.extraExpBonus = extraExpBonus;
    }

    public double getExtraExpBonus() {
        return extraExpBonus;
    }

    @Override
    public void print() {
        System.out.println(
                "The rareSword's id is " + this.getId() +
                ", name is " + this.getName() +
                ", sharpness is " + this.getSharpness() +
                ", extraExpBonus is " + getExtraExpBonus() + "."
        );
    }

    @Override
    public void use(Adventurer user) {
        user.setHealth(user.getHealth() - 10.0);
        user.setExp(user.getExp() + 10.0);
        user.setMoney(user.getMoney() + getSharpness());
        user.setExp(user.getExp() + getExtraExpBonus());
        System.out.println(
                user.getName() + " used " +
                getName() + " and earned " + getSharpness() +
                ".\n" + user.getName() + " got extra exp " + getExtraExpBonus() + "."
        );
    }
}

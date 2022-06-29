import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Adventurer {
    private int id;
    private String name;
    private double health = 100.0;
    private double exp = 0.0;
    private double money = 0.0;
    private ArrayList<Equipment> equipments = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getHealth() {
        return health;
    }

    public double getExp() {
        return exp;
    }

    public double getMoney() {
        return money;
    }

    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
    }

    public void delEquipment(int id) {
        equipments.removeIf(equipment -> equipment.getId() == id);
    }

    public long maxEquipment() {
        long maxPrice = 0;
        for (Equipment equipment : equipments) {
            if (equipment.getPrice() > maxPrice) {
                maxPrice = equipment.getPrice();
            }
        }
        return maxPrice;
    }

    public BigInteger sumEquipment() {
        BigInteger sum = new BigInteger("0");
        for (Equipment equipment : equipments) {
            sum = sum.add(BigInteger.valueOf(equipment.getPrice()));
        }
        return sum;
    }

    public int numEquipment() {
        return equipments.size();
    }

    public void printEquipment(int id) {
        for (Equipment item : equipments) {
            if (item.getId() == id) {
                item.print();
                break;
            }
        }
    }

    public void print() {
        System.out.println(
                "The adventurer's id is " +  getId() +
                ", name is " + getName() +
                ", health is " + getHealth() +
                ", exp is " + getExp() +
                ", money is " + getMoney() + "."
        );
    }

    public void useEquipment() {
        Collections.sort(equipments);
        for (Equipment item : equipments) {
            try {
                item.use(this);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

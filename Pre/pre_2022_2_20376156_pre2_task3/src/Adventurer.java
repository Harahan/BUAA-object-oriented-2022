import java.math.BigInteger;
import java.util.ArrayList;

public class Adventurer {
    private int id;
    private String name;
    private ArrayList<Equipment> equipments = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
                if (item instanceof HealingPotion) {
                    HealingPotion tmp = (HealingPotion) item;
                    tmp.print();
                }
                else if (item instanceof ExpBottle) {
                    ExpBottle tmp = (ExpBottle) item;
                    tmp.print();
                }
                else if (item instanceof Bottle) {
                    Bottle tmp = (Bottle) item;
                    tmp.print();
                }
                else if (item instanceof RareSword) {
                    RareSword tmp = (RareSword) item;
                    tmp.print();
                }
                else if (item instanceof EpicSword) {
                    EpicSword tmp = (EpicSword) item;
                    tmp.print();
                }
                else if (item instanceof Sword) {
                    Sword tmp = (Sword) item;
                    tmp.print();
                }
                break;
            }
        }
    }
}

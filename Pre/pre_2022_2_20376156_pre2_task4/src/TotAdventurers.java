import java.math.BigInteger;
import java.util.ArrayList;

public class TotAdventurers {
    private ArrayList<Adventurer> adventurers = new ArrayList<>();

    public void addAdventurer(int id, String name) {
        Adventurer a = new Adventurer();
        a.setName(name);
        a.setId(id);
        this.adventurers.add(a);
    }

    public void addEquipment(int aid, Equipment equipment) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.addEquipment(equipment);
                break;
            }
        }
    }

    public void delEquipment(int aid, int bid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.delEquipment(bid);
                break;
            }
        }
    }

    public long searchMaxPrice(int id) {
        long maxPrice = 0;
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                maxPrice = item.maxEquipment();
                break;
            }
        }
        return maxPrice;
    }

    public BigInteger sumPrice(int id) {
        BigInteger sum = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                sum = item.sumEquipment();
                break;
            }
        }
        return sum;
    }

    public int numEquipment(int id) {
        int size = 0;
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                size = item.numEquipment();
                break;
            }
        }
        return size;
    }

    public void printEquipment(int aid, int eid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.printEquipment(eid);
                break;
            }
        }
    }

    public void printAdventurer(int id) {
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                item.print();
                break;
            }
        }
    }

    public void useEquipment(int id) {
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                item.useEquipment();
                break;
            }
        }
    }
}
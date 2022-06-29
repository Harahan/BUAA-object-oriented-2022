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

    public void addValueBody(int aid, ValueBody valueBody) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.addValueBody(valueBody);
                break;
            }
        }
    }

    public Adventurer findAdventurer(int aid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                return item;
            }
        }
        return null;
    }

    public void delValueBody(int aid, int bid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.delValueBody(bid);
                break;
            }
        }
    }

    public BigInteger searchMaxPrice(int id) {
        BigInteger maxPrice = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                maxPrice = item.maxValueBody();
                break;
            }
        }
        return maxPrice;
    }

    public BigInteger sumPrice(int id) {
        BigInteger sum = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                sum = item.sumValueBody();
                break;
            }
        }
        return sum;
    }

    public int numValueBody(int id) {
        int size = 0;
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                size = item.numValueBody();
                break;
            }
        }
        return size;
    }

    public void printValueBody(int aid, int eid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.printValueBody(eid);
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

    public void useValueBody(int id) {
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                item.useValueBody();
                break;
            }
        }
    }
}
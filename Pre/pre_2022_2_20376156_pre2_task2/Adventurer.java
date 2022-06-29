import java.math.BigInteger;
import java.util.ArrayList;

public class Adventurer {
    private int id;
    private String name;
    private ArrayList<Bottle> bottles = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addBottle(Bottle bottle) {
        bottles.add(bottle);
    }

    public void delBottle(int id) {
        bottles.removeIf(bottle -> bottle.getId() == id);
    }

    public long maxBottle() {
        long maxPrice = 0;
        for (Bottle bottle : bottles) {
            if (bottle.getPrice() > maxPrice) {
                maxPrice = bottle.getPrice();
            }
        }
        return maxPrice;
    }

    public BigInteger sumBottle() {
        BigInteger sum = new BigInteger("0");
        for (Bottle bottle : bottles) {
            sum = sum.add(BigInteger.valueOf(bottle.getPrice()));
        }
        return sum;
    }
}

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Adventurer implements ValueBody {
    private int id;
    private String name;
    private double health = 100.0;
    private double exp = 0.0;
    private double money = 0.0;
    private ArrayList<ValueBody> valueBodies = new ArrayList<>();

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

    @Override
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

    public void addValueBody(ValueBody valueBody) {
        valueBodies.add(valueBody);
    }

    public void delValueBody(int id) {
        valueBodies.removeIf(valueBody -> valueBody.getId() == id);
    }

    public BigInteger maxValueBody() {
        BigInteger maxPrice = new BigInteger("0");
        for (ValueBody valueBody : valueBodies) {
            if (valueBody.getPrice().compareTo(maxPrice) > 0) {
                maxPrice = valueBody.getPrice();
            }
        }
        return maxPrice;
    }

    public BigInteger sumValueBody() {
        BigInteger sum = new BigInteger("0");
        //System.out.println("[" + valueBodies.size() + "]");
        for (ValueBody valueBody : valueBodies) {
            sum = sum.add(valueBody.getPrice());
            //System.out.println("-" + valueBody.getPrice() + "-");
        }
        return sum;
    }

    public int numValueBody() {
        return valueBodies.size();
    }

    public void printValueBody(int id) {
        for (ValueBody item : valueBodies) {
            if (item.getId() == id) {
                item.print();
                break;
            }
        }
    }

    @Override
    public void print() {
        System.out.println(
                "The adventurer's id is " +  getId() +
                ", name is " + getName() +
                ", health is " + getHealth() +
                ", exp is " + getExp() +
                ", money is " + getMoney() + "."
        );
    }

    public void useValueBody() {
        Collections.sort(valueBodies);
        for (ValueBody item : valueBodies) {
            try {
                item.use(this);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    @Override
    public int compareTo(ValueBody other) {
        int a = 0;
        if ((a = getPrice().compareTo(other.getPrice())) != 0) {
            return -a;
        }
        return other.getId() - getId();
    }
    
    @Override
    public void use(Adventurer adventurer) throws Exception {
        Collections.sort(valueBodies);
        for (ValueBody item : valueBodies) {
            try {
                item.use(adventurer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    @Override
    public BigInteger getPrice() {
        BigInteger sum = new BigInteger("0");
        for (ValueBody item : valueBodies) {
            sum = sum.add(item.getPrice());
            //System.out.println("-" + item.getPrice() + "-" + item.getId());
        }
        return sum;
    }
}

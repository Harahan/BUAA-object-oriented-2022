import java.math.BigInteger;

public class Equipment implements ValueBody {
    private int id = 0;
    private String name = null;
    private BigInteger price = new BigInteger("0");

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public BigInteger getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public void print() {
    }

    @Override
    public void use(Adventurer user) throws Exception {
    }

    @Override
    public int compareTo(ValueBody other) {
        int a = 0;
        if ((a = getPrice().compareTo(other.getPrice())) != 0) {
            return -a;
        }
        return other.getId() - getId();
    }
}

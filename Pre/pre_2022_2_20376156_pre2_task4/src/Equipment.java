public class Equipment implements Comparable<Equipment> {
    private int id = 0;
    private String name = null;
    private long price = 0;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void print() {
    }

    public void use(Adventurer user) throws Exception {
    }

    @Override
    public int compareTo(Equipment other) {
        if (this.getPrice() < other.getPrice()) {
            return 1;
        }
        else if (this.getPrice() > other.getPrice()) {
            return -1;
        }
        else if (this.getId() < other.getId()) {
            return 1;
        }
        else if (this.getId() > other.getId()) {
            return -1;
        }
        return 0;
    }
}

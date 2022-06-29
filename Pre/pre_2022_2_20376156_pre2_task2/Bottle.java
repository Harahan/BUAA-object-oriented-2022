public class Bottle {
    private int id = 0;
    private String name = null;
    private long price = 0;
    private double capacity = 0.0;
    private boolean filled = true;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public int getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "The bottle's id is " + this.id +
                ", name is " + this.name +
                ", capacity is " + this.capacity +
                ", filled is " + this.filled +
                '.';
    }
}






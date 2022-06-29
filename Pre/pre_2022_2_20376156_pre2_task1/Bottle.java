import java.util.Scanner;

public class Bottle {
    private int id = 0;
    private String name = null;
    private long price = 0;
    private double capacity = 0.0;
    private boolean filled = true;

    public void setPrice(long price) {
        this.price = price;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    @Override
    public String toString() {
        return "The bottle's id is " + id +
                ", name is " + name +
                ", capacity is " + capacity +
                ", filled is " + filled +
                '.';
    }

    public static void main(String[] args) {
        Bottle a = new Bottle();
        Scanner scanner = new Scanner(System.in);
        a.id = scanner.nextInt();
        a.name = scanner.next();
        a.price = scanner.nextLong();
        a.capacity = scanner.nextDouble();
        int m = scanner.nextInt();
        while (m-- > 0) {
            int op = scanner.nextInt();
            switch (op) {
                case 1:
                    System.out.println(a.name);
                    break;
                case 2:
                    System.out.println(a.price);
                    break;
                case 3:
                    System.out.println(a.capacity);
                    break;
                case 4:
                    System.out.println(a.filled);
                    break;
                case 5:
                    long price = scanner.nextLong();
                    a.setPrice(price);
                    break;
                case 6:
                    boolean filled = scanner.nextBoolean();
                    a.setFilled(filled);
                    break;
                case 7:
                    System.out.println(a.toString());
                    break;
                default:
                    break;
            }
        }
    }
}

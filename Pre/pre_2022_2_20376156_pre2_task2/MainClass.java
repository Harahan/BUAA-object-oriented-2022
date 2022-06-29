import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    private ArrayList<Adventurer> adventurers = new ArrayList<>();

    public void addAdventurer(Adventurer a) {
        this.adventurers.add(a);
    }

    public void addBottle(int aid, Bottle bottle) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.addBottle(bottle);
                break;
            }
        }
    }

    public void delBottle(int aid, int bid) {
        for (Adventurer item : adventurers) {
            if (item.getId() == aid) {
                item.delBottle(bid);
                break;
            }
        }
    }

    public long searchMaxPrice(int id) {
        long maxPrice = 0;
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                maxPrice = item.maxBottle();
                break;
            }
        }
        return maxPrice;
    }

    public BigInteger sumPrice(int id) {
        BigInteger sum = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == id) {
                sum = item.sumBottle();
                break;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int aid = 0;
        int bid = 0;
        String name = "";
        Long price = 0L;
        Long maxPrice = 0L;
        double capacity = 0.0;
        BigInteger sum = new BigInteger("0");
        MainClass totAdventurers = new MainClass();

        int m = scanner.nextInt();
        while (m-- > 0) {
            int op = scanner.nextInt();
            switch (op) {
                case 1:
                    aid = scanner.nextInt();
                    name = scanner.next();
                    Adventurer adventurer = new Adventurer();
                    adventurer.setId(aid);
                    adventurer.setName(name);
                    totAdventurers.addAdventurer(adventurer);
                    break;
                case 2:
                    aid = scanner.nextInt();
                    bid = scanner.nextInt();
                    name = scanner.next();
                    price = scanner.nextLong();
                    capacity = scanner.nextDouble();
                    Bottle bottle = new Bottle();
                    bottle.setCapacity(capacity);
                    bottle.setPrice(price);
                    bottle.setName(name);
                    bottle.setId(bid);
                    totAdventurers.addBottle(aid, bottle);
                    break;
                case 3:
                    aid = scanner.nextInt();
                    bid = scanner.nextInt();
                    totAdventurers.delBottle(aid, bid);
                    break;
                case 4:
                    aid = scanner.nextInt();
                    sum = totAdventurers.sumPrice(aid);
                    System.out.println(sum);
                    break;
                case 5:
                    aid = scanner.nextInt();
                    maxPrice = totAdventurers.searchMaxPrice(aid);
                    System.out.println(maxPrice);
                    break;
                default:
                    break;
            }
        }
    }

}
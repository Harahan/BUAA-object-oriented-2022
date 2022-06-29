import java.util.Scanner;

public class Main {
    private TotAdventurers totAdventurers = new TotAdventurers();
    private Scanner scanner = new Scanner(System.in);

    public void buildBottle(int aid) {
        int eid = scanner.nextInt();
        String name = scanner.next();
        Long price = scanner.nextLong();
        double capacity = scanner.nextDouble();
        Bottle bottle = new Bottle();
        bottle.setCapacity(capacity);
        bottle.setId(eid);
        bottle.setName(name);
        bottle.setPrice(price);
        totAdventurers.addEquipment(aid, (Equipment) bottle);
    }

    public void buildExpBottle(int aid) {
        int eid = scanner.nextInt();
        String name = scanner.next();
        Long price = scanner.nextLong();
        double capacity = scanner.nextDouble();
        double expRatio = scanner.nextDouble();
        ExpBottle expBottle = new ExpBottle();
        expBottle.setCapacity(capacity);
        expBottle.setId(eid);
        expBottle.setName(name);
        expBottle.setPrice(price);
        expBottle.setExpRatio(expRatio);
        totAdventurers.addEquipment(aid, (Equipment) expBottle);
    }

    public void buildHealingPotion(int aid) {
        int eid = scanner.nextInt();
        String name = scanner.next();
        Long price = scanner.nextLong();
        double capacity = scanner.nextDouble();
        double efficiency = scanner.nextDouble();
        HealingPotion healingPotion = new HealingPotion();
        healingPotion.setCapacity(capacity);
        healingPotion.setId(eid);
        healingPotion.setName(name);
        healingPotion.setPrice(price);
        healingPotion.setEfficiency(efficiency);
        totAdventurers.addEquipment(aid, (Equipment) healingPotion);
    }

    public void buildSword(int aid) {
        int eid = scanner.nextInt();
        String name = scanner.next();
        Long price = scanner.nextLong();
        double sharpness = scanner.nextDouble();
        Sword sword = new Sword();
        sword.setName(name);
        sword.setPrice(price);
        sword.setId(eid);
        sword.setSharpness(sharpness);
        totAdventurers.addEquipment(aid, (Equipment) sword);
    }

    public void buildEpicSword(int aid) {
        int eid = scanner.nextInt();
        String name = scanner.next();
        Long price = scanner.nextLong();
        double sharpness = scanner.nextDouble();
        double evolveRatio = scanner.nextDouble();
        EpicSword epicSword = new EpicSword();
        epicSword.setName(name);
        epicSword.setPrice(price);
        epicSword.setId(eid);
        epicSword.setSharpness(sharpness);
        epicSword.setEvolveRatio(evolveRatio);
        totAdventurers.addEquipment(aid, (Equipment) epicSword);
    }

    public void buildRareSword(int aid) {
        int eid = scanner.nextInt();
        String name = scanner.next();
        Long price = scanner.nextLong();
        double sharpness = scanner.nextDouble();
        double extraExpBonus = scanner.nextDouble();
        RareSword rareSword = new RareSword();
        rareSword.setName(name);
        rareSword.setPrice(price);
        rareSword.setId(eid);
        rareSword.setSharpness(sharpness);
        rareSword.setExtraExpBonus(extraExpBonus);
        totAdventurers.addEquipment(aid, (Equipment) rareSword);
    }

    public void scanfBuildEquipment(int aid) {
        int op = scanner.nextInt();
        switch (op) {
            case 1:
                buildBottle(aid);
                break;
            case 2:
                buildHealingPotion(aid);
                break;
            case 3:
                buildExpBottle(aid);
                break;
            case 4:
                buildSword(aid);
                break;
            case 5:
                buildRareSword(aid);
                break;
            case 6:
                buildEpicSword(aid);
                break;
            default:
                break;
        }

    }

    public static void main(String[] args) {
        int aid = 0;
        int eid = 0;
        String name = "";
        Main obj = new Main();
        int m = obj.scanner.nextInt();
        while (m-- > 0) {
            int op = obj.scanner.nextInt();
            switch (op) {
                case 1:
                    aid = obj.scanner.nextInt();
                    name = obj.scanner.next();
                    obj.totAdventurers.addAdventurer(aid, name);
                    break;
                case 2:
                    aid = obj.scanner.nextInt();
                    obj.scanfBuildEquipment(aid);
                    break;
                case 3:
                    aid = obj.scanner.nextInt();
                    eid = obj.scanner.nextInt();
                    obj.totAdventurers.delEquipment(aid, eid);
                    break;
                case 4:
                    aid = obj.scanner.nextInt();
                    System.out.println(obj.totAdventurers.sumPrice(aid));
                    break;
                case 5:
                    aid = obj.scanner.nextInt();
                    System.out.println(obj.totAdventurers.searchMaxPrice(aid));
                    break;
                case 6:
                    aid = obj.scanner.nextInt();
                    System.out.println(obj.totAdventurers.numEquipment(aid));
                    break;
                case 7:
                    aid = obj.scanner.nextInt();
                    eid = obj.scanner.nextInt();
                    obj.totAdventurers.printEquipment(aid, eid);
                    break;
                default:
                    break;
            }
        }
    }
}

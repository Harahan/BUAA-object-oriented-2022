import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AvlTree<String> tree = new AvlTree<>();
        Scanner scanner = new Scanner(System.in);
        int cnt = scanner.nextInt();
        for (int i = 0; i < cnt; i++) {
            int cmd = Integer.parseInt(scanner.next());
            switch (cmd) {
                case 1:
                    tree.add(scanner.next());
                    System.out.println("add ok");
                    break;
                case 2:
                    tree.delete(scanner.next());
                    System.out.println("delete ok");
                    break;
                case 3:
                    System.out.println(tree.getRank(scanner.next()));
                    break;
                default:
                    break;
            }
        }
        scanner.close();
    }
}
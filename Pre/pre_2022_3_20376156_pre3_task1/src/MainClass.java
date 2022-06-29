import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TotInformation tot = new TotInformation();
        while (scanner.hasNextLine()) {
            tot.getInformation(scanner.nextLine());
        }
        tot.totPrint();
    }
}

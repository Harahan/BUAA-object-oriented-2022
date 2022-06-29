import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TotInformation tot = new TotInformation();
        String tmp = "";
        String[] tmpQuestion = new String[3];

        while (true) {
            tmp = scanner.nextLine();
            if (tmp.equals("END_OF_MESSAGE")) {
                break;
            }
            tot.getInformation(tmp);
        }

        while (scanner.hasNextLine()) {
            tmp = scanner.nextLine();
            tmpQuestion = tmp.split(" ");
            if (tmpQuestion[1].equals("A")) {
                if (tmpQuestion[2].equals("1")) {
                    tot.queryMessageA1();
                }
                else if (tmpQuestion[2].equals("2")) {
                    tot.queryMessageA2();
                }
                else if (tmpQuestion[2].equals("3")) {
                    tot.queryMessageA3();
                }
                else {
                    tot.queryMessageA4();
                }
            }
            else {
                if (tmpQuestion[2].equals("1")) {
                    tot.queryMessageB1();
                }
                else if (tmpQuestion[2].equals("2")) {
                    tot.queryMessageB2();
                }
                else if (tmpQuestion[2].equals("3")) {
                    tot.queryMessageB3();
                }
                else {
                    tot.queryMessageB4();
                }
            }
        }
    }
}

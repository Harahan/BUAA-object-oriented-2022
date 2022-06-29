import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TotInformation tot = new TotInformation();
        String tmp = "";
        String[] tmpQuestion = new String[2];

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
            if (tmpQuestion[0].equals("qdate")) {
                tot.queryDate(tmpQuestion[1]);
            }
            else if (tmpQuestion[0].equals("qsend")) {
                tot.querySendUsername(tmpQuestion[1]);
            }
            else if (tmpQuestion[0].equals("qrecv")) {
                tot.queryReceiveUsername(tmpQuestion[1]);
            }
        }
    }
}

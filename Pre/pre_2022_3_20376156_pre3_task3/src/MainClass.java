import java.util.Scanner;
import java.util.regex.Pattern;

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

        Pattern pattern1 = Pattern.compile("\\d+/\\d+/\\d+");
        Pattern pattern2 = Pattern.compile("\\d+//");
        Pattern pattern3 = Pattern.compile("/\\d+/");
        Pattern pattern4 = Pattern.compile("//\\d+");
        Pattern pattern5 = Pattern.compile("\\d+/\\d+/");
        Pattern pattern6 = Pattern.compile("/\\d+/\\d+");
        while (scanner.hasNextLine()) {
            tmp = scanner.nextLine();
            tmpQuestion = tmp.split(" ");
            if (tmpQuestion[0].equals("qdate") && pattern1.matcher(tmpQuestion[1]).matches()) {
                tot.queryDate1(tmpQuestion[1]);
            }
            else if (tmpQuestion[0].equals("qdate") && pattern2.matcher(tmpQuestion[1]).matches()) {
                tot.queryDate2(tmpQuestion[1]);
            }
            else if (tmpQuestion[0].equals("qdate") && pattern3.matcher(tmpQuestion[1]).matches()) {
                tot.queryDate3(tmpQuestion[1]);
            }
            else if (tmpQuestion[0].equals("qdate") && pattern4.matcher(tmpQuestion[1]).matches()) {
                tot.queryDate4(tmpQuestion[1]);
            }
            else if (tmpQuestion[0].equals("qdate") && pattern5.matcher(tmpQuestion[1]).matches()) {
                tot.queryDate5(tmpQuestion[1]);
            }
            else if (tmpQuestion[0].equals("qdate") && pattern6.matcher(tmpQuestion[1]).matches()) {
                tot.queryDate6(tmpQuestion[1]);
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

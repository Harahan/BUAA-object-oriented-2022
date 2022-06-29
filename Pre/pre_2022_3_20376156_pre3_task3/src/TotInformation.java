import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TotInformation {
    private String [] totInformation = new String[3000];
    private int tot = 0;
    private Pattern personPattern = Pattern.compile("\\d.+?;");

    public void getInformation(String str) {
        Matcher perMatcher = personPattern.matcher(str);
        while (perMatcher.find()) {
            totInformation[tot++] = perMatcher.group();
        }
    }

    public void totPrint() {
        for (int i = 0; i < tot; i++) {
            System.out.println(totInformation[i]);
        }
    }

    public void queryDate1(String date) {
        Pattern tmp = Pattern.compile("0*(\\d*)/0*(\\d*)/0*(\\d*)");
        Matcher m = tmp.matcher(date);
        String res = m.replaceAll("^0*$1/0*$2/0*$3-");
        tmp = Pattern.compile(res);
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryDate2(String date) {
        Pattern tmp = Pattern.compile("0*(\\d*)//");
        Matcher m = tmp.matcher(date);
        String res = m.replaceAll("^0*$1/\\\\d+/\\\\d+-");
        tmp = Pattern.compile(res);
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryDate3(String date) {
        Pattern tmp = Pattern.compile("/0*(\\d*)/");
        Matcher m = tmp.matcher(date);
        String res = m.replaceAll("^\\\\d+/0*$1/\\\\d+-");
        tmp = Pattern.compile(res);
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryDate4(String date) {
        Pattern tmp = Pattern.compile("//0*(\\d*)");
        Matcher m = tmp.matcher(date);
        String res = m.replaceAll("^\\\\d+/\\\\d+/0*$1-");
        tmp = Pattern.compile(res);
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryDate5(String date) {
        Pattern tmp = Pattern.compile("0*(\\d*)/0*(\\d*)/");
        Matcher m = tmp.matcher(date);
        String res = m.replaceAll("^0*$1/0*$2/\\\\d+-");
        tmp = Pattern.compile(res);
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryDate6(String date) {
        Pattern tmp = Pattern.compile("/0*(\\d*)/0*(\\d*)");
        Matcher m = tmp.matcher(date);
        String res = m.replaceAll("^\\\\d+/0*$1/0*$2-");
        tmp = Pattern.compile(res);
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void querySendUsername(String username) {
        Pattern tmp = Pattern.compile("-" + username + "[@:]");
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryReceiveUsername(String username) {
        Pattern tmp = Pattern.compile("@" + username + " ");
        for (int i = 0; i < tot; i++) {
            if (tmp.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }
}

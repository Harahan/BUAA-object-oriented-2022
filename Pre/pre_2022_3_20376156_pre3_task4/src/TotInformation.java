import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TotInformation {
    private String [] totInformation = new String[3000];
    private int tot = 0;
    private Pattern personPattern = Pattern.compile("\\d.+?;");
    private Pattern messageA1 = Pattern.compile(":\"a{2,3}?b{2,4}?c{2,4}?");
    private Pattern messageA2 = Pattern.compile("a{2,3}?b{2,4}?c{2,4}?\";");
    private Pattern messageA3 = Pattern.compile(":.*a{2,3}?b{2,4}?c{2,4}?.*");
    private Pattern messageA4 = Pattern.compile(":.*a.*a.*b.*b.*c.*c");
    private Pattern messageB1 = Pattern.compile(":\"a{2,3}?b{2,1000000}?c{2,4}?");
    private Pattern messageB2 = Pattern.compile("a{2,3}?b{2,1000000}?c{2,4}?\";");
    private Pattern messageB3 = Pattern.compile(":.*a{2,3}?b{2,1000000}?c{2,4}?.*");
    private Pattern messageB4 = Pattern.compile(":.*a.*a.*b.*b.*c.*c");

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

    public void queryMessageA1() {
        for (int i = 0; i < tot; i++) {
            if (messageA1.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryMessageA2() {
        for (int i = 0; i < tot; i++) {
            if (messageA2.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryMessageA3() {
        for (int i = 0; i < tot; i++) {
            if (messageA3.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryMessageA4() {
        for (int i = 0; i < tot; i++) {
            if (messageA4.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryMessageB1() {
        for (int i = 0; i < tot; i++) {
            if (messageB1.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryMessageB2() {
        for (int i = 0; i < tot; i++) {
            if (messageB2.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryMessageB3() {
        for (int i = 0; i < tot; i++) {
            if (messageB3.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }

    public void queryMessageB4() {
        for (int i = 0; i < tot; i++) {
            if (messageB4.matcher(totInformation[i]).find()) {
                System.out.println(totInformation[i]);
            }
        }
    }
}

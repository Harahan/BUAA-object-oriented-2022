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
}

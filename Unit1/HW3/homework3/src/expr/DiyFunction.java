package expr;

import java.util.ArrayList;

public class DiyFunction extends Function {
    private static String f;
    private static String g;
    private static String h;

    /**j, k, l*/
    public void addDiyFunction(String input) {
        String str = input.replaceAll("[ \\t]", "");
        int j = 0;
        for (int i = 0; ")".indexOf(str.charAt(i)) == -1; i++) {
            char c = str.charAt(i);
            if ("xyz".indexOf(c) != -1) {
                str = str.replaceAll(String.valueOf(c), String.valueOf((char)('j' + j)));
                j += 1;
            }
        }
        char c = str.charAt(0);
        if (c == 'f') {
            f = str.split("=")[1];
        } else if (c == 'g') {
            g = str.split("=")[1];
        } else {
            h = str.split("=")[1];
        }
    }

    public String substituteInto(String type, ArrayList<Factor> factors) {
        String function = ((type.equals("f")) ? f : ((type.equals("g")) ? g : h));
        int i = 0;
        for (Factor factor : factors) {
            char tmp = (char)('j' + i);
            function = function.replaceAll(String.valueOf(tmp), "(" + factor.toString() + ')');
            i += 1;
        }
        return function;
    }
}

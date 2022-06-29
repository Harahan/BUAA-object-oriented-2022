import expr.Expr;

import java.math.BigInteger;
import java.util.HashMap;

public class Function {
    private final String rawstr;
    private String head;
    private String[] xyz;
    private String fun;

    public Function(String rawstr) {
        this.rawstr = rawstr;
        this.head = String.valueOf(rawstr.charAt(0));
        this.fun = rawstr.substring(rawstr.indexOf("=") + 1);
        this.xyz = rawstr.substring(rawstr.indexOf("(") + 1, rawstr.indexOf(")")).split(",");
    }

    public String getSimplify(String rawstr1) {
        String[] temp = rawstr1.substring(rawstr1.indexOf("(") + 1,
                rawstr1.length() - 1).split(",");
        String refun = this.fun;
        for (int i = 0; i < this.xyz.length; i++) {
            refun = refun.replaceAll(this.xyz[i], "(" + temp[i] + ")");
        }
        //System.out.println(refun);
        while (refun.contains("sin((") || refun.contains("cos((")) {
            int begin = refun.contains("sin((") ? refun.indexOf("sin((") : refun.indexOf("cos((");
            int end = refun.indexOf(")", begin);
            StringBuilder refun1 = new StringBuilder(refun);
            refun1.delete(end, end + 1);
            refun1.delete(begin + 3, begin + 4);
            refun = String.valueOf(refun1);
        }
        Lexer lexer = new Lexer(refun);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        HashMap<HashMap<String, Integer>, BigInteger> var1 = expr.calculate();
        PrintAns printAns = new PrintAns(var1);
        return "(" + printAns.printans() + ")";
    }

    public String getHead() {
        return head;
    }
}

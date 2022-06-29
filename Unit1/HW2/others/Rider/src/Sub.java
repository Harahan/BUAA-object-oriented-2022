import java.util.ArrayList;

public class Sub extends Operator {
    public Sub(Operator left, Operator right) {
        super(left, right);
    }

    public String getResult() {
        String monomialRight = process(getRight().getResult());
        String monomialLeft = "";
        if (getLeft().getResult() != "") {
            monomialLeft = process(getLeft().getResult());
        }
        String subResult = monomialLeft;
        ArrayList<String> polysRight = new ArrayList<>();
        polysRight = monoTopoly(monomialRight);
        for (String polyRight : polysRight) {
            if (!polyRight.equals("")) {
                if (polyRight.charAt(0) == '+') {
                    subResult = subResult + "-" + polyRight.substring(1);
                }
                else if (polyRight.charAt(0) == '-') {
                    subResult = subResult + "+" + polyRight.substring(1);
                }
                else
                {
                    subResult = subResult + "-" + polyRight;
                }
            }

        }
        subResult = process(subResult);
        return subResult;
    }

    private static ArrayList<String> monoTopoly(String expression) {
        int stack = 0;
        int j = 0;
        int i;
        int flag = 0;
        String monomial = expression.replaceAll("\\s","");
        ArrayList<String> polynomials = new ArrayList<>();
        for (i = 0;i < monomial.length();i++) {
            if (monomial.charAt(i) == '(') {
                stack += 1;
                flag = 0;
            }
            else if (monomial.charAt(i) == ')') {
                stack -= 1;
                flag = 0;
            }
            else  if (monomial.charAt(i) == '*') {
                if (stack == 0) {
                    flag = 1;

                }
            }
            else if (monomial.charAt(i) == '+' || monomial.charAt(i) == '-') {
                if (stack == 0 && flag == 0) {
                    String polynomial = monomial.substring(j,i);
                    polynomials.add(polynomial);
                    j = i;
                }
            }
            else
            {
                flag = 0;
            }
        }
        polynomials.add(monomial.substring(j,i));
        return polynomials;
    }

    private static String process(String line) {
        int i = 0;
        int j = 0;
        char[] monomial = new char[line.length()];
        int flag = 0;
        int symbol = 1;
        for (i = 0;i < line.length();i++) {
            if (line.charAt(i) == '-') {
                symbol *= -1;
                flag = 1;
            }
            else if (line.charAt(i) == '+') {
                symbol *= 1;
                flag = 1;
            }
            else {
                if (flag == 1) {
                    monomial[j++] = (symbol == 1) ? '+' : '-';
                    monomial[j++] = line.charAt(i);
                    flag = 0;
                    symbol = 1;
                }
                else
                {
                    monomial[j++] = line.charAt(i);
                }
            }
        }
        String processed = new String(monomial);
        processed = processed.trim();
        return processed;
    }
}
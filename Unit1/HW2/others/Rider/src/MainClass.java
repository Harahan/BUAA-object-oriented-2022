import java.util.HashMap;
import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        HashMap<String,String> functions = new HashMap<>();
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int cnt = scanner.getCount();
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            func = func.replaceAll("\\s","");
            int position = findEqual(func);
            functions.put(func.substring(0,position),func.substring(position + 1));
        }
        String expression = scanner.readLine();
        expression = expression.replaceAll("\\s","");
        String fakeExpression = expression;
        for (int i = 0;i < expression.length();i++) {
            if (expression.charAt(i) == 'f' || expression.charAt(i) == 'g'
                    || expression.charAt(i) == 'h') {
                for (String name : functions.keySet()) {
                    if (expression.charAt(i) == name.charAt(0)) {
                        String fakeFunction = functions.get(name);
                        int brancket2 = i + 2 + findBrancket2(expression.substring(i + 2));
                        int brancket1 = i + 2;
                        String[] elements = expression.substring(brancket1,brancket2).split(",");
                        String[] originValues = name.substring(2,name.length() - 1).split(",");
                        for (int j = 0;j < elements.length;j++) {
                            fakeFunction = fakeFunction.replaceAll(originValues[j],elements[j]);
                        }
                        fakeFunction = "(" + fakeFunction + ")";
                        int start = fakeExpression.indexOf(expression.substring(i,brancket2 + 1));
                        fakeExpression = fakeExpression.substring(0,start) + fakeFunction
                                + fakeExpression.substring(start + brancket2 + 1 - i);
                    }
                }
            }
        }
        expression = fakeExpression;
        for (int j = 0;j < expression.length() - 2;j++) {
            if (expression.substring(j,j + 3).equals("sum")) {
                String fakeSum = "";
                int position = findBrancket2(expression.substring(j + 4)) + j + 4;
                String[] sumElements = expression.substring(j + 4,position).split(",");
                if (Integer.parseInt(sumElements[1]) > Integer.parseInt(sumElements[2])) {
                    fakeSum = "0";
                } else
                {
                    for (int i = Integer.parseInt(sumElements[1]);
                         i <= Integer.parseInt(sumElements[2]);i++) {
                        fakeSum = fakeSum + "+"
                                + sumElements[3].replaceAll("i", String.valueOf(i));
                    }
                    fakeSum = "(" + fakeSum + ")";
                }
                int start = fakeExpression.indexOf(expression.substring(j,position + 1));
                fakeExpression = fakeExpression.substring(0,start) + fakeSum
                        + fakeExpression.substring(start + position + 1 - j);
            }
        }
        expression = process(fakeExpression.replaceAll("s[\\+\\-]*\\d*n","sin")).trim();
        Parser parser = new Parser();
        System.out.print(simplify(process(parser.parse(expression).getResult())));
    }

    private static int findEqual(String func) {
        int i = -1;
        for (i = 0;i < func.length();i++) {
            if (func.charAt(i) == '=') {
                return i;
            }
        }
        return i;
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

    private static int findBrancket2(String expression) {
        int position = -1;
        int stack = 1;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                stack += 1;
            }
            if (expression.charAt(i) == ')') {
                stack -= 1;
                if (stack == 0) {
                    position = i;
                    return position;
                }

            }
        }
        return position;
    }

    private static String simplify(String expression) {
        char[] temp = new char[expression.length() + 1];
        int tempNum = 0;
        int j = 0;
        int flag = 0;
        int symbol = 1;
        int stack = 0;
        String polynomial = "";
        for (int i = 0;i < expression.length();i++) {
            if (expression.charAt(i) == '(') {
                if (expression.substring(i - 3,i).equals("sin")) {
                    if (expression.charAt(i + 1) == '-') {
                        symbol *= -1;
                    }
                }
                int theOther = findBrancket2(expression.substring(i + 1));
                theOther = theOther + i + 1;
                i = theOther;
            }
            else if (expression.charAt(i) == '*') {
                flag = 1;
            }
            else if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                if (flag == 0) {
                    polynomial = expression.substring(j,i).replaceAll("\\+|-","");
                    temp[tempNum++] = (symbol == 1) ? '+' : '-';
                    for (int k = 0;k < polynomial.length();k++) {
                        temp[tempNum++] = polynomial.charAt(k);
                    }
                    symbol = (expression.charAt(i) == '+') ? 1 : -1;
                    j = i;
                }
                else
                {
                    if (expression.charAt(i) == '+') {
                        symbol *= 1;
                    }
                    else if (expression.charAt(i) == '-') {
                        symbol *= -1;
                    }
                }
            }
            else
            {
                flag = 0;
            }
        }
        polynomial = expression.substring(j).replaceAll("\\+|-","");
        temp[tempNum++] = (symbol == 1) ? '+' : '-';
        for (int k = 0;k < polynomial.length();k++) {
            temp[tempNum++] = polynomial.charAt(k);
        }
        String answer = new String(temp).trim();
        return answer;
    }

}
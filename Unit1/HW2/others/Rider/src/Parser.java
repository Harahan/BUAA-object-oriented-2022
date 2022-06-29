import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Pattern pattern = Pattern.compile("[\\+|\\-]*\\d+|x|y|z|" +
            "sin\\([\\+\\*\\-x0-9\\s]*\\)|cos\\([\\+\\*\\-x0-9\\s]*\\)");

    private final HashMap<Character, String> functions = new HashMap<>();

    public Operator parse(String expression) {
        if (!expression.equals("")) {
            int position = findAddOrSub(expression);
            if (position != -1) {
                if (expression.charAt(position) == '+') {
                    return new Add(parse(expression.substring(0, position)),
                            parse(expression.substring(position + 1)));
                } else {
                    return new Sub(parse(expression.substring(0, position)),
                            parse(expression.substring(position + 1)));
                }
            } else {
                position = findMul(expression);
                if (position != -1) {
                    return new Mul(parse(expression.substring(0, position))
                            , parse(expression.substring(position + 1)));
                } else {
                    position = findPow(expression);
                    if (position != -1) {
                        int upNum = Integer.parseInt(expression.substring(position + 1));
                        String downNum = expression.substring(0,position - 1);
                        String powTomul = downNum;
                        if (upNum == 0) {
                            return new Num("1");
                        }
                        else if (upNum == 1) {
                            return new Brancket(parse(downNum),parse(""));
                        }
                        else {
                            for (int i = 0;i < upNum - 1;i++) {
                                powTomul = powTomul + "*" + downNum;
                            }
                            return new Mul(parse(powTomul.substring(0, position - 1))
                                    , parse(powTomul.substring(position)));
                        }
                    } else {
                        int position1 = findBrancket1(expression);
                        if (position1 != -1) {
                            int position2 = findBrancket2(expression.substring(0,position1));
                            return new Brancket(parse(expression.substring(position2 + 1,position1))
                                    ,parse(""));
                        } else {
                            Matcher matcher = pattern.matcher(expression);
                            if (matcher.matches()) {
                                String value = expression;
                                return new Num(value);
                            } else {
                                return new Num("");
                            }

                        }
                    }
                }
            }
        }
        return new Num("");
    }

    private int findAddOrSub(String expression) {
        int position = -1;
        int stack = 0;
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (expression.charAt(i) == ')') {
                stack += 1;
            }
            else if (expression.charAt(i) == '(') {
                stack -= 1;
            }
            else if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                if (i != 0) {
                    if (expression.charAt(i - 1) == '*') {
                        continue;
                    }
                    else
                    {
                        if (stack == 0) {
                            position = i;
                            return position;
                        }
                    }
                }
                else
                {
                    position = i;
                    return position;
                }
            }
        }
        return position;
    }

    private int findMul(String expression) {
        int position = -1;
        int stack = 0;
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (expression.charAt(i) == ')') {
                stack += 1;
            }
            else if (expression.charAt(i) == '(') {
                stack -= 1;
            }
            else if (expression.charAt(i) == '*' && expression.charAt(i - 1) == '*') {
                i -= 1;
                continue;
            }
            else if (expression.charAt(i) == '*' && expression.charAt(i - 1) != '*') {
                if (stack == 0) {
                    position = i;
                    return position;
                }
            }
        }
        return position;
    }

    private int findPow(String expression) {
        int position = -1;
        int stack = 0;
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (expression.charAt(i) == ')') {
                stack += 1;
            }
            else if (expression.charAt(i) == '(') {
                stack -= 1;
            }
            else if (expression.charAt(i) == '*' && expression.charAt(i - 1) == '*') {
                if (stack == 0) {
                    position = i;
                    return position;
                }

            }
        }
        return position;
    }

    private int findBrancket1(String expression) {
        int position = -1;
        int flag = 0;
        int stack = 1;
        int i = expression.length() - 1;
        while (i >= 0) {
            if (expression.charAt(i) == ')') {
                int j = 0;
                while (j < i) {
                    if (expression.charAt(i - j - 1) == ')') {
                        stack += 1;
                    }
                    else if (expression.charAt(i - j - 1) == '(') {
                        stack -= 1;
                        if (stack == 0) {
                            if (i - j - 1 > 2) {
                                if (!expression.substring(i - j - 4,i - j - 1).equals("cos") &&
                                        !expression.substring(i - j - 4,i - j - 1).equals("sin")) {
                                    position = i;
                                    return position;
                                }
                                else {
                                    i = i - j - 4;
                                    break;
                                }
                            }
                            else {
                                position = i;
                                return position;
                            }
                        }
                    }
                    j++;
                }
            }
            i--;
        }
        return position;
    }

    private int findBrancket2(String expression) {
        int position = -1;
        int stack = 1;
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (expression.charAt(i) == ')') {
                stack += 1;
            }
            else if (expression.charAt(i) == '(') {
                stack -= 1;
                if (stack == 0) {
                    position = i;
                    return position;
                }

            }
        }
        return position;
    }
}

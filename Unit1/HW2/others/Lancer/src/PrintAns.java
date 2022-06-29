import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

public class PrintAns {
    private final HashMap<HashMap<String, Integer>, BigInteger> ans;

    public PrintAns(HashMap<HashMap<String, Integer>, BigInteger> ans) {
        this.ans = ans;
    }

    public String printans() {
        StringBuilder anss = new StringBuilder();
        for (HashMap<String, Integer> i : ans.keySet()) {
            if (ans.get(i).compareTo(BigInteger.valueOf(0)) > 0) {
                StringBuilder ansss = new StringBuilder();
                printterm(i, ansss);
                anss.append("+").append(ansss);
                ans.remove(i);
                break;
            }
        }
        for (HashMap<String, Integer> i : ans.keySet()) {
            StringBuilder ansss = null;
            if (!ans.get(i).equals(BigInteger.valueOf(0))) {
                ansss = new StringBuilder();
                printterm(i, ansss);
            }
            if (ansss != null) {
                anss.append("+").append(ansss);
            }
        }
        if (anss.length() == 0) {
            anss.append("0");
        } else {
            anss.delete(0, 1);
        }
        String ansb = String.valueOf(anss);
        ansb = ansb.replace("^", "**");
        ansb = ansb.replaceAll("\\x2B-", "-");
        return ansb;

    }

    private void printterm(HashMap<String, Integer> i, StringBuilder ansss) {
        if (ans.get(i).equals(BigInteger.valueOf(-1))) {
            ansss.append("-");
        } else if (!ans.get(i).equals(BigInteger.valueOf(1))) {
            ansss.append(ans.get(i)).append("*");
        }
        for (String j : i.keySet()) {
            if (i.get(j) != 0) {
                if (i.get(j) == 1) {
                    ansss.append(j).append("*");
                } else {
                    if (i.get(j) == 2 && Objects.equals(j, "x")) {
                        ansss.append("x*x").append("*");
                    } else {
                        ansss.append(j).append("**").append(i.get(j)).append("*");
                    }
                }
            }
        }
        if (ansss.length() == 0 || Objects.equals(String.valueOf(ansss), "-")) {
            ansss.append("1");
        } else {
            ansss.delete(ansss.length() - 1, ansss.length());
        }
    }
    /*
        StringBuilder anss = new StringBuilder();
        //第一项输出正数
        for (Integer i : ans.keySet()) {
            if (ans.get(i).compareTo(BigInteger.valueOf(0)) > 0) {
                if (ans.get(i).equals(BigInteger.valueOf(1))) {
                    if (i == 0) {
                        anss.append(ans.get(i)); } else if (i == 1) {
                        anss.append("x"); } else if (i == 2) {
                        anss.append("x*x"); } else if (i >= 3) {
                        anss.append("x**").append(i); }
                } else {
                    anss.append(ans.get(i));
                    if (i == 1) {
                        anss.append("*x"); } else if (i == 2) {
                        anss.append("*x*x"); } else if (i >= 3) {
                        anss.append("*x**").append(i); } }
                ans.remove(i);
                break; } }
        //
        for (Integer i : ans.keySet()) {
            if (!ans.get(i).equals(BigInteger.valueOf(0))) {
                if (i == 0) {
                    anss.append("+").append(ans.get(i));
                } else if (i == 1) {
                    if (ans.get(i).equals(BigInteger.valueOf(1))) {
                        anss.append("+x");
                    } else if (ans.get(i).equals(BigInteger.valueOf(-1))) {
                        anss.append("-x");
                    } else {
                        anss.append("+").append(ans.get(i)).append("*x");
                    }
                } else if (i == 2) {
                    if (ans.get(i).equals(BigInteger.valueOf(1))) {
                        anss.append("+x*x");
                    } else if (ans.get(i).equals(BigInteger.valueOf(-1))) {
                        anss.append("-x*x");
                    } else {
                        anss.append("+").append(ans.get(i)).append("*x*x");
                    }
                } else {
                    if (ans.get(i).equals(BigInteger.valueOf(1))) {
                        anss.append("+x**").append(i);
                    } else if (ans.get(i).equals(BigInteger.valueOf(-1))) {
                        anss.append("-x**").append(i);
                    } else {
                        anss.append("+").append(ans.get(i)).append("*x**").append(i);
                    }
                }
            }
        }
        if (anss.length() == 0) {
            System.out.print(0);
        } else {
            if (anss.charAt(0) == '+') {
                anss.delete(0, 1);
            }
            String ansss = String.valueOf(anss);
            ansss = ansss.replaceAll("\\x2B-", "-");
            System.out.print(ansss);
        }*/
}

import java.math.BigInteger;

public class SumFunc {
    public String displaceSumFuncs(String str) {
        String expr = str;
        while (expr.contains("sum")) {
            int headIndex = expr.indexOf("sum");
            String params = expr.substring(headIndex + 4);
            int paramsLength = 0;
            String temp = params;
            while (temp.contains("(") && temp.indexOf("(") < temp.indexOf(")")) {
                paramsLength = paramsLength + temp.indexOf(")") + 1;
                temp = params.substring(temp.indexOf(")") + 1);
            }
            paramsLength = paramsLength + temp.indexOf(")");
            params = params.substring(0, paramsLength);
            //System.out.println(params);
            String[] paramsList =  params.split(",");
            //String iterator = paramsList[0];
            BigInteger start = new BigInteger(paramsList[1]);
            BigInteger end = new BigInteger(paramsList[2]);
            String sumExpr = paramsList[3];
            //System.out.println(sumExpr + start + end);
            boolean hasInterator = false;
            String out = "(";
            for (int j = 0;j < sumExpr.length();j++) {
                if (sumExpr.charAt(j) == 'i' && (sumExpr.length() == j + 1
                        || sumExpr.charAt(j + 1) != 'n')) {
                    hasInterator = true;
                    break;
                }
            }
            if (!hasInterator) {
                for (BigInteger i = start;i.compareTo(end) <= 0;i = i.add(BigInteger.ONE)) {
                    out = out + "+(" + sumExpr + ")";
                }
            } else {
                for (BigInteger i = start; i.compareTo(end) <= 0; i = i.add(BigInteger.ONE)) {
                    out = out + "+(";
                    for (int j = 0; j < sumExpr.length(); j++) {
                        if (sumExpr.charAt(j) == 'i' && (sumExpr.length() == j + 1
                                || sumExpr.charAt(j + 1) != 'n')) {
                            out = out + i;
                        } else {
                            out = out + sumExpr.charAt(j);
                        }
                    }
                    out = out + ")";
                }
            }
            out = out + ")";
            if (out.equals("()")) { out = "0"; }
            expr = expr.substring(0,headIndex) + out + expr.substring(headIndex + paramsLength + 5);
        }
        return expr;
    }
}

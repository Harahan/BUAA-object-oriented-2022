package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Term {
    private final ArrayList<Factor> factors;
    private int index;

    public Term() {
        this.factors = new ArrayList<>();
        this.index = 0;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
        index++;
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" *");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" *");
            }
        }
        return sb.toString();
    }

    public HashMap<BigInteger,BigInteger> cal() {
        HashMap<BigInteger,BigInteger> term1 = new HashMap<>();
        HashMap<BigInteger,BigInteger> term2 = new HashMap<>();
        HashMap<BigInteger,BigInteger> temp = new HashMap<>();
        term1 = factors.get(0).cal();
        for (int i = 1; i < factors.size(); i++) {
            term2 = factors.get(i).cal();
            Iterator<Map.Entry<BigInteger, BigInteger>> entries1 = term1.entrySet().iterator();
            while (entries1.hasNext()) {
                Iterator<Map.Entry<BigInteger, BigInteger>> entries2 = term2.entrySet().iterator();
                Map.Entry<BigInteger, BigInteger> entry1 = entries1.next();
                BigInteger exp1 = entry1.getKey();
                BigInteger coff1 = entry1.getValue();
                while (entries2.hasNext()) {
                    Map.Entry<BigInteger, BigInteger> entry2 = entries2.next();
                    BigInteger exp2 = entry2.getKey();
                    BigInteger coff2 = entry2.getValue();
                    BigInteger tempExp = exp1.add(exp2);
                    BigInteger tempCoff = coff1.multiply(coff2);
                    if (temp.containsKey(tempExp)) {
                        tempCoff = tempCoff.add(temp.get(tempExp));
                        temp.replace(tempExp,tempCoff);
                    }
                    else {
                        temp.put(tempExp,tempCoff);
                    }
                }
            }
            term1.clear();
            term1.putAll(temp);
            temp.clear();
        }
        return term1;
    }

    public void deleteFactor() {
        index--;
        this.factors.remove(index);
    }
}

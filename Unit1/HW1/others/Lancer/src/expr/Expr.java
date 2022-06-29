package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" +");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" +");
            }
        }
        return sb.toString();
    }

    public HashMap<BigInteger,BigInteger> cal() {
        HashMap<BigInteger,BigInteger> term1 = new HashMap<>();
        HashMap<BigInteger,BigInteger> term2 = new HashMap<>();
        term1 = terms.get(0).cal();
        for (int i = 1; i < terms.size(); i++) {
            term2 = terms.get(i).cal();
            Iterator<Map.Entry<BigInteger, BigInteger>> entries2 = term2.entrySet().iterator();
            while (entries2.hasNext()) {
                Map.Entry<BigInteger, BigInteger> entry2 = entries2.next();
                BigInteger exp2 = entry2.getKey();
                BigInteger coff2 = entry2.getValue();
                if (term1.containsKey(exp2)) {
                    BigInteger coff1 = term1.get(exp2);
                    term1.replace(exp2,coff2.add(coff1));
                }
                else {
                    term1.put(exp2,coff2);
                }
            }
        }
        return term1;
    }

    public void print() {
        boolean isBlank = true;
        HashMap<BigInteger,BigInteger> result = cal();
        boolean isZero = false;
        for (BigInteger i : result.keySet()) {
            if (result.get(i).compareTo(new BigInteger("0")) != 0) {
                isBlank = false;//系数不等于0
                if (i.toString().equals("0")) {
                    if (!isZero || result.get(i).compareTo(new BigInteger("0")) < 0) {
                        isZero = true;//常数项不是0
                        System.out.print(result.get(i));
                    } else { System.out.print("+" + result.get(i)); }
                } else if (result.get(i).compareTo(new BigInteger("0")) < 0) { //系数为负数
                    if (result.get(i).compareTo(new BigInteger("-1")) == 0) { //系数为-1
                        if (i.compareTo(new BigInteger("1")) == 0) {
                            System.out.print("-x");//次数为1
                        } else if (i.compareTo(new BigInteger("2")) == 0) {
                            System.out.print("-x*x");//次数为2
                        } else { System.out.print("-x**" + i); }
                    } else { if (i.compareTo(new BigInteger("1")) == 0) {
                            System.out.print(result.get(i) + "*x");
                        } else if (i.compareTo(new BigInteger("2")) == 0) {
                            System.out.print(result.get(i) + "*x*x");
                        } else { System.out.print(result.get(i) + "*x**" + i); }
                    }
                    isZero = true;
                } else { if (!isZero) {
                        if (result.get(i).compareTo(new BigInteger("1")) == 0) {
                            if (i.compareTo(new BigInteger("1")) == 0) {
                                System.out.print("x");
                            } else if (i.compareTo(new BigInteger("2")) == 0) {
                                System.out.print("x*x"); }
                            else { System.out.print("x**" + i); }
                        } else {
                            if (i.compareTo(new BigInteger("1")) == 0) {
                                System.out.print(result.get(i) + "*x");
                            } else if (i.compareTo(new BigInteger("2")) == 0) {
                                System.out.print(result.get(i) + "*x*x");
                            } else { System.out.print(result.get(i) + "*x**" + i); }
                        }
                        isZero = true;
                    } else {
                        if (result.get(i).compareTo(new BigInteger("1")) == 0) {
                            if (i.compareTo(new BigInteger("1")) == 0) {
                                System.out.print("+" + "x"); }
                            else if (i.compareTo(new BigInteger("2")) == 0) {
                                System.out.print("+" + "x*x");
                            } else { System.out.print("+" + "x**" + i); }
                        } else { if (i.compareTo(new BigInteger("1")) == 0) {
                                System.out.print("+" + result.get(i) + "*x"); }
                            else if (i.compareTo(new BigInteger("2")) == 0) {
                                System.out.print("+" + result.get(i) + "*x*x");
                            } else { System.out.print("+" + result.get(i) + "*x**" + i); }
                        }
                        isZero = true;
                    }
                }
            }
        } if (isBlank) { System.out.println(0); }
    }
}

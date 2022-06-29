import java.math.BigInteger;
import java.util.HashMap;

public class Expr {
    private HashMap<Integer,BigInteger> expr = new HashMap<>();

    public void addTerm(int index, BigInteger term) {
        if (this.expr.containsKey(index)) {
            this.expr.put(index,term.add(this.expr.get(index)));
        }
        else {
            this.expr.put(index,term);
        }
    }

    public void calculateM(HashMap<Integer,BigInteger> another) {
        HashMap<Integer, BigInteger> tmp = new HashMap<>();
        for (Integer i : this.expr.keySet()) {
            for (Integer j : another.keySet()) {
                if (tmp.containsKey(i + j)) {
                    tmp.put(i + j, tmp.get(i + j).add(expr.get(i).multiply(another.get(j))));
                }
                else {
                    tmp.put(i + j, expr.get(i).multiply(another.get(j)));
                }
            }
        }
        expr.clear();
        expr = (HashMap<Integer, BigInteger>)tmp.clone();
    }

    public void calculateA(HashMap<Integer,BigInteger> another) {
        HashMap<Integer,BigInteger> tmp = (HashMap<Integer,BigInteger>)this.expr.clone();
        for (Integer j :another.keySet()) {
            if (tmp.containsKey(j)) {
                tmp.put(j,tmp.get(j).add(another.get(j)));
            }
            else {
                tmp.put(j,another.get(j));
            }
        }
        expr.clear();
        expr = (HashMap<Integer, BigInteger>)tmp.clone();
    }

    public void calculateS(HashMap<Integer,BigInteger> another) {
        HashMap<Integer,BigInteger> tmp = (HashMap<Integer,BigInteger>)this.expr.clone();
        for (Integer j :another.keySet()) {
            if (tmp.containsKey(j)) {
                tmp.put(j,tmp.get(j).subtract(another.get(j)));
            }
            else {
                tmp.put(j,another.get(j).negate());
            }
        }
        expr.clear();
        expr = (HashMap<Integer, BigInteger>)tmp.clone();
    }

    public HashMap<Integer,BigInteger> getExpr() {
        return this.expr;
    }

    public void negateExpr() {
        for (Integer i:expr.keySet()) {
            expr.put(i,expr.get(i).negate());
        }
    }
}

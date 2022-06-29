import java.math.BigInteger;
import java.util.HashSet;

public class Term {
    private HashSet<Factor> factors;
    private HashSet<Variable> variables;
    private boolean value;

    public Term(boolean value) {
        this.factors = new HashSet<>();
        this.variables = new HashSet<>();
        this.value = value;
        if (this.value) {
            this.variables.add(new Variable(new BigInteger("1"),0));
        }
        else {
            this.variables.add(new Variable(new BigInteger("-1"),0));
        }
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public HashSet<Variable> getVariables() {
        return variables;
    }

    public void simplity() {
        BigInteger zero = new BigInteger("0");
        for (Factor item : factors) {
            if (item instanceof Variable) {
                Variable newItem = (Variable) item;
                for (Variable variable : variables) {
                    variable.setPower(variable.getPower() + newItem.getPower());
                    variable.setCoefficient(variable.getCoefficient().
                            multiply(newItem.getCoefficient()));
                }
            } else {
                Expr expr = (Expr) item;
                expr.simplify(false);
                BigInteger[] newList = new BigInteger[9];
                for (int i = 0; i < 9; i++) {
                    newList[i] = zero;
                }
                for (Variable variable : variables) {
                    for (int i = 0; i < 9; i++) {
                        if (i + variable.getPower() < 9) {
                            newList[i + variable.getPower()] = newList[i + variable.getPower()].
                                    add(variable.getCoefficient().multiply(expr.getBoxs().get(i)));
                        }
                    }
                }
                this.variables = new HashSet<>();
                for (int i = 0; i < 9; i++) {
                    if (newList[i].compareTo(zero) != 0) {
                        this.variables.add(new Variable(newList[i], i));
                    }
                }
            }
        }

    }
}

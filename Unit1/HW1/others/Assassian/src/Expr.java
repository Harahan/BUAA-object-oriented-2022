import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

public class Expr implements Factor {
    private int power;
    private HashSet<Term> terms;
    private HashMap<Integer, BigInteger> boxs;

    public void setPower(int power) {
        this.power = power;
    }

    public Expr() {
        this.terms = new HashSet<>();
        this.boxs = new HashMap<>();
        for (int i = 0;i < 9;i++) {
            this.boxs.put(i,new BigInteger("0"));
        }
        this.power = 1;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public HashMap<Integer, BigInteger> getBoxs() {
        return boxs;
    }

    public void simplify(boolean fack) {
        BigInteger zero = new BigInteger("0");
        for (Term item : this.terms) {
            item.simplity();
            for (Variable variable : item.getVariables()) {
                this.boxs.put(variable.getPower(), variable.getCoefficient().
                        add(this.boxs.get(variable.getPower())));
            }
        }
        if (this.power != 0) {
            BigInteger[] list = new BigInteger[9];
            for (int i = 0; i < 9; i++) {
                list[i] = this.boxs.get(i);
            }
            for (int m = 0;m < this.power - 1;m++) {
                BigInteger[] newlist = new BigInteger[9];
                for (int i = 0;i < 9;i++) {
                    newlist[i] = zero;
                }
                for (int key = 0;key < 9;key++) {
                    for (int j = 0;j < 9;j++) {
                        if (j + key < 9 && !list[j].equals(zero)) {
                            newlist[j + key] = newlist[j + key].add(
                                    list[j].multiply(this.boxs.get(key)));
                        }
                    }
                }
                System.arraycopy(newlist, 0, list, 0, 9);
            }
            for (int i = 0;i < 9;i++) {
                this.boxs.put(i,list[i]);
            }
        }
        else  {
            for (int i = 1;i < 9;i++) {
                this.boxs.put(i,zero);
            }
            this.boxs.put(0,new BigInteger("1"));
        }
        if (fack) {
            this.tosing();
        }
    }

    public void tosing() {
        int check = 0;
        for (int i = 0; i <= 8; i++) {
            if (this.boxs.get(i).compareTo(new BigInteger("0")) > 0) {
                check = 1;
                if (i == 0) {
                    System.out.print(this.boxs.get(i));
                }
                else {
                    if (!this.boxs.get(i).equals(new BigInteger("1"))) {
                        System.out.print(this.boxs.get(i) + "*");
                    }
                    if (i == 1) {
                        System.out.print("x");
                    }
                    else if (i == 2) {
                        System.out.print("x*x");
                    }
                    else {
                        System.out.print("x" + "**" + i);
                    }
                }
                this.boxs.put(i,new BigInteger("0"));
                break;
            }
        }
        if (this.boxs.get(0).compareTo(new BigInteger("0")) != 0) {
            check = 1;
            System.out.print(this.boxs.get(0));
        }
        for (int i = 1; i <= 8; i++) {
            if (this.boxs.get(i).compareTo(new BigInteger("0")) != 0) {
                check = 1;
                if (this.boxs.get(i).compareTo(new BigInteger("0")) > 0) {
                    System.out.print("+");
                }
                if (this.boxs.get(i).equals(new BigInteger("-1"))) {
                    System.out.print("-");
                }
                else if (!this.boxs.get(i).equals(new BigInteger("1"))) {
                    System.out.print(this.boxs.get(i) + "*");
                }
                if (i == 1) {
                    System.out.print("x");
                }
                else if (i == 2) {
                    System.out.print("x*x");
                }
                else {
                    System.out.print("x" + "**" + i);
                }
            }
        }
        if (check == 0) {
            System.out.print("0");
        }
    }
}
//(+ - -01 * x ** +02 - 5 * x**1 * x * -01) ** 3
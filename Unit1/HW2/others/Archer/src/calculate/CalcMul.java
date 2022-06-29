package calculate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

// coeff, var ** power, sin(factor) ** power, cos(factor) ** power

public class CalcMul implements Calculate {
    private final BigInteger coeff;
    private final HashMap<String, Integer> vars;
    private final HashMap<Calculate, Integer> sins;
    private final HashMap<Calculate, Integer> coss;

    public static final CalcMul ZERO = CalcMul.newDec(BigInteger.ZERO);

    private CalcMul(BigInteger coeff, HashMap<String, Integer> vars,
            HashMap<Calculate, Integer> sins, HashMap<Calculate, Integer> coss) {
        this.coeff = coeff;
        this.vars = vars;
        this.sins = sins;
        this.coss = coss;
    }

    public static CalcMul newDec(BigInteger ncoeff) {
        HashMap<String, Integer> nvars = new HashMap<>();
        HashMap<Calculate, Integer> nsins = new HashMap<>();
        HashMap<Calculate, Integer> ncoss = new HashMap<>();
        return new CalcMul(ncoeff, nvars, nsins, ncoss);
    }

    public static CalcMul newVar(String name, Integer power) {
        BigInteger ncoeff = BigInteger.ONE;
        HashMap<String, Integer> nvars = new HashMap<>();
        HashMap<Calculate, Integer> nsins = new HashMap<>();
        HashMap<Calculate, Integer> ncoss = new HashMap<>();
        nvars.put(name, power);
        return new CalcMul(ncoeff, nvars, nsins, ncoss);
    }

    public static CalcMul newSin(Calculate sinInner, Integer power) {
        BigInteger ncoeff = BigInteger.ONE;
        HashMap<String, Integer> nvars = new HashMap<>();
        HashMap<Calculate, Integer> nsins = new HashMap<>();
        HashMap<Calculate, Integer> ncoss = new HashMap<>();
        nsins.put(sinInner, power);
        return new CalcMul(ncoeff, nvars, nsins, ncoss);
    }

    public static CalcMul newCos(Calculate cosInner, Integer power) {
        BigInteger ncoeff = BigInteger.ONE;
        HashMap<String, Integer> nvars = new HashMap<>();
        HashMap<Calculate, Integer> nsins = new HashMap<>();
        HashMap<Calculate, Integer> ncoss = new HashMap<>();
        ncoss.put(cosInner, power);
        return new CalcMul(ncoeff, nvars, nsins, ncoss);
    }

    @SuppressWarnings("unchecked")
    public CalcMul pow(int power) {
        if (power == 1) {
            return this;
        }
        // coeff power, exponent multiply
        BigInteger ncoeff = this.coeff.pow(power);
        if (ncoeff.equals(BigInteger.ZERO)) {
            return CalcMul.ZERO;
        }
        HashMap<String, Integer> nvars = (HashMap<String, Integer>) this.vars.clone();
        HashMap<Calculate, Integer> nsins = (HashMap<Calculate, Integer>) this.sins.clone();
        HashMap<Calculate, Integer> ncoss = (HashMap<Calculate, Integer>) this.coss.clone();
        nvars.replaceAll(new BiFunction<String, Integer, Integer>() {
            public Integer apply(String t, Integer u) {
                return u * power;
            }
        });
        nsins.replaceAll(new BiFunction<Calculate, Integer, Integer>() {
            public Integer apply(Calculate t, Integer u) {
                return u * power;
            }
        });
        ncoss.replaceAll(new BiFunction<Calculate, Integer, Integer>() {
            public Integer apply(Calculate t, Integer u) {
                return u * power;
            }
        });
        return new CalcMul(ncoeff, nvars, nsins, ncoss);
    }

    public boolean bodyEquals(CalcMul rhsMul) {
        return this.vars.equals(rhsMul.vars) //
                && this.sins.equals(rhsMul.sins) //
                && this.coss.equals(rhsMul.coss);
    }

    public Calculate substitute(HashMap<String, Calculate> varValues) {
        Calculate varFinalResult = CalcMul.newDec(this.coeff);

        for (Entry<String, Integer> entry : this.vars.entrySet()) {
            String varName = entry.getKey();
            Integer varPower = entry.getValue();
            Calculate varValue = varValues.get(varName);
            if (varValue == null) {
                // not present, keep it
                varFinalResult = varFinalResult.mul(CalcMul.newVar(varName, varPower));
            } else {
                // present, multiply it
                varFinalResult = varFinalResult.mul(varValue.pow(varPower));
            }
        }

        for (Entry<Calculate, Integer> entry : this.sins.entrySet()) {
            varFinalResult = varFinalResult
                    .mul(CalcMul.newSin(entry.getKey().substitute(varValues), entry.getValue()));
        }

        for (Entry<Calculate, Integer> entry : this.coss.entrySet()) {
            varFinalResult = varFinalResult
                    .mul(CalcMul.newCos(entry.getKey().substitute(varValues), entry.getValue()));
        }

        return varFinalResult;
    }

    public Calculate add(Calculate rhs) {
        if (rhs.equals(CalcMul.ZERO)) {
            return this;
        }
        if (this.equals(CalcMul.ZERO)) {
            return rhs;
        }
        if (rhs instanceof CalcMul) {
            CalcMul rhsMul = (CalcMul) rhs;
            if (this.bodyEquals(rhsMul)) {
                BigInteger ncoeff = this.coeff.add(rhsMul.coeff);
                if (ncoeff.equals(BigInteger.ZERO)) {
                    return CalcMul.ZERO;
                }
                return new CalcMul(ncoeff, //
                        this.vars, //
                        this.sins, //
                        this.coss //
                );
            }
            return CalcSum.fromTwoMul(this, (CalcMul) rhs);
        }
        if (rhs instanceof CalcSum) {
            // swap
            return ((CalcSum) rhs).add(this);
        }
        throw new RuntimeException("impossible.");
    }

    @SuppressWarnings("unchecked")
    public Calculate mul(Calculate rhs) {
        if (rhs instanceof CalcSum) {
            return ((CalcSum) rhs).mul(this);
        } else if (!(rhs instanceof CalcMul)) {
            throw new RuntimeException("impossible.");
        }
        CalcMul rhsMul = (CalcMul) rhs;
        BigInteger ncoeff = this.coeff.multiply(rhsMul.coeff);
        if (ncoeff.equals(BigInteger.ZERO)) {
            return CalcMul.ZERO;
        }
        HashMap<String, Integer> nvars = (HashMap<String, Integer>) this.vars.clone();
        HashMap<Calculate, Integer> nsins = (HashMap<Calculate, Integer>) this.sins.clone();
        HashMap<Calculate, Integer> ncoss = (HashMap<Calculate, Integer>) this.coss.clone();
        rhsMul.vars.forEach(new BiConsumer<String, Integer>() {
            public void accept(String t, Integer u) {
                Integer p = nvars.get(t);
                if (p == null) {
                    p = u;
                } else {
                    p = u + p;
                }
                if (p == 0) {
                    nvars.remove(t);
                } else {
                    nvars.put(t, p);
                }
            }
        });
        rhsMul.sins.forEach(new BiConsumer<Calculate, Integer>() {
            public void accept(Calculate t, Integer u) {
                Integer p = nsins.get(t);
                if (p == null) {
                    p = u;
                } else {
                    p = u + p;
                }
                if (p == 0) {
                    nsins.remove(t);
                } else {
                    nsins.put(t, p);
                }
            }
        });
        rhsMul.coss.forEach(new BiConsumer<Calculate, Integer>() {
            public void accept(Calculate t, Integer u) {
                Integer p = ncoss.get(t);
                if (p == null) {
                    p = u;
                } else {
                    p = u + p;
                }
                if (p == 0) {
                    ncoss.remove(t);
                } else {
                    ncoss.put(t, p);
                }
            }
        });
        return new CalcMul(ncoeff, nvars, nsins, ncoss);
    }

    public String toString() {
        if (this.coeff.equals(BigInteger.ZERO)) {
            return "+0";
        }
        ArrayList<String> segments = new ArrayList<>();
        String sign = "+";
        BigInteger coeffAbs = this.coeff.abs();
        if (this.coeff.compareTo(BigInteger.ZERO) < 0) {
            sign = "-";
        }
        if (!coeffAbs.equals(BigInteger.ONE)) {
            segments.add(coeffAbs.toString());
        }
        this.vars.forEach(new BiConsumer<String, Integer>() {
            public void accept(String t, Integer u) {
                if (u == 1) {
                    segments.add(t);
                } else {
                    segments.add(t + "**" + u);
                }
            }
        });
        this.sins.forEach(new BiConsumer<Calculate, Integer>() {
            public void accept(Calculate t, Integer u) {
                String inner = "sin(" + t.toString().replaceFirst("^\\+*", "") + ")";
                if (u == 1) {
                    segments.add(inner);
                } else {
                    segments.add(inner + "**" + u);
                }
            }
        });
        this.coss.forEach(new BiConsumer<Calculate, Integer>() {
            public void accept(Calculate t, Integer u) {
                String inner = "cos(" + t.toString().replaceFirst("^\\+*", "") + ")";
                if (u == 1) {
                    segments.add(inner);
                } else {
                    segments.add(inner + "**" + u);
                }
            }
        });
        if (segments.size() < 1) {
            return sign + "1";
        } else {
            return sign + String.join("*", segments);
        }
    }

    // for hashmap
    public boolean equals(Object other) {
        if (!(other instanceof CalcMul)) {
            return false;
        }
        CalcMul rhsMul = (CalcMul) other;
        return this.coeff.equals(rhsMul.coeff) && this.bodyEquals(rhsMul);
    }

    public void printBody() {
        System.out.println("VARS: " + this.vars);
        System.out.println("SINS: " + this.sins);
        System.out.println("COSS: " + this.coss);
    }

    // fuck hashmap
    public int hashCode() {
        return 0;
    }

    public CalcMul neg() {
        return new CalcMul(this.coeff.negate(), this.vars, this.sins, this.coss);
    }
}

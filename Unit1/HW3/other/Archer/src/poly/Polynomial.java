package poly;

import java.math.BigInteger;
import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;

public class Polynomial {
    private ArrayList<PolyTerm> polyTerms;

    private final LinkedHashMap<HashMap<String, Integer>, BigInteger> answer;

    public Polynomial() {
        polyTerms = new ArrayList<>();
        answer = new LinkedHashMap<>();
    }

    public boolean isSingle() {
        boolean singleVar = (this.polyTerms.size() == 1) &&
                (this.polyTerms.get(0).getSize() == 1) &&
                (this.polyTerms.get(0).getCoefficient().equals(BigInteger.ONE));
        boolean singleNumber = (this.polyTerms.size() == 1) &&
                (this.polyTerms.get(0).getSize() == 0);
        return singleVar || singleNumber;
    }

    public void add(PolyTerm polyTerm) {
        this.polyTerms.add(polyTerm);
    }

    public void combine(Polynomial polynomial) {
        this.polyTerms.addAll(polynomial.polyTerms);
        //this.partUnit();
    }

    public void reverse() {
        for (PolyTerm polyTerm : this.polyTerms) {
            polyTerm.setCoefficient(polyTerm.getCoefficient().negate());
        }
    }

    public void multiply(Polynomial polynomial) {
        ArrayList<PolyTerm> newPolyTerms = new ArrayList<>();
        for (PolyTerm pt1 : this.polyTerms) {
            for (PolyTerm pt2 : polynomial.polyTerms) {
                PolyTerm polyTerm = pt1.multiply(pt2);
                newPolyTerms.add(polyTerm);
            }
        }
        this.polyTerms = newPolyTerms;
        //this.partUnit();
    }

    /*
    private void partUnit() {
        this.unit();
        ArrayList<PolyTerm> newPolyTerms2 = new ArrayList<>();
        this.answer.forEach((key, value) -> {
            PolyTerm newPoly = new PolyTerm(value, key);
            newPolyTerms2.add(newPoly);
        });
        this.polyTerms = newPolyTerms2;
    }


     */

    public void trigSimplify() {
        HashMap<String, Integer> sin = new HashMap<>();
        sin.put("sin(x)", 2);
        HashMap<String, Integer> cos = new HashMap<>();
        cos.put("cos(x)", 2);
        if (this.answer.containsKey(sin) && this.answer.containsKey(cos)
                && this.answer.get(sin).equals(this.answer.get(cos))) {
            HashMap<String, Integer> constant = new HashMap<>();
            this.answer.put(constant, this.answer.get(sin));
            this.answer.remove(sin);
            this.answer.remove(cos);
        }
    }

    public void sinSimplify() {
        for (HashMap<String, Integer> element : this.answer.keySet()) {
            if (element.containsKey("sin(x)") && element.get("sin(x)") == 1) {
                if (element.containsKey("cos(x)") && element.get("cos(x)") == 1) {
                    this.answer.replace(element,
                            this.answer.get(element).divide(BigInteger.valueOf(2)));
                    element.remove("sin(x)");
                    element.remove("cos(x)");
                    element.put("sin((2*x))", 1);
                }
            }
        }
    }

    public void cosSimplify() {
        HashMap<String, Integer> sin = new HashMap<>();
        sin.put("sin(x)", 2);
        HashMap<String, Integer> cos = new HashMap<>();
        cos.put("cos(x)", 2);
        if (this.answer.containsKey(sin) && this.answer.containsKey(cos)
                && (this.answer.get(sin).add(this.answer.get(cos))).equals(BigInteger.ZERO)) {
            HashMap<String, Integer> doublecos = new HashMap<>();
            doublecos.put("cos((2*x))", 1);
            this.answer.put(doublecos, this.answer.get(cos));
            this.answer.remove(sin);
            this.answer.remove(cos);
        }
    }

    public void simplify() {

        HashMap<String, Integer> opposite = new HashMap<>();
        HashMap<String, Integer> combined = new HashMap<>();

        Iterator<HashMap<String, Integer>> iter = this.answer.keySet().iterator();

        if (iter.hasNext()) {
            HashMap<String, Integer> original = iter.next();
            for (String key : original.keySet()) {
                if (key.matches("^sin\\((.*)\\)$") && original.get(key) == 2) {
                    opposite.putAll(original);
                    opposite.remove(key);
                    opposite.put(key.replace("sin", "cos"), original.get(key));

                    combined.putAll(original);
                    combined.remove(key);
                    break;
                } else if (key.matches("^cos\\((.*)\\)$") && original.get(key) == 2) {
                    opposite.putAll(original);
                    opposite.put(key.replace("cos", "sin"), original.get(key));
                    opposite.remove(key);
                    combined.putAll(original);
                    combined.remove(key);
                    break;
                }
            }

            if (!opposite.isEmpty() && this.answer.containsKey(opposite)) {
                if (this.answer.get(opposite).equals(this.answer.get(original))) {
                    this.answer.merge(combined, this.answer.get(original), BigInteger::add);
                    this.answer.remove(original);
                    this.answer.remove(opposite);
                }
            }
        }
    }

    public void simplify2() {

        HashMap<String, Integer> opposite = new HashMap<>();
        HashMap<String, Integer> combined = new HashMap<>();

        Iterator<HashMap<String, Integer>> iter = this.answer.keySet().iterator();

        if (iter.hasNext()) {
            int tag = 0;
            HashMap<String, Integer> original = iter.next();
            for (String key : original.keySet()) {
                if (key.matches("^sin\\((.*)\\)$") && original.get(key) == 2) {
                    opposite.putAll(original);
                    opposite.put(key.replace("sin", "cos"), original.get(key));
                    opposite.remove(key);

                    combined.putAll(original);
                    combined.remove(key);
                    combined.put("cos((2*x))", 1);
                    break;

                } else if (key.matches("^cos\\((.*)\\)$") && original.get(key) == 2) {
                    tag = 1;
                    opposite.putAll(original);
                    opposite.put(key.replace("cos", "sin"), original.get(key));
                    opposite.remove(key);

                    combined.putAll(original);
                    combined.remove(key);
                    combined.put("cos((2*x))", 1);
                    break;
                }
            }

            if (!opposite.isEmpty() && this.answer.containsKey(opposite)) {
                if ((this.answer.get(opposite).add(this.answer.get(original)))
                        .equals(BigInteger.ZERO)) {
                    if (tag == 1) {
                        this.answer.merge(combined, this.answer.get(original), BigInteger::add);
                    } else {
                        this.answer.merge(combined, this.answer.get(opposite), BigInteger::add);
                    }

                    this.answer.remove(original);
                    this.answer.remove(opposite);
                }
            }
        }
    }

    public void simplify3() {
        for (HashMap<String, Integer> element : this.answer.keySet()) {
            if (element.containsKey("sin(x)") && element.containsKey("cos(x)") &&
                    element.get("sin(x)").equals(element.get("cos(x)"))) {
                int power = element.get("sin(x)");
                BigInteger divider = BigInteger.valueOf(2).pow(power);
                BigInteger[] newCo = this.answer.get(element).divideAndRemainder(divider);
                if (newCo[1].equals(BigInteger.ZERO)) {
                    this.answer.replace(element, newCo[0]);
                    element.remove("sin(x)");
                    element.remove("cos(x)");
                    element.put("sin((2*x))", power);
                }
            }
        }
    }

    public void unit() {

        if (! this.polyTerms.isEmpty()) {
            for (int i = 0; i < this.polyTerms.size(); i++) {
                if (this.polyTerms.get(i).getCoefficient().compareTo(BigInteger.ZERO) < 0) {
                    PolyTerm tmpPt = this.polyTerms.get(i);
                    this.polyTerms.remove(i);
                    this.polyTerms.add(tmpPt);
                } else {
                    break;
                }
            }
        }

        for (PolyTerm polyTerm : this.polyTerms) {
            HashMap<String, Integer> key = polyTerm.getElements();

            if (answer.containsKey(key)) {
                BigInteger newCo = answer.get(key).add(polyTerm.getCoefficient());
                answer.replace(key, newCo);
            } else {
                answer.put(key, polyTerm.getCoefficient());
            }
        }

    }

    public String printElements(HashMap<String, Integer> hashMap) {
        StringBuilder sb = new StringBuilder();

        hashMap.forEach((key, value) -> {
            if (value == 1) {
                sb.append(key);
                sb.append("*");
            } else if (value != 0) {
                sb.append(key).append("**").append(value);
                sb.append("*");
            }
        });

        String polyTerm = sb.toString();
        String polyTerm1 = "";

        if (polyTerm.length() >= 1) {
            polyTerm1 = polyTerm.substring(0, polyTerm.length() - 1);
        }

        return polyTerm1;

    }

    public String toString() {
        this.unit();
        this.simplify();
        //this.trigSimplify();
        this.simplify3();
        //this.sinSimplify();
        this.simplify2();
        //this.cosSimplify();

        StringBuilder sb = new StringBuilder();

        answer.forEach((key, value) -> {
            if (value.equals(BigInteger.ONE)) {
                sb.append("+");
                if (! key.isEmpty()) {
                    sb.append(printElements(key));
                } else {
                    sb.append("1");
                }

            } else if (value.compareTo(BigInteger.ZERO) > 0) {
                sb.append("+");
                sb.append(value.abs());
                if (! key.isEmpty()) {
                    sb.append("*");
                    sb.append(printElements(key));
                }

            } else if (value.equals(BigInteger.valueOf(-1))) {
                sb.append("-");
                if (!key.isEmpty()) {
                    sb.append(printElements(key));
                } else {
                    sb.append("1");
                }

            } else if (value.compareTo(BigInteger.ZERO) < 0) {
                sb.append("-");
                sb.append(value.abs());
                if (! key.isEmpty()) {
                    sb.append("*");
                    sb.append(printElements(key));
                }

            }
        });

        if (sb.length() == 0) {
            sb.append("0");
        }

        String formatAnswer = sb.toString()
                .replaceFirst("(^[+])", "");

        // return sb.toString();
        return formatAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Polynomial)) {
            return false;
        }
        Polynomial that = (Polynomial) o;
        return Objects.equals(polyTerms, that.polyTerms) && Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(polyTerms, answer);
    }
}



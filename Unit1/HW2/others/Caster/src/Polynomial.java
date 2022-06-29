import java.math.BigInteger;
import java.util.HashMap;

public class Polynomial {
    public HashMap<BodyMap,BigInteger> getPolyTerms() {
        return polyTerms;
    }
    
    private HashMap<BodyMap,BigInteger> polyTerms;
    
    public Polynomial() {
        this.polyTerms = new HashMap<>();
    }
    
    public void extend(BodyMap bodyMap,BigInteger factor) {
        if (this.polyTerms.containsKey(bodyMap)) {
            //to be coutinue
            this.polyTerms.put(bodyMap,this.polyTerms.get(bodyMap).add(factor));
        } else {
            this.polyTerms.put(bodyMap,factor);
        }
    }
    
    public Polynomial mult(Polynomial mltper) {
        HashMap<BodyMap,BigInteger> mltperMap = mltper.getPolyTerms();
        Polynomial ans = new Polynomial();
        for (BodyMap a : this.polyTerms.keySet()) {
            for (BodyMap b : mltperMap.keySet()) {
                BodyMap temp = a.mult(b);
                BigInteger newFactor = this.polyTerms.get(a).multiply(mltperMap.get(b));
                if (newFactor.compareTo(BigInteger.ZERO) == 0) {
                    BodyMap one = new BodyMap("1",BigInteger.ZERO);
                    ans.extend(one,BigInteger.ZERO);
                } else {
                    ans.extend(temp,newFactor);
                }
            }
        }
        return ans;
    }
    
    public Polynomial add(Polynomial adder) {
        HashMap<BodyMap,BigInteger> adderMap = adder.getPolyTerms();
        for (BodyMap b : adderMap.keySet()) {
            this.extend(b,adderMap.get(b));
        }
        return this;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String head;
        String tail;
        boolean isPositiveHead = false;
        for (BodyMap i : polyTerms.keySet()) {
            HashMap<String,BigInteger> bodyMap = i.getBodyMap();
            BigInteger factor = polyTerms.get(i);
            boolean isPositive = factor.compareTo(BigInteger.ZERO) > 0;
            head = factor.equals(BigInteger.ZERO) ? "" :
                    factor.equals(BigInteger.ONE) ? "+" :
                    factor.equals(BigInteger.valueOf(-1)) ? "-" :
                    isPositive ? ("+" + factor + "*") :
                    (factor + "*");
            if (factor.equals(BigInteger.ZERO)) { tail = ""; }
            else {
                tail = "";
                for (String body : bodyMap.keySet()) {
                    BigInteger index =  bodyMap.get(body);
                    if (index.compareTo(BigInteger.ZERO) == 0) { tail = tail + "1*"; }
                    else if (index.compareTo(BigInteger.ONE) == 0) { tail = tail + body + "*"; }
                    else { tail = tail + body  + "**" + index + "*"; }
                }
                if (tail.substring(tail.length() - 1).equals("*")) {
                    tail = tail.substring(0,tail.length() - 1);
                }
                if ((tail.length() >= 2 && tail.substring(tail.length() - 2).equals("*1"))
                    && !(tail.length() >= 3 && tail.substring(tail.length() - 3).equals("**1"))) {
                    tail = tail.substring(0,tail.length() - 2);
                }
            }
            if (!isPositiveHead && head.startsWith("+")) {
                sb.insert(0,tail).insert(0,head);
                isPositiveHead = true;
            } else {
                sb.append(head);
                sb.append(tail);
            }
            String temp = sb.toString();
            temp = temp.replace("+1*","+");
            temp = temp.replace("-1*","-");
            temp = temp.replace("*1*","*");
            temp = temp.replace("*1+","+");
            temp = temp.replace("*1-","-");
            temp = temp.replace("**1","");
            if (temp.startsWith("1*")) { temp = temp.substring(2); }
            sb.replace(0,sb.length(),temp);
        }
        String str = sb.toString();
        if (str.equals("")) {
            return "0";
        } else if (str.startsWith("+")) {
            return str.substring(1);
        }
        return str.substring(0,str.length());
    }
    
}

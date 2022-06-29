import java.math.BigInteger;

public class TriangleFunc implements Factor {
    private BigInteger index;
    private boolean neg;
    private String type;
    private String body;
    // -sin(x^3)**2 >> 2 , true , sin , x^3
    
    public TriangleFunc(String type, String body, BigInteger index, boolean neg) {
        this.body = body;
        while (this.body.startsWith("(")) {
            this.body = this.body.substring(1,this.body.length() - 1);
            this.body = this.body.replace("^", "**");
        }
        this.body = "(" + this.body + ")";
        this.type = type;
        this.index = index;
        this.neg = neg;
        //System.out.println(type + body + index + neg);
    }
    
    public Polynomial toPolynomial() {
        if (type.equals("sin") && body.startsWith("(-")) {
            neg = !neg;
            body = "(" + body.substring(2);
        } else if (type.equals("cos") && body.startsWith("(-")) {
            body = "(" + body.substring(2);
        }
        int factor = neg ? -1 : 1;
        Polynomial triangleFunc = new Polynomial();
        if (type.equals("sin") && body.equals("0")) {
            BodyMap bodyMap = new BodyMap("1",BigInteger.ZERO);
            triangleFunc.extend(bodyMap,BigInteger.ZERO);
        } else if ((type.equals("cos") && body.equals("0"))
                  || (index.compareTo(BigInteger.ZERO) == 0)) {
            BodyMap bodyMap = new BodyMap("1",BigInteger.ZERO);
            triangleFunc.extend(bodyMap,BigInteger.valueOf(factor));
        } else {
            BodyMap bodyMap = new BodyMap(type + body,index);
            triangleFunc.extend(bodyMap,BigInteger.valueOf(factor));
        }
        return triangleFunc;
    }
    
}

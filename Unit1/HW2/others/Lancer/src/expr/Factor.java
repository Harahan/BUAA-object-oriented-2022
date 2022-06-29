package expr;

import java.math.BigInteger;
import java.util.HashMap;

public interface Factor {
    HashMap<HashMap<String, Integer>, BigInteger> calculate();
    /*ArrayList<BigInteger> calculatecoe();

    ArrayList<Integer> calculateexp();*/
}

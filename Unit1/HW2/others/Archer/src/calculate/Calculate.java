package calculate;

import java.util.HashMap;

public interface Calculate {
    public Calculate pow(int power);

    public Calculate add(Calculate rhs);

    public Calculate mul(Calculate rhs);

    public Calculate substitute(HashMap<String, Calculate> varValues);

    public Calculate neg();
}

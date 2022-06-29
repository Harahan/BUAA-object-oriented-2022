package calculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Function {
    private final List<String> varNames;
    private final Calculate innerExpr;

    public Function(Calculate innerExpr, List<String> varNames) {
        this.innerExpr = innerExpr;
        this.varNames = new ArrayList<>(varNames);
    }

    public Calculate call(List<Calculate> args) {
        int nNames = varNames.size();
        int nArgs = args.size();
        HashMap<String, Calculate> varValues = new HashMap<>();
        for (int i = 0; i < nNames && i < nArgs; i++) {
            String varName = this.varNames.get(i);
            Calculate varValue = args.get(i);
            varValues.put(varName, varValue);
        }
        return this.innerExpr.substitute(varValues);
    }

    public String toString() {
        return "func(" + String.join(", ", this.varNames) + ") => " + this.innerExpr.toString();
    }
}

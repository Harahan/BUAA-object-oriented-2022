import java.util.ArrayList;
import java.util.Arrays;

public class SelfDefFuncs {
    private ArrayList<SelfDefFunc> selfDefFuncs;
    
    public SelfDefFuncs() {
        this.selfDefFuncs = new ArrayList<>();
    }
    
    public void addFunc(SelfDefFunc a) {
        selfDefFuncs.add(a);
    }
    
    public void readFunc(String func) {
        String def = func;
        def = def.replace("**", "^").replaceAll("\\s", "");
        String params = def.split("\\(",2)[1];
        params = params.substring(0,params.indexOf(")"));
        String[] temp = params.split(",");
        ArrayList<String> defParams = new ArrayList<>();
        defParams.add(temp[0].substring(temp[0].length() - 1));
        defParams.addAll(Arrays.asList(temp).subList(1, temp.length));
        String expr = def.split("=")[1];
        String funcName = def.split("\\(",2)[0];
        addFunc(new SelfDefFunc(funcName,defParams,expr));
    }
    
    public String callFunc(String funcName,ArrayList<String> callParams) {
        String str = new String();
        for (SelfDefFunc i : selfDefFuncs) {
            if (i.getFuncName().equals(funcName)) {
                str = "(" + i.call(callParams) + ")";
                break;
            }
        }
        return str;
    }
    
    public String displaceSelfDefFuncs(String expr) {
        String out = expr;
        boolean finished = false;
        while (!finished) {
            finished = true;
            for (int i = 0; i < out.length(); i++) {
                if (isFunc(out.substring(i, i + 1))) {
                    finished = false;
                    String params = out.substring(i + 2);
                    int paramsLength = 0;
                    String temp = params;
                    while (temp.contains("(") && temp.indexOf("(") < temp.indexOf(")")) {
                        paramsLength = paramsLength + temp.indexOf(")") + 1;
                        temp = temp.substring(temp.indexOf(")") + 1);
                    }
                    paramsLength = paramsLength + temp.indexOf(")");
                    params = params.substring(0, paramsLength);
                    String[] paramList = params.split(",");
                    ArrayList<String> callParams = new ArrayList<>();
                    callParams.add(paramList[0]);
                    callParams.addAll(Arrays.asList(paramList).subList(1, paramList.length));
                    String funcname = out.substring(i, i + 1);
                    out = out.substring(0, i) + callFunc(funcname, callParams)
                            + out.substring(i + 3 + paramsLength);
                    break;
                }
            }
        }
        return out;
    }
    
    public boolean isFunc(String funcName) {
        for (SelfDefFunc i : selfDefFuncs) {
            if (i.getFuncName().equals(funcName)) {
                return true;
            }
        }
        return false;
    }
}

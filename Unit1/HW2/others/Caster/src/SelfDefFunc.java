import java.util.ArrayList;

public class SelfDefFunc {
    public String getFuncName() {
        return funcName;
    }
    
    private String funcName;
    private ArrayList<String> defParams;
    private String expr;
    
    public SelfDefFunc(String funcName,ArrayList<String> defParams,String expr) {
        this.defParams = (ArrayList<String>)defParams.clone();
        this.funcName = funcName;
        this.expr = expr;
    }
    
    public String call(ArrayList<String> callParams) {
        String str = expr;
        for (int i = 0;i < str.length();i++) {
            for (int j = 0;j < defParams.size();j++) {
                if (defParams.get(j).equals(str.substring(i,i + 1))) {
                    str = str.substring(0,i) + "(" + callParams.get(j) + ")" + str.substring(i + 1);
                    i = i + callParams.get(j).length();
                    break;
                }
            }
        }
        return str;
    }
    
}
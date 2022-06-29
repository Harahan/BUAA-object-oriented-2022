package parse;

import java.util.ArrayList;

public class CustomFunc {
    private final String name;
    private final ArrayList<String> variables;
    private final String function;

    public CustomFunc(String name, ArrayList<String> variables, String function) {
        this.name = name;
        this.variables = variables;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getVariables() {
        return variables;
    }

    public String getFunction() {
        return function;
    }
}

package formula;

import java.util.ArrayList;

public class Func {
    private String name;
    private ArrayList<String> parameters = new ArrayList<>();
    private String formula;

    public Func(String name, String formula) {
        this.name = name;
        this.formula = formula;
    }

    public void addParameter(String parameter) {
        parameters.add(parameter);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public ArrayList<String> getArrayList() {
        return parameters;
    }
}

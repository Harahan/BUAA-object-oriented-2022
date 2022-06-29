package parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomFuncs {
    private final HashMap<String, CustomFunc> customFuncHashMap;

    // private static final String FUNCTION = "([fgh])[\\s]*\\(([uyz,]+)\\)[\\s]*[=][\\s]*(.+$)";
    private static final String FUNCTION = "([fgh])\\(([uyz,]+)\\)[=](.+$)";

    public CustomFuncs() {
        this.customFuncHashMap = new HashMap<>();
    }

    public void addFunc(String input) {
        Pattern r = Pattern.compile(FUNCTION);
        String str = input.replaceAll("x","u").trim().replaceAll("[\\s]", "");
        Matcher m = r.matcher(str);
        String name = null;
        ArrayList<String> variables = new ArrayList<>();
        String function = null;
        if (m.find()) {
            name = m.group(1);
            Collections.addAll(variables, m.group(2).split(","));
            function = m.group(3);
        }
        CustomFunc customFunc = new CustomFunc(name, variables, function);
        this.customFuncHashMap.put(customFunc.getName(), customFunc);
    }

    public HashMap<String, CustomFunc> getCustomFuncHashMap() {
        return customFuncHashMap;
    }
}

package classdiagram;

import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class MyOperation {
    private final String name;
    private boolean error = false;
    private final Visibility visibility;
    private final HashMap<NameableType, Integer> inValues = new HashMap<>();
    // nameableType, num
    private final HashSet<String> referenceTypes = new HashSet<>();
    // id
    private static final HashSet<String> PARAMETER_TYPES = new HashSet<String>() {{
            add("byte");
            add("short");
            add("int");
            add("long");
            add("float");
            add("double");
            add("char");
            add("boolean");
            add("String"); // !!!
        }};

    public MyOperation(UmlOperation umlOperation) {
        name = umlOperation.getName();
        visibility = umlOperation.getVisibility();
    }

    public String getName() {
        return name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void addParameter(UmlParameter umlParameter) {
        NameableType tmp = umlParameter.getType();
        if (umlParameter.getDirection() == Direction.IN) {
            inValues.merge(tmp, 1, Integer::sum);
        }
        if (tmp instanceof NamedType && !error) {
            String tmpName = ((NamedType) tmp).getName();
            error = (umlParameter.getDirection() == Direction.IN) ?
                    !PARAMETER_TYPES.contains(tmpName) :
                    !(PARAMETER_TYPES.contains(tmpName) || tmpName.equals("void"));
        } else if (tmp instanceof ReferenceType) {
            referenceTypes.add(((ReferenceType) tmp).getReferenceId());
        }
    }

    public boolean getError() {
        return error;
    }

    public boolean isSame(MyOperation o) {
        return !o.error && !error && Objects.equals(o.name, name)
                && o.inValues.equals(inValues);
    }

    public HashSet<String> getReferenceTypes() {
        return referenceTypes;
    }
}

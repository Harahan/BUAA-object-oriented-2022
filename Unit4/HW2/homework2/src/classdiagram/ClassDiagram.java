package classdiagram;

import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.models.common.ReferenceType;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDiagram {
    private int classCount = 0;
    private final HashMap<String, Object> myElements;
    private final HashMap<String, ArrayList<MyClass>> myClasses = new HashMap<>();
    // name, arrayList<myClass>()

    public ClassDiagram(HashMap<String, Object> myElements) {
        this.myElements = myElements;
    }

    public void parseUmlClass(UmlClass umlClass) {
        ++classCount;
        ArrayList<MyClass> tmpList = new ArrayList<MyClass>() {{
                add(new MyClass(umlClass));
            }};
        myElements.put(umlClass.getId(), tmpList.get(0));
        myClasses.merge(umlClass.getName(), tmpList, (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        });
    }

    public void parseUmlInterface(UmlInterface umlInterface) {
        myElements.put(umlInterface.getId(), new MyInterface(umlInterface));
    }

    public void parseUmlAttribute(UmlAttribute umlAttribute) {
        Object parent = myElements.get(umlAttribute.getParentId());
        if (umlAttribute.getType() instanceof ReferenceType && parent instanceof MyClass) {
            ((MyClass) parent).addReference((ReferenceType) umlAttribute.getType());
        }
    }

    public void parseUmlOperation(UmlOperation umlOperation) {
        Object parent = myElements.get(umlOperation.getParentId());
        MyOperation tmp = new MyOperation(umlOperation);
        if (parent instanceof MyClass) {
            ((MyClass) parent).addOperation(tmp);
        }
        myElements.put(umlOperation.getId(), tmp);
    }

    public void parseUmlParameter(UmlParameter umlParameter) {
        ((MyOperation) myElements.get(umlParameter.getParentId())).addParameter(umlParameter);
    }

    public void parseUmlGeneration(UmlGeneralization umlGeneralization) {
        Object target = myElements.get(umlGeneralization.getTarget());
        Object source = myElements.get(umlGeneralization.getSource());
        if (target instanceof MyClass) {
            ((MyClass) target).addSubClassNum();
            ((MyClass) source).setSuperClass((MyClass) target);
        } else {
            ((MyInterface) source).addSuperInterface((MyInterface) target);
        }
    }

    public void parseUmlInterfaceRealization(UmlInterfaceRealization umlInterfaceRealization) {
        ((MyClass) myElements.get(umlInterfaceRealization.getSource())).
                addRealizationInterface(
                        (MyInterface) myElements.get(umlInterfaceRealization.getTarget()));
    }

    public int getClassCount() {
        return classCount;
    }

    public int getClassSubClassCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return getClass(s).getSubClassNum();
    }

    public int getClassOperationCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return getClass(s).getSelfOperationNum();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException {
        return getClass(s).getOperationWithVisibility(s1);
    }

    public List<Integer> getClassOperationCouplingDegree(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return getClass(s).getOperationCouplingDegree(s1);
    }

    public int getClassAttributeCouplingDegree(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return getClass(s).getAttributeCouplingDegree();
    }

    public List<String> getClassImplementInterfaceList(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return getClass(s).getRealizationInterfacesNames();
    }

    public int getClassDepthOfInheritance(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return getClass(s).getClassDepth();
    }

    private MyClass getClass(String name) throws
            ClassNotFoundException, ClassDuplicatedException {
        ArrayList<MyClass> classes = myClasses.getOrDefault(name, null);
        if (classes == null) {
            throw new ClassNotFoundException(name);
        } else if (classes.size() > 1) {
            throw new ClassDuplicatedException(name);
        }
        return classes.get(0);
    }

}

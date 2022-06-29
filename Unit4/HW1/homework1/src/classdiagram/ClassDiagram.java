package classdiagram;

import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDiagram {
    private final UmlElement[] elements;
    private int classCount = 0;
    private final HashMap<String, Object> myElements = new HashMap<>();
    // id, myElem --- myClass, myInterface, myOperation
    private final HashMap<String, ArrayList<MyClass>> myClasses = new HashMap<>();
    // name, arrayList<myClass>()

    public ClassDiagram(UmlElement... elements) {
        this.elements = elements;
        parseAll();
    }

    private void parseAll() {
        for (UmlElement element : elements) {
            ElementType elementType = element.getElementType();
            switch (elementType) {
                case UML_CLASS:
                    parseUmlClass((UmlClass) element);
                    break;
                case UML_INTERFACE:
                    parseUmlInterface((UmlInterface) element);
                    break;
                default:
                    break;
            }
        }
        for (UmlElement element : elements) {
            ElementType elementType = element.getElementType();
            switch (elementType) {
                case UML_ATTRIBUTE:
                    parseUmlAttribute((UmlAttribute) element);
                    break;
                case UML_OPERATION:
                    parseUmlOperation((UmlOperation) element);
                    break;
                default:
                    break;
            }
        }
        for (UmlElement element : elements) {
            ElementType elementType = element.getElementType();
            switch (elementType) {
                case UML_PARAMETER:
                    parseUmlParameter((UmlParameter) element);
                    break;
                case UML_GENERALIZATION:
                    parseUmlGeneration((UmlGeneralization) element);
                    break;
                case UML_INTERFACE_REALIZATION:
                    parseUmlInterfaceRealization((UmlInterfaceRealization) element);
                    break;
                default:
                    break;
            }
        }
    }

    private void parseUmlClass(UmlClass umlClass) {
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

    private void parseUmlInterface(UmlInterface umlInterface) {
        myElements.put(umlInterface.getId(), new MyInterface(umlInterface));
    }

    private void parseUmlAttribute(UmlAttribute umlAttribute) {
        Object parent = myElements.get(umlAttribute.getParentId());
        if (umlAttribute.getType() instanceof ReferenceType && parent instanceof MyClass) {
            ((MyClass) parent).addReference((ReferenceType) umlAttribute.getType());
        }
    }

    private void parseUmlOperation(UmlOperation umlOperation) {
        Object parent = myElements.get(umlOperation.getParentId());
        MyOperation tmp = new MyOperation(umlOperation);
        if (parent instanceof MyClass) {
            ((MyClass) parent).addOperation(tmp);
        }
        myElements.put(umlOperation.getId(), tmp);
    }

    private void parseUmlParameter(UmlParameter umlParameter) {
        ((MyOperation) myElements.get(umlParameter.getParentId())).addParameter(umlParameter);
    }

    private void parseUmlGeneration(UmlGeneralization umlGeneralization) {
        Object target = myElements.get(umlGeneralization.getTarget());
        Object source = myElements.get(umlGeneralization.getSource());
        if (target instanceof MyClass) {
            ((MyClass) target).addSubClassNum();
            ((MyClass) source).setSuperClass((MyClass) target);
        } else {
            ((MyInterface) source).addSuperInterface((MyInterface) target);
        }
    }

    private void parseUmlInterfaceRealization(UmlInterfaceRealization umlInterfaceRealization) {
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

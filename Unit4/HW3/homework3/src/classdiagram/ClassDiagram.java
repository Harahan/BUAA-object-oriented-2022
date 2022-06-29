package classdiagram;

import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.interact.common.AttributeClassInformation;

import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Pattern;

public class ClassDiagram {
    private int classCount = 0;
    private final HashMap<String, Object> myElements;
    private final HashMap<String, ArrayList<MyClass>> myClasses = new HashMap<>();
    // name, arrayList<myClass>()

    private final HashSet<MyInterface> totInterfaces =  new HashSet<>();

    private final HashSet<MyClass> totClasses = new HashSet<>();

    private final HashMap<MyClassOrInterface, Integer> dfn = new HashMap<>();

    private final HashMap<MyClassOrInterface, Integer> low = new HashMap<>();

    private final LinkedList<MyClassOrInterface> stack = new LinkedList<>();

    private final HashSet<MyClassOrInterface> vis = new HashSet<>();

    private final HashSet<UmlClassOrInterface> ans = new HashSet<>();

    private final HashMap<String, UmlAssociationEnd> associations = new HashMap<>();

    private int tot = 0;

    private boolean checkRule001 = true;

    private boolean checkRule005 = true;

    public ClassDiagram(HashMap<String, Object> myElements) {
        this.myElements = myElements;
    }

    public void parseUmlClass(UmlClass umlClass) {
        if (checkRule001) {
            checkName(umlClass.getName());
        }
        ++classCount;
        ArrayList<MyClass> tmpList = new ArrayList<MyClass>() {{
                add(new MyClass(umlClass));
            }};
        myElements.put(umlClass.getId(), tmpList.get(0));
        totClasses.add(tmpList.get(0));
        myClasses.merge(umlClass.getName(), tmpList, (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        });
    }

    public void parseUmlInterface(UmlInterface umlInterface) {
        if (checkRule001) {
            checkName(umlInterface.getName());
        }
        MyInterface myInterface = new MyInterface(umlInterface);
        totInterfaces.add(myInterface);
        myElements.put(umlInterface.getId(), myInterface);
    }

    public void parseUmlAttribute(UmlAttribute umlAttribute) {
        Object parent = myElements.get(umlAttribute.getParentId());
        if (checkRule001 && (parent instanceof MyClass || parent instanceof MyInterface)) {
            checkName(umlAttribute.getName()); // only check the element in class diagram
        }
        myElements.put(umlAttribute.getId(), umlAttribute);
        if (umlAttribute.getType() instanceof ReferenceType && parent instanceof MyClass) {
            ((MyClass) parent).addReference((ReferenceType) umlAttribute.getType());
        }
        if (parent instanceof MyClass) {
            ((MyClass) parent).addAttributeAndAssociationEnd(umlAttribute.getName());
        }
        if (parent instanceof MyInterface
                && umlAttribute.getVisibility() != Visibility.PUBLIC && checkRule005) {
            checkRule005 = false;
        }
    }

    public void parseUmlOperation(UmlOperation umlOperation) {
        if (checkRule001) {
            checkName(umlOperation.getName());
        }
        Object parent = myElements.get(umlOperation.getParentId());
        MyOperation tmp = new MyOperation(umlOperation);
        if (parent instanceof MyClass) {
            ((MyClass) parent).addOperation(tmp);
        }
        myElements.put(umlOperation.getId(), tmp);
    }

    public void parseUmlParameter(UmlParameter umlParameter) {
        if (umlParameter.getDirection() != Direction.RETURN && checkRule001) {
            checkName(umlParameter.getName());
        }
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

    public void parseUmlAssociation(UmlAssociation umlAssociation) {
        associations.put(umlAssociation.getId(), null);
    }

    public void parseUmlAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        UmlAssociationEnd other = associations.get(umlAssociationEnd.getParentId());
        if (other == null) {
            associations.put(umlAssociationEnd.getParentId(), umlAssociationEnd);
            return;
        }
        String otherName = other.getName();
        String myName = umlAssociationEnd.getName();
        Object otherObject = myElements.get(other.getReference());
        Object myObject = myElements.get(umlAssociationEnd.getReference());
        if (otherObject instanceof MyClass
                && myName != null && !Pattern.matches("[ \\t]*", myName)) {
            ((MyClass) otherObject).addAttributeAndAssociationEnd(myName);
        }
        if (myObject instanceof MyClass
                && otherName != null && !Pattern.matches("[ \\t]*", otherName)) {
            ((MyClass) myObject).addAttributeAndAssociationEnd(otherName);
        }
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

    private void checkName(String name) {
        if (name == null || Pattern.matches("[ \\t]*", name)) {
            checkRule001 = false;
        }
    }

    private void getCircle(MyClassOrInterface u) {
        dfn.put(u, ++tot);
        low.put(u, tot);
        stack.addLast(u);
        vis.add(u);
        for (MyClassOrInterface v : u.getEdges()) {
            if (!dfn.containsKey(v)) {
                getCircle(v);
                low.merge(u, low.get(v), Integer::min);
            } else if (vis.contains(v)) {
                low.merge(u, dfn.get(v), Integer::min);
            }
        }
        HashSet<UmlClassOrInterface> ans1 = new HashSet<>();
        if (Objects.equals(dfn.get(u), low.get(u))) {
            MyClassOrInterface tmp;
            do {
                tmp = stack.removeLast();
                vis.remove(tmp);
                ans1.add(tmp.getClassOrInterface());
            } while (!tmp.equals(u));
        }
        if (ans1.size() > 1) {
            ans.addAll(ans1);
        }
    }

    public void checkForUml001() throws UmlRule001Exception {
        if (!checkRule001) {
            throw new UmlRule001Exception();
        }
    }

    public void checkForUml002() throws UmlRule002Exception {
        HashSet<AttributeClassInformation> ans = new HashSet<>();
        for (MyClass item : totClasses) {
            ans.addAll(item.getAttributeClassInformation());
        }
        if (!ans.isEmpty()) {
            throw new UmlRule002Exception(ans);
        }
    }

    public void checkForUml003() throws UmlRule003Exception {
        for (MyClass item : totClasses) {
            if (!dfn.containsKey(item)) {
                stack.clear();
                getCircle(item);
            }
            if (Objects.equals(item.getSuperClass(), item)) {
                ans.add(item.getClassOrInterface());
            }
        }
        for (MyInterface item : totInterfaces) {
            if (!dfn.containsKey(item)) {
                stack.clear();
                getCircle(item);
            }
            if (item.getDirectSuperInterfaces().contains(item)) {
                ans.add(item.getUmlInterface());
            }
        }
        if (!ans.isEmpty()) {
            throw new UmlRule003Exception(ans);
        }
    }

    public void checkForUml004() throws UmlRule004Exception {
        HashSet<UmlClassOrInterface> ans = new HashSet<>();
        for (MyInterface item : totInterfaces) {
            if (item.checkDuplicateInheritance(new HashSet<>())) {
                ans.add(item.getUmlInterface());
            }
        }
        if (!ans.isEmpty()) {
            throw new UmlRule004Exception(ans);
        }
    }

    public void checkForUml005() throws UmlRule005Exception {
        if (!checkRule005) {
            throw new UmlRule005Exception();
        }
    }
}

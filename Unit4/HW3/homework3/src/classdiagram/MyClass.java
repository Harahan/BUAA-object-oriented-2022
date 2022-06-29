package classdiagram;

import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.interact.common.AttributeClassInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyClass implements MyClassOrInterface {
    private int subClassNum = 0;
    private int selfOperationNum = 0;
    private final UmlClass umlClass;
    private final HashMap<String, HashMap<Visibility, Integer>> operationsWithVisibility
            = new HashMap<>();
    // name, hashMap<visibility, integer>()
    private final HashMap<String, HashSet<MyOperation>> operations = new HashMap<>();
    // name, hashSet<myOperation>()
    private MyClass superClass = null;
    private final HashSet<MyInterface> realizationInterfaces = new HashSet<>();
    // myInterface
    private final HashSet<String> referenceTypesForAttributes = new HashSet<>();
    // id
    private final HashSet<String> attributeAndAssociationEnd = new HashSet<>();
    private final HashSet<AttributeClassInformation> attributeClassInformation = new HashSet<>();
    private final String id;
    private final String name;

    public MyClass(UmlClass umlClass) {
        id = umlClass.getId();
        name = umlClass.getName();
        this.umlClass = umlClass;
    }

    public void addRealizationInterface(MyInterface myInterface) {
        realizationInterfaces.add(myInterface);
    }

    public void setSuperClass(MyClass superClass) {
        this.superClass = superClass;
    }

    private HashSet<MyInterface> getRealizationInterfaces() {
        HashSet<MyInterface> rt = new HashSet<>(realizationInterfaces);
        rt.addAll(((superClass != null) ?
                superClass.getRealizationInterfaces() : new HashSet<>()));
        for (MyInterface item : realizationInterfaces) {
            rt.addAll(item.getSuperInterfaces());
        }
        return rt;
    }

    public List<String> getRealizationInterfacesNames() {
        HashSet<MyInterface> tmp = getRealizationInterfaces();
        return new ArrayList<String>() {{
                tmp.forEach(item -> add(item.getName()));
            }};
    }

    public void addSubClassNum() {
        ++subClassNum;
    }

    public int getSubClassNum() {
        return subClassNum;
    }

    public int getSelfOperationNum() {
        return selfOperationNum;
    }

    public void addOperation(MyOperation myOperation) {
        String name = myOperation.getName();
        Visibility visibility = myOperation.getVisibility();
        operations.merge(name, new HashSet<MyOperation>() {{
                add(myOperation);
            }}, (oldSet, newSet) -> {
                oldSet.add(myOperation);
                return oldSet;
            });
        ++selfOperationNum;
        operationsWithVisibility.merge(name, new HashMap<Visibility, Integer>() {{
                put(visibility, 1);
            }}, (oldMap, newMap) -> {
                oldMap.merge(visibility, 1, Integer::sum);
                return oldMap;
            });
    }

    public HashMap<Visibility, Integer> getOperationWithVisibility(String name) {
        return operationsWithVisibility.getOrDefault(name, new HashMap<>());
    }

    public void addReference(ReferenceType referenceType) {
        referenceTypesForAttributes.add(referenceType.getReferenceId());
    }

    public int getClassDepth() {
        return (superClass == null) ? 0 : superClass.getClassDepth() + 1;
    }

    private HashSet<String> getReferenceTypesForAttributes() {
        HashSet<String> tmp = new HashSet<>(referenceTypesForAttributes);
        tmp.addAll((superClass != null) ? superClass.getReferenceTypesForAttributes() :
                new HashSet<>());
        return tmp;
    }

    public int getAttributeCouplingDegree() {
        HashSet<String> tmp = getReferenceTypesForAttributes();
        return (tmp.contains(id)) ? tmp.size() - 1 : tmp.size();
    }

    private boolean checkMethodWrongTypeException(HashSet<MyOperation> tmp) {
        for (MyOperation item : tmp) {
            if (item.getError()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMethodDuplicatedException(HashSet<MyOperation> tmp) {
        ArrayList<MyOperation> ops = new ArrayList<>(tmp);
        for (int i = 0; i < ops.size() - 1; i++) {
            for (int j = i + 1; j < ops.size(); j++) {
                if (ops.get(i).isSame(ops.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Integer> getOperationCouplingDegree(String s) throws
            MethodWrongTypeException, MethodDuplicatedException { // if there's no method
        HashSet<MyOperation> tmp = operations.getOrDefault(s, new HashSet<>());
        if (checkMethodWrongTypeException(tmp)) {
            throw new MethodWrongTypeException(name, s);
        } else if (checkMethodDuplicatedException(tmp)) {
            throw new MethodDuplicatedException(name, s);
        }
        ArrayList<Integer> rt = new ArrayList<>();
        for (MyOperation item : tmp) {
            HashSet<String> iter = item.getReferenceTypes();
            rt.add((iter.contains(id)) ? iter.size() - 1 : iter.size());
        }
        return rt;
    }

    public void addAttributeAndAssociationEnd(String name) {
        if (attributeAndAssociationEnd.contains(name)) {
            attributeClassInformation.add(new AttributeClassInformation(name, this.name));
        } else {
            attributeAndAssociationEnd.add(name);
        }
    }

    @Override
    public UmlClassOrInterface getClassOrInterface() {
        return umlClass;
    }

    @Override
    public HashSet<MyClassOrInterface> getEdges() {
        return (superClass != null) ? new HashSet<MyClassOrInterface>() {{
                add(superClass);
            }} : new HashSet<>();
    }

    public HashSet<AttributeClassInformation> getAttributeClassInformation() {
        return attributeClassInformation;
    }

    public MyClass getSuperClass() {
        return superClass;
    }
}

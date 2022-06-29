package classdiagram;

import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MyInterface implements MyClassOrInterface {
    private final String name;

    private final UmlInterface umlInterface;

    private final HashMap<MyInterface, Integer> superInterfaces = new HashMap<>();
    // myInterface

    public MyInterface(UmlInterface umlInterface) {
        name = umlInterface.getName();
        this.umlInterface = umlInterface;
    }

    public HashSet<MyInterface> getSuperInterfaces() {
        HashSet<MyInterface> rt = new HashSet<>(superInterfaces.keySet());
        for (MyInterface item : superInterfaces.keySet()) {
            rt.addAll(item.getSuperInterfaces());
        }
        return rt;
    }

    public void addSuperInterface(MyInterface myInterface) {
        superInterfaces.merge(myInterface, 1, Integer::sum);
    }

    public String getName() {
        return name;
    }

    public UmlInterface getUmlInterface() {
        return umlInterface;
    }

    public boolean checkDuplicateInheritance(HashSet<UmlInterface> vis) {
        if (vis.contains(umlInterface)) {
            return true;
        }
        vis.add(umlInterface);
        for (MyInterface item : superInterfaces.keySet()) {
            if (superInterfaces.get(item) > 1 || item.checkDuplicateInheritance(vis)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashSet<MyClassOrInterface> getEdges() {
        return new HashSet<MyClassOrInterface>() {{
                addAll(superInterfaces.keySet());
            }};
    }

    @Override
    public UmlClassOrInterface getClassOrInterface() {
        return umlInterface;
    }

    public Set<MyInterface> getDirectSuperInterfaces() {
        return superInterfaces.keySet();
    }
}

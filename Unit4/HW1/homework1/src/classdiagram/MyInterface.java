package classdiagram;

import com.oocourse.uml1.models.elements.UmlInterface;

import java.util.HashSet;

public class MyInterface {
    private final String name;
    private final HashSet<MyInterface> superInterfaces = new HashSet<>();
    // myInterface

    public MyInterface(UmlInterface umlInterface) {
        name = umlInterface.getName();
    }

    public HashSet<MyInterface> getSuperInterfaces() {
        HashSet<MyInterface> rt = new HashSet<>(superInterfaces);
        for (MyInterface item : superInterfaces) {
            rt.addAll(item.getSuperInterfaces());
        }
        return rt;
    }

    public void addSuperInterface(MyInterface myInterface) {
        superInterfaces.add(myInterface);
    }

    public String getName() {
        return name;
    }
}

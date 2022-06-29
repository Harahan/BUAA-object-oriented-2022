package classdiagram;

import com.oocourse.uml3.models.elements.UmlClassOrInterface;

import java.util.HashSet;

public interface MyClassOrInterface {
    HashSet<MyClassOrInterface> getEdges();

    UmlClassOrInterface getClassOrInterface();
}

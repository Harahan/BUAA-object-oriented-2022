import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlElement;
import classdiagram.ClassDiagram;

import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final ClassDiagram classDiagram;

    public MyImplementation(UmlElement... elements) {
        classDiagram = new ClassDiagram(elements);
    }

    @Override
    public int getClassCount() {
        return classDiagram.getClassCount();
    }

    @Override
    public int getClassSubClassCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassSubClassCount(s);
    }

    @Override
    public int getClassOperationCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationCount(s);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationVisibility(s, s1);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return classDiagram.getClassOperationCouplingDegree(s, s1);
    }

    @Override
    public int getClassAttributeCouplingDegree(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAttributeCouplingDegree(s);
    }

    @Override
    public List<String> getClassImplementInterfaceList(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassImplementInterfaceList(s);
    }

    @Override
    public int getClassDepthOfInheritance(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassDepthOfInheritance(s);
    }
}

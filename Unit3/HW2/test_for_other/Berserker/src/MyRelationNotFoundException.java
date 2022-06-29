import com.oocourse.spec2.exceptions.RelationNotFoundException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static Counter counter = new Counter();
    private int currentId1;
    private int currentId2;

    public MyRelationNotFoundException(int id1, int id2) {
        counter.add();
        counter.addId(id1);
        counter.addId(id2);
        currentId1 = min(id1, id2);
        currentId2 = max(id1, id2);
    }
    
    @Override
    public void print() {
        System.out.printf("rnf-%d, %d-%d, %d-%d\n", counter.getAll(), currentId1,
                counter.getId(currentId1), currentId2, counter.getId(currentId2));
    }
}
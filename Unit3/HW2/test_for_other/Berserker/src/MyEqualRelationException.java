import com.oocourse.spec2.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private static Counter counter = new Counter();
    private int currentId1;
    private int currentId2;

    public MyEqualRelationException(int id1, int id2) {
        counter.add();
        counter.addId(id1);
        if (id1 != id2) {
            counter.addId(id2);
        }
        currentId1 = Math.min(id1, id2);
        currentId2 = Math.max(id1, id2);
    }
    
    @Override
    public void print() {
        System.out.printf("er-%d, %d-%d, %d-%d\n", counter.getAll(), currentId1,
                counter.getId(currentId1), currentId2, counter.getId(currentId2));
    }
}
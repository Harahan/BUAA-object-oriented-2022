import com.oocourse.spec2.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Counter counter = new Counter();
    private int currentId;

    public MyEqualGroupIdException(int id) {
        counter.add();
        counter.addId(id);
        currentId = id;
    }
    
    @Override
    public void print() {
        System.out.printf("egi-%d, %d-%d\n", counter.getAll(), currentId, counter.getId(currentId));
    }
}
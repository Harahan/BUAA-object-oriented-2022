import com.oocourse.spec2.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static Counter counter = new Counter();
    private int currentId;

    public MyEqualPersonIdException(int id) {
        counter.add();
        counter.addId(id);
        currentId = id;
    }
    
    @Override
    public void print() {
        System.out.printf("epi-%d, %d-%d\n",counter.getAll(), currentId, counter.getId(currentId));
    }
}
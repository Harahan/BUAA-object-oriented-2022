import com.oocourse.spec2.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static Counter counter = new Counter();
    private int currentId;
    
    public MyEqualMessageIdException(int id) {
        counter.add();
        counter.addId(id);
        this.currentId = id;
    }

    @Override
    public void print() {
        System.out.printf("emi-%d, %d-%d\n", counter.getAll(), currentId, counter.getId(currentId));
    }
}

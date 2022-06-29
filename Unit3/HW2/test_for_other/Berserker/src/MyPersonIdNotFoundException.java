import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static Counter counter = new Counter();
    private final int currentId;

    public MyPersonIdNotFoundException(int id) {
        counter.add();
        counter.addId(id);
        currentId = id;
    }
    
    @Override
    public void print() {
        System.out.printf("pinf-%d, %d-%d\n",counter.getAll(), currentId, counter.getId(currentId));
    }
}
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static Counter counter = new Counter();
    private int currentId;

    public MyMessageIdNotFoundException(int id) {
        counter.add();
        counter.addId(id);
        currentId = id;
    }
    
    @Override
    public void print() {
        System.out.printf("minf-%d, %d-%d\n", counter.getAll(),
                currentId, counter.getId(currentId));
    }
}

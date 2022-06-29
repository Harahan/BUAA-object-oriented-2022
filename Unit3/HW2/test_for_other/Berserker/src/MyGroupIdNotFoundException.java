import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static Counter counter = new Counter();
    private int currentId;

    public MyGroupIdNotFoundException(int id) {
        counter.add();
        counter.addId(id);
        currentId = id;
    }
    
    @Override
    public void print() {
        System.out.printf("ginf-%d, %d-%d\n",counter.getAll(), currentId, counter.getId(currentId));
    }
}
package exception;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import program.Counter;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private int id;
    private static Counter counter = new Counter();

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        counter.trigger(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("ginf-%d, %d-%d",
                counter.getTriggerTotalTimes(), id, counter.checkTriggerTimes(id)));
    }
}

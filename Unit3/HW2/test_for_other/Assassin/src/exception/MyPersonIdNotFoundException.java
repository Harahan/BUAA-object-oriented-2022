package exception;

import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import program.Counter;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static Counter counter = new Counter();
    private int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        counter.trigger(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("pinf-%d, %d-%d",
                counter.getTriggerTotalTimes(), id, counter.checkTriggerTimes(id)));
    }
}

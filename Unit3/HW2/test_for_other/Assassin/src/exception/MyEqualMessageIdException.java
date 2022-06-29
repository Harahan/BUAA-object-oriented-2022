package exception;

import com.oocourse.spec2.exceptions.EqualMessageIdException;
import program.Counter;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private int id;
    private static Counter counter = new Counter();

    public MyEqualMessageIdException(int id) {
        this.id = id;
        this.counter.trigger(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("emi-%d, %d-%d",
                counter.getTriggerTotalTimes(), id, counter.checkTriggerTimes(id)));
    }
}

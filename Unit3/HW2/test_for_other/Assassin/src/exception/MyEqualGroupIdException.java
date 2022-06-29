package exception;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import program.Counter;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Counter counter = new Counter();
    private int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        MyEqualGroupIdException.counter.trigger(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("egi-%d, %d-%d",
                counter.getTriggerTotalTimes(), id, counter.checkTriggerTimes(id)));
    }
}

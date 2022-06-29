package homeworkv.data;

import com.oocourse.elevator1.PersonRequest;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {
    private final Queue<PersonRequest> queue = new LinkedList<PersonRequest>();
    private boolean inputEnd = false;

    public void setInputEnd(boolean flag) {
        this.inputEnd = flag;
    }

    public boolean InputEnd() {
        if (queue.peek() == null && inputEnd) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized PersonRequest getRequest() {
        while (queue.peek() == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue.remove();
    }

    public synchronized void putRequest(PersonRequest personRequest) {
        queue.offer(personRequest);
        notifyAll();
    }
}

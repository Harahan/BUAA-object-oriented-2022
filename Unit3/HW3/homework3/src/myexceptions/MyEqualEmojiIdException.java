package myexceptions;

import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> EMOJIS = new HashMap<>();
    private final int id;

    public MyEqualEmojiIdException(int id) {
        sum += 1;
        EMOJIS.merge(id, 1, Integer::sum);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("eei-" + sum + ", " + id + "-" + EMOJIS.get(id));
    }
}

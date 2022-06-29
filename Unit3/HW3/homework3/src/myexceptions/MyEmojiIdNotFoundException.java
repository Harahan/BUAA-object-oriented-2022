package myexceptions;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> EMOJIS = new HashMap<>();

    private final int id;

    public MyEmojiIdNotFoundException(int id) {
        sum += 1;
        EMOJIS.merge(id, 1, Integer::sum);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("einf-" + sum + ", " + id + "-" + EMOJIS.get(id));
    }
}

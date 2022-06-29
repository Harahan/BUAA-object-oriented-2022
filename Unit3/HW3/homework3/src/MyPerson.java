import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int socialValue = 0;
    private int money = 0;

    private final HashMap<Integer, Integer> acquaintance = new HashMap<>();
    private final LinkedList<Message> messages = new LinkedList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void addAcquaintance(int id, int value) {
        acquaintance.put(id, value);
    }

    public HashMap<Integer, Integer> getAcquaintance() {
        return acquaintance;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Person && ((Person) obj).getId() == id;
    }

    @Override
    public boolean isLinked(Person person) {
        return person.getId() == id || acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        return acquaintance.getOrDefault(person.getId(), 0);
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    // be careful don't fix the subList
    @Override
    public List<Message> getReceivedMessages() {
        return (messages.size() < 4) ? messages : messages.subList(0, 4);
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addMessage(Message message) {
        messages.addFirst(message);
    }

    public void clearNotices() {
        messages.removeIf(message -> message instanceof NoticeMessage);
    }
}

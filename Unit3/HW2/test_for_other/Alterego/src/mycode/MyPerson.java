package mycode;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {

    private int id;
    private String name;
    private int age;
    private HashMap<Person, Integer> linkage = new HashMap<>();
    // 2nd
    private int money = 0;
    private int socialValue = 0;
    private LinkedList<Message> messages = new LinkedList<>();

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
    public boolean equals(Object o) {
        if (o != null && o instanceof MyPerson) {
            return ((MyPerson) o).getId() == id;
        }
        return false;
    }

    @Override
    /**
     * O(1)
     * */
    public boolean isLinked(Person person) {
        return linkage.containsKey(person) || person.getId() == id;
    }

    @Override
    /**
     * O(1)
     */
    public int queryValue(Person person) {
        return linkage.getOrDefault(person, 0);
    }

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }


    ////////////////////////////2nd////////////////////////////////
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

    @Override
    public List<Message> getReceivedMessages() {
        LinkedList<Message> res = new LinkedList<>();
        for (int i = 0; i <= 3 && i < messages.size(); i++) {
            res.addLast(messages.getFirst());
        }
        return res;
    }

    public void addFirst(Message message) {
        messages.addFirst(message);
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addLink(Person p2, int value) {
        linkage.put(p2, value);
    }

    @Override
    public String toString() {
        return "MyPerson{" +
                "id=" + id +
                '}';
    }
}

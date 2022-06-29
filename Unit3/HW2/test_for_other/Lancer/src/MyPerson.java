import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.List;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private int money;
    private int socialValue;
    private ArrayList<Person> acquaintance;
    private ArrayList<Integer> value;
    private ArrayList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        money = 0;
        socialValue = 0;
        this.acquaintance = new ArrayList<>();
        this.value = new ArrayList<>();
        this.messages = new ArrayList<>();
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
        if (obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        if (id == person.getId()) {
            return true;
        }
        for (Person item : acquaintance) {
            if (item.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int queryValue(Person person) {
        for (int i = 0; i < acquaintance.size(); i++) {
            if (acquaintance.get(i).getId() == person.getId()) {
                return value.get(i);
            }
        }
        return 0;
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

    public void addMessage(Message message) {
        messages.add(0, message);
    }

    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> received = new ArrayList<>();
        for (int i = 0; i < messages.size() && i < 4; i++) {
            received.add(messages.get(i));
        }
        return received;
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    public void addAcquaintance(Person p, int v) {
        acquaintance.add(p);
        value.add(v);
    }

    public ArrayList<Person> getAcquaintance() {
        return acquaintance;
    }
}

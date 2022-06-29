package program;

import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;
import program.network.Edge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Person, Integer> acquaintance = new HashMap<>();
    private HashSet<Edge> relation = new HashSet<>();
    private int money = 0;
    private int socialValue = 0;
    private LinkedList<Message> messages = new LinkedList<>();
    private int messageNumber = 0;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
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

    public void constructLink(Person person, int value) {
        relation.add(new Edge(this, person, value));
        acquaintance.put(person, value);
    }

    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        }
        return acquaintance.get(person) != null;
    }

    @Override
    public int queryValue(Person person) {
        if (!acquaintance.containsKey(person)) {
            return 0;
        }
        return acquaintance.get(person);
    }

    @Override
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
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

    @Override
    public List<Message> getReceivedMessages() {
        return messages.subList(0, Math.min(4, messageNumber));
    }

    public void receiveMessage(Message message) {
        messages.addFirst(message);
        socialValue += message.getSocialValue();
        messageNumber++;
    }

    @Override
    public void addMoney(int num) {
        money += num;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Person)) {
            return false;
        }
        MyPerson myPerson = (MyPerson) o;
        return id == myPerson.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public HashSet<Edge> getNetwork() {
        return relation;
    }
}

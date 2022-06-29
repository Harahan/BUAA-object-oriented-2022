import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private int socialValue;
    private int money;
    private HashMap<Integer, Person> acquaintance;  // id - person
    private HashMap<Person, Integer> value; // person - value
    private ArrayList<Message> messages = new ArrayList<>();
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.money = 0;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }
    
    public void addAcquaintance(Person person, int value) {
        this.acquaintance.put(person.getId(), person);
        this.value.put(person, value);
    }
    
    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getAge() {
        return this.age;
    }
    
    @Override
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return acquaintance.containsKey(person.getId());
    }
    
    @Override
    public int queryValue(Person person) {
        int result = 0;
        if (acquaintance.containsKey(person.getId())) {
            result = value.get(acquaintance.get(person.getId()));
        }
        return result;
    }
    
    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
    
    @Override
    public void addSocialValue(int num) {
        this.socialValue += num;
    }
    
    @Override
    public int getSocialValue() {
        return this.socialValue;
    }
    
    @Override
    public List<Message> getMessages() { // TODO: Deep Clone
        ArrayList<Message> subList = new ArrayList<>(messages);
        return subList;
    }
    
    public void addMessage(Message message) {
        messages.add(0, message);
    }
    
    @Override
    public List<Message> getReceivedMessages() {
        ArrayList<Message> results = new ArrayList<>();
        for (int i = 0; i <= 3 && i < messages.size(); i++) {
            results.add(messages.get(i));
        }
        return results;
    }
    
    @Override
    public void addMoney(int num) {
        this.money += num;
    }
    
    @Override
    public int getMoney() {
        return this.money;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyPerson myPerson = (MyPerson) o;
        return id == myPerson.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

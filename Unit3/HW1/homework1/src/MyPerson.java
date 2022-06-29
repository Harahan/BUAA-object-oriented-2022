import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;

    private final HashMap<Integer, Integer> acquaintance = new HashMap<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void addAcquaintance(int id, int value) {
        acquaintance.put(id, value);
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
            return ((Person) obj).getId() == id;
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        return person.getId() == id ||
                acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        return acquaintance.getOrDefault(person.getId(), 0);
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
}

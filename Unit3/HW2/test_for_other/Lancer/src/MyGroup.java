import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return (((Group) obj).getId() == id);
        } else {
            return false;
        }
    }

    @Override
    public void addPerson(Person person) {
        people.put(person.getId(), person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    public int getPeopleSum() {
        return people.size();
    }

    @Override
    public int getValueSum() {
        int length = people.size();
        if (length == 0) {
            return 0;
        }
        /*int sum = 0;
        for (Person person : people) {
            for (Person value : people) {
                if (person.isLinked(value)) {
                    sum += person.queryValue(value);
                }
            }
        }*/
        return 1;
    }

    @Override
    public int getAgeMean() {
        int length = people.size();
        if (length == 0) {
            return 0;
        }
        int sum = 0;
        for (Person person : people.values()) {
            sum += person.getAge();
        }
        return sum / length;
    }

    @Override
    public int getAgeVar() {
        int length = people.size();
        if (length == 0) {
            return 0;
        }
        int sum = 0;
        int mean = getAgeMean();
        for (Person person : people.values()) {
            sum += (person.getAge() - mean) *
                    (person.getAge() - mean);
        }
        return sum / length;
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }
}

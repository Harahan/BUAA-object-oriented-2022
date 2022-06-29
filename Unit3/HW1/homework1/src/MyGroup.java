import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private int totAge = 0;
    private int totAgeSquare = 0;
    /*TODO: overflow, if there are 5300 people whose ages are 200 as the same*/
    private int totValue = 0;
    private final HashMap<Integer, Person> people = new HashMap<>();

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == id;
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        people.put(person.getId(), person);
        totAge += person.getAge();
        totAgeSquare += person.getAge() * person.getAge();
        people.forEach((id, p) -> totValue += person.queryValue(p));
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return totValue << 1;
    }

    @Override
    public int getAgeMean() {
        return people.isEmpty() ? 0 : totAge / people.size();
    }

    @Override
    public int getAgeVar() {
        return people.isEmpty() ? 0 : (totAgeSquare - (getAgeMean() * totAge << 1) +
                getAgeMean() * getAgeMean() * people.size()) / people.size();
        // be careful about the precision
    }

    @Override
    public void delPerson(Person person) {
        people.remove(person.getId());
        totAge -= person.getAge();
        totAgeSquare -= person.getAge() * person.getAge();
        people.forEach((id, p) -> totValue -= person.queryValue(p));
    }

    public void addRelation(int value) {
        totValue += value;
    }

    @Override
    public int getSize() {
        return people.size();
    }
}

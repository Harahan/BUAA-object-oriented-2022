import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private int totAge = 0;
    private int totAgeSquare = 0;
    // overflow, 53000 p
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
        return obj instanceof Group && ((Group) obj).getId() == id;
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
        return people.isEmpty() ? 0 : (totAgeSquare - (getAgeMean() * totAge << 1)
            +  getAgeMean() * getAgeMean() * people.size()) / people.size();
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

    public void addSocialValue(int num) {
        people.forEach((id, p) -> p.addSocialValue(num));
    }

    public void addMoney(int num) {
        people.forEach((id, p) -> p.addMoney(num));
    }
}

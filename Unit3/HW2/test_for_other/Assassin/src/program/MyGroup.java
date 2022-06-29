package program;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.Objects;

public class MyGroup implements Group {
    private int id;
    private int ageSum = 0;
    private int ageVar = 0;
    private int valueSum = 0;
    private int peopleSize = 0;
    private HashMap<Integer, Person> people = new HashMap<>();

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        if (hasPerson(person)) {
            return;
        }
        ageSum += person.getAge();
        peopleSize++;
        /* keep ageVar */
        int ageMean = getAgeMean();
        ageVar = (person.getAge() - ageMean) * (person.getAge() - ageMean);
        people.values().forEach(person1 -> {
            valueSum += 2 * person.queryValue(person1);
            int age = person1.getAge();
            ageVar += (age - ageMean) * (age - ageMean);
        });
        people.put(person.getId(), person);
        ageVar /= peopleSize;
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        if (peopleSize == 0) {
            return 0;
        }
        return ageSum / peopleSize;
    }

    @Override
    public int getAgeVar() {
        if (peopleSize == 0) {
            return 0;
        }
        return ageVar;
    }

    @Override
    public void delPerson(Person person) {
        if (!hasPerson(person)) {
            return;
        }
        peopleSize--;
        ageSum -= person.getAge();
        people.keySet().remove(person.getId());
        int ageMean = getAgeMean();
        ageVar = 0;
        people.values().forEach(person1 -> {
            valueSum -= 2 * person.queryValue(person1);
            int age = person1.getAge();
            ageVar += (age - ageMean) * (age - ageMean);
        });
        if (peopleSize != 0) {
            ageVar /= peopleSize;
        }
    }

    @Override
    public int getSize() {
        return peopleSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Group))  {
            return false;
        }
        MyGroup myGroup = (MyGroup) o;
        return id == myGroup.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void receiveMessage(Message message) {
        int socialValue = message.getSocialValue();
        people.values().forEach(person -> person.addSocialValue(socialValue));
    }

    public void addValue(int value) {
        this.valueSum += 2 * value;
    }
}

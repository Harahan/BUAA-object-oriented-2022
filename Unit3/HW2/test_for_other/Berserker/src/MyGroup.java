import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.Objects;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;
    private int valueSum = 0;
    private int ageSum = 0;
    
    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
    }
    
    public void receiveMessage(Message message) {
        for (Integer i : people.keySet()) {
            people.get(i).addSocialValue(message.getSocialValue());
        }
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public void addPerson(Person person) {
        if (this.hasPerson(person)) {
            return;
        }
        for (Integer pid : people.keySet()) {
            valueSum += person.queryValue(people.get(pid));
        }
        people.put(person.getId(), person);
        ageSum += person.getAge();
    }
    
    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }
    
    public void addValueSum(Integer value) {
        valueSum += value;
    }
    
    @Override
    public int getValueSum() {
        //int sum = 0;
        //for (Integer i : people.keySet()) {
        //    for (Integer j : people.keySet()) {
        //        if (people.get(i).isLinked(people.get(j))) {
        //            sum += people.get(i).queryValue(people.get(j));
        //        }
        //    }
        //}
        //return sum;
        return valueSum * 2;
    }
    
    @Override
    public int getAgeMean() {
        //int sum = 0;
        //for (Integer i : people.keySet()) {
        //    sum += people.get(i).getAge();
        //}
        //return sum / people.size();
        return ageSum / people.size();
    }
    
    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int result = 0;
        int ageMean = getAgeMean();
        for (Integer i : people.keySet()) {
            result += (people.get(i).getAge() - ageMean) * (people.get(i).getAge() - ageMean);
        }
        return result / people.size();
    }
    
    @Override
    public void delPerson(Person person) {
        for (Integer pid : people.keySet()) {
            valueSum -= person.queryValue(people.get(pid));
        }
        people.remove(person.getId());
        ageSum -= person.getAge();
    }
    
    @Override
    public int getSize() {
        return people.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyGroup myGroup = (MyGroup) o;
        return id == myGroup.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

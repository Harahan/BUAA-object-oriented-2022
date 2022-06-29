package mycode;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MyGroup implements Group {
    private BigInteger valueSum = new BigInteger("0");
    private BigInteger ageSum = new BigInteger("0");
    private BigInteger ageSquareSum = new BigInteger("0");
    private int id;
    private HashMap<Integer, Person> people = new HashMap<>();
    // id - person

    @Override
    public int getId() {
        return id;
    }

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Group) {
            return (((MyGroup) o).getId() == id);
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            for (Map.Entry<Integer, Person> entryPerson : people.entrySet()) {
                valueSum = valueSum.add(BigInteger.valueOf(2).multiply(
                        BigInteger.valueOf(person.queryValue(entryPerson.getValue())))
                );
            }
            ageSum = ageSum.add(BigInteger.valueOf(person.getAge()));
            ageSquareSum = ageSquareSum.add(BigInteger.valueOf(
                    person.getAge()).multiply(BigInteger.valueOf(person.getAge())));
            people.put(person.getId(), person);
        } //这块容易如果containvalue
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() { // sum of weights
        return valueSum.intValue();
    }

    @Override
    public int getAgeMean() { // avg of points
        return ageSum.divide(BigInteger.valueOf(people.size())).intValue();
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        //        BigInteger part1 = ageSquareSum;
        //        BigInteger part2 =
        //                (new BigInteger("2"))
        //                        .multiply(ageSum)
        //                        .multiply(BigInteger.valueOf(getAgeMean()));
        //        BigInteger part3 =
        //                BigInteger.valueOf(getAgeMean())
        //                        .multiply(BigInteger.valueOf(getAgeMean()))
        //                        .multiply(BigInteger.valueOf(people.size()));
        //        BigInteger result = (part1.subtract(part2).add(part3))
        //                .divide(BigInteger.valueOf(people.size()));
        //        return result.intValue();
        BigInteger mean = BigInteger.valueOf(getAgeMean());
        BigInteger sum = BigInteger.ZERO;
        for (Map.Entry<Integer, Person> entryPerson : people.entrySet()) {
            BigInteger p1 = BigInteger.valueOf(entryPerson.getValue().getAge()).subtract(mean);
            sum = sum.add(p1.multiply(p1));
        }
        return sum.divide(BigInteger.valueOf(people.size())).intValue();
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            for (Map.Entry<Integer, Person> entryPerson : people.entrySet()) {
                valueSum = valueSum.subtract(BigInteger.valueOf(2).multiply(
                        BigInteger.valueOf(person.queryValue(entryPerson.getValue())))
                );
            }
            ageSum = ageSum.subtract(BigInteger.valueOf(person.getAge()));
            ageSquareSum = ageSquareSum.add(BigInteger.valueOf(
                    person.getAge()).multiply(BigInteger.valueOf(person.getAge())));
            people.remove(person.getId());
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void addEverySocialValue(int value) {
        for (Map.Entry<Integer, Person> entryPerson : people.entrySet()) {
            entryPerson.getValue().addSocialValue(value);
        }
    }

    public void addValueSum(int value) {
        valueSum = valueSum.add(BigInteger.valueOf(value).multiply(BigInteger.valueOf(2)));
    }
}

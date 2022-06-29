package program.network;

import com.oocourse.spec2.main.Person;

public class Edge implements Comparable {
    private Person from;
    private Person to;
    private int value;

    public Edge(Person from, Person to, int value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public Person getTo() {
        return to;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.value, ((Edge) o).value);
    }
}

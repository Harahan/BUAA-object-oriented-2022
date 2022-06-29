import com.oocourse.spec2.main.Person;

import java.util.Objects;

public class Edge {
    private int value;
    private Person person1;
    private Person person2;
    
    public Edge(Person person1, Person person2, int value) {
        this.value = value;
        this.person1 = person1;
        this.person2 = person2;
    }
    
    public int getValue() {
        return value;
    }
    
    public Person getPerson1() {
        return person1;
    }
    
    public Person getPerson2() {
        return person2;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return value == edge.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

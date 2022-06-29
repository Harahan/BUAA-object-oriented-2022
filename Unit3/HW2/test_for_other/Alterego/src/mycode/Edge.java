package mycode;

import com.oocourse.spec2.main.Person;

class Edge implements Comparable<Edge> {
    private Person person1;
    private Person person2;
    private int val;

    public Edge(Person person1, Person person2, int val) {
        this.person1 = person1;
        this.person2 = person2;
        this.val = val;
    }

    @Override
    public int compareTo(Edge o) {
        return Integer.compare(this.val, o.val);
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public int getVal() {
        return val;
    }
}
import java.util.ArrayList;

public class Requestlist {
    //相当于一个requestlist ,要求列表
    private ArrayList<Person> people;
    private boolean isEnd;

    public Requestlist() {
        people = new ArrayList<>();
        isEnd = false;
    }

    public synchronized void addRequest(Person person) {
        this.people.add(person);
        notifyAll();
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public synchronized Person getPerson() {
        if (people.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (people.isEmpty()) {
            return null;
        }
        Person person = people.get(0);
        people.remove(0);
        notifyAll();
        return person;
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return people.isEmpty();
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }
}

import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private ArrayList<Person> people;
    private Requestlist requestlist;
    private int floor;
    private boolean up;
    private char id;

    public void setUp(boolean up) {
        this.up = up;
    }

    public Requestlist getRequestlist() {
        return requestlist;
    }

    public Elevator(char id, Requestlist requestlist) {
        this.people = new ArrayList<>();
        this.requestlist = requestlist;
        this.floor = 1;
        this.id = id;
        this.up = true;
    }

    public void up() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floor++;
        TimableOutput.println("ARRIVE-" + id + "-" + floor + "-" + (id - 'A' + 1));
    }

    public void down() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        floor--;
        TimableOutput.println("ARRIVE-" + id + "-" + floor + "-" + (id - 'A' + 1));
    }

    public void open() {
        TimableOutput.println("OPEN-" + id + "-" + floor + "-" + (id - 'A' + 1));
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println("CLOSE-" + id + "-" + floor + "-" + (id - 'A' + 1));
    }

    public void addPerson(boolean ifHas) {
        if (ifHas) {
            ArrayList<Person> delp = new ArrayList<>();
            for (Person person : requestlist.getPeople()) {
                if (person.getLeavefloor() == floor &&
                        ((person.up() == up && !people.isEmpty()) || people.isEmpty())
                        && people.size() <= 6) {
                    TimableOutput.println("IN-" + person.getId() + "-" +
                            id + "-" + floor + "-" + (id - 'A' + 1));
                    delp.add(person);
                    people.add(person);
                    up = person.up();
                }
            }
            for (Person person : delp) {
                if (requestlist.getPeople().contains(person)) {
                    requestlist.getPeople().remove(person);
                }
            }
        }
    }

    public void removePerson(boolean ifHas) {
        if (ifHas) {
            ArrayList<Person> delp = new ArrayList<>();
            for (Person person : people) {
                if (person.getDesfloor() == floor) {
                    TimableOutput.println("OUT-" + person.getId() + "-" +
                            id + "-" + floor + "-" + (id - 'A' + 1));
                    delp.add(person);
                }
            }
            for (Person person : delp) {
                if (people.contains(person)) {
                    people.remove(person);
                }
            }
        }
    }

    public void addRequest(Person person) {
        this.requestlist.addRequest(person);
    }

    public void run() {
        while (!requestlist.isEnd() || !requestlist.isEmpty() || !people.isEmpty()) {
            if (people.isEmpty()) {
                synchronized (requestlist) {
                    if (requestlist.isEmpty()) {
                        try {
                            requestlist.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (requestlist.isEmpty()) {
                        return;
                    }
                    Person request = requestlist.getPeople().get(0);
                    if (request.getLeavefloor() > floor) {
                        up = true;
                        up();
                        continue;
                    } else if (request.getLeavefloor() < floor) {
                        up = false;
                        down();
                        continue;
                    }
                    requestlist.notifyAll();
                }
            }
            if (pOut() || pIn()) {
                open();
                addPerson(pIn());
                removePerson(pOut());
                close();
                continue;
            } else if (people.get(0).getDesfloor() > floor) {
                up = true;
                up();
                continue;
            } else if (people.get(0).getDesfloor() < floor) {
                up = false;
                down();
                continue;
            }
        }
    }

    public boolean pOut() {
        for (Person person : people) {
            if (person.getDesfloor() == floor) {
                return true;
            }
        }
        return false;
    }

    public boolean pIn() {
        for (Person person : requestlist.getPeople()) {
            if (person.getLeavefloor() == floor &&
                    ((person.up() == up && !people.isEmpty()) || people.isEmpty())
                    && people.size() <= 6) {
                return true;
            }
        }
        return false;
    }

}

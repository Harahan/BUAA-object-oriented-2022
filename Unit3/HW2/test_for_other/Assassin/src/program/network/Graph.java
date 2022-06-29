package program.network;

import com.oocourse.spec2.main.Person;
import program.MyPerson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {

    public int prim(Person person, HashSet<Person> brother) {
        Queue<Edge> queue = new PriorityQueue<>();
        HashMap<Person, Integer> dis = new HashMap<>();
        HashMap<Person, Boolean> mark = new HashMap<>();
        int ans = 0;
        for (Person p: brother) {
            dis.put(p, 0x3f3f3f3f);
            mark.put(p, false);
        }
        queue.offer(new Edge(person, person, 0));
        while (!queue.isEmpty()) {
            while (!queue.isEmpty() && mark.get(queue.peek().getTo())) {
                queue.poll();
            }
            if (queue.isEmpty()) {
                break;
            }
            MyPerson target = (MyPerson) queue.peek().getTo();
            mark.put(target, true);
            ans += queue.poll().getValue();

            for (Edge edge: target.getNetwork()) {
                if (edge.getValue() < dis.get(edge.getTo()) && !mark.get(edge.getTo())) {
                    dis.put(edge.getTo(), edge.getValue());
                    queue.offer(edge);
                }
            }
        }
        return ans;
    }
}

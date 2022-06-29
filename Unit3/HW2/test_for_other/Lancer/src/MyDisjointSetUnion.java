import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyDisjointSetUnion {
    private int sum;
    private ArrayList<Integer> personId;
    private HashMap<Integer, Integer> fa;
    private HashMap<Integer, Integer> rank;
    private ArrayList<MyValue> values;

    public MyDisjointSetUnion() {
        sum = 0;
        personId = new ArrayList<>();
        fa = new HashMap<>();
        rank = new HashMap<>();
        values = new ArrayList<>();
    }

    public void addPerson(int id) {
        personId.add(id);
        fa.put(id, id);
        rank.put(id, 1);
        sum++;
    }

    public void addValues(ArrayList<MyValue> values) {
        this.values = values;
    }

    public int find(int x) {
        if (x == fa.get(x)) {
            return x;
        } else {
            fa.put(x, find(fa.get(x)));  //父节点设为根节点
            return fa.get(x);         //返回父节点
        }
    }

    public void merge(int i, int j) {
        int x = find(i);
        int y = find(j);    //先找到两个根节点
        if (x == y) {
            return;
        }
        if (rank.get(x) <= rank.get(y)) {
            fa.put(x, y);
            rank.put(x, rank.get(x) + rank.get(y));
        } else {
            fa.put(y, x);
            rank.put(y, rank.get(x) + rank.get(y));
        }
        sum--;
    }

    public int getSum() {
        return sum;
    }

    public boolean isConnected(int i, int j) {
        int x = find(i);
        int y = find(j);
        return x == y;
    }

    public int kruskal(int id) {
        MyDisjointSetUnion tmp = new MyDisjointSetUnion();
        HashSet<Integer> memberId = new HashSet<>();
        int findId = find(id);
        for (Integer person : personId) { //取小团体中所有人的id
            if (findId == find(person)) {
                tmp.addPerson(person);
                memberId.add(person);
            }
        }
        int personNum = memberId.size();
        if (personNum == 1) { //如果只有一个人，直接返回0
            return 0;
        }

        values.sort(new MyCompLen()); //values按value从小到大排序
        int sum = 0;
        int i = 0;
        int num = 0;
        while (num < personNum - 1) { //kruskal算法
            MyValue value = values.get(i++);
            int id1 = value.getId1();
            int id2 = value.getId2();
            boolean hasId1 = memberId.contains(id1);
            boolean hasId2 = memberId.contains(id2);
            if (!hasId1 && !hasId2) {
                continue;
            }
            if (tmp.isConnected(id1, id2)) {
                continue;
            }
            tmp.merge(id1, id2);
            sum += value.getValue();
            num++;
            //System.out.println(value.getId1() + " " + value.getId2() + " " + value.getValue());
        }
        return sum;
    }
}

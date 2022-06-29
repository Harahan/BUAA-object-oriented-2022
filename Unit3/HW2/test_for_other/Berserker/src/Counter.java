import java.util.HashMap;

public class Counter {
    private int all  = 0;
    private HashMap<Integer, Integer> map = new HashMap<>();
    
    public void add() {
        all++;
    }
    
    public int getAll() {
        return all;
    }
    
    public void addId(int id) {
        if (map.containsKey(id)) {
            map.put(id, map.get(id) + 1);
        } else {
            map.put(id, 1);
        }
    }
    
    public int getId(int id) {
        return map.getOrDefault(id, 0);
    }
}

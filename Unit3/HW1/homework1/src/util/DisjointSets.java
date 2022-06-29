package util;

import java.util.HashMap;

public class DisjointSets {
    private final HashMap<Integer, Integer> fa = new HashMap<>();
    private final HashMap<Integer, Integer> rk = new HashMap<>();
    private int tot = 0;

    public void add(int x) {
        fa.put(x, x);
        rk.put(x, 1);
        tot += 1;
    }

    public int getTot() {
        return tot;
    }

    public int find(int x) {
        return fa.get(x) == x ? x : fa.merge(x, find(fa.get(x)), (a, b) -> b);
    }

    public void merge(int xx, int yy) {
        int fx = find(xx);
        int fy = find(yy);
        if (fx == fy) {
            return;
        }
        if (rk.get(fx) < rk.get(fy)) {
            fa.merge(fx, fy, (a, b) -> b);
        } else {
            fa.merge(fy, fx, (a, b) -> b);
            if (rk.get(fx).equals(rk.get(fy))) {
                rk.merge(fx, 1, Integer::sum);
            }
        }
        tot -= 1;
    }

}

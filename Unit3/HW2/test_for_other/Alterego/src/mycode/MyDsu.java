package mycode;

import java.util.HashMap;

public class MyDsu<T> {
    private HashMap<T, T> fatherMap = new HashMap<>();
    private HashMap<T, Integer> rankMap = new HashMap<>();

    public MyDsu() {
    }

    public void addElement(T a) {
        fatherMap.put(a, a);
        rankMap.put(a, 1);
    }

    public T findRoot(T a) {
        T r = a;
        while (fatherMap.get(r) != r) {
            r = fatherMap.get(r);
        }
        T i = a;
        T tmp;
        while (i != r) {
            tmp = fatherMap.get(i);
            fatherMap.put(i, r);
            i = tmp;
        }
        return r;
    }

    public void unionSet(T a, T b) {
        if (a == null || b == null) {
            return;
        }
        T root1 = findRoot(a);
        T root2 = findRoot(b);
        if (rankMap.get(root1) == rankMap.get(root2)) {
            fatherMap.put(root2, root1); //root 1 is fa
            rankMap.put(root1, rankMap.get(root1) + 1);
        } else {
            if (rankMap.get(root1) < rankMap.get(root2)) {
                fatherMap.put(root1, root2);
            } else {
                fatherMap.put(root2, root1);
            }
        }
    }

}

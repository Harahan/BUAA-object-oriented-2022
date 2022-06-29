package util;

public class Node {
    private final int id;
    private final int dis;

    public Node(int id, int dis) {
        this.id = id;
        this.dis = dis;
    }

    public int getId() {
        return id;
    }

    public int getDis() {
        return dis;
    }
}

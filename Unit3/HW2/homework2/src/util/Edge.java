package util;

public class Edge {
    private final int ux;
    private final int vx;
    private final int wx;

    public Edge(int ux, int vx, int wx) {
        this.ux = ux;
        this.vx = vx;
        this.wx = wx;
    }

    public int getUx() {
        return ux;
    }

    public int getVx() {
        return vx;
    }

    public int getWx() {
        return wx;
    }
}

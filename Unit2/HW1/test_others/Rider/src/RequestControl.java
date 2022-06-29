import java.util.Vector;

public class RequestControl {
    private boolean hasNewRequest;
    private Vector<Request> requestsForA;
    private Vector<Request> requestsForB;
    private Vector<Request> requestsForC;
    private Vector<Request> requestsForD;
    private Vector<Request> requestsForE;
    private Object obj;

    public RequestControl(Object obj) {
        hasNewRequest = true;
        requestsForA = new Vector<>();
        requestsForB = new Vector<>();
        requestsForC = new Vector<>();
        requestsForD = new Vector<>();
        requestsForE = new Vector<>();
        this.obj = obj;
    }

    public boolean isHasNewRequest() {
        return hasNewRequest;
    }

    public Vector<Request> getRequestsForA() {
        return requestsForA;
    }

    public Vector<Request> getRequestsForB() {
        return requestsForB;
    }

    public Vector<Request> getRequestsForC() {
        return requestsForC;
    }

    public Vector<Request> getRequestsForD() {
        return requestsForD;
    }

    public Vector<Request> getRequestsForE() {
        return requestsForE;
    }

    public void noNewRequest() {
        synchronized (obj) {
            hasNewRequest = false;
            obj.notifyAll();
        }
    }

    public void putRequest(Request request) {
        synchronized (obj) {
            if (request.getFromBuilding() == 'A') {
                synchronized (requestsForA) {
                    requestsForA.add(request);
                }
            }
            else if (request.getFromBuilding() == 'B') {
                synchronized (requestsForB) {
                    requestsForB.add(request);
                }
            }
            else if (request.getFromBuilding() == 'C') {
                synchronized (requestsForC) {
                    requestsForC.add(request);
                }
            }
            else if (request.getFromBuilding() == 'D') {
                synchronized (requestsForD) {
                    requestsForD.add(request);
                }
            }
            else if (request.getFromBuilding() == 'E') {
                synchronized (requestsForE) {
                    requestsForE.add(request);
                }
            }
            obj.notifyAll();
        }
    }
}

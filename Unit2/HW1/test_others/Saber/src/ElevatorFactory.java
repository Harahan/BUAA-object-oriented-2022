public class ElevatorFactory {
    public static Elevator lookElevator(int id, RequestTable requestTable) {
        return new Elevator(id, requestTable, new LookStrategy());
    }
}

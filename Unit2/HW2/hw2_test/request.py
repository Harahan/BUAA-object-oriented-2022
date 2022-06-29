class Request:
    def __init__(self):
        pass


class PersonRequest(Request):

    def __init__(self, time: float, id: int, fromBuilding: str, toBuilding: str, fromFloor: int, toFloor: int):
        self.time = time
        self.id = id
        self.fromBuilding = fromBuilding
        self.toBuilding = toBuilding
        self.fromFloor = fromFloor
        self.toFloor = toFloor
        self.finished = False
        self.inelevator = False

    def __str__(self):
        return f"[%.1f]%d-FROM-%s-%d-TO-%s-%d" % \
               (self.time, self.id, self.fromBuilding, self.fromFloor, self.toBuilding, self.toFloor)


# ADD-building-电梯ID-楼座ID
# ADD-floor-电梯ID-楼层ID
class ElevatorRequest(Request):
    def __init__(self, time: float, elevatorType: str, elevatorID: int, floor: int, building: str):
        self.time = time
        self.elevatorType = elevatorType
        self.elevatorID = elevatorID
        self.floor = floor
        self.building = building

    def __str__(self):
        if self.elevatorType == "building":
            return f"[%.1f]ADD-%s-%d-%s" % \
               (self.time, self.elevatorType, self.elevatorID, self.building)
        else:
            return f"[%.1f]ADD-%s-%d-%d" % \
                   (self.time, self.elevatorType, self.elevatorID, self.floor)

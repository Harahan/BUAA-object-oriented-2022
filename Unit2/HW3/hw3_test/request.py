class Request:
    pass


class PersonRequest(Request):
    def __init__(self, time: str, id: int, fromBuilding: str, toBuilding: str,
                 fromFloor: int, toFloor: int, locateFloor = 1, locateBuilding = 'A'):
        self.time = time
        self.id = id
        self.fromBuilding = fromBuilding
        self.toBuilding = toBuilding
        self.fromFloor = fromFloor
        self.toFloor = toFloor
        self.locateFloor = locateFloor
        self.locateBuilding = locateBuilding
        self.finished = False
        self.inelevator = False

    def __str__(self):
        return f"[%s]%d-FROM-%s-%d-TO-%s-%d" % \
               (self.time, self.id, self.fromBuilding, self.fromFloor, self.toBuilding, self.toFloor)


class ElevatorRequest(Request):
    def __init__(self, time: str, elevatorType: str, elevatorID: int,
                 floor: int, building: str, speed: str, capacity: int, switchInfo=31):
        self.time = time
        self.elevatorType = elevatorType
        self.elevatorID = elevatorID
        self.floor = floor
        self.building = building
        self.speed = speed
        self.capacity = capacity
        self.switchInfo = switchInfo

    def __str__(self):
        if self.elevatorType == "building":
            return f"[%s]ADD-building-%d-%s-%d-%s" % \
               (self.time, self.elevatorID, self.building, self.capacity, self.speed)
        else:
            return f"[%s]ADD-floor-%d-%d-%d-%s-%d" % \
                   (self.time, self.elevatorID,
                    self.floor, self.capacity, self.speed, self.switchInfo)

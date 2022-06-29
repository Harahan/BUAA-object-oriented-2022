class Instruction:

    # [ 0.3160]IN-1-B-1-2
    # IN, OUT
    # [ 0.0120]OPEN-B-1-2
    # ARRIVE, OPEN, CLOSE
    def __init__(self, type: str, time: float, building: str, locateFloor: int, elevatorID: int, passengerID=None):
        if passengerID is not None:
            self.type = type
            self.time = time
            self.passengerID = passengerID
            self.building = building
            self.locateFloor = locateFloor
            self.elevatorID = elevatorID
        else:
            self.type = type
            self.time = time
            self.building = building
            self.locateFloor = locateFloor
            self.elevatorID = elevatorID

    def __str__(self):
        if self.type in ['IN', 'OUT']:
            return f"[%.4f]%s-%d-%s-%d-%d" % \
                   (self.time, self.type, self.passengerID, self.building, self.locateFloor, self.elevatorID)
        else:
            return f"[%.4f]%s-%s-%d-%d" % \
                   (self.time, self.type, self.building, self.locateFloor, self.elevatorID)

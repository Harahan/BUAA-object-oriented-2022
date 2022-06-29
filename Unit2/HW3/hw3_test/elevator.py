import math


class Elevator:

    def __init__(self, locateBuilding: str, locateFloor: int, id: int,
                 valid: bool, elevatorType: str, activateTime: str,
                 capacity: int, speed: str, switchInfo=31):
        self.valid = valid
        self.elevatorType = elevatorType
        self.locateFloor = locateFloor
        self.locateBuilding = locateBuilding
        self.id = id
        self.innerPassenger = []
        self.lastEventTime = 0
        self.capacity = capacity
        if speed == "0.4":
            self.speed = 400
        elif speed == "0.6":
            self.speed = 600
        else:
            self.speed = 800
        self.isDoorClosed = True
        self.activateTime = self.getIntegerTime(activateTime)
        self.switchInfo = switchInfo

    def getIntegerTime(self, time: str) -> int:
        # time: [x.y]
        x, y = time.split('.')
        if len(y) < 4:
            new_y = y + "0"*(4 - len(y))
            return int(x+new_y)
        else:
            new_y = y[0:4]
            return int(x+new_y)

    def open(self, time: str):
        if self.elevatorType == "floor":
            if ((self.switchInfo >> (ord(self.locateBuilding) - ord('A'))) & 1) != 1:
                print('Fatal Error: This elevator cannot open the door here')
                return False
        if not self.isDoorClosed:
            print('Fatal Error: Your cannot open the door twice')
            return False
        self.lastEventTime = self.getIntegerTime(time)
        self.isDoorClosed = False
        return True

    def close(self, time: str):
        if self.isDoorClosed:
            print('Fatal Error: Your cannot close the door twice')
            return False
        # print(time * 1000, self.lastEventTime * 1000)
        if self.getIntegerTime(time) - self.lastEventTime < 400:
            print('Fatal Error: You open/close the door too fast')
            return False
        self.lastEventTime = self.getIntegerTime(time)
        self.isDoorClosed = True
        return True

    def pickUp(self, passengerID) -> bool:
        if self.isDoorClosed:
            print('Fatal Error: Your elevator is closed, cannot pick up person')
            return False
        for i in self.innerPassenger:
            if i == passengerID:
                print('Fatal Error: Person ID ' + str(passengerID) + ' get into the elevator too many times')
                return False
        if len(self.innerPassenger) >= self.capacity:
            print('Fatal Error: Too many people in the elevator')
            return False
        self.innerPassenger.append(passengerID)
        return True

    def putDown(self, passengerID) -> bool:
        if self.isDoorClosed:
            print('Fatal Error: Your elevator is closed, cannot put down person')
            return False
        try:
            self.innerPassenger.remove(passengerID)
        except ValueError:
            print('Fatal Error: Person ID ' + str(passengerID) + ' is not exist in the elevator')
            return False
        finally:
            return True

    def isValid(self, instr_time):
        return self.activateTime < self.getIntegerTime(instr_time)

    def move(self, target, time: str):
        if self.elevatorType == 'building':
            if type(target) != int:
                print('Fatal Error: Your elevator run in wrong direction')
                return False
            # print(time*1000, self.lastEventTime*1000)
            if self.getIntegerTime(time) - self.lastEventTime < self.speed:
                print('Fatal Error: Your elevator move too fast')
                return False
            if not self.isDoorClosed:
                print('Fatal Error: Your elevator cannot move keep the door opened')
                return False
            if math.fabs(target - self.locateFloor) > 1:
                print('Fatal Error: Your elevator jump too fast')
                return False
            if target > 10 or target <= 0:
                print('Fatal Error: Your elevator fly to the moon or deep into the ground')
                return False
            self.locateFloor = target
            self.lastEventTime = self.getIntegerTime(time)
            return True
        elif self.elevatorType == 'floor':
            if type(target) != str:
                print('Fatal Error: Your elevator run in wrong direction')
                return False
            if self.getIntegerTime(time) - self.lastEventTime < self.speed:
                print('Fatal Error: Your elevator move too fast')
                return False
            if not self.isDoorClosed:
                print('Fatal Error: Your elevator cannot move keep the door opened')
                return False
            if not ((self.locateBuilding == 'A' and target == 'E') or
                    (self.locateBuilding == 'E' and target == 'A') or
                    (math.fabs(ord(self.locateBuilding) - ord(target)) == 1)):
                print('Fatal Error: Your elevator jump too fast')
                return False
            self.locateBuilding = target
            self.lastEventTime = self.getIntegerTime(time)
            return True

    def __str__(self):
        print(self.innerPassenger, self.id, self.locateFloor)

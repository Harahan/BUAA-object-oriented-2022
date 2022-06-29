import math
import re

import input_parser
import instruction
import request


class Elevator:

    def __init__(self, building: str, locateFloor: int, id: int, valid: bool, elevatorType: str, activateTime: float):
        self.valid = valid
        self.elevatorType = elevatorType
        self.locateFloor = locateFloor
        self.building = building
        self.id = id
        self.innerPassenger = []
        self.lastEventTime = -1
        self.isDoorClosed = True
        self.activateTime = activateTime

    def open(self, time):
        if not self.isDoorClosed:
            print('Fatal Error: Your cannot open the door twice')
            return False
        self.lastEventTime = time
        self.isDoorClosed = False
        return True

    def close(self, time):
        if self.isDoorClosed:
            print('Fatal Error: Your cannot close the door twice')
            return False
        # print(time * 1000, self.lastEventTime * 1000)
        if time*1000 - self.lastEventTime*1000 < 395:
            print('Fatal Error: You open/close the door too fast')
            return False
        self.lastEventTime = time
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
        if len(self.innerPassenger) >= 6:
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
        return self.activateTime < instr_time

    def move(self, target, time):
        if self.elevatorType == 'building':
            if type(target) != int:
                print('Fatal Error: Your elevator run in wrong direction')
                return False
            # print(time*1000, self.lastEventTime*1000)
            if time*1000 - self.lastEventTime*1000 < 395:
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
            self.lastEventTime = time
            return True
        elif self.elevatorType == 'floor':
            if type(target) != str:
                print('Fatal Error: Your elevator run in wrong direction')
                return False
            if time*1000 - self.lastEventTime*1000 < 195:
                print('Fatal Error: Your elevator move too fast')
                return False
            if not self.isDoorClosed:
                print('Fatal Error: Your elevator cannot move keep the door opened')
                return False
            if not ((self.building == 'A' and target == 'E') or (self.building == 'E' and target == 'A') or (math.fabs(ord(self.building) - ord(target)) == 1)):
                print('Fatal Error: Your elevator jump too fast')
                return False
            self.building = target
            self.lastEventTime = time
            return True

    def __str__(self):
        print(self.innerPassenger, self.id, self.locateFloor)


def check_correct(input: list, raw_output: list, elevators: list) -> bool:
    # Step 0: parse the output, initialize the elevator
    output = parse_output(raw_output)
    if output[0] is None:
        return False

    # Step 1: check the output time rise
    for i in range(1, len(output)):
        if output[i].time < output[i - 1].time:
            print('Error in line' + str(i + 1))
            print("Fatal Error: time sequence incorrect")
            return False

    # Step 2: Try to simulate the output process
    for instr in output:
        explain = open('explain.txt', 'a')
        explain.write(str(instr) + '\n')
        for e in elevators:
            explain.write(str(e.id) + ' ' + str(e.innerPassenger) + ' ' + str(e.isValid(instr.time)) + ' ' + str(e.locateFloor) + ' ' + str(e.building) + ' ' + str(e.elevatorType) + '\n')
        explain.close()
        # print(instr)
        if instr.type == 'IN':
            ok = False
            for e in elevators:
                if e is None: continue
                if e.isValid(instr.time) is False: continue
                if e.id == instr.elevatorID and e.building == instr.building and e.locateFloor == instr.locateFloor:
                    for passenger in input:
                        if isinstance(passenger, request.ElevatorRequest) : continue
                        if passenger.id == instr.passengerID and passenger.fromBuilding == instr.building and instr.locateFloor == passenger.fromFloor:
                            if passenger.finished is True:
                                print('Fatal Error: You cannot pick person twice')
                                print('Passenger Message ' + str(passenger))
                                print('Instruction Message ' + str(instr))
                                return False
                            if passenger.inelevator is True:
                                print('Fatal Error: Passenger cannot get on the elevator twice')
                                print('Passenger Message ' + str(passenger))
                                print('Instruction Message ' + str(instr))
                                return False
                            if passenger.time > instr.time:
                                print('Fatal Error: You cannot pick person before he/she come')
                                print('Passenger Message ' + str(passenger))
                                print('Instruction Message ' + str(instr))
                                return False
                            ok = e.pickUp(passenger.id)
                            if ok: passenger.inelevator = True
                            #print(passenger)
                            #print(e.id)
                            break
                    if ok: break
            if not ok:
                print('Fatal Error: You pick up nothing')
                print('Instruction Message ' + str(instr))
                return False
        elif instr.type == 'OUT':
            ok = False
            for i in range(len(elevators)):
                if elevators[i] is None: continue
                if elevators[i].isValid(instr.time) is False: continue
                if elevators[i].id == instr.elevatorID and elevators[i].building == instr.building and elevators[i].locateFloor == instr.locateFloor:
                    for passenger in input:
                        if isinstance(passenger, request.ElevatorRequest): continue
                        if passenger.id == instr.passengerID and passenger.toBuilding == instr.building and instr.locateFloor == passenger.toFloor:
                            if passenger.inelevator is False: continue
                            ok = elevators[i].putDown(passenger.id)
                            if ok:
                                passenger.finished = True
                                passenger.inelevator = False
                                break
                            else:
                                print('Passenger Message ' + passenger)
                                print('Instruction Message ' + instr)
                                return False
                    if ok: break
            if not ok:
                print('Fatal Error: You put down nothing')
                print('Instruction Message ' + str(instr))
                return False
        elif instr.type == 'OPEN':
            for e in elevators:
                if e is None: continue
                if e.id == instr.elevatorID and e.building == instr.building and e.locateFloor == instr.locateFloor:
                    ok = e.open(instr.time)
                    if ok is True:
                        continue
                    else:
                        print('Instruction Message ' + str(instr))
                        return False
        elif instr.type == 'CLOSE':
            for e in elevators:
                if e is None: continue
                if e.id == instr.elevatorID and e.building == instr.building and e.locateFloor == instr.locateFloor:
                    ok = e.close(instr.time)
                    if ok is True:
                        continue
                    else:
                        print('Instruction Message ' + str(instr))
                        return False
        elif instr.type == 'ARRIVE':
            for e in elevators:
                if e is None:
                    continue
                if e.elevatorType == 'building':
                    if e.id == instr.elevatorID and e.building == instr.building:
                        ok = e.move(instr.locateFloor, instr.time)
                        if ok is True:
                            continue
                        else:
                            print('Instruction Message ' + str(instr))
                            return False
                elif e.elevatorType == 'floor':
                    if e.id == instr.elevatorID and e.locateFloor == instr.locateFloor:
                        ok = e.move(instr.building, instr.time)
                        if ok is True:
                            continue
                        else:
                            print('Instruction Message ' + str(instr))
                            return False
        else:
            print('Fatal Error: invalid instruction')
            print('Instruction Message ' + str(instr))
            return False

    for i in input:
        if isinstance(i, request.ElevatorRequest): continue
        else:
            if i.finished is False or i.inelevator is True:
                print(i)
                print('Fatal Error: You did not finish the request')
                return False

    return True


# [时间戳]ARRIVE-所在座-所在层-电梯ID
# [时间戳]IN-乘客ID-所在座-所在层-电梯ID
def parse_output(raw_output: list) -> list:
    pattern = re.compile(r"\[(\s*\d+\.\d+\s*)\](.*)")
    output = []
    for instr in raw_output:
        matcher = re.match(pattern, str(instr).strip())

        if matcher.group(1) is not None and matcher.group(2) is not None:
            time = float(matcher.group(1))
            others = matcher.group(2)
            arguments = others.split('-')
            # print(arguments)
            if arguments[0] in ['IN', 'OUT']:
                try:
                    passengerID = int(arguments[1])
                    building = arguments[2]
                    locateFloor = int(arguments[3])
                    elevatorID = int(arguments[4])
                    output.append(
                        instruction.Instruction(arguments[0], time, building, locateFloor, elevatorID, passengerID))
                except TypeError:
                    print("Fatal Error: Wrong output format in line " + str(raw_output.index(instr)))
                    return [None]
            else:
                try:
                    building = arguments[1]
                    locateFloor = int(arguments[2])
                    elevatorID = int(arguments[3])
                    output.append(instruction.Instruction(arguments[0], time, building, locateFloor, elevatorID))
                except TypeError:
                    print("Fatal Error: Wrong output format in line " + str(raw_output.index(instr)))
                    return [None]
        else:
            print("Error: Wrong output format in line " + str(raw_output.index(instr)))
            return [None]
    return output


if __name__ == '__main__':
    elevators = [Elevator('A', 1, 1, True, "building", 0.0), Elevator('B', 1, 2, True, "building", 0.0),
                 Elevator('C', 1, 3, True, "building", 0.0), Elevator('D', 1, 4, True, "building", 0.0),
                 Elevator('E', 1, 5, True, "building", 0.0)]
    # print(elevators)
    file = open("stdin.txt", "r")
    input = input_parser.parse(file)
    if input[0] is None:
        print('Fatal Error: input error')
        exit(0)
    for i in input:
        if type(i) == request.ElevatorRequest:
            print(i)
            if i.elevatorType == 'building':
                elevators.append(Elevator(i.building, 1, i.elevatorID, True, 'building', i.time))
            else:
                elevators.append(Elevator('A', i.floor, i.elevatorID, True, 'floor', i.time))
        # print(i)
    file = open('stdout.txt')
    raw_output = file.readlines()
    output = parse_output(raw_output)
    print(check_correct(input, output, elevators))

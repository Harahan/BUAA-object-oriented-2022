import re

import elevator
import input_parser
import request
import instruction


def getIntegerTime(time: str) -> int:
    # time: [x.y(4)]
    x, y = time.split('.')
    if len(y) < 4:
        new_y = y + "0" * (4 - len(y))
        return int(x + new_y)
    else:
        new_y = y[0:4]
        return int(x + new_y)


def check_correct(input: list, raw_output: list, elevators: list) -> bool:
    # Step 0: parse the output, initialize the elevator
    output = parse_output(raw_output)
    if output[0] is None:
        return False

    # Step 1: check the output time rise
    for i in range(1, len(output)):
        if getIntegerTime(output[i].time) < getIntegerTime(output[i - 1].time):
            print('Error in line' + str(i + 1))
            print("Fatal Error: time sequence incorrect")
            return False

    # Step 2: Try to simulate the output process
    for instr in output:
        # print(instr)
        explain = open('explain.txt', 'a')
        explain.write(str(instr) + '\n')
        for e in elevators:
            explain.write(str(e.id) + ' ' + str(e.innerPassenger) + ' ' +
                          str(e.isValid(instr.time)) + ' ' + str(e.locateFloor) + ' ' +
                          str(e.locateBuilding) + ' ' + str(e.elevatorType) + '\n')
        explain.close()
        # print(instr)
        if instr.type == 'IN':
            ok = False
            for e in elevators:
                if e is None: continue
                if e.isValid(instr.time) is False: continue
                if e.id == instr.elevatorID and e.locateBuilding == instr.building and e.locateFloor == instr.locateFloor:
                    for passenger in input:
                        if isinstance(passenger, request.ElevatorRequest) : continue
                        if passenger.id == instr.passengerID and passenger.locateBuilding == instr.building and instr.locateFloor == passenger.locateFloor:
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
                            if getIntegerTime(passenger.time) > getIntegerTime(instr.time):
                                print('Fatal Error: You cannot pick person before he/she come')
                                print('Passenger Message ' + str(passenger))
                                print('Instruction Message ' + str(instr))
                                return False
                            ok = e.pickUp(passenger.id)
                            if ok: passenger.inelevator = True
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
                if elevators[i].id == instr.elevatorID and \
                    elevators[i].locateBuilding == instr.building and \
                    elevators[i].locateFloor == instr.locateFloor:
                    for passenger in input:
                        if isinstance(passenger, request.ElevatorRequest): continue
                        if passenger.id == instr.passengerID:
                            if passenger.inelevator is False: continue
                            ok = elevators[i].putDown(passenger.id)
                            if ok:
                                passenger.locateFloor = instr.locateFloor
                                passenger.locateBuilding = instr.building
                                passenger.inelevator = False
                                if passenger.locateFloor == passenger.toFloor and \
                                    passenger.locateBuilding == passenger.toBuilding:
                                    passenger.finished = True
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
                if e.id == instr.elevatorID and e.locateBuilding == instr.building and e.locateFloor == instr.locateFloor:
                    ok = e.open(instr.time)
                    if ok is True:
                        continue
                    else:
                        print('Instruction Message ' + str(instr))
                        return False
        elif instr.type == 'CLOSE':
            for e in elevators:
                if e is None: continue
                if e.id == instr.elevatorID and e.locateBuilding == instr.building and e.locateFloor == instr.locateFloor:
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
                    if e.id == instr.elevatorID and e.locateBuilding == instr.building:
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
    pattern = re.compile(r"\[(\s*[\w\.]+\s*)\](.*)")
    output = []
    for instr in raw_output:
        matcher = re.match(pattern, str(instr).strip())

        if matcher.group(1) is not None and matcher.group(2) is not None:
            time = matcher.group(1).strip()
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
    elevators = [elevator.Elevator('A', 1, 1, True, "building", "0.0", 8, "0.6"),
                 elevator.Elevator('B', 1, 2, True, "building", "0.0", 8, "0.6"),
                 elevator.Elevator('C', 1, 3, True, "building", "0.0", 8, "0.6"),
                 elevator.Elevator('D', 1, 4, True, "building", "0.0", 8, "0.6"),
                 elevator.Elevator('E', 1, 5, True, "building", "0.0", 8, "0.6"),
                 elevator.Elevator('A', 1, 6, True, "floor", "0.0", 8, "0.6")]
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
                elevators.append(elevator.Elevator(i.building, 1, i.elevatorID, True, 'building',
                                                   i.time, i.capacity, i.speed))
            else:
                elevators.append(elevator.Elevator('A', i.floor, i.elevatorID, True, 'floor',
                                                   i.time, i.capacity, i.speed, i.switchInfo))
        # print(i)
    file = open('stdout.txt')
    raw_output = file.readlines()
    # output = parse_output(raw_output)
    # print(output)
    print(check_correct(input, raw_output, elevators))

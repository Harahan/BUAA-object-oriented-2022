import random
import request


def data_generate(max_time: float) -> list:
    instr_list = []
    id_list = []
    elevator_list = []
    elevator_id = 114514
    valid_level = set()
    req_id = 1
    for i in range(15):
        elevator_list.append(random.choice([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 'A', 'B', 'C', 'D', 'E']))
    time = 0.0
    # print(elevator_list)
    while time <= max_time:
        if (time in [0.0, 2.0, 6.0, 8.0, 10.0, 12.0, 16.0, 20.0, 24.0, 26.0, 28.0, 36.0, 40.0, 44.0, 48.0, 52.0, 56.0, 58.0, 60.0, 64.0, 68.0]) and len(elevator_list) > 0:
            newElevator = elevator_list.pop(0)
            if type(newElevator) == int:
                instr_list.append(request.ElevatorRequest(time, "floor", elevator_id, newElevator, 'A'))
                valid_level.add(newElevator)
            else:
                instr_list.append(request.ElevatorRequest(time, "building", elevator_id, 1, newElevator))
            elevator_id += 1
        req_cnt = random.randint(1, 9)
        req_type = random.choice(["building", "floor"])
        req_level = random.random()
        if req_level <= 0.8:
            for i in range(req_cnt):
                if req_type == 'floor' and len(valid_level) > 0:
                    floor = random.choice(list(valid_level))
                    fromBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                    toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                    while toBuilding == fromBuilding: toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                    instr = request.PersonRequest(time + random.random(), req_id, fromBuilding, toBuilding, floor, floor)
                    instr_list.append(instr)
                else:
                    building = random.choice(["A", 'B', 'C', 'D', 'E'])
                    FromFloor = random.randint(1, 10)
                    ToFloor = random.randint(1, 10)
                    while ToFloor == FromFloor: ToFloor = random.randint(1, 10)
                    instr = request.PersonRequest(time + random.random(), req_id, building, building, FromFloor, ToFloor)
                    instr_list.append(instr)
                req_id += 1
        else:
            req_cnt = 10
            if req_type == 'floor' and len(valid_level) > 0:
                floor = random.choice(list(valid_level))
                fromBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                while toBuilding == fromBuilding: toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                for i in range(req_cnt):
                    instr = request.PersonRequest(time + random.random(), req_id, fromBuilding, toBuilding, floor, floor)
                    instr_list.append(instr)
                    req_id += 1
            else:
                building = random.choice(["A", 'B', 'C', 'D', 'E'])
                FromFloor = random.randint(1, 10)
                ToFloor = random.randint(1, 10)
                while ToFloor == FromFloor: ToFloor = random.randint(1, 10)
                for i in range(req_cnt):
                    instr = request.PersonRequest(time + random.random(), req_id, building, building, FromFloor, ToFloor)
                    instr_list.append(instr)
                    req_id += 1
        time += 1
    instr_list.sort(key=lambda x: x.time)
    return instr_list


if __name__ == '__main__':
    list = data_generate(20.0)
    for i in list:
        print(i)

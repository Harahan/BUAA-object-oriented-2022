import random
import request


def data_generate(max_time: float) -> list:
    instr_list = []
    id_list = []
    elevator_list = []
    capacity_list = [4, 6, 8]
    speed_list = [0.2, 0.4, 0.6]
    switchInfor_list = [_ for _ in range(1, 32)]
    # switchInfor_list = [3, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31]
    elevator_id = 114514
    valid_level = set()
    req_id = 1
    for i in range(50):
        elevator_list.append(random.choice([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 'A', 'B', 'C', 'D', 'E', 'A', 'B', 'C', 'D', 'E']))
    time = 0.0
    # print(elevator_list)
    while time <= max_time:
        if (random.randint(1, 5000) % 2 == 1) and len(elevator_list) > 0:
            newElevator = elevator_list.pop(0)
            if type(newElevator) == int:
                instr_list.append(request.ElevatorRequest(str(time), "floor", elevator_id, newElevator, 'A', str(random.choice(speed_list)), random.choice(capacity_list), random.choice(switchInfor_list)))
                valid_level.add(newElevator)
            else:
                instr_list.append(request.ElevatorRequest(str(time), "building", elevator_id, 1, newElevator, str(random.choice(speed_list)), random.choice(capacity_list), 31))
            elevator_id += 1
        req_cnt = random.randint(1, 9)
        req_level = random.random()
        if req_level <= 0.6:
            for i in range(req_cnt):
                fromBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                FromFloor = random.randint(1, 10)
                ToFloor = random.randint(1, 10)
                while toBuilding == fromBuilding or ToFloor == FromFloor:
                    toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                    ToFloor = random.randint(1, 10)
                instr = request.PersonRequest(str(round(time + random.random(), 1)), req_id, fromBuilding, toBuilding, FromFloor, ToFloor)
                instr_list.append(instr)
                req_id += 1
        elif req_level <= 0.8:
            for i in range(req_cnt):
                fromBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                toBuilding = fromBuilding
                FromFloor = random.randint(1, 10)
                ToFloor = random.randint(1, 10)
                while ToFloor == FromFloor:
                    ToFloor = random.randint(1, 10)
                instr = request.PersonRequest(str(round(time + random.random(), 1)), req_id, fromBuilding, toBuilding, FromFloor, ToFloor)
                instr_list.append(instr)
                req_id += 1
        else:
            for i in range(req_cnt):
                fromBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                FromFloor = random.randint(1, 10)
                ToFloor = FromFloor
                while toBuilding == fromBuilding:
                    toBuilding = random.choice(["A", 'B', 'C', 'D', 'E'])
                instr = request.PersonRequest(str(round(time + random.random(), 1)), req_id, fromBuilding, toBuilding, FromFloor, ToFloor)
                instr_list.append(instr)
                req_id += 1
        time += 1
    instr_list.sort(key=lambda x: float(x.time))
    return instr_list


if __name__ == '__main__':
    list = data_generate(20.0)
    file = open("stdin.txt", "w")
    for i in list:
        file.write(str(i) + "\n")

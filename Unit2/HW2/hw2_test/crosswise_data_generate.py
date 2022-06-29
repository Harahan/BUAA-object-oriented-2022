import random
import request


def crosswise_data_generate() -> list:
    instr_list = []
    instr_list.append(request.ElevatorRequest(0, "floor", 114514, 1, 'A'))
    instr_list.append(request.ElevatorRequest(0, "floor", 114515, 1, 'A'))
    instr_list.append(request.ElevatorRequest(0, "floor", 114516, 1, 'A'))
    req_id = 1
    lasttype = 2
    level1 = 0.7
    level2 = 0.85
    magicint = 3
    while True:
        for i in range(5):
            if random.random() < 0.4:
                continue
            magicnumber = random.random()
            if lasttype == 0:
                level1 = 0.8
                level2 = 0.9
            if lasttype == 1:
                level1 = 0.1
                level2 = 0.2
            if lasttype == 2:
                level1 = 0.1
                level2 = 0.9
            if magicnumber < level1:
                instr = request.PersonRequest(10, req_id, chr(ord('A') + i), chr(ord('A') + (i + 1) % 5), 1, 1)
                instr_list.append(instr)
                req_id += 1
                lasttype = 2
                magicint = 3
            elif magicnumber < level2:
                instr = request.PersonRequest(10, req_id, chr(ord('A') + i), chr(ord('A') + (i + magicint) % 5), 1, 1)
                instr_list.append(instr)
                req_id += 1
                if magicint == 2:
                    lasttype = 0
                else:
                    lasttype = 1
            else:
                instr = request.PersonRequest(10, req_id, chr(ord('A') + i), chr(ord('A') + (i + 4) % 5), 1, 1)
                instr_list.append(instr)
                req_id += 1
                lasttype = 2
                magicint = 2
        if len(instr_list) >= 200:
            break
    return instr_list

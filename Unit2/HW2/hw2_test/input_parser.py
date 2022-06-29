import re
import request

# ADD-building-µçÌÝID-Â¥×ùID
# ADD-floor-µçÌÝID-Â¥²ãID
def parse(file) -> list:
    request_list = []
    pattern = re.compile(r'\[\s*(\d+(\.\d+)?)\](\d+)-FROM-([A-E])-(\d+)-TO-([A-E])-(\d+)')
    add_pattern = re.compile(r'\[\s*(\d+\.\d+)\]ADD-([a-z]*)-(\d+)-([A-E\d]+)')
    for line in file:
        # print(line)
        normal_match = pattern.match(line.strip())
        add_match = add_pattern.match(line.strip())
        if normal_match is not None:
            time, id, fromBuilding, toBuilding, from_floor, to_floor =  \
                 normal_match.group(1), normal_match.group(3), normal_match.group(4), normal_match.group(6), normal_match.group(5), normal_match.group(7)
            request_list.append(request.PersonRequest(float(time), int(id), fromBuilding, toBuilding, int(from_floor), int(to_floor)))
        elif add_match is not None:
            time = float(add_match.group(1))
            elevatorType = add_match.group(2)
            if elevatorType == 'building':
                elevatorID = int(add_match.group(3))
                buildingID = add_match.group(4)
                request_list.append(request.ElevatorRequest(time, elevatorType, elevatorID, 1, buildingID))
            elif elevatorType == 'floor':
                elevatorID = int(add_match.group(3))
                floor = int(add_match.group(4))
                request_list.append(request.ElevatorRequest(time, elevatorType, elevatorID, floor, 'A'))
    return request_list


if __name__ == '__main__':
    requests = parse()
    for request in requests:
        print(request)
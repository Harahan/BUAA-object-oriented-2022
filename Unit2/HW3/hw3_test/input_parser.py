#!/usr/local/bin/python
# coding: utf-8

import re
import request

# ADD-building-电梯ID-楼座ID-容纳人数V1-运行速度V2
# ADD-floor-电梯ID-楼层ID-容纳人数V1-运行速度V2-可开关门信息M
def parse(file) -> list:
    request_list = []
    pattern = re.compile(r'\[\s*(\d+(\.\d+)?)\](\d+)-FROM-([A-E])-(\d+)-TO-([A-E])-(\d+)')
    add_pattern = re.compile(r'\[\s*(\d+\.\d+)\]ADD-([a-z]*)-(\d+)-([A-E\d]+)-(\d)-(\d+\.\d+)(-(\d+))*')
    for line in file:
        # print(line)
        normal_match = pattern.match(line.strip())
        add_match = add_pattern.match(line.strip())
        if normal_match is not None:
            time, id, fromBuilding, toBuilding, from_floor, to_floor =  \
                 normal_match.group(1), normal_match.group(3), normal_match.group(4), normal_match.group(6), normal_match.group(5), normal_match.group(7)
            request_list.append(request.PersonRequest(str(time), int(id), fromBuilding, toBuilding, int(from_floor), int(to_floor), int(from_floor), fromBuilding))
        elif add_match is not None:
            time = add_match.group(1)
            elevatorType = add_match.group(2)
            if elevatorType == 'building':
                elevatorID = int(add_match.group(3))
                buildingID = add_match.group(4)
                capacity = int(add_match.group(5))
                speed = add_match.group(6)
                request_list.append(request.ElevatorRequest(time, elevatorType, elevatorID, 1, buildingID, speed, capacity, 31))
            elif elevatorType == 'floor':
                elevatorID = int(add_match.group(3))
                floor = int(add_match.group(4))
                capacity = int(add_match.group(5))
                speed = add_match.group(6)
                switchInfor = int(add_match.group(8))
                request_list.append(request.ElevatorRequest(time, elevatorType, elevatorID, floor, 'A', speed, capacity, switchInfor))
    return request_list


if __name__ == '__main__':
    file = open("stdin.txt", "r")
    requests = parse(file)
    for request in requests:
        print(request)
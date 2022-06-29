import re


class PersonRequest:
    def __init__(self, time: float, id: int, building: str, fromFloor: int, toFloor: int) -> None:
        self.time = time
        self.id = id
        self.building = building
        self.fromFloor = fromFloor
        self.toFloor = toFloor


    def __str__(self) -> str:
       return f"[%.2f]%d-FROM-%s-%d-TO-%s-%d" % \
           (self.time, self.id, self.building, self.fromFloor, self.building, self.toFloor)
           

def parse(file) -> list:
    request_list = []
    pattern = re.compile(r'\[\s*(\d+(.\d+)?)\](\d+)-FROM-([A-E])-(\d+)-TO-([A-E])-(\d+)')
    file = open(file, 'r')
    for line in file:
        match = pattern.match(line)
        if match == None:
            return None
        time, id, building, from_floor, to_floor =  \
             match.group(1), match.group(3), match.group(4), match.group(5), match.group(7)
        request_list.append(PersonRequest(float(time), int(id), building, int(from_floor), int(to_floor)))
    return request_list


if __name__ == '__main__':
    requests = parse('text.txt')
    for request in requests:
        print(request)
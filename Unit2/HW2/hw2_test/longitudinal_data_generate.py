import random

import request

time_list = [2.6, 2.7, 2.8, 2.9]
floor_list = [1, 10, 5]
magic_list = [1, -1, 0, 1, -1, 1, -1]
instr_list = []
cnt = 0
flag = 0
person_id = 1


def get_time(time: float) -> float:
	index = time_list.index(time)
	if 0 < index < len(time_list) - 1:
		return time_list[index + random.choice(magic_list)]
	elif index == 0:
		return time_list[1]
	else:
		return time_list[len(time_list) - 2]


def get_floor(floor: int) -> int:
	tmp_floor = floor + random.choice(magic_list)
	while tmp_floor <= 0 or tmp_floor > 10:
		tmp_floor = floor + random.choice(magic_list)
	return tmp_floor


def add_instr(time, from_floor, to_floor, max_instr):
	global cnt
	global flag
	global person_id
	while from_floor == to_floor:
		to_floor = random.choice(floor_list)
	p = random.randint(1, 24)
	for i in range(p):
		cnt += 1
		if cnt > max_instr - 2:
			flag = 1
			break
		person_id += 1
		instr_list.append(request.PersonRequest(time, person_id, 'A', 'A', from_floor, to_floor))


def longitudinal_data_generate(max_instr: int) -> list:
	global cnt
	global flag
	global person_id
	instr_list.clear()
	cnt = 0
	flag = 0
	person_id = 1
	elevator_id = 6
	time = 1.0
	for i in range(2):
		instr_list.append(request.ElevatorRequest(time, "building", elevator_id + i, 1, 'A'))
	while cnt <= max_instr - 2:
		from_floor = random.choice(floor_list)
		to_floor = random.choice(floor_list)
		time = random.choice(time_list)
		add_instr(time, from_floor, to_floor, max_instr)
		if flag == 1:
			break
		from_floor = get_floor(from_floor)
		to_floor = get_floor(to_floor)
		time = get_time(time)
		add_instr(time, from_floor, to_floor, max_instr)
		if flag == 1:
			break
	instr_list.sort(key=lambda x: x.time)
	return instr_list


if __name__ == '__main__':
	l = longitudinal_data_generate(200)
	f = open('stdin.txt', 'w')
	for i in l:
		f.write(str(i) + '\n')

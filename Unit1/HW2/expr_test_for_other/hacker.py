import os
import random

import sympy as spy


def generate_space():
	if random.randint(0, 100) % 3 == 0:
		return ' '
	elif random.randint(0, 100) % 3 == 1:
		return '\t'
	else:
		return ''


def generate_const():
	const = random.randint(-8, 8)
	if const > 0:
		return '+' + str(const)
	else:
		return str(const)


def generate_pow(alphabet_list=None):
	if alphabet_list is None:
		alphabet_list = ['x', 'y', 'z']
	power_num = random.randint(1, 4)
	variable = random.choice(alphabet_list)
	if power_num == 1:
		return variable
	elif random.randint(0, 100) % 2 == 0:
		return variable + generate_space() + '**' + generate_space() + str(power_num)
	else:
		return variable + generate_space() + '**' + generate_space() + '+' + str(power_num)


def generate_triangle_function(type: bool):
	choice = (random.randint(0, 100) % 2 == 0)
	if choice is True:
		string = random.choice(['x', generate_const()])
		power_num = random.randint(0, 8)
		return ("sin(" if type is True else "cos(") + string + ")" + "**" + str(power_num)
	else:
		string = generate_const()
		power_num = random.randint(0, 8)
		return ("sin(" if type is True else "cos(") + string + ")" + "**" + str(power_num)


def generate_function_expr(term_num, factor_num, power_num, symbol_num, recursion=3, alphabet_list=None):
	if alphabet_list is None:
		alphabet_list = ['x']
	ret_str = ""
	ret_str += generate_space() + '(' + generate_space()
	if symbol_num % 2 == 0:
		ret_str += generate_space() + '-' + generate_space()
		expr_sign = spy.sympify("-1")
	for i in range(term_num):
		if random.randint(0, 100) % 2 == 0:
			ret_str += generate_space() + '+' + generate_space()
		else:
			ret_str += generate_space() + '-' + generate_space()
		
		tstring = generate_pow(alphabet_list) if random.randint(0, 100) % 2 == 0 else generate_const()
		ret_str += generate_space() + tstring + generate_space()
		
		for j in range(factor_num):
			"""
			type=1: 生成简单常数
			type=2: 生成一个幂函数
			type=3,4: 生成一个三角函数
			type>4: 生成一个表达式因子
			"""
			type = random.randint(1, 6)
			ret_str += generate_space() + '*' + generate_space()
			if type == 1:
				string = generate_const()
				ret_str += generate_space() + string + generate_space()
			elif type == 2:
				string = generate_pow(alphabet_list)
				ret_str += generate_space() + string + generate_space()
			elif type == 3:
				string = generate_triangle_function(random.choice([True, False]))
				ret_str += generate_space() + string + generate_space()
			elif type >= 4 and recursion > 0:
				string = generate_function_expr(2, 2, 2, random.randint(0, 100), recursion=recursion - 1,
												alphabet_list=alphabet_list)
				ret_str += generate_space() + string + generate_space()
			else:
				string = generate_pow(alphabet_list)
				ret_str += generate_space() + string + generate_space()
	
	if power_num != 1:
		ret_str += generate_space() + ')' + generate_space() + '**' + generate_space() + str(power_num)
	else:
		ret_str += generate_space() + ')' + generate_space()
	return ret_str


if __name__ == '__main__':
	while True:
		test = generate_function_expr(2, 2, 1, random.randint(0, 100), 2)
		print(test)
		std_ans = spy.sympify(test).expand()
		print(std_ans)
		os.chdir("Z:\\buaa_oo_2022\\Unit1\\HW2\\expr_test_for_other")
		file = open("input.txt", "w")
		file.write('0' + '\n')
		file.write(test)
		file.close()
		os.system("java -jar Saber.jar <input.txt >output.txt")
		file = open("output.txt", "r")
		line = file.readline()
		normal = False
		if line.find('Normal') != -1:
			normal = True
		
		src_out = file.readline()
		src_ans = spy.sympify(src_out).expand()
		
		correct = str(std_ans).strip() == str(src_ans).strip()
		print(correct)
		if correct is False:
			os.system("pause")

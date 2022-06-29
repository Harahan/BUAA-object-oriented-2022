import datetime
import os

import sympy as spy
from sympy.abc import x
import random


def generate_space() -> str:
    if random.randint(0, 100) % 3 == 0:
        return ' '
    elif random.randint(0, 100) % 3 == 1:
        return '\t'
    else:
        return ''


def generate_const() -> str:
    const = random.randint(-8, 8)
    if const > 0:
        return '+' + str(const)
    else:
        return str(const)


def generate_pow(power_num) -> str:
    if power_num == 1:
        return 'x'
    elif random.randint(0, 100) % 2 == 0:
        return 'x' + generate_space() + '**' + generate_space() + str(power_num)
    else:
        return 'x' + generate_space() + '**' + generate_space() + '+' + str(power_num)


def generate_expr(term_num, factor_num, power_num, symbol_num, recursion=False) -> str:
    ret_str = ""
    if power_num != 1 or recursion:
        ret_str += generate_space() + '(' + generate_space()
    if symbol_num % 2 == 0:
        ret_str += generate_space() + '-' + generate_space()
    for i in range(term_num):
        if random.randint(0, 100) % 2 == 0:
            ret_str += generate_space() + '+' + generate_space()
        else:
            ret_str += generate_space() + '-' + generate_space()
        ret_str += generate_space() \
                   + generate_pow(random.randint(1, 3)) if random.randint(0, 100) % 2 == 0 else generate_const() \
                   + generate_space()
        for j in range(factor_num):
            type = random.randint(1, 5)
            ret_str += generate_space() + '*' + generate_space()
            if type == 1:
                ret_str += generate_space() + generate_const() + generate_space()
            elif type == 2:
                ret_str += generate_space() + generate_pow(random.randint(1, 3)) + generate_space()
            elif type >= 3 and not recursion:
                ret_str += generate_space() + generate_expr(random.randint(2, 3), 2, random.randint(0, 4),
                                                            random.randint(0, 100), recursion=True) \
                           + generate_space()
            else:
                ret_str += generate_space() + generate_pow(random.randint(1, 3)) + generate_space()
    if power_num != 1 or recursion:
        ret_str += generate_space() + ')' + generate_space() + '**' + generate_space() + str(power_num)
    return ret_str


if __name__ == '__main__':
    count = 1
    while True:
        print('Running Test Case' + str(count))
        test_expr = generate_expr(3, 4, 1, random.randint(0, 100))
        print(test_expr)
        file = open("input.txt", "w")
        file.write("0\n")
        file.write(test_expr + '\n')
        file.close()
        os.chdir("Z:\\buaa_oo_2022\\Unit1\\HW1\\expr_test")
        os.system("java -jar homework2.jar <input.txt >output.txt")
        file = open("output.txt", "r")
        line = file.readline()

        normal = False
        if line.find('Normal') != -1:
            normal = True
        src_out = file.readline()
        expr = spy.sympify(test_expr)
        std_ans = expr.expand()
        src_ans = spy.sympify(src_out).expand()

        correct = str(std_ans) == str(src_ans)
        if not correct:
            print('Your Answer is NOT correct!')
            print('Your Answer is: ' + str(src_out))
            print('Standard Answer is: ' + str(std_ans))
            break

        std_length = len(str(std_ans).replace(' ', '').replace('\t', '').strip())
        src_length = len(str(src_out).replace(' ', '').replace('\t', '').strip())
        file.close()
        file = open("log.txt", "a")
        file.write(str(datetime.datetime.now()) + '\n')
        file.write('origin:' + str(test_expr) + '\n')
        file.write('std:' + str(std_ans).replace(' ', '').replace('\t', '') + '\n')
        file.write('src:' + str(src_out).replace(' ', '').replace('\t', '') + '\n')
        file.close()
        print('Your Answer is Accepted? ' + str(correct))
        print('Your Answer Length / Standard Answer Length: ' + str(src_length / std_length))

        # os.system("pause")
        count += 1

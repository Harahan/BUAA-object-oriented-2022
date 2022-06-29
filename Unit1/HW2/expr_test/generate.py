import math
import os

import sympy
import sympy as spy
from sympy.abc import x, y, z, i
import random


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
        return '+' + str(const), spy.sympify(str(const))
    else:
        return str(const), spy.sympify(str(const))


def generate_pow(alphabet_list=None):
    if alphabet_list is None:
        alphabet_list = ['x', 'y', 'z']
    power_num = random.randint(1, 4)
    variable = random.choice(alphabet_list)
    if power_num == 1:
        return variable, spy.sympify(variable)
    elif random.randint(0, 100) % 2 == 0:
        return variable + generate_space() + '**' + generate_space() + str(power_num), \
               spy.sympify(variable + '**' + str(power_num))
    else:
        return variable + generate_space() + '**' + generate_space() + '+' + str(power_num), \
               spy.sympify(variable + '**' + str(power_num))


"""
type=True: sin, type=False: cos
"""


def generate_triangle_function(type: bool):
    choice = (random.randint(0, 100) % 2 == 0)
    if choice is True:
        string, expr = generate_pow(['x'])
        power_num = random.randint(0, 8)
        return ("sin(" if type is True else "cos(") + string + ")" + "**" + str(power_num), \
               spy.sympify(("sin(" if type is True else "cos(") + string + ")" + "**" + str(power_num))
    else:
        string, expr = generate_const()
        power_num = random.randint(0, 8)
        return ("sin(" if type is True else "cos(") + string + ")" + "**" + str(power_num), \
               spy.sympify(("sin(" if type is True else "cos(") + string + ")" + "**" + str(power_num))


def generate_sum_function():
    start = -10
    end = random.randint(5, 10)
    s_str, s_expr = generate_function_expr(1, 1, 1, random.randint(0, 100), 0, ['x', 'i', 'i', 'i', 'x'])
    return 'sum(i,'+str(start)+','+str(end)+','+s_str+')', spy.Sum(s_expr, (i, start, end)).doit()


def generate_custom_function(f_expr, g_expr, h_expr):
    x_para_str, x_para_expr = generate_pow(['x']) if (random.randint(0, 100) % 2 == 0) else generate_const()
    y_para_str, y_para_expr = generate_pow(['x']) if (random.randint(0, 100) % 2 == 0) else generate_const()
    z_para_str, z_para_expr = generate_pow(['x']) if (random.randint(0, 100) % 2 == 0) else generate_const()
    choice = random.choice(['f', 'g', 'h'])
    if choice == 'f':
        return 'f(' + x_para_str + ',' + y_para_str + ',' + z_para_str + ')', f_expr.subs(
            {x:x_para_expr, y:y_para_expr, z:z_para_expr})
    elif choice == 'g':
        return 'g(' + x_para_str + ',' + y_para_str + ',' + z_para_str + ')', g_expr.subs(
            {x: x_para_expr, y: y_para_expr, z: z_para_expr})
    else:
        return 'h(' + x_para_str + ',' + y_para_str + ',' + z_para_str + ')', h_expr.subs(
            {x: x_para_expr, y: y_para_expr, z: z_para_expr})


'''
recursion: 最大递归层数
term_num, factor_num, power_num: 字面意思
symbol_num: 为奇数取负号，为偶数取正号
'''


def generate_function_expr(term_num, factor_num, power_num, symbol_num, recursion=3, alphabet_list=None):
    if alphabet_list is None:
        alphabet_list = ['x']

    ret_str = ""
    temp_expr = spy.sympify("0")
    ret_str += generate_space() + '(' + generate_space()
    expr_sign = spy.sympify("1")
    if symbol_num % 2 == 0:
        ret_str += generate_space() + '-' + generate_space()
        expr_sign = spy.sympify("-1")
    for i in range(term_num):
        if random.randint(0, 100) % 2 == 0:
            ret_str += generate_space() + '+' + generate_space()
            term_sign = spy.sympify("1")
        else:
            ret_str += generate_space() + '-' + generate_space()
            term_sign = spy.sympify("-1")

        temp_term_expr = term_sign
        if i == 0:
            temp_term_expr *= expr_sign

        tstring, texpr = generate_pow(alphabet_list) if random.randint(0, 100) % 2 == 0 else generate_const()
        ret_str += generate_space() + tstring + generate_space()
        temp_term_expr *= texpr

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
                string, expr = generate_const()
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif type == 2:
                string, expr = generate_pow(alphabet_list)
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif type == 3:
                string, expr = generate_triangle_function(random.choice([True, False]))
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif type >= 4 and recursion > 0:
                string, expr = generate_function_expr(2, 2, 2, random.randint(0, 100), recursion=recursion-1, alphabet_list=alphabet_list)
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            else:
                string, expr = generate_pow(alphabet_list)
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
        temp_expr += temp_term_expr
    ret_expr = temp_expr
    if power_num != 1 and power_num != 0:
        ret_str += generate_space() + ')' + generate_space() + '**' + generate_space() + str(power_num)
        for k in range(power_num-1):
            ret_expr *= temp_expr
    elif power_num == 0:
        ret_str += generate_space() + ')' + generate_space() + '**' + generate_space() + str(power_num)
        ret_expr = sympy.sympify("1")
    else:
        ret_str += generate_space() + ')' + generate_space()
    return ret_str, ret_expr

'''
recursion: 最大递归层数
term_num, factor_num, power_num: 字面意思
symbol_num: 为奇数取负号，为偶数取正号
'''


def generate_expr(term_num, factor_num, power_num, symbol_num, f_expr, g_expr, h_expr, recursion=3, alphabet_list=None):
    if alphabet_list is None:
        alphabet_list = ['x']

    ret_str = ""
    temp_expr = spy.sympify("0")
    ret_str += generate_space() + '(' + generate_space()
    expr_sign = spy.sympify("1")
    if symbol_num % 2 == 0:
        ret_str += generate_space() + '-' + generate_space()
        expr_sign = spy.sympify("-1")
    for i in range(term_num):
        if random.randint(0, 100) % 2 == 0:
            ret_str += generate_space() + '+' + generate_space()
            term_sign = spy.sympify("1")
        else:
            ret_str += generate_space() + '-' + generate_space()
            term_sign = spy.sympify("-1")

        temp_term_expr = term_sign
        if i == 0:
            temp_term_expr *= expr_sign

        tstring, texpr = generate_pow(alphabet_list) if random.randint(0, 100) % 2 == 0 else generate_const()
        ret_str += generate_space() + tstring + generate_space()
        temp_term_expr *= texpr

        for j in range(factor_num):
            """
            type=1: 生成简单常数
            type=2: 生成一个幂函数
            type=3,4: 生成一个三角函数
            type>4: 生成一个表达式因子
            """
            type = random.randint(1, 13)
            ret_str += generate_space() + '*' + generate_space()
            if type == 1:
                string, expr = generate_const()
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif type == 2:
                string, expr = generate_pow(alphabet_list)
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif type == 3 or type == 8:
                string, expr = generate_triangle_function(random.choice([True, False]))
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif type == 4 or type == 5:
                string, expr = generate_sum_function()
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif type == 6 or type == 7 or type > 10:
                string, expr = generate_custom_function(f_expr, g_expr, h_expr)
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            elif (type == 9 or type == 10) and recursion > 0:
                string, expr = generate_expr(2, 2, random.randint(0, 8), random.randint(0, 100),
                                             f_expr, g_expr, h_expr, recursion=recursion-1, alphabet_list=alphabet_list)
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
            else:
                string, expr = generate_pow(alphabet_list)
                ret_str += generate_space() + string + generate_space()
                temp_term_expr *= expr
        temp_expr += temp_term_expr
    ret_expr = temp_expr
    if power_num != 1 and power_num != 0:
        ret_str += generate_space() + ')' + generate_space() + '**' + generate_space() + str(power_num)
        for k in range(power_num-1):
            ret_expr *= temp_expr
    elif power_num == 0:
        ret_str += generate_space() + ')' + generate_space() + '**' + generate_space() + str(power_num)
        ret_expr = sympy.sympify("1")
    else:
        ret_str += generate_space() + ')' + generate_space()
    return ret_str, ret_expr


if __name__ == '__main__':
    count = 1
    while True:
        print("\nCase:" + str(count))
        f_str, f_expr = generate_function_expr(2, 2, 1, random.randint(0, 100), 0, ['x', 'y', 'z'])
        g_str, g_expr = generate_function_expr(2, 2, 1, random.randint(0, 100), 0, ['x', 'y', 'z'])
        h_str, h_expr = generate_function_expr(2, 2, 1, random.randint(0, 100), 0, ['x', 'y', 'z'])
        s, e = generate_expr(2, 3, 1, random.randint(0, 100), f_expr, g_expr, h_expr, 1, ['x'])
        print(3)
        print('f(x, y, z) = ' + f_str.strip())
        print('g(x, y, z) = ' + g_str.strip())
        print('h(x, y, z) = ' + h_str.strip())
        print(s.strip())
        print(e)
        std_ans = e.expand()
        print("Expression OK!")
        os.chdir("Z:\\buaa_oo_2022\\Unit1\\HW2\\expr_test")
        file = open("input.txt", "w")
        file.write('3' + '\n')
        file.write('f(x, y, z)  = ' + f_str + '\n')
        file.write('g(x, y, z)  = ' + g_str + '\n')
        file.write('h(x, y, z)  = ' + h_str + '\n')
        file.write(s + '\n')
        file.close()
        os.system("java -jar homework3.jar <input.txt >output.txt")
        file = open("output.txt", "r")
        line = file.readline()
        normal = False
        if line.find('Normal') != -1:
            normal = True
    
        src_out = file.readline()
        src_ans = spy.sympify(src_out).expand()
    
        correct = str(std_ans).strip() == str(src_ans).strip()
        test = 0.0
        while test <= 10:
            print('Test on Point ' + str(test), end=':')
            print(math.fabs(src_ans.evalf(subs={x:test}) - std_ans.evalf(subs={x:test})) < 1e-7)
            test += 0.1
        print(correct)
        print(std_ans)
        print(src_ans)
        print(src_out)
        print(len(str(src_out).replace(' ', '').replace('\t','').strip()) / len(str(std_ans).replace(' ', '').replace('\t','').strip()))
        if correct is False:
            os.system("pause")
        count += 1
        
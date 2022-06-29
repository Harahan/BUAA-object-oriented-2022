import os
import random
import subprocess

const = [65 + _ for _ in range(26)] + [97 + _ for _ in range(26)]
group_num = 0  # the amount of groups
people_num = 0  # the amount of people
people = []
group = []
type_list = ['normal', 'special']


def init(instr_num: int) -> None:
    global people
    global group
    global group_num
    global people_num
    group_num = (round(random.random() * 50) + 50) * instr_num // 5000  # the amount of groups
    people_num = round((8 + random.random() * 4) * group_num)  # the amount of people
    people = [_ for _ in range(1, people_num + 1)]
    group = [_ for _ in range(1, group_num + 1)]


def get_name() -> str:
    var = random.randint(1, 10)
    rt = ''
    for i in range(var):
        rt += chr(random.choice(const))
    return rt


def get_pid() -> int:
    global people
    exp = random.random()
    if len(people) > 0 and exp <= 0.8:
        rt = random.choice(people)
        people.remove(rt)
        return rt
    else:
        return random.randint(1, people_num)


def get_gid() -> int:
    global group
    exp = random.random()
    if len(group) > 0 and exp <= 0.8:
        rt = random.choice(group)
        group.remove(rt)
        return rt
    else:
        return random.randint(1, group_num)


def ap() -> str:
    return 'ap ' + str(get_pid()) + ' ' + get_name() + ' ' + str(random.randint(0, 200))


def ar() -> str:
    exp = random.random()
    pid = get_pid()
    if exp >= 0.8:
        return 'ar ' + str(pid) + ' ' + str(pid) + ' ' + str(random.randint(0, 1000))
    else:
        return 'ar ' + str(pid) + ' ' + str(get_pid()) + ' ' + str(random.randint(0, 1000))


def qv() -> str:
    exp = random.random()
    pid = get_pid()
    if exp >= 0.8:
        return 'qv ' + str(pid) + ' ' + str(pid)
    else:
        return 'qv ' + str(pid) + ' ' + str(get_pid())


def qps() -> str:
    return 'qps'


def cn() -> str:
    pid = get_pid()
    if random.random() >= 0.8:
        return 'cn ' + str(pid) + ' ' + str(pid)
    else:
        return 'cn ' + str(pid) + ' ' + str(get_pid())


def qci() -> str:
    exp = random.random()
    pid = get_pid()
    if exp >= 0.8:
        return 'qci ' + str(pid) + ' ' + str(pid)
    else:
        return 'qci ' + str(pid) + ' ' + str(get_pid())


def qbs() -> str:
    return 'qbs'


def ag() -> str:
    return 'ag ' + str(get_gid())


def atg() -> str:
    return 'atg ' + str(get_pid()) + ' ' + str(get_gid())


def qgps() -> str:
    return 'qgps ' + str(get_gid())


def qgvs() -> str:
    return 'qgvs ' + str(get_gid())


def qgam() -> str:
    return 'qgam ' + str(get_gid())


def qgav() -> str:
    return 'qgav ' + str(get_gid())


def dfg() -> str:
    return 'dfg ' + str(get_pid()) + ' ' + str(get_gid())


def data_generate(instr_num: int, my_type: str) -> list:
    init(instr_num)
    ans = []
    for i in range(instr_num):
        exp = random.random()
        exp_judge = 0.4 if my_type == "special" else 0.65
        if exp <= exp_judge:
            var = random.choice([1, 2, 7, 8])
        else:
            if my_type == "special":
                var = random.choice([3, 4, 5, 6, 9, 10, 11, 12, 13, 14])
            else:
                var = random.choice([3, 4, 5, 6, 9])
        if var == 1:
            ans.append(ap())
        elif var == 2:
            ans.append(ar())
        elif var == 3:
            ans.append(qv())
        elif var == 4:
            ans.append(qps())
        elif var == 5:
            ans.append(qci())
        elif var == 6:
            ans.append(qbs())
        elif var == 7:
            ans.append(ag())
        elif var == 8:
            ans.append(atg())
        elif var == 9:
            ans.append(dfg())
        elif var == 10:
            ans.append(cn())
        elif var == 11:
            ans.append(qgps())
        elif var == 12:
            ans.append(qgvs())
        elif var == 13:
            ans.append(qgam())
        elif var == 14:
            ans.append(qgav())
        else:
            assert False, 'var overflow'
    return ans


def test(src_list: list, num: int, time_limit: float) -> None:
    print('----- TEST CASE ' + str(num) + ' BEGIN -----')
    print('           TIME')
    for src in src_list:
        os.environ["COMSPEC"] = 'powershell'
        p = subprocess.Popen(
            'Measure-Command{Get-Content input.txt | java -jar ' + src + '.jar > ' + src + '.txt}',
            shell=True, stdout=subprocess.PIPE)
        p.wait()
        time_list = p.stdout.read()
        sorted_list = time_list.decode('utf-8').strip().split('\n')
        time_used = float(sorted_list[9].split(": ")[1])
        print(src + ' used : ' + str(time_used) + 's')
        if time_used >= time_limit:
            print(src + ' is TLE')
            os.system('pause')
    print('          RESULT')
    pre = src_list[0]
    for i in range(1, len(src_list)):
        os.system('fc ' + pre + '.txt ' + src_list[i] + '.txt /n > result.txt')
        result = open('result.txt', 'r')
        result.readline()
        if 'FC' not in result.readline():
            print(src_list[i] + ' is different with ' + pre)
            os.system('pause')
        pre = src_list[i]
    print('  All answers are identical')
    print('------ TEST CASE ' + str(num) + ' END ------\n\n')


if __name__ == '__main__':
    case = 1
    while True:
        # you can change the amount of instruments and type of instruments(0 for normal, 1 for special)
        data = data_generate(100000, type_list[0])
        file = open('input.txt', 'w')
        for line in data:
            file.write(line + '\n')
        file.close()
        os.chdir('Z:\\buaa_oo_2022\\Unit3\\HW1\\hw1_test')  # change the path
        jar_list = ['hys', 'sjh']  # add your Java archive's name without suffix
        test(jar_list, 1, 20.0)  # you can change the third parameter to change the procedure's execute time
        case += 1

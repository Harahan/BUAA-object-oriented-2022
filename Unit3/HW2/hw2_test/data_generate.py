import os
import random
import subprocess

const = [65 + _ for _ in range(26)] + [97 + _ for _ in range(26)]
group_num = 30  # the amount of groups
people_num = 300  # the amount of people
message_num = 5000
people = []
group = []
message = []
pool = [[30, 300, 5000], [30, 2000, 5000]]


def init() -> None:
    global people
    global group
    global message
    global people_num
    global message_num
    global group_num
    exp = random.random()
    if exp <= 0.5:
        group_num, people_num, message_num = pool[0]
    else:
        group_num, people_num, message_num = pool[1]
    print('    G: ' + str(group_num) + ' P: ' + str(people_num) + ' M: ' + str(message_num))
    people = [_ for _ in range(1, people_num + 1)]
    group = [_ for _ in range(1, group_num + 1)]
    message = [_ for _ in range(1, message_num + 1)]


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


def get_mid() -> int:
    global message
    exp = random.random()
    if len(message) > 0 and exp <= 0.8:
        rt = random.choice(message)
        message.remove(rt)
        return rt
    else:
        return random.randint(1, message_num)


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


def dfg() -> str:
    return 'dfg ' + str(get_pid()) + ' ' + str(get_gid())


def qgps() -> str:
    return 'qgps ' + str(get_gid())


def qgvs() -> str:
    return 'qgvs ' + str(get_gid())


def qgav() -> str:
    return 'qgav ' + str(get_gid())


def am() -> str:
    exp = random.random()
    pid = get_pid()
    if exp <= 0.7:
        return 'am ' + str(get_mid()) + ' ' + str(random.randint(-1000, 1000)) + ' ' + str(0) + ' ' \
               + str(pid // 10) + ' ' + str(get_pid() // 10)
    elif exp <= 0.8:
        return 'am ' + str(get_mid()) + ' ' + str(random.randint(-1000, 1000)) + ' ' + str(0) + ' ' \
               + str(pid // 10) + ' ' + str(pid // 10)
    else:
        return 'am ' + str(get_mid()) + ' ' + str(random.randint(-1000, 1000)) + ' ' + str(1) + ' ' \
               + str(pid // 10) + ' ' + str(get_gid())


def sm() -> str:
    return 'sm ' + str(get_mid())


def qsv() -> str:
    return 'qsv ' + str(get_pid() // 10)


def qrm() -> str:
    return 'qrm ' + str(get_pid() // 10)


def qlc() -> str:
    return 'qlc ' + str(get_pid())


def data_generate1(instr_num: int) -> list:
    init()
    i, ans, cnt = 0, [], [0 for _ in range(18)]
    while i < instr_num:
        if i <= instr_num // 2:
            var = random.choice([1, 7, 8, 9])
        else:
            var = random.choice([12])
        if var == 1:
            if cnt[1] >= people_num * 3:
                continue
            cnt[1] += 1
            ans.append(ap())
        elif var == 2:
            cnt[2] += 1
            ans.append(ar())
        elif var == 3:
            if cnt[3] >= 400:
                continue
            cnt[3] += 1
            ans.append(qv())
        elif var == 4:
            cnt[4] += 1
            ans.append(qps())
        elif var == 5:
            if cnt[5] >= 1000:
                continue
            cnt[5] += 1
            ans.append(qci())
        elif var == 6:
            cnt[6] += 1
            ans.append(qbs())
        elif var == 7:
            if cnt[7] >= group_num * 3:
                continue
            cnt[7] += 1
            ans.append(ag())
        elif var == 8:
            ans.append(atg())
        elif var == 9:
            ans.append(dfg())
        elif var == 10:
            cnt[10] += 1
            ans.append(qgps())
        elif var == 11:
            cnt[11] += 1
            ans.append(qgvs())
        elif var == 12:
            cnt[12] += 1
            ans.append(qgav())
        elif var == 13:
            cnt[13] += 1
            ans.append(am())
        elif var == 14:
            cnt[14] += 1
            ans.append(sm())
        elif var == 15:
            cnt[15] += 1
            ans.append(qsv())
        elif var == 16:
            cnt[16] += 1
            ans.append(qrm())
        elif var == 17:
            cnt[17] += 1
            ans.append(qlc())
        else:
            assert False, 'var overflow'
        i += 1
    return ans


def data_generate2(instr_num: int) -> list:
    init()
    ans = []
    for i in range(instr_num):
        exp = random.random()
        if exp <= 0.65:
            var = random.choice([1, 2, 7, 8])
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
        else:
            assert False, 'var overflow'
    return ans


def data_generate(instr_num: int) -> list:
    exp = random.random()
    if exp <= 0.7:
        print("---------- TEST HW2 ----------")
        return data_generate1(instr_num)
    else:
        print("---------- TEST HW1 ----------")
        return data_generate2(instr_num)


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
        data = data_generate(100000)
        file = open('input.txt', 'w')
        for line in data:
            file.write(line + '\n')
        file.close()
        os.chdir('Z:\\buaa_oo_2022\\Unit3\\HW2\\hw2_test')  # change the path
        jar_list = ['sjh', 'hys']  # add your Java archive's name without suffix
        test(jar_list, case, 100.0)  # you can change the third parameter to change the procedure's execute time
        case += 1

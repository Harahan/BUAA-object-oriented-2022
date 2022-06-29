import os
import random
import subprocess

const = [65 + _ for _ in range(26)] + [97 + _ for _ in range(26)]
people_num = 0
group_num = 0
message_num = 0
emoji_num = 0
messages = [1 + _ for _ in range(message_num)]
emojis = [1 + _ for _ in range(emoji_num)]


def get_xid(x_num: int, x: list) -> int:
    if len(x) > 0:
        tmp = random.choice(x)
        x.remove(tmp)
        return tmp
    else:
        return random.randint(1, x_num << 2)


def get_name() -> str:
    var = random.randint(1, 10)
    rt = ''
    for i in range(var):
        rt += chr(random.choice(const))
    return rt


def get_str() -> str:
    var = random.randint(1, 100)
    rt = ''
    for i in range(var):
        rt += chr(random.choice(const))
    return rt


def ap(p: int) -> str:
    return 'ap ' + str(p) + ' ' + get_name() + ' ' + str(random.randint(0, 200))


def ar(p1: int, p2: int) -> str:
    return 'ar ' + str(p1) + ' ' + str(p2) + ' ' + str(random.randint(0, 1000))


def qv(p1: int, p2: int) -> str:
    return 'qv ' + str(p1) + ' ' + str(p2)


def qps() -> str:
    return 'qps'


def qci(p1: int, p2: int) -> str:
    return 'qci ' + str(p1) + ' ' + str(p2)


def qbs() -> str:
    return 'qbs'


def ag(g: int) -> str:
    return 'ag ' + str(g)


def atg(p: int, g: int) -> str:
    return 'atg ' + str(p) + ' ' + str(g)


def dfg(p: int, g: int) -> str:
    return 'dfg ' + str(p) + ' ' + str(g)


def qgps(g: int) -> str:
    return 'qgps ' + str(g)


def qgvs(g: int) -> str:
    return 'qgvs ' + str(g)


def qgav(g: int) -> str:
    return 'qgav ' + str(g)


def am(m: int, op: int, p1: int, p2=0, g=0) -> str:
    if op <= 0:
        return 'am ' + str(m) + ' ' + str(random.randint(-1000, 1000)) + ' ' + str(0) + ' ' \
               + str(p1) + ' ' + str(p2)
    else:
        return 'am ' + str(m) + ' ' + str(random.randint(-1000, 1000)) + ' ' + str(1) + ' ' \
               + str(p1) + ' ' + str(g)


def sm(m: int) -> str:
    return 'sm ' + str(m)


def qsv(p: int) -> str:
    return 'qsv ' + str(p)


def qrm(p: int) -> str:
    return 'qrm ' + str(p)


def qlc(p: int) -> str:
    return 'qlc ' + str(p)


def arem(m: int, op: int, p1: int, p2=0, g=0) -> str:
    if op == 0:
        return 'arem ' + str(m) + ' ' + str(random.randint(0, 200)) + ' ' + str(0) + ' ' \
               + str(p1) + ' ' + str(p2)
    else:
        return 'arem ' + str(m) + ' ' + str(random.randint(0, 200)) + ' ' + str(1) + ' ' \
               + str(p1) + ' ' + str(g)


def anm(m: int, op: int, p1: int, p2=0, g=0) -> str:
    if op == 0:
        return 'anm ' + str(m) + ' ' + get_str() + ' ' + str(0) + ' ' \
               + str(p1) + ' ' + str(p2)
    else:
        return 'anm ' + str(m) + ' ' + get_str() + ' ' + str(1) + ' ' \
               + str(p1) + ' ' + str(g)


def cn(p: int) -> str:
    return 'cn ' + str(p)


def aem(m: int, e: int, op: int, p1: int, p2=0, g=0) -> str:
    if op == 0:
        return 'aem ' + str(m) + ' ' + str(e) + ' ' + str(0) + ' ' \
               + str(p1) + ' ' + str(p2)
    else:
        return 'aem ' + str(m) + ' ' + str(e) + ' ' + str(1) + ' ' \
               + str(p1) + ' ' + str(g)


def sei(e: int) -> str:
    return 'sei ' + str(e)


def qp(e: int) -> str:
    return 'qp ' + str(e)


def dce(limit: int) -> str:
    return 'dce ' + str(limit)


def qm(p: int) -> str:
    return 'qm ' + str(p)


def sim(m: int) -> str:
    return 'sim ' + str(m)


def get_graph(exp: float) -> list:
    rt = []
    for i in range(1, people_num + 1):
        rt.append(ap(i))
    tmp = []
    for i in range(1, people_num + 1):
        for j in range(1, people_num + 1):
            if i < j:
                tmp.append(ar(i, j))
    rt += random.sample(tmp, int(len(tmp) * exp))
    for i in range(1, group_num + 1):
        rt.append(ag(i))
    for i in range(1, people_num + 1):
        j = random.randint(2, group_num)
        k = random.randint(1, j)
        for t in range(k, j + 1):
            rt.append(atg(i, t))
    return rt


def get_message(op1: int, op2: int) -> list:
    m = get_xid(message_num, messages)
    p1 = random.randint(1, people_num + 1)
    p2 = random.randint(1, people_num + 1)
    g = random.randint(1, group_num + 1)
    if op1 == 1:
        return [am(m, op2, p1, p2, g)]
    elif op1 == 2:
        return [arem(m, op2, p1, p2, g)]
    elif op1 == 3:
        e = get_xid(emoji_num, emojis)
        if random.randint(0, 1) == 0:
            return [sei(e), aem(m, e, op2, p1, p2, g), sm(m)]
        else:
            return [sei(e), aem(m, e, op2, p1, p2, g), sim(m)]
    else:
        return [anm(m, op2, p1, p2, g)]
    
    
def init():
    global people_num
    global group_num
    global message_num
    global emoji_num
    global messages
    global emojis
    people_num = 500
    message_num = 10000
    emoji_num = 150
    group_num = 5
    messages = [1 + _ for _ in range(message_num)]
    emojis = [1 + _ for _ in range(emoji_num)]
    
    
def get_instr() -> str:
    m = random.randint(message_num + 1, message_num << 1)
    p1 = random.randint(people_num + 1, people_num << 1)
    p2 = random.randint(people_num + 1, people_num << 1)
    g = random.randint(group_num + 1, group_num << 1)
    e = random.randint(emoji_num + 1, emoji_num << 1)
    exp = random.randint(1, 26)
    if exp == 1:
        return ap(p1)
    elif exp == 2:
        return ar(p1, p2)
    elif exp == 3:
        return qv(p1, p2)
    elif exp == 4:
        return qps()
    elif exp == 5:
        return qci(p1, p2)
    elif exp == 6:
        return qbs()
    elif exp == 7:
        return ag(g)
    elif exp == 8:
        return atg(p1, g)
    elif exp == 9:
        return dfg(p1, g)
    elif exp == 10:
        return qgps(g)
    elif exp == 11:
        return qgav(g)
    elif exp == 12:
        return qgvs(g)
    elif exp == 13:
        return am(m, random.randint(0, 1), p1, p2, g)
    elif exp == 14:
        return sm(m)
    elif exp == 15:
        return qsv(p1)
    elif exp == 16:
        return qrm(p1)
    elif exp == 17:
        return qlc(p1)
    elif exp == 18:
        return arem(m, random.randint(0, 1), p1, p2, g)
    elif exp == 19:
        return anm(m, random.randint(0, 1), p1, p2, g)
    elif exp == 20:
        return cn(p1)
    elif exp == 21:
        return aem(m, e, random.randint(0, 1), p1, p2, g)
    elif exp == 22:
        return sei(e)
    elif exp == 23:
        return qp(e)
    elif exp == 24:
        return dce(50)
    elif exp == 25:
        return qm(p1)
    else:
        return sim(m)
    

def data_generate(instr_num: int) -> list:
    init()
    ans = get_graph(0.01)
    for i in range(instr_num - len(ans)):
        var = random.randint(1, 10)
        m = random.randint(1, message_num)
        p = random.randint(1, people_num)
        e = random.randint(1, emoji_num)
        if var == 1:
            ans.append(sm(m))
        elif var == 2:
            ans.append(qsv(p))
        elif var == 3:
            ans.append(qrm(p))
        elif var == 4:
            ans.append(cn(p))
        elif var == 5:
            ans.append(qp(e))
        elif var == 6:
            ans.append(dce(random.randint(0, 2)))
        elif var == 7:
            ans.append(qm(p))
        elif var == 8:
            ans.append(sim(m))
        elif var == 9:
            ans += get_message(random.randint(1, 4), random.randint(0, 1))
        else:
            ans.append(get_instr())
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
        data = data_generate(200000)
        file = open('input.txt', 'w')
        for line in data:
            file.write(line + '\n')
        file.close()
        os.chdir('Z:\\buaa_oo_2022\\Unit3\\HW3\\hw3_test')  # change the path
        jar_list = ['hys', 'cjy', 'sjh']  # add your Java archive's name without suffix
        test(jar_list, case, 100.0)  # you can change the third parameter to change the procedure's execute time
        case += 1

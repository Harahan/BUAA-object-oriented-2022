import os
import random

const = [65 + _ for _ in range(26)] + [97 + _ for _ in range(26)]


def get_name() -> str:
    var = random.randint(1, 10)
    rt = ''
    for i in range(var):
        rt += chr(random.choice(const))
    return rt


def data(num: int) -> list:
    ret, na, i = [], [], 0
    while i < num:
        var = random.randint(1, 3)
        if var == 1:
            tmp = get_name()
            ret.append('1 ' + tmp)
            na.append(tmp)
        else:
            if not na:
                continue
            if var == 2:
                tmp = random.choice(na)
                na.remove(tmp)
                ret.append('2 ' + tmp)
            else:
                tmp = random.choice(na)
                ret.append('3 ' + tmp)
        i += 1
    return ret


def std():
    ft = open('input.txt', 'r').readlines()
    ft.pop(0)
    t = []
    rt = []
    for it in ft:
        ct = it.split()
        if int(ct[0]) == 1:
            rt.append('add ok')
            t.append(ct[1])
        elif int(ct[0]) == 2:
            t.remove(ct[1])
            rt.append('delete ok')
        else:
            t.sort()
            rt.append(str(t.index(ct[1]) + 1))
    st = open('std.txt', 'w')
    for ki in rt:
        st.write(ki + '\n')
    st.close()
    
          
if __name__ == '__main__':
    nu = 500000
    da = data(nu)
    fi = open('input.txt', 'w')
    fi.write(str(nu) + '\n')
    for li in da:
        fi.write(li + '\n')
    fi.close()
    std()
    os.chdir('Z:\\buaa_oo_2022\\Unit3\\HW2\\testAvl')
    os.system('java -jar Avl.jar < input.txt > src.txt')
    os.system('fc src.txt std.txt')
        
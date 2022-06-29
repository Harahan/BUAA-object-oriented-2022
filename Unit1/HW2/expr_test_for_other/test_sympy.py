import sympy as spy
from sympy import sin, cos
from sympy.abc import x, y, z, i

if __name__ == '__main__':
    f = x + 1
    f += -1 + x
    print(f)
    # print(spy.Sum(i, (i, 1, 3)).doit())
    print(spy.sympify("f").s)

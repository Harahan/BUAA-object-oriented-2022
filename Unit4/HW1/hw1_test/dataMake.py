import os
import subprocess

from randomUmlMake import randomUmlMake
from randomUmlMake import methodsName
from randomUmlMake import makeClassName
from setting import *
import sys
import random


def createClassCount():
	return 'CLASS_COUNT'


def createClassSubCount(className):
	return 'CLASS_SUBCLASS_COUNT ' + className


def createClassOpCount(className):
	'''modes = ['NON_RETURN','RETURN','NON_PARAM','PARAM','ALL']
	result = []
	for i in modes:
		result.append('CLASS_OPERATION_COUNT '+className+' '+i)'''
	return 'CLASS_OPERATION_COUNT ' + className


def createOpVisibility(className, methodName):
	return 'CLASS_OPERATION_VISIBILITY ' + className + ' ' + methodName


def createClassOpCoupling(className, methodName):
	return 'CLASS_OPERATION_COUPLING_DEGREE ' + className + ' ' + methodName


def createClassAttrCoupling(className):
	return 'CLASS_ATTR_COUPLING_DEGREE ' + className


def createImpleInterList(className):
	return 'CLASS_IMPLEMENT_INTERFACE_LIST ' + className


def createClassDepth(className):
	return 'CLASS_DEPTH_OF_INHERITANCE ' + className


'''
def createClassAttrCount(className):
    modes = ['ALL','SELF_ONLY']
    result = []
    for s in modes:
        result.append('CLASS_ATTR_COUNT '+className+' '+s)
    return result

def createClassAssoCount(className):
    return 'CLASS_ASSO_COUNT '+className

def createClassAssoClassList(className):
    return 'CLASS_ASSO_CLASS_LIST '+className

def createAttrVisibility(className,AttrName):
    return 'CLASS_ATTR_VISIBILITY '+className+' '+AttrName

def createClassTop(className):
    return 'CLASS_TOP_BASE '+className

def createInfoHidden(className):
    return 'CLASS_INFO_HIDDEN '+className 
'''
'''
def StrongData(count='1',mode='normal'):
    with randomUmlMake(count) as m:
        instrs = []
        if (mode == 'normal'):
            model = m[0]
            datafile = m[1]
            classidMap = model.getClass()
            interfaceidMap = model.getInterface()
            for classid in classidMap:
                className = classidMap[classid]
                attrnames = []
                id = classid
                while (id !=  None):
                    attrnames.extend(model.getClassAttributes(id))
                    id = model.getClassParentId(id)
                attrnames = set(attrnames)
                for i in range(normalCount):
                    instrs.extend(createClassAttrCount(className))
                    instrs.append(createClassAssoCount(className))
                    instrs.append(createClassAssoClassList(className))
                    for s in attrnames:
                        instrs.append(createAttrVisibility(className,s))
                    instrs.append(createClassTop(className))
        elif (mode == 'strong'):
            model = m[0]
            datafile = m[1]
            classidMap = model.getClass()
            interfaceidMap = model.getInterface()
            for classid in classidMap:
                className = classidMap[classid]
                for i in range(strongCount):
                    instrs.append(createImpleInterList(className))
                    # instrs.append(createInfoHidden(className))
        for instr in range(len(instrs)-1):
            datafile.write(instrs[instr]+'\n')
        datafile.write(instrs[-1])
'''


def randomMake(count='1'):
	instrs = []
	with randomUmlMake(count) as m:
		model = m[0]
		datafile = m[1]
		classidMap = model.getClass()
		interfaceidMap = model.getInterface()
		instrs.append(createClassCount())
		if (notFoundClass):
			notFoundClassName = random.randint(len(classidMap), 99999)
			notFoundClassName = makeClassName(notFoundClassName)
			instrs.append(createImpleInterList(notFoundClassName))
			instrs.append(createClassOpCount(notFoundClassName))
			instrs.append(createOpVisibility(notFoundClassName, notFoundClassName))
			# instrs.append(createInfoHidden(notFoundClassName))
			# instrs.extend(createClassAttrCount(notFoundClassName))
			# instrs.append(createAttrVisibility(notFoundClassName,notFoundClassName))
			# instrs.append(createClassAssoClassList(notFoundClassName))
			# instrs.append(createClassAssoCount(notFoundClassName))
		for i in classidMap:
			className = classidMap[i]
			instrs.append(createClassSubCount(className))
			instrs.append(createClassOpCount(className))
			instrs.append(createClassAttrCoupling(className))
			instrs.append(createImpleInterList(className))
			# instrs.extend(createClassAttrCount(className))
			# instrs.append(createClassAssoCount(className))
			# instrs.append(createClassAssoClassList(className))
			# instrs.append(createClassTop(className))
			# instrs.append(createInfoHidden(className))
			classid = i
			methods = list(methodsName)
			for method in methods:
				instrs.append(createOpVisibility(className, method))
				instrs.append(createClassOpCoupling(className, method))
			'''attrnames = []
			while (classid != None):
				attrnames.extend(model.getClassAttributes(classid))
				classid = model.getClassParentId(classid)
			attrnames = set(attrnames)
			for s in attrnames:
				instrs.append(createAttrVisibility(className,s))
			if (notFountAttri):
				instrs.append(createAttrVisibility(className,className))'''
		for i in range(len(instrs)):
			if i != len(instrs) - 1:
				datafile.write(instrs[i] + '\n')
			else:
				datafile.write(instrs[i])


def dataMake(count='1', mode='normal', isStrong=False):
	randomMake('1')
	# print('Data gen finish!')


def test(src_list: list, num: int, time_limit: float) -> None:
	print('----- TEST CASE ' + str(num) + ' BEGIN -----')
	print('           TIME')
	for src in src_list:
		os.environ["COMSPEC"] = 'powershell'
		p = subprocess.Popen(
			'Measure-Command{Get-Content data1.txt | java -jar ' + src + '.jar > ' + src + '.txt}',
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
	# while True:
	dataMake('1')
	os.chdir('Z:\\buaa_oo_2022\\Unit4\\HW1\\hw1_test')  # change the path
	jar_list = ['homework3']  # add your Java archive's name without suffix
	test(jar_list, case, 100.0)  # you can change the third parameter to change the procedure's execute time
	case += 1

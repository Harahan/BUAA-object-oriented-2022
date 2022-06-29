# 按间距中的绿色按钮以运行脚本。
import ctypes
import os
import subprocess
import longitudinal_data_generate
import crosswise_data_generate

import data_generate
import input_parser
import request
from special_judge import Elevator, parse_output, check_correct

if __name__ == '__main__':
    tmp = -1
    while True:
        tmp += 1
        if tmp % 3 == 0:
            print("RANDOM")
            l = data_generate.data_generate(10.0)
        if tmp % 3 == 1:
            print("CROSSWISE")
            l = crosswise_data_generate.crosswise_data_generate()
        if tmp % 3 == 2:
            print("LONGITUDINAL")
            l = longitudinal_data_generate.longitudinal_data_generate(70)
        stdin_file = open("stdin.txt", "w")
        for i in l:
            stdin_file.write(str(i) + '\n')
        stdin_file.close()
    
        data_in = open("stdin.txt", 'r')
        stderr = open("stderr.txt", 'w')
        process = subprocess.Popen('datainput_student_win64.exe | java -jar homework2.jar> stdout.txt',
                                   cwd=r'Z:\buaa_oo_2022\Unit2\HW2\hw2_test',
                                   shell=True, stderr=stderr)
    
        # must be shell=False
        process.wait()
    
        handle = process._handle
    
        creation_time = ctypes.c_ulonglong()
        exit_time = ctypes.c_ulonglong()
        kernel_time = ctypes.c_ulonglong()
        user_time = ctypes.c_ulonglong()
    
        rc = ctypes.windll.kernel32.GetProcessTimes(handle,
                                                    ctypes.byref(creation_time),
                                                    ctypes.byref(exit_time),
                                                    ctypes.byref(kernel_time),
                                                    ctypes.byref(user_time),
                                                    )
    
        print("实际运行时间: " + str((exit_time.value - creation_time.value) / 10000000))
        print("CPU使用时间: " + str((kernel_time.value + user_time.value) / 10000000))
    
        data_in.close()
        stderr.close()
    
        elevators = [Elevator('A', 1, 1, True, "building", 0.0), Elevator('B', 1, 2, True, "building", 0.0),
                     Elevator('C', 1, 3, True, "building", 0.0), Elevator('D', 1, 4, True, "building", 0.0),
                     Elevator('E', 1, 5, True, "building", 0.0)]
        # print(elevators)
        stdin_file = open("stdin.txt", "r")
        input = input_parser.parse(stdin_file)
        stdin_file.close()
    
        if input[0] is None:
            print('Fatal Error: input error')
            exit(0)
        for i in input:
            if type(i) == request.ElevatorRequest:
                # print(i)
                if i.elevatorType == 'building':
                    elevators.append(Elevator(i.building, 1, i.elevatorID, True, 'building', i.time))
                else:
                    elevators.append(Elevator('A', i.floor, i.elevatorID, True, 'floor', i.time))
            # print(i)
        stdout_file = open('stdout.txt')
        raw_output = stdout_file.readlines()
        output = parse_output(raw_output)
        explain = open('explain.txt', "w")
        explain.close()
        if not check_correct(input, output, elevators):
            print(False)
            os.system("pause")
        print(True)
        stdout_file.close()
        print('')



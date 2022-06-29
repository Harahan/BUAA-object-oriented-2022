# 按间距中的绿色按钮以运行脚本。
import ctypes
import os
import subprocess

import data_generate
import elevator
import input_parser
import request
import spj

if __name__ == '__main__':
    while True:
        l = data_generate.data_generate(70.0)
        stdin_file = open("stdin.txt", "w")
        for i in l:
            stdin_file.write(str(i) + '\n')
        stdin_file.close()
    
        data_in = open("stdin.txt", 'r')
        stderr = open("stderr.txt", 'w')
        process = subprocess.Popen('datainput_student_win64.exe | java -jar homework3.jar> stdout.txt',
                                   cwd=r'Z:\buaa_oo_2022\Unit2\HW3\hw3_test',
                                   shell=True, stderr=stderr)
    
        # must be shell=False
        process.wait()
    
        handle = process._handle
    
        """creation_time = ctypes.c_ulonglong()
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
        """
    
        data_in.close()
        stderr.close()

        elevators = [elevator.Elevator('A', 1, 1, True, "building", "0.0", 8, "0.6"),
                     elevator.Elevator('B', 1, 2, True, "building", "0.0", 8, "0.6"),
                     elevator.Elevator('C', 1, 3, True, "building", "0.0", 8, "0.6"),
                     elevator.Elevator('D', 1, 4, True, "building", "0.0", 8, "0.6"),
                     elevator.Elevator('E', 1, 5, True, "building", "0.0", 8, "0.6"),
                     elevator.Elevator('A', 1, 6, True, "floor", "0.0", 8, "0.6")]
        # print(elevators)
        file = open("stdin.txt", "r")
        input = input_parser.parse(file)
        if input[0] is None:
            print('Fatal Error: input error')
            exit(0)
        for i in input:
            if type(i) == request.ElevatorRequest:
                print(i)
                if i.elevatorType == 'building':
                    elevators.append(elevator.Elevator(i.building, 1, i.elevatorID, True, 'building',
                                                       i.time, i.capacity, i.speed))
                else:
                    elevators.append(elevator.Elevator('A', i.floor, i.elevatorID, True, 'floor',
                                                       i.time, i.capacity, i.speed, i.switchInfo))
            # print(i)
        file = open('stdout.txt')
        raw_output = file.readlines()
        output = spj.parse_output(raw_output)
        # print(output)
        ok = spj.check_correct(input, output, elevators)
        print(ok)
        if ok is False:
            os.system("pause")
        print("")



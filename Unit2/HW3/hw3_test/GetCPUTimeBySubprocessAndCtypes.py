import subprocess
import ctypes

data_in = open("stdin.txt")
stdout = open("stdout.txt")
stderr = open("stderr.txt")
process = subprocess.Popen('java -jar TimeInput.jar | java -jar homework6.jar> stdout.txt', cwd=r'F:\MyWorkspace\Object_Oriented_Design\tools\UnlimitedBladeWorks', shell=False, stdin = data_in, stderr=stderr)
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

print((exit_time.value - creation_time.value) / 10000000)
print((kernel_time.value + user_time.value) / 10000000)

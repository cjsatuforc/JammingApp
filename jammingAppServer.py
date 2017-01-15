import socket
import sys
import time
from rflib import *

host, port = #server Ip Addr, #port Number
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
d = RfCat()

d.setMdmDRate(int(1.0/0.000550))

try:
	sock.bind((host, port))
except socket.error as msg:
	sys.exit()

sock.listen(1)

while 1:
	conn, addr = sock.accept()
	data = conn.recv(1024)
	freq = int(data)
	freq = freq*1000
	conn, addr = sock.accept()
	data = conn.recv(1024)
	whiletime = int(data)
	timeout = int(time.time()%60) + whiletime
	while (time.time()%60) < timeout:
		d.setFreq(freq)
		d.RFxmit("\xFF\xFF\xFF\xFF\xFF\xFF\xFF\xFF" * 40)
		time.sleep(0.25)
		

		

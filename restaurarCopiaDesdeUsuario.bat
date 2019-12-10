@echo off
SET /P fecha= Escriba la fecha del archivo(solo los numeros, sin espacios):
cd C:\Program Files\MySQL\MySQL Server 8.0\bin
mysql -h 192.168.100.200 -u root -p imprexa2 < C:\Users\Remedios\Desktop\respaldoImprexa_%fecha%.sql
pause
exit
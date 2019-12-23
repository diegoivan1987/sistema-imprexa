@echo off
cd C:\Program Files\MySQL\MySQL Server 8.0\bin
mysqldump -h 192.168.100.200 -u root -p imprexa2 > C:\Users\Alex\Desktop\respaldoImprexa_%date:~-10,2%%date:~-7,2%%date:~-4,4%.sql
copy C:\Users\Alex\Desktop\respaldoImprexa_%date:~-10,2%%date:~-7,2%%date:~-4,4%.sql \\DESKTOP-TV4HCNN\Users\Public\Documents
popd
pause
exit
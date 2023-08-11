ALTER USER 'root'@'localhost' IDENTIFIED VIA unix_socket;
UPDATE mysql.user SET plugin = 'mysql_native_password' WHERE user = 'root' AND host = 'localhost';
FLUSH PRIVILEGES;

CREATE USER 'lldj'@'%' IDENTIFIED BY 'lldj123414';
GRANT ALL PRIVILEGES ON *.* TO 'lldj'@'%';

CREATE USER 'lldjlocal'@'127.0.0.1' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'lldjlocal'@'127.0.0.1';

CREATE USER 'lldjlocal'@'172.17.%.%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'lldjlocal'@'172.17.%.%';

CREATE DATABASE wanted__prod;
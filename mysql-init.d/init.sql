GRANT ALL PRIVILEGES ON *.* TO lldj@'%' IDENTIFIED BY 'lldj123414';

-- # lldj 계정이 아이디와 비번만 알면 어디서든 접속이 가능하도록 설정

GRANT ALL PRIVILEGES ON *.* TO lldjlocal@127.0.0.1 IDENTIFIED BY '1234';
-- # lldjlocal 계정이 MariaDB 컨테이너에서만 접속 가능하도록 허용
GRANT ALL PRIVILEGES ON *.* TO lldjlocal@'172.17.%.%' IDENTIFIED BY '1234';
CREATE DATABASE IF NOT EXISTS wanted__prod;
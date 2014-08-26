--Setup Default user
CREATE USER 'app'@'localhost' IDENTIFIED BY 'some_pass';
GRANT ALL PRIVILEGES ON *.* TO 'app'@'localhost' WITH GRANT OPTION;

CREATE USER 'app'@'%' IDENTIFIED BY 'some_pass';
GRANT ALL PRIVILEGES ON *.* TO 'app'@'%'WITH GRANT OPTION;

CREATE USER 'admin'@'localhost';
GRANT RELOAD,PROCESS ON *.* TO 'admin'@'localhost';
CREATE USER 'dummy'@'localhost';
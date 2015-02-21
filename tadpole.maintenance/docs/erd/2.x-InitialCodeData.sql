INSERT INTO tadpole_role(ROLE_ID, ROLE_NAME) VALUES ('SYSTEM_ADMIN','System Admin');
INSERT INTO tadpole_role(ROLE_ID, ROLE_NAME) VALUES ('ADMIN','Admin');
INSERT INTO tadpole_role(ROLE_ID, ROLE_NAME) VALUES ('DBA','DBA');
INSERT INTO tadpole_role(ROLE_ID, ROLE_NAME) VALUES ('MANAGER','Manager');
INSERT INTO tadpole_role(ROLE_ID, ROLE_NAME) VALUES ('USER','User');
INSERT INTO tadpole_role(ROLE_ID, ROLE_NAME) VALUES ('GUEST', 'Guest')

INSERT INTO dbtype(TYPE_CODE, TYPE_NAME, SMALL_NAME) VALUES ('PRODUCTION','Production Sever', '');
INSERT INTO dbtype(TYPE_CODE, TYPE_NAME, SMALL_NAME) VALUES ('DEVELOP','Develop Sever', '');
INSERT INTO dbtype(TYPE_CODE, TYPE_NAME, SMALL_NAME) VALUES ('TEST','Test Sever', '');
INSERT INTO dbtype(TYPE_CODE, TYPE_NAME, SMALL_NAME) VALUES ('BACKUP','Backup Sever', '');
INSERT INTO dbtype(TYPE_CODE, TYPE_NAME, SMALL_NAME) VALUES ('OTHERS','Others Sever', '');

INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) 
VALUES ( 'SESSION_LIST', 'SESSION LIST', 'SHOW FULL PROCESSLIST' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'GENERAL_LOG', 'GENERAL LOG', 'select * from mysql.general_log' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) 
VALUES ( 'SLOW_QUERY', 'SLOW QUERY', 'select * from mysql.slow_log' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) VALUES  ( 'CPU', 'CPU', '' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) VALUES  ( 'DISK', 'DISK', '' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'CONNECTION', 'Connection', 'SHOW STATUS WHERE variable_name = Connections' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'PROCESS', 'Process count', 'SHOW STATUS WHERE variable_name = Threads_connected' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'NETWORK_IN', 'Network in', 'SHOW STATUS WHERE variable_name = Bytes_received' ); 
INSERT INTO monitoring_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'NETWORK_OUT', 'Network out', 'SHOW STATUS WHERE variable_name = Bytes_sent' ); 

INSERT INTO monitoring_read_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'SQL', 'SQL', '' ); 
INSERT INTO monitoring_read_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'REST_API', 'REST API', '' ); 
INSERT INTO monitoring_read_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'PL/SQL', 'PL SQL', '' ); 
INSERT INTO monitoring_read_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'Agent', 'Agent', '' );
 
INSERT INTO monitoring_after_type (TYPE, NAME, DESCRIPTION) 
VALUES ( 'EMAIL', 'EMAIL', '' );
 INSERT INTO monitoring_after_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'KILL_AFTER_EMAIL', 'Kill Session after EMAIL send', '' ); 
 INSERT INTO monitoring_after_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'SMS', 'SMS', '' ); 
 INSERT INTO monitoring_after_type (TYPE, NAME, DESCRIPTION) 
VALUES  ( 'KILL_AFTER_SMS', 'Kill Session after SMS send', '' ); 
 
 
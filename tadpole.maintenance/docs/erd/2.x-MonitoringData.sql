select * from tadpole_monitoring_template;

delete from  tadpole_monitoring_template;

select * from mysql.slow_log;
INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, parameter_1,
 after_type, 
 monitoring_type) 
VALUES 
 ( 
 -1, 'MySQL', 'SLOW QUERY', 'SLOW QUERY', 
 'select * from mysql.slow_log where start_time > ? order by start_time',
 'rows_examined', 'RISE_EXCEPTION', '0', '2015-01-29 02:15:52',
 'EMAIL',
 'SLOW_QUERY'
 ); 
 
 SHOW FULL PROCESSLIST;
 INSERT INTO tadpole_monitoring_template
(user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 ( -1, 'MySQL', 'Show processlist', 'Show processlist', 
 'SHOW FULL PROCESSLIST',
 'Time', 'GREATEST', '50', 'KILL_AFTER_EMAIL',
 'SESSION_LIST' ); 
 
 SHOW STATUS WHERE `variable_name` = 'Connections';
 INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MySQL', 'connection', 'connection count',
 'SHOW STATUS WHERE \`variable_name\` = \'Connections\'',
 'VARIABLE_VALUE', 'GREATEST', '10', 'EMAIL',
 'Connections' ); 
 

  INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MySQL', 'process', 'process count',
 'SHOW STATUS WHERE \`variable_name\` = \'Threads_connected\'',
 'VARIABLE_VALUE', 'GREATEST', '100', 'EMAIL',
 'Process'
  ); 
 
 INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MySQL', 'Network In', 'Network In',
 'SHOW STATUS WHERE \`variable_name\` = \'Bytes_received\'',
 'VARIABLE_VALUE', 'EQUALS', '0', 'EMAIL',
 'NETWORK_IN'
  ); 
 
 INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MySQL', 'Network Out', 'Network Out',
 'SHOW STATUS WHERE \`variable_name\` = \'Bytes_sent\'',
 'VARIABLE_VALUE', 'EQUALS', '0', 'EMAIL',
 'NETWORK_OUT' ); 
 

select * from mysql.slow_log;
INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, parameter_1,
 after_type,
 monitoring_type) 
VALUES 
 ( 
 -1, 'MariaDB', 'SLOW QUERY', 'SLOW QUERY', 
 'select * from mysql.slow_log where start_time > ? order by start_time',
 'rows_examined', 'RISE_EXCEPTION', '0', '2015-01-29 02:15:52', 'EMAIL', 
 'SLOW_QUERY'
 );  
 
 SHOW FULL PROCESSLIST;
 INSERT INTO tadpole_monitoring_template
(user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 ( -1, 'MariaDB', 'Show processlist', 'Show processlist', 
 'SHOW FULL PROCESSLIST',
 'Time', 'GREATEST', '50', 'KILL_AFTER_EMAIL',
 'SESSION_LIST' ); 
 
 SHOW STATUS WHERE `variable_name` = 'Connections';
 INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MariaDB', 'connection', 'connection count',
 'SHOW STATUS WHERE \`variable_name\` = \'Connections\'',
 'VARIABLE_VALUE', 'GREATEST', '10', 'EMAIL',
 'Connections' ); 
 
  INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MariaDB', 'process', 'process count',
 'SHOW STATUS WHERE \`variable_name\` = \'Threads_connected\'',
 'VARIABLE_VALUE', 'GREATEST', '100', 'EMAIL',
 'Process'
  ); 
 
 INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MariaDB', 'Network In', 'Network In',
 'SHOW STATUS WHERE \`variable_name\` = \'Bytes_received\'',
 'VARIABLE_VALUE', 'EQUALS', '0', 'EMAIL',
 'NETWORK_IN'
  ); 
 
 INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type)
VALUES 
 (-1, 'MariaDB', 'Network Out', 'Network Out',
 'SHOW STATUS WHERE \`variable_name\` = \'Bytes_sent\'',
 'VARIABLE_VALUE', 'EQUALS', '0', 'EMAIL',
 'NETWORK_OUT' ); 
 

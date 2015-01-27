select * from tadpole_monitoring_template;

delete from  tadpole_monitoring_template;

select * from mysql.slow_log;
INSERT INTO tadpole_monitoring_template
 (user_seq, db_type, title, description, query, 
 index_nm, condition_type, condition_value, after_type, 
 monitoring_type) 
VALUES 
 ( 
 -1, 'MySQL', 'SLOW QUERY', 'SLOW QUERY', 
 'select * from mysql.slow_log',
 'queryTime', 'GREATEST', '0', 'EMAIL',
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
 

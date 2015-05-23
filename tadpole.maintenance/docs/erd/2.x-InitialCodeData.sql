insert into tadpole_system(name, major_version, sub_version, information)
values ('Tadpole DB HUB', '1.6.1', 'b06', 'none'); 
	
insert into tadpole_role(role_id, role_name) values ('system_admin','system admin');
insert into tadpole_role(role_id, role_name) values ('admin','admin');
insert into tadpole_role(role_id, role_name) values ('dba','dba');
insert into tadpole_role(role_id, role_name) values ('manager','manager');
insert into tadpole_role(role_id, role_name) values ('user','user');
insert into tadpole_role(role_id, role_name) values ('guest', 'guest');

insert into dbtype(type_code, type_name, small_name) values ('production','production sever', '');
insert into dbtype(type_code, type_name, small_name) values ('develop','develop sever', '');
insert into dbtype(type_code, type_name, small_name) values ('test','test sever', '');
insert into dbtype(type_code, type_name, small_name) values ('backup','backup sever', '');
insert into dbtype(type_code, type_name, small_name) values ('others','others sever', '');

-- sequence table
insert into tadpole_sequence (name, no) values ('monitoring', 0);

insert into monitoring_type (type, name, description) 
values ( 'session_list', 'session list', 'show full processlist' ); 
insert into monitoring_type (type, name, description) 
values  ( 'general_log', 'general log', 'select * from mysql.general_log' ); 
insert into monitoring_type (type, name, description) 
values ( 'slow_query', 'slow query', 'select * from mysql.slow_log' ); 
insert into monitoring_type (type, name, description) values  ( 'cpu', 'cpu', '' ); 
insert into monitoring_type (type, name, description) values  ( 'disk', 'disk', '' ); 
insert into monitoring_type (type, name, description) 
values  ( 'connection', 'connection', 'show status where variable_name = connections' ); 
insert into monitoring_type (type, name, description) 
values  ( 'process', 'process count', 'show status where variable_name = threads_connected' ); 
insert into monitoring_type (type, name, description) 
values  ( 'network_in', 'network in', 'show status where variable_name = bytes_received' ); 
insert into monitoring_type (type, name, description) 
values  ( 'network_out', 'network out', 'show status where variable_name = bytes_sent' ); 

insert into monitoring_read_type (type, name, description) 
values  ( 'sql', 'sql', '' ); 
insert into monitoring_read_type (type, name, description) 
values  ( 'rest_api', 'rest api', '' ); 
insert into monitoring_read_type (type, name, description) 
values  ( 'pl/sql', 'pl sql', '' ); 
insert into monitoring_read_type (type, name, description) 
values  ( 'agent', 'agent', '' );
 
insert into monitoring_after_type (type, name, description) 
values ( 'email', 'email', '' );
 insert into monitoring_after_type (type, name, description) 
values  ( 'kill_after_email', 'kill session after email send', '' ); 
 insert into monitoring_after_type (type, name, description) 
values  ( 'sms', 'sms', '' ); 
 insert into monitoring_after_type (type, name, description) 
values  ( 'kill_after_sms', 'kill session after sms send', '' ); 
 
 
/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.ace.editor.core.texteditor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.utils.TadpoleEditorUtils;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Editor의 확장을 정의한다.
 * 
 * @author hangum
 *
 */
public abstract class EditorExtension extends EditorPart implements IEditorExtension {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EditorExtension.class);
	
	///[browser editor]/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected Browser browserQueryEditor;
	/** browser.browserFunction의 서비스 헨들러 */
	protected EditorFunctionService editorService;

	/** session에서 사용자 정보를 가져다 놓습니다.
	 * No context available outside of the request service lifecycle.
	 */
	/**
	 * 사용자 email
	 */
	private final String userEMail 		= SessionManager.getEMAIL();
	
	/**
	 * 사용자 seq
	 */
	private final int userSeq 			= SessionManager.getUserSeq();
	protected String strRoleType 		= "";
//	protected final int intUserSeq 		= SessionManager.getSeq();
	
	/** 현재 에디터에서 처리해야하는 디비 정보. */
	protected UserDBDAO userDB;
//	/** 현재 유저 그룹의 그룹 리스트 */
//	protected List<UserDBDAO> listUserGroup = new ArrayList<>();
	
	public EditorExtension() {
		super();
	}
	
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public String getUserEMail() {
		return userEMail;
	}
	
	public int getUserSeq() {
		return userSeq;
	}
	

	/**
	 * find editor extension
	 * 
	 * eg) mysql, pgsql
	 * @return
	 */
	protected String findEditorExt() {
		return userDB.getDBDefine().getExt();
	}
	/**
	 * define constant
	 * @return
	 */
	protected String findDefaultConstant() {
		String strConstant = "";
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine() || DBDefine.MARIADB_DEFAULT == userDB.getDBDefine()) {
			strConstant = "by|bool|boolean|bit|blob|decimal|double|enum|float|long|longblob|longtext|medium|mediumblob|mediumint|mediumtext|time|timestamp|tinyblob|tinyint|tinytext|text|bigint|int|int1|int2|int3|int4|int8|integer|float|float4|float8|double|char|varbinary|varchar|varcharacter|precision|date|datetime|year|unsigned|signed|numeric";
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			strConstant = "getprotobynumber|getprotobyname|getservbyname|gethostbyaddr|" +
			         "gethostbyname|getservbyport|getnetbyaddr|getnetbyname|getsockname|" +
			         "getpeername|setpriority|getprotoent|setprotoent|getpriority|" +
			         "endprotoent|getservent|setservent|endservent|sethostent|socketpair|" +
			         "getsockopt|gethostent|endhostent|setsockopt|setnetent|quotemeta|" +
			         "localtime|prototype|getnetent|endnetent|rewinddir|wantarray|getpwuid|" +
			         "closedir|getlogin|readlink|endgrent|getgrgid|getgrnam|shmwrite|" +
			         "shutdown|readline|endpwent|setgrent|readpipe|formline|truncate|" +
			         "dbmclose|syswrite|setpwent|getpwnam|getgrent|getpwent|ucfirst|sysread|" +
			         "setpgrp|shmread|sysseek|sysopen|telldir|defined|opendir|connect|" +
			         "lcfirst|getppid|binmode|syscall|sprintf|getpgrp|readdir|seekdir|" +
			         "waitpid|reverse|unshift|symlink|dbmopen|semget|msgrcv|rename|listen|" +
			         "chroot|msgsnd|shmctl|accept|unpack|exists|fileno|shmget|system|" +
			         "unlink|printf|gmtime|msgctl|semctl|values|rindex|substr|splice|" +
			         "length|msgget|select|socket|return|caller|delete|alarm|ioctl|index|" +
			         "undef|lstat|times|srand|chown|fcntl|close|write|umask|rmdir|study|" +
			         "sleep|chomp|untie|print|utime|mkdir|atan2|split|crypt|flock|chmod|" +
			         "BEGIN|bless|chdir|semop|shift|reset|link|stat|chop|grep|fork|dump|" +
			         "join|open|tell|pipe|exit|glob|warn|each|bind|sort|pack|eval|push|" +
			         "keys|getc|kill|seek|sqrt|send|wait|rand|tied|read|time|exec|recv|" +
			         "eof|chr|int|ord|exp|pos|pop|sin|log|abs|oct|hex|tie|cos|vec|END|ref|" +
			         "map|die|uc|lc|do";
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			strConstant = "typeof|int|integer|tinyint|smallint|mediumint|bigint|unsigned|big|int|int2|int8|character|varchar|varying|character|nchar|native||nvarchar|text|clob|blob|real|double|precision|float|numberic|decimal|boolean|date|datetime";
		// 테이블명이 올바로 표시되지 않는 오류로 sql 확장자로 처리 할수 있도록 수정합니다. 	
		} else if(DBDefine.MSSQL_8_LE_DEFAULT == userDB.getDBDefine() || DBDefine.MSSQL_DEFAULT == userDB.getDBDefine()) {
			strConstant =  "OPENDATASOURCE|OPENQUERY|OPENROWSET|OPENXML|" +
			        "AVG|CHECKSUM_AGG|COUNT|COUNT_BIG|GROUPING|GROUPING_ID|MAX|MIN|STDEV|STDEVP|SUM|VAR|VARP|" +
			        "DENSE_RANK|NTILE|RANK|ROW_NUMBER" +
			        "@@DATEFIRST|@@DBTS|@@LANGID|@@LANGUAGE|@@LOCK_TIMEOUT|@@MAX_CONNECTIONS|@@MAX_PRECISION|@@NESTLEVEL|@@OPTIONS|@@REMSERVER|@@SERVERNAME|@@SERVICENAME|@@SPID|@@TEXTSIZE|@@VERSION|" +
			        "CAST|CONVERT|PARSE|TRY_CAST|TRY_CONVERT|TRY_PARSE" +
			        "@@CURSOR_ROWS|@@FETCH_STATUS|CURSOR_STATUS|" +
			        "@@DATEFIRST|@@LANGUAGE|CURRENT_TIMESTAMP|DATEADD|DATEDIFF|DATEFROMPARTS|DATENAME|DATEPART|DATETIME2FROMPARTS|DATETIMEFROMPARTS|DATETIMEOFFSETFROMPARTS|DAY|EOMONTH|GETDATE|GETUTCDATE|ISDATE|MONTH|SET DATEFIRST|SET DATEFORMAT|SET LANGUAGE|SMALLDATETIMEFROMPARTS|SP_HELPLANGUAGE|SWITCHOFFSET|SYSDATETIME|SYSDATETIMEOFFSET|SYSUTCDATETIME|TIMEFROMPARTS|TODATETIMEOFFSET|YEAR|" +
			        "CHOOSE|IIF|" +
			        "ABS|ACOS|ASIN|ATAN|ATN2|CEILING|COS|COT|DEGREES|EXP|FLOOR|LOG|LOG10|PI|POWER|RADIANS|RAND|ROUND|SIGN|SIN|SQRT|SQUARE|TAN|" +
			        "@@PROCID|APPLOCK_MODE|APPLOCK_TEST|APP_NAME|ASSEMBLYPROPERTY|COLUMNPROPERTY|COL_LENGTH|COL_NAME|DATABASEPROPERTYEX|DATABASE_PRINCIPAL_ID|DB_ID|DB_NAME|FILEGROUPPROPERTY|FILEGROUP_ID|FILEGROUP_NAME|FILEPROPERTY|FILE_ID|FILE_IDEX|FILE_NAME|FULLTEXTCATALOGPROPERTY|FULLTEXTSERVICEPROPERTY|INDEXKEY_PROPERTY|INDEXPROPERTY|INDEX_COL|OBJECTPROPERTY|OBJECTPROPERTYEX|OBJECT_DEFINITION|OBJECT_ID|OBJECT_NAME|OBJECT_SCHEMA_NAME|ORIGINAL_DB_NAME|PARSENAME|SCHEMA_ID|SCHEMA_NAME|SCOPE_IDENTITY|SERVERPROPERTY|STATS_DATE|TYPEPROPERTY|TYPE_ID|TYPE_NAME|" +
			        "CERTENCODED|CERTPRIVATEKEY|CURRENT_USER|DATABASE_PRINCIPAL_ID|HAS_PERMS_BY_NAME|IS_MEMBER|IS_ROLEMEMBER|IS_SRVROLEMEMBER|ORIGINAL_LOGIN|PERMISSIONS|PWDCOMPARE|PWDENCRYPT|SCHEMA_ID|SCHEMA_NAME|SESSION_USER|SUSER_ID|SUSER_NAME|SUSER_SID|SUSER_SNAME|SYS.FN_BUILTIN_PERMISSIONS|SYS.FN_GET_AUDIT_FILE|SYS.FN_MY_PERMISSIONS|SYSTEM_USER|USER_ID|USER_NAME|" +
			        "ASCII|CHAR|CHARINDEX|CONCAT|DIFFERENCE|FORMAT|LEN|LOWER|LTRIM|NCHAR|PATINDEX|QUOTENAME|REPLACE|REPLICATE|REVERSE|RTRIM|SOUNDEX|SPACE|STR|STUFF|SUBSTRING|UNICODE|UPPER|" +
			        "$PARTITION|@@ERROR|@@IDENTITY|@@PACK_RECEIVED|@@ROWCOUNT|@@TRANCOUNT|BINARY_CHECKSUM|CHECKSUM|CONNECTIONPROPERTY|CONTEXT_INFO|CURRENT_REQUEST_ID|ERROR_LINE|ERROR_MESSAGE|ERROR_NUMBER|ERROR_PROCEDURE|ERROR_SEVERITY|ERROR_STATE|FORMATMESSAGE|GETANSINULL|GET_FILESTREAM_TRANSACTION_CONTEXT|HOST_ID|HOST_NAME|ISNULL|ISNUMERIC|MIN_ACTIVE_ROWVERSION|NEWID|NEWSEQUENTIALID|ROWCOUNT_BIG|XACT_STATE|" +
			        "@@CONNECTIONS|@@CPU_BUSY|@@IDLE|@@IO_BUSY|@@PACKET_ERRORS|@@PACK_RECEIVED|@@PACK_SENT|@@TIMETICKS|@@TOTAL_ERRORS|@@TOTAL_READ|@@TOTAL_WRITE|FN_VIRTUALFILESTATS|" +
			        "PATINDEX|TEXTPTR|TEXTVALID|" +
			        "COALESCE|NULLIF";
		} else {
			strConstant =  "count|min|max|avg|sum|rank|now|coalesce";
		}
		return strConstant;
	}
	
	/**
	 * define function
	 * 
	 * @return
	 */
	protected String findDefaultFunction() {
		String strFunction = "";
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine() || DBDefine.MARIADB_DEFAULT == userDB.getDBDefine()) {
			strFunction = "by|bool|boolean|bit|blob|decimal|double|enum|float|long|longblob|longtext|medium|mediumblob|mediumint|mediumtext|time|timestamp|tinyblob|tinyint|tinytext|text|bigint|int|int1|int2|int3|int4|int8|integer|float|float4|float8|double|char|varbinary|varchar|varcharacter|precision|date|datetime|year|unsigned|signed|numeric";
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			strFunction = "getprotobynumber|getprotobyname|getservbyname|gethostbyaddr|" +
			         "gethostbyname|getservbyport|getnetbyaddr|getnetbyname|getsockname|" +
			         "getpeername|setpriority|getprotoent|setprotoent|getpriority|" +
			         "endprotoent|getservent|setservent|endservent|sethostent|socketpair|" +
			         "getsockopt|gethostent|endhostent|setsockopt|setnetent|quotemeta|" +
			         "localtime|prototype|getnetent|endnetent|rewinddir|wantarray|getpwuid|" +
			         "closedir|getlogin|readlink|endgrent|getgrgid|getgrnam|shmwrite|" +
			         "shutdown|readline|endpwent|setgrent|readpipe|formline|truncate|" +
			         "dbmclose|syswrite|setpwent|getpwnam|getgrent|getpwent|ucfirst|sysread|" +
			         "setpgrp|shmread|sysseek|sysopen|telldir|defined|opendir|connect|" +
			         "lcfirst|getppid|binmode|syscall|sprintf|getpgrp|readdir|seekdir|" +
			         "waitpid|reverse|unshift|symlink|dbmopen|semget|msgrcv|rename|listen|" +
			         "chroot|msgsnd|shmctl|accept|unpack|exists|fileno|shmget|system|" +
			         "unlink|printf|gmtime|msgctl|semctl|values|rindex|substr|splice|" +
			         "length|msgget|select|socket|return|caller|delete|alarm|ioctl|index|" +
			         "undef|lstat|times|srand|chown|fcntl|close|write|umask|rmdir|study|" +
			         "sleep|chomp|untie|print|utime|mkdir|atan2|split|crypt|flock|chmod|" +
			         "BEGIN|bless|chdir|semop|shift|reset|link|stat|chop|grep|fork|dump|" +
			         "join|open|tell|pipe|exit|glob|warn|each|bind|sort|pack|eval|push|" +
			         "keys|getc|kill|seek|sqrt|send|wait|rand|tied|read|time|exec|recv|" +
			         "eof|chr|int|ord|exp|pos|pop|sin|log|abs|oct|hex|tie|cos|vec|END|ref|" +
			         "map|die|uc|lc|do";
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			strFunction = "typeof|int|integer|tinyint|smallint|mediumint|bigint|unsigned|big|int|int2|int8|character|varchar|varying|character|nchar|native||nvarchar|text|clob|blob|real|double|precision|float|numberic|decimal|boolean|date|datetime";
		// 테이블명이 올바로 표시되지 않는 오류로 sql 확장자로 처리 할수 있도록 수정합니다. 	
		} else if(DBDefine.MSSQL_8_LE_DEFAULT == userDB.getDBDefine() || DBDefine.MSSQL_DEFAULT == userDB.getDBDefine()) {
			strFunction =  "OPENDATASOURCE|OPENQUERY|OPENROWSET|OPENXML|" +
			        "AVG|CHECKSUM_AGG|COUNT|COUNT_BIG|GROUPING|GROUPING_ID|MAX|MIN|STDEV|STDEVP|SUM|VAR|VARP|" +
			        "DENSE_RANK|NTILE|RANK|ROW_NUMBER" +
			        "@@DATEFIRST|@@DBTS|@@LANGID|@@LANGUAGE|@@LOCK_TIMEOUT|@@MAX_CONNECTIONS|@@MAX_PRECISION|@@NESTLEVEL|@@OPTIONS|@@REMSERVER|@@SERVERNAME|@@SERVICENAME|@@SPID|@@TEXTSIZE|@@VERSION|" +
			        "CAST|CONVERT|PARSE|TRY_CAST|TRY_CONVERT|TRY_PARSE" +
			        "@@CURSOR_ROWS|@@FETCH_STATUS|CURSOR_STATUS|" +
			        "@@DATEFIRST|@@LANGUAGE|CURRENT_TIMESTAMP|DATEADD|DATEDIFF|DATEFROMPARTS|DATENAME|DATEPART|DATETIME2FROMPARTS|DATETIMEFROMPARTS|DATETIMEOFFSETFROMPARTS|DAY|EOMONTH|GETDATE|GETUTCDATE|ISDATE|MONTH|SET DATEFIRST|SET DATEFORMAT|SET LANGUAGE|SMALLDATETIMEFROMPARTS|SP_HELPLANGUAGE|SWITCHOFFSET|SYSDATETIME|SYSDATETIMEOFFSET|SYSUTCDATETIME|TIMEFROMPARTS|TODATETIMEOFFSET|YEAR|" +
			        "CHOOSE|IIF|" +
			        "ABS|ACOS|ASIN|ATAN|ATN2|CEILING|COS|COT|DEGREES|EXP|FLOOR|LOG|LOG10|PI|POWER|RADIANS|RAND|ROUND|SIGN|SIN|SQRT|SQUARE|TAN|" +
			        "@@PROCID|APPLOCK_MODE|APPLOCK_TEST|APP_NAME|ASSEMBLYPROPERTY|COLUMNPROPERTY|COL_LENGTH|COL_NAME|DATABASEPROPERTYEX|DATABASE_PRINCIPAL_ID|DB_ID|DB_NAME|FILEGROUPPROPERTY|FILEGROUP_ID|FILEGROUP_NAME|FILEPROPERTY|FILE_ID|FILE_IDEX|FILE_NAME|FULLTEXTCATALOGPROPERTY|FULLTEXTSERVICEPROPERTY|INDEXKEY_PROPERTY|INDEXPROPERTY|INDEX_COL|OBJECTPROPERTY|OBJECTPROPERTYEX|OBJECT_DEFINITION|OBJECT_ID|OBJECT_NAME|OBJECT_SCHEMA_NAME|ORIGINAL_DB_NAME|PARSENAME|SCHEMA_ID|SCHEMA_NAME|SCOPE_IDENTITY|SERVERPROPERTY|STATS_DATE|TYPEPROPERTY|TYPE_ID|TYPE_NAME|" +
			        "CERTENCODED|CERTPRIVATEKEY|CURRENT_USER|DATABASE_PRINCIPAL_ID|HAS_PERMS_BY_NAME|IS_MEMBER|IS_ROLEMEMBER|IS_SRVROLEMEMBER|ORIGINAL_LOGIN|PERMISSIONS|PWDCOMPARE|PWDENCRYPT|SCHEMA_ID|SCHEMA_NAME|SESSION_USER|SUSER_ID|SUSER_NAME|SUSER_SID|SUSER_SNAME|SYS.FN_BUILTIN_PERMISSIONS|SYS.FN_GET_AUDIT_FILE|SYS.FN_MY_PERMISSIONS|SYSTEM_USER|USER_ID|USER_NAME|" +
			        "ASCII|CHAR|CHARINDEX|CONCAT|DIFFERENCE|FORMAT|LEN|LOWER|LTRIM|NCHAR|PATINDEX|QUOTENAME|REPLACE|REPLICATE|REVERSE|RTRIM|SOUNDEX|SPACE|STR|STUFF|SUBSTRING|UNICODE|UPPER|" +
			        "$PARTITION|@@ERROR|@@IDENTITY|@@PACK_RECEIVED|@@ROWCOUNT|@@TRANCOUNT|BINARY_CHECKSUM|CHECKSUM|CONNECTIONPROPERTY|CONTEXT_INFO|CURRENT_REQUEST_ID|ERROR_LINE|ERROR_MESSAGE|ERROR_NUMBER|ERROR_PROCEDURE|ERROR_SEVERITY|ERROR_STATE|FORMATMESSAGE|GETANSINULL|GET_FILESTREAM_TRANSACTION_CONTEXT|HOST_ID|HOST_NAME|ISNULL|ISNUMERIC|MIN_ACTIVE_ROWVERSION|NEWID|NEWSEQUENTIALID|ROWCOUNT_BIG|XACT_STATE|" +
			        "@@CONNECTIONS|@@CPU_BUSY|@@IDLE|@@IO_BUSY|@@PACKET_ERRORS|@@PACK_RECEIVED|@@PACK_SENT|@@TIMETICKS|@@TOTAL_ERRORS|@@TOTAL_READ|@@TOTAL_WRITE|FN_VIRTUALFILESTATS|" +
			        "PATINDEX|TEXTPTR|TEXTVALID|" +
			        "COALESCE|NULLIF";
		} else {
			strFunction =  "count|min|max|avg|sum|rank|now|coalesce";
		}
		return strFunction;
	}
	
	/**
	 * default keyword
	 * @return
	 */
	protected String findDefaultKeyword() {
		String strKeyword = "";
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine() || DBDefine.MARIADB_DEFAULT == userDB.getDBDefine()) {
			strKeyword = "alter|and|as|asc|between|count|create|delete|desc|distinct|drop|from|having|in|insert|into|is|join|like|not|on|or|order|select|set|table|union|update|values|where" + 
						"|accessible|action|add|after|algorithm|all|analyze|asensitive|at|authors|auto_increment|autocommit|avg|avg_row_length|before|binary|binlog|both|btree|cache|call|cascade|cascaded|case|catalog_name|chain|change|changed|character|check|checkpoint|checksum|class_origin|client_statistics|close|coalesce|code|collate|collation|collations|column|columns|comment|commit|committed|completion|concurrent|condition|connection|consistent|constraint|contains|continue|contributors|convert|cross|current_date|current_time|current_timestamp|current_user|cursor|data|database|databases|day_hour|day_microsecond|day_minute|day_second|deallocate|dec|declare|default|delay_key_write|delayed|delimiter|des_key_file|describe|deterministic|dev_pop|dev_samp|deviance|directory|disable|discard|distinctrow|div|dual|dumpfile|each|elseif|enable|enclosed|end|ends|engine|engines|enum|errors|escape|escaped|even|event|events|every|execute|exists|exit|explain|extended|fast|fetch|field|fields|first|flush|for|force|foreign|found_rows|full|fulltext|function|general|global|grant|grants|group|groupby_concat|handler|hash|help|high_priority|hosts|hour_microsecond|hour_minute|hour_second|if|ignore|ignore_server_ids|import|index|index_statistics|infile|inner|innodb|inout|insensitive|insert_method|install|interval|invoker|isolation|iterate|key|keys|kill|language|last|leading|leave|left|level|limit|linear|lines|list|load|local|localtime|localtimestamp|lock|logs|low_priority|master|master_heartbeat_period|master_ssl_verify_server_cert|masters|match|max|max_rows|maxvalue|message_text|middleint|migrate|min|min_rows|minute_microsecond|minute_second|mod|mode|modifies|modify|mutex|mysql_errno|natural|next|no|no_write_to_binlog|offline|offset|one|online|open|optimize|option|optionally|out|outer|outfile|pack_keys|parser|partition|partitions|password|phase|plugin|plugins|prepare|preserve|prev|primary|privileges|procedure|processlist|profile|profiles|purge|query|quick|range|read|read_write|reads|real|rebuild|recover|references|regexp|relaylog|release|remove|rename|reorganize|repair|repeatable|replace|require|resignal|restrict|resume|return|returns|revoke|right|rlike|rollback|rollup|row|row_format|rtree|savepoint|schedule|schema|schema_name|schemas|second_microsecond|security|sensitive|separator|serializable|server|session|share|show|signal|slave|slow|smallint|snapshot|soname|spatial|specific|sql|sql_big_result|sql_buffer_result|sql_cache|sql_calc_found_rows|sql_no_cache|sql_small_result|sqlexception|sqlstate|sqlwarning|ssl|start|starting|starts|status|std|stddev|stddev_pop|stddev_samp|storage|straight_join|subclass_origin|sum|suspend|table_name|table_statistics|tables|tablespace|temporary|terminated|to|trailing|transaction|trigger|triggers|truncate|uncommitted|undo|uninstall|unique|unlock|upgrade|usage|use|use_frm|user|user_resources|user_statistics|using|utc_date|utc_time|utc_timestamp|value|variables|varying|view|views|warnings|when|while|with|work|write|xa|xor|year_month|zerofill|begin|do|then|else|loop|repeat" +
						"|substr|ifnull";
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			strKeyword = "abort|absolute|abstime|access|aclitem|action|add|admin|after|aggregate|all|also|alter|always|" +
			        "analyse|analyze|and|any|anyarray|anyelement|anyenum|anynonarray|anyrange|array|as|asc|" +
			        "assertion|assignment|asymmetric|at|attribute|authorization|backward|before|begin|between|" +
			        "bigint|binary|bit|bool|boolean|both|box|bpchar|by|bytea|cache|called|cascade|cascaded|case|cast|" +
			        "catalog|chain|char|character|characteristics|check|checkpoint|cid|cidr|circle|class|close|" +
			        "cluster|coalesce|collate|collation|column|comment|comments|commit|committed|concurrently|" +
			        "configuration|connection|constraint|constraints|content|continue|conversion|copy|cost|" +
			        "create|cross|cstring|csv|current|current_catalog|current_date|current_role|" +
			        "current_schema|current_time|current_timestamp|current_user|cursor|cycle|data|database|" +
			        "date|daterange|day|deallocate|dec|decimal|declare|default|defaults|deferrable|deferred|" +
			        "definer|delete|delimiter|delimiters|desc|dictionary|disable|discard|distinct|do|document|" +
			        "domain|double|drop|each|else|enable|encoding|encrypted|end|enum|escape|event|event_trigger|" +
			        "except|exclude|excluding|exclusive|execute|exists|explain|extension|external|extract|false|" +
			        "family|fdw_handler|fetch|first|float|float4|float8|following|for|force|foreign|forward|" +
			        "freeze|from|full|function|functions|global|grant|granted|greatest|group|gtsvector|handler|" +
			        "having|header|hold|hour|identity|if|ilike|immediate|immutable|implicit|in|including|" +
			        "increment|index|indexes|inet|inherit|inherits|initially|inline|inner|inout|input|" +
			        "insensitive|insert|instead|int|int2|int2vector|int4|int4range|int8|int8range|integer|" +
			        "internal|intersect|interval|into|invoker|is|isnull|isolation|join|json|key|label|language|" +
			        "language_handler|large|last|lateral|lc_collate|lc_ctype|leading|leakproof|least|left|level|" +
			        "like|limit|line|listen|load|local|localtime|localtimestamp|location|lock|lseg|macaddr|" +
			        "mapping|match|materialized|maxvalue|minute|minvalue|mode|money|month|move|name|names|" +
			        "national|natural|nchar|next|no|none|not|nothing|notify|notnull|nowait|null|nullif|nulls|" +
			        "numeric|numrange|object|of|off|offset|oid|oids|oidvector|on|only|opaque|operator|option|" +
			        "options|or|order|out|outer|over|overlaps|overlay|owned|owner|parser|partial|partition|passing|" +
			        "password|path|pg_attribute|pg_auth_members|pg_authid|pg_class|pg_database|pg_node_tree|" +
			        "pg_proc|pg_type|placing|plans|point|polygon|position|preceding|precision|prepare|prepared|" +
			        "preserve|primary|prior|privileges|procedural|procedure|program|quote|range|read|real|" +
			        "reassign|recheck|record|recursive|ref|refcursor|references|refresh|regclass|regconfig|" +
			        "regdictionary|regoper|regoperator|regproc|regprocedure|regtype|reindex|relative|release|" +
			        "reltime|rename|repeatable|replace|replica|reset|restart|restrict|returning|returns|revoke|" +
			        "right|role|rollback|row|rows|rule|savepoint|schema|scroll|search|second|security|select|" +
			        "sequence|sequences|serializable|server|session|session_user|set|setof|share|show|similar|" +
			        "simple|smallint|smgr|snapshot|some|stable|standalone|start|statement|statistics|stdin|" +
			        "stdout|storage|strict|strip|substring|symmetric|sysid|system|table|tables|tablespace|temp|" +
			        "template|temporary|text|then|tid|time|timestamp|timestamptz|timetz|tinterval|to|trailing|" +
			        "transaction|treat|trigger|trim|true|truncate|trusted|tsquery|tsrange|tstzrange|tsvector|" +
			        "txid_snapshot|type|types|unbounded|uncommitted|unencrypted|union|unique|unknown|unlisten|" +
			        "unlogged|until|update|user|using|uuid|vacuum|valid|validate|validator|value|values|varbit|" +
			        "varchar|variadic|varying|verbose|version|view|void|volatile|when|where|whitespace|window|" +
			        "with|without|work|wrapper|write|xid|xml|xmlattributes|xmlconcat|xmlelement|xmlexists|" +
			        "xmlforest|xmlparse|xmlpi|xmlroot|xmlserialize|year|yes|zone";
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			strKeyword = "abort|abs|action|add|after|all|alter|analyze|and|as|asc|attach|autoincrement|avg|before|begin|between|by|cascade|case|cast|check|collate|column|count|commit|conflict|constraint|create|cross|current_date|current_time|current_timestamp|database|default|deferrable|deferred|delete|desc|detach|distinct|drop|each|else|end|escape|except|exclusive|exists|explain|fail|for|foreign|from|full|glob|group|having|if|ignore|immediate|in|index|indexed|initially|inner|insert|instead|intersect|into|is|isnull|join|key|left|like|limit|lower|match|max|min|natural|no|not|notnull|null|of|offset|on|or|order|outer|plan|pragma|primary|query|raise|random|recursive|references|regexp|reindex|release|rename|replace|restrict|right|rollback|row|savepoint|select|set|sqlite_version|sum|table|temp|temporary|then|to|transaction|trigger|upper|union|unique|update|using|vacuum|values|view|virtual|when|where|with|without";
		// 테이블명이 올바로 표시되지 않는 오류로 sql 확장자로 처리 할수 있도록 수정합니다. 	
		} else if(DBDefine.MSSQL_8_LE_DEFAULT == userDB.getDBDefine() || DBDefine.MSSQL_DEFAULT == userDB.getDBDefine()) {
			strKeyword = "ABSOLUTE|ACTION|ADA|ADD|ADMIN|AFTER|AGGREGATE|ALIAS|ALL|ALLOCATE|ALTER|AND|ANY|ARE|ARRAY|AS|ASC|ASENSITIVE|ASSERTION|ASYMMETRIC|AT|ATOMIC|AUTHORIZATION|BACKUP|BEFORE|BEGIN|BETWEEN|BIT_LENGTH|BLOB|BOOLEAN|BOTH|BREADTH|BREAK|BROWSE|BULK|BY|CALL|CALLED|CARDINALITY|CASCADE|CASCADED|CASE|CATALOG|CHARACTER|CHARACTER_LENGTH|CHAR_LENGTH|CHECK|CHECKPOINT|CLASS|CLOB|CLOSE|CLUSTERED|COALESCE|COLLATE|COLLATION|COLLECT|COLUMN|COMMIT|COMPLETION|COMPUTE|CONDITION|CONNECT|CONNECTION|CONSTRAINT|CONSTRAINTS|CONSTRUCTOR|CONTAINS|CONTAINSTABLE|CONTINUE|CORR|CORRESPONDING|COVAR_POP|COVAR_SAMP|CREATE|CROSS|CUBE|CUME_DIST|CURRENT|CURRENT_CATALOG|CURRENT_DATE|CURRENT_DEFAULT_TRANSFORM_GROUP|CURRENT_PATH|CURRENT_ROLE|CURRENT_SCHEMA|CURRENT_TIME|CURRENT_TRANSFORM_GROUP_FOR_TYPE|CYCLE|DATA|DATABASE|DBCC|DEALLOCATE|DEC|DECLARE|DEFAULT|DEFERRABLE|DEFERRED|DELETE|DENY|DEPTH|DEREF|DESC|DESCRIBE|DESCRIPTOR|DESTROY|DESTRUCTOR|DETERMINISTIC|DIAGNOSTICS|DICTIONARY|DISCONNECT|DISK|DISTINCT|DISTRIBUTED|DOMAIN|DOUBLE|DROP|DUMP|DYNAMIC|EACH|ELEMENT|ELSE|END|END-EXEC|EQUALS|ERRLVL|ESCAPE|EVERY|EXCEPT|EXCEPTION|EXEC|EXECUTE|EXISTS|EXIT|EXTERNAL|EXTRACT|FETCH|FILE|FILLFACTOR|FILTER|FIRST|FOR|FOREIGN|FORTRAN|FOUND|FREE|FREETEXT|FREETEXTTABLE|FROM|FULL|FULLTEXTTABLE|FUNCTION|FUSION|GENERAL|GET|GLOBAL|GO|GOTO|GRANT|GROUP|HAVING|HOLD|HOLDLOCK|HOST|HOUR|IDENTITY|IDENTITYCOL|IDENTITY_INSERT|IF|IGNORE|IMMEDIATE|IN|INCLUDE|INDEX|INDICATOR|INITIALIZE|INITIALLY|INNER|INOUT|INPUT|INSENSITIVE|INSERT|INTEGER|INTERSECT|INTERSECTION|INTERVAL|INTO|IS|ISOLATION|ITERATE|JOIN|KEY|KILL|LANGUAGE|LARGE|LAST|LATERAL|LEADING|LESS|LEVEL|LIKE|LIKE_REGEX|LIMIT|LINENO|LN|LOAD|LOCAL|LOCALTIME|LOCALTIMESTAMP|LOCATOR|MAP|MATCH|MEMBER|MERGE|METHOD|MINUTE|MOD|MODIFIES|MODIFY|MODULE|MULTISET|NAMES|NATIONAL|NATURAL|NCLOB|NEW|NEXT|NO|NOCHECK|NONCLUSTERED|NONE|NORMALIZE|NOT|NULL|NULLIF|OBJECT|OCCURRENCES_REGEX|OCTET_LENGTH|OF|OFF|OFFSETS|OLD|ON|ONLY|OPEN|OPERATION|OPTION|OR|ORDER|ORDINALITY|OUT|OUTER|OUTPUT|OVER|OVERLAPS|OVERLAY|PAD|PARAMETER|PARAMETERS|PARTIAL|PARTITION|PASCAL|PATH|PERCENT|PERCENTILE_CONT|PERCENTILE_DISC|PERCENT_RANK|PIVOT|PLAN|POSITION|POSITION_REGEX|POSTFIX|PRECISION|PREFIX|PREORDER|PREPARE|PRESERVE|PRIMARY|PRINT|PRIOR|PRIVILEGES|PROC|PROCEDURE|PUBLIC|RAISERROR|RANGE|READ|READS|READTEXT|RECONFIGURE|RECURSIVE|REF|REFERENCES|REFERENCING|REGR_AVGX|REGR_AVGY|REGR_COUNT|REGR_INTERCEPT|REGR_R2|REGR_SLOPE|REGR_SXX|REGR_SXY|REGR_SYY|RELATIVE|RELEASE|REPLICATION|RESTORE|RESTRICT|RESULT|RETURN|RETURNS|REVERT|REVOKE|ROLE|ROLLBACK|ROLLUP|ROUTINE|ROW|ROWCOUNT|ROWGUIDCOL|ROWS|RULE|SAVE|SAVEPOINT|SCHEMA|SCOPE|SCROLL|SEARCH|SECOND|SECTION|SECURITYAUDIT|SELECT|SEMANTICKEYPHRASETABLE|SEMANTICSIMILARITYDETAILSTABLE|SEMANTICSIMILARITYTABLE|SENSITIVE|SEQUENCE|SESSION|SET|SETS|SETUSER|SHUTDOWN|SIMILAR|SIZE|SOME|SPECIFIC|SPECIFICTYPE|SQL|SQLCA|SQLCODE|SQLERROR|SQLEXCEPTION|SQLSTATE|SQLWARNING|START|STATE|STATEMENT|STATIC|STATISTICS|STDDEV_POP|STDDEV_SAMP|STRUCTURE|SUBMULTISET|SUBSTRING_REGEX|SYMMETRIC|SYSTEM|TABLESAMPLE|TEMPORARY|TERMINATE|TEXTSIZE|THAN|THEN|TIMEZONE_HOUR|TIMEZONE_MINUTE|TO|TOP|TRAILING|TRAN|TRANSACTION|TRANSLATE|TRANSLATE_REGEX|TRANSLATION|TREAT|TRIGGER|TRIM|TRUNCATE|TSEQUAL|UESCAPE|UNDER|UNION|UNIQUE|UNKNOWN|UNNEST|UNPIVOT|UPDATE|UPDATETEXT|USAGE|USE|USER|USING|VALUE|VALUES|VARIABLE|VARYING|VAR_POP|VAR_SAMP|VIEW|WAITFOR|WHEN|WHENEVER|WHERE|WHILE|WIDTH_BUCKET|WINDOW|WITH|WITHIN|WITHIN GROUP|WITHOUT|WORK|WRITE|WRITETEXT|XMLAGG|XMLATTRIBUTES|XMLBINARY|XMLCAST|XMLCOMMENT|XMLCONCAT|XMLDOCUMENT|XMLELEMENT|XMLEXISTS|XMLFOREST|XMLITERATE|XMLNAMESPACES|XMLPARSE|XMLPI|XMLQUERY|XMLSERIALIZE|XMLTABLE|XMLTEXT|XMLVALIDATE|ZONE" +
		    		"|KEEPIDENTITY|KEEPDEFAULTS|IGNORE_CONSTRAINTS|IGNORE_TRIGGERS|XLOCK|FORCESCAN|FORCESEEK|HOLDLOCK|NOLOCK|NOWAIT|PAGLOCK|READCOMMITTED|READCOMMITTEDLOCK|READPAST|READUNCOMMITTED|REPEATABLEREAD|ROWLOCK|SERIALIZABLE|SNAPSHOT|SPATIAL_WINDOW_MAX_CELLS|TABLOCK|TABLOCKX|UPDLOCK|XLOCK|IGNORE_NONCLUSTERED_COLUMNSTORE_INDEX|EXPAND|VIEWS|FAST|FORCE|KEEP|KEEPFIXED|MAXDOP|MAXRECURSION|OPTIMIZE|PARAMETERIZATION|SIMPLE|FORCED|RECOMPILE|ROBUST|PLAN|SPATIAL_WINDOW_MAX_CELLS|NOEXPAND|HINT" +
		    		"|LOOP|HASH|MERGE|REMOTE|TRY|CATCH|THROW|TYPE";
		} else {
			strKeyword =  "select|insert|update|delete|from|where|and|or|group|by|order|limit|offset|having|as|case|when|else|end|type|left|right|join|on|outer|desc|asc|union";
		}
		return strKeyword;
	}
	
	/**
	 * command
	 * 
	 * @param command
	 */
	public void browserEvaluate(String command) {
		browserEvaluate(command, "");
	}
	
	/** 
	 * browser function call
	 * 
	 *  @param command brower command
	 *  @param args command argument
	 */
	public void browserEvaluate(String command, String ... args) {
		if(logger.isDebugEnabled()) {
			StringBuffer strOption = new StringBuffer();
			for(String arg : args) {
				strOption.append(arg + ", \t");
			}
			logger.debug("\t ### send command is : [command] " + command + ", \n [args]" + strOption.toString());
		}
		
		try {
			browserQueryEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
		} catch(Exception e) {
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", getUserEMail()), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * append text at position
	 * @param strText
	 */
	public void appendTextAtPosition(String strText) {
		try {
			browserEvaluate(EditorFunctionService.INSERT_TEXT, strText);
		} catch(Exception ee){
			logger.error("query text at position" , ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * append text at position
	 * 
	 * @param strText
	 */
	public void appendText(String strText) {
		try {
			if(!StringUtils.endsWith(StringUtils.trimToEmpty(strText), PublicTadpoleDefine.SQL_DELIMITER)) {
				strText += PublicTadpoleDefine.SQL_DELIMITER;
			}
			
			browserEvaluate(EditorFunctionService.APPEND_TEXT, strText);
		} catch(Exception ee){
			logger.error("query text" , ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public String browserEvaluateToStr(String command) {
		return browserEvaluateToStr(command, "");
	}
	
	/**
	 * 
	 * @param command
	 * @param args
	 * @return
	 */
	public String browserEvaluateToStr(String command, String ... args) {
		if(logger.isDebugEnabled()) {
			logger.debug("\t ### send command is : " + command);
		}
		
		try {
			Object ret = browserQueryEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
			if(ret != null) return ret.toString();
		} catch(Exception e) {
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", getUserEMail()), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return "";
	}
	
	/**
	 * orion text setfocus
	 */
	public void setOrionTextFocus() {
		try {
			browserQueryEditor.evaluate(EditorFunctionService.SET_FOCUS);
		} catch(Exception e) {
			// ignore exception
//			logger.error("set focous", e);
		}
	}
	
	public void setUserType(String userRoleType) {
		strRoleType = userRoleType;
	}
	
	/**
	 * 사용자 role_type을 리턴한다.
	 * 
	 * @return
	 */
	public String getUserType() {
		return strRoleType;
	}
	
	/**
	 * register browser function
	 */
	protected abstract void registerBrowserFunctions();
	
	/**
	 * unregister browser function
	 */
	private void unregisterBrowserFunctions() {
		if(editorService != null && editorService instanceof BrowserFunction) {
			editorService.dispose();
		}
	}
	
	@Override
	public void dispose() {
		unregisterBrowserFunctions();
		super.dispose();
	}

}

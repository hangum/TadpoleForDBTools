/*******************************************************************************
 * @license
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (http://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (http://www.eclipse.org/org/documents/edl-v10.html). 
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *               Alex Lakatos - fix for bug#369781
 ******************************************************************************/

/*global document window navigator define */

define("tadpole/textview/textStyler", ['orion/textview/annotations'], function(mAnnotations) {
	
	var MYSQL_KEYWORDS =
		[
		 "alert", "add", "all", "alter", "and", "as", "asc", "auto_increment",
		 "between", "binary", "both", "by", "begin", "before",
		 "change", "check", "column", "columns", "create", "cross", "continue", "cursor",
		 "data", "database", "databases", "default", "delayed", "delete", "desc", "describe", "distinct", "drop",
		 "delimiter", "declare", "deterministic", "deallocate",
		 "enclosed", "escaped", "exists", "explain", "end", "else", "execute", "each",
		 "field", "fields", "flush", "for", "foreign", "from", "function", "fetch",
		 "grant", "group",
		 "having", "handler",
		 "identified", "if", "ignore", "index", "insert", "infile", "into", "in", "is",
		 "join",
		 "key", "keys", "kill",
		 "leading", "left", "like", "limit", "lines", "load", "local", "lock", "low_priority", "leave",
		 "modify",
		 "natural", "not", "null",
		 "on", "optimize", "option", "optionally", "or", "order", "outer", "outfile",
		 "primary", "procedure", "prepare",
		 "read", "references", "regexp", "rename", "replace", "returns", "revoke", "rlike", "return", "row",
		 "select", "set", "show", "soname", "status", "straight_join",
		 "table", "tables", "teminated", "to", "trailing", "then", "trigger",
		 "unique", "unlock", "unsigned", "update", "use", "using",
		 "values", "variables", "view", 
		 "where", "with", "write",
		 "zerofill",
		 "xor",
		 "abs", "acos", "adddate", "ascii", "asin", "atan", "atan2", "avg", "aes_decrypt", "aes_encrypt",
		 "bin", "bit_and", "bit_count", "bit_or",
		 "ceiling", "char_lengh", "character_length", "concat", "conv", "cos", "cot", "count", "curdate",
		 "curtime", "current_time", "current_timestamp", "close",
		 "concat_ws",
		 "date_add", "date_format", "date_sub", "dayname", "dayofmonth", "dayofweek", "dayofyear", "degrees",
		 "elt", "encrypt", "exp",
		 "find_in_set", "floor", "format", "from_days", "from_unixtime",
		 "get_lock", "greatest", "group_concat",
		 "hex", "hour",
		 "ifnull", "instr", "isnull", "interval",
		 "last_insert_id", "lcase", "lower", "least", "length", "locate", "log", "log10", "lpad", "ltrim",
		 "max", "mid", "min", "minute", "mod", "month", "monthname",
		 "now",
		 "oct", "octet_length", "open", "out", 
		 "password", "period_add", "period_diff", "pi", "position", "pow",
		 "quarter",
		 "radians", "rand", "release_lock", "repeat", "reverse", "right", "round", "rpad", "rtrim",
		 "second", "sec_to_time", "session_user", "sign", "sin", "soundex", "space", "sqrt", "strcmp", "substring",
		 "substring_index", "sysdate", "system_user", "std", "sum",
		 "tan", "time_format", "time_to_sec", "to_days", "trim", "truncate",
		 "ucase", "unix_timestamp", "user", "until",
		 "version",
		 "week", "weekday",
		 "year",
		 "bigint", "blob",
		 "char",
		 "date", "datetime", "decimal", "double", "doubleprecision",
		 "enum",
		 "float", "float4", "float8",
		 "int", "int1", "int2", "int3", "int4", "int8", "integer",
		 "long", "longblob", "longtext",
		 "mediumblob", "mediumint", "mediumtext", "middleint",
		 "numeric",
		 "real",
		 "smallint",
		 "text", "time", "timestamp", "tinyint", "tinytext", "tinyblob",
		 "varbinary", "varchar", "varying",
		 "=", "-", "+", "\\", "*", ">", "<", "<=", ">=",
		 "sqlstate",
		 "commit", "rollback"
		 ];

	// 기존 mysql 키워드 중에 auto_increment를 autoincrement로 바꾼것입니다.
	var SQLITE_KEYWORDS = 
		[
		 "alert", "add", "all", "alter", "and", "as", "asc", "autoincrement",
		 "between", "binary", "both", "by", "begin", "before", 
		 "change", "check", "column", "columns", "create", "cross", "continue", "cursor",
		 "data", "database", "databases", "default", "delayed", "delete", "desc", "describe", "distinct", "drop",
		 "delimiter", "declare", "deterministic", "deallocate",
		 "enclosed", "escaped", "exists", "explain", "end", "else", "execute", "each", 
		 "field", "fields", "flush", "for", "foreign", "from", "function", "fetch",
		 "grant", "group",
		 "having", "handler",
		 "identified", "if", "ignore", "index", "insert", "infile", "into", "in", "is",
		 "join",
		 "key", "keys", "kill",
		 "leading", "left", "like", "limit", "lines", "load", "local", "lock", "low_priority", "leave",
		 "modify",
		 "natural", "not", "null",
		 "on", "optimize", "option", "optionally", "or", "order", "outer", "outfile",
		 "primary", "procedure", "prepare",
		 "read", "references", "regexp", "rename", "replace", "returns", "revoke", "rlike", "return", "row", 
		 "select", "set", "show", "soname", "status", "straight_join",
		 "table", "tables", "teminated", "to", "trailing", "then", "trigger", 
		 "unique", "unlock", "unsigned", "update", "use", "using",
		 "values", "variables", "view",
		 "where", "with", "write",
		 "zerofill",
		 "xor",
		 "abs", "acos", "adddate", "ascii", "asin", "atan", "atan2", "avg", "aes_decrypt", "aes_encrypt",
		 "bin", "bit_and", "bit_count", "bit_or",
		 "ceiling", "char_lengh", "character_length", "concat", "conv", "cos", "cot", "count", "curdate",
		 "curtime", "current_time", "current_timestamp", "close",
		 "concat_ws",
		 "date_add", "date_format", "date_sub", "dayname", "dayofmonth", "dayofweek", "dayofyear", "degrees",
		 "elt", "encrypt", "exp",
		 "find_in_set", "floor", "format", "from_days", "from_unixtime",
		 "get_lock", "greatest", "group_concat",
		 "hex", "hour",
		 "ifnull", "instr", "isnull", "interval",
		 "last_insert_id", "lcase", "lower", "least", "length", "locate", "log", "log10", "lpad", "ltrim",
		 "max", "mid", "min", "minute", "mod", "month", "monthname",
		 "now",
		 "oct", "octet_length", "open",
		 "password", "period_add", "period_diff", "pi", "position", "pow",
		 "quarter",
		 "radians", "rand", "release_lock", "repeat", "reverse", "right", "round", "rpad", "rtrim",
		 "second", "sec_to_time", "session_user", "sign", "sin", "soundex", "space", "sqrt", "strcmp", "substring",
		 "substring_index", "sysdate", "system_user", "std", "sum",
		 "tan", "time_format", "time_to_sec", "to_days", "trim", "truncate",
		 "ucase", "unix_timestamp", "user", "until",
		 "version",
		 "week", "weekday",
		 "year",
		 "bigint", "blob",
		 "char",
		 "date", "datetime", "decimal", "double", "doubleprecision",
		 "enum",
		 "float", "float4", "float8",
		 "int", "int1", "int2", "int3", "int4", "int8", "integer",
		 "long", "longblob", "longtext",
		 "mediumblob", "mediumint", "mediumtext", "middleint",
		 "numeric",
		 "real",
		 "smallint",
		 "text", "time", "timestamp", "tinyint", "tinytext", "tinyblob",
		 "varbinary", "varchar", "varying",
		 "=", "-", "+", "\\", "*", ">", "<", "<=", ">=",
		 "sqlstate"	,
		 "commit", "rollback"	 
		 ];

	var ORACLE_KEYWORDS =
		["abort", "accept", "access", "add", "all", "alter", "and", "any", "array", "arraylen", "as", "asc", "assert", "assign", "at", "attributes", "audit",
		 "authorization", "avg", 
		 "base_table", "begin", "between", "binary_integer", "body", "boolean", "by", "before",  
		 "case", "cast", "char", "char_base", "check", "close", "cluster", "clusters", "colauth", "column", "comment", "commit", "compress", "connect", 
		 "connected", "constant", "constraint", "crash", "create", "current", "currval", "cursor", 
		 "data_base", "database", "date", "dba", "deallocate", "debugoff", "debugon", "decimal", "declare", "default", "definition", "delay", "delete", 
		 "desc", "digits", "dispose", "distinct", "do", "drop", 
		 "else", "elsif", "enable", "end", "entry", "escape", "exception", "exception_init", "exchange", "exclusive", "exists", "exit", "external", "each",  
		 "fast", "fetch", "file", "for", "force", "form", "from", "function", 
		 "generic", "goto", "grant", "group", 
		 "having", 
		 "identified", "if", "immediate", "in", "increment", "index", "indexes", "indicator", "initial", "initrans", "insert", "interface", "intersect", 
		 "into", "is", 
		 "key", 
		 "level", "library", "like", "limited", "local", "lock", "log", "logging", "long", "loop", 
		 "master", "maxextents", "maxtrans", "member", "minextents", "minus", "mislabel", "mode", "modify", "multiset", 
		 "new", "next", "no", "noaudit", "nocompress", "nologging", "noparallel", "not", "nowait", "number_base", 
		 "object", "of", "off", "offline", "on", "online", "only", "open", "option", "or", "order", "out", 
		 "package", "parallel", "partition", "pctfree", "pctincrease", "pctused", "pls_integer", "positive", "positiven", "pragma", "primary", "prior", 
		 "private", "privileges", "procedure", "public", 
		 "raise", "range", "raw", "read", "rebuild", "record", "ref", "references", "refresh", "release", "rename", "replace", "resource", "restrict", "return", "returns",  
		 "returning", "reverse", "revoke", "rollback", "row", "rowid", "rowlabel", "rownum", "rows", "run", 
		 "savepoint", "schema", "segment", "select", "separate", "session", "set", "share", "snapshot", "some", "space", "split", "sql", "start", "statement", 
		 "storage", "subtype", "successful", "synonym", 
		 "tabauth", "table", "tables", "tablespace", "task", "terminate", "then", "to", "trigger", "truncate", "type", 
		 "union", "unique", "unlimited", "unrecoverable", "unusable", "update", "use", "using", 
		 "validate", "value", "values", "variable", "view", "views", 
		 "when", "whenever", "where", "while", "with", "work",
		 "abs", "acos", "add_months", "ascii", "asin", "atan", "atan2", "average", 
		 "bfilename", 
		 "ceil", "chartorowid", "chr", "concat", "convert", "cos", "cosh", "count", 
		 "decode", "deref", "dual", "dump", "dup_val_on_index", 
		 "empty", "error", "exp", 
		 "false", "floor", "found", 
		 "glb", "greatest", 
		 "hextoraw", 
		 "initcap", "instr", "instrb", "isopen", 
		 "last_day", "least", "lenght", "lenghtb", "ln", "lower", "lpad", "ltrim", "lub", 
		 "make_ref", "max", "min", "mod", "months_between", 
		 "new_time", "next_day", "nextval", "nls_charset_decl_len", "nls_charset_id", "nls_charset_name", "nls_initcap", "nls_lower", 
		 "nls_sort", "nls_upper", "nlssort", "no_data_found", "notfound", "null", "nvl", 
		 "others", 
		 "power", 
		 "rawtohex", "reftohex", "round", "rowcount", "rowidtochar", "rpad", "rtrim", 
		 "sign", "sin", "sinh", "soundex", "sqlcode", "sqlerrm", "sqrt", "stddev", "substr", "substrb", "sum", "sysdate", 
		 "tan", "tanh", "to_char", "to_date", "to_label", "to_multi_byte", "to_number", "to_single_byte", "translate", "true", "trunc", 
		 "uid", "upper", "user", "userenv", 
		 "variance", "vsize", 
		 "bfile", "blob", 
		 "character", "clob", 
		 "dec", 
		 "float", 
		 "int", "integer", 
		 "mlslabel", 
		 "natural", "naturaln", "nchar", "nclob", "number", "numeric", "nvarchar2", 
		 "real", "rowtype", 
		 "signtype", "smallint", "string", 
		 "varchar", "varchar2",
		 "commit", "rollback"
		 ];
		 
	var MSSQL_KEYWORDS =
		["add", "all", "alter", "and", "any", "as", "asc", "authorization", "avg", "auto_increment",
		"backup", "begin", "between", "break", "browse", "bulk", "by", "before", 
		"cascade", "case", "check", "checkpoint", "close", "clustered", "coalesce", "column", "commit", "committed", "compute", "confirm", "concat", 
		"constraint", "contains", "containstable", "continue", "controlrow", "convert", "count", "create", "cross", "current", "current_date", "current_time", "current_timestamp", "current_user", "cursor",
		"database", "dbcc", "deallocate", "declare", "default", "delete", "deny", "desc", "disk", "distinct", "distributed", "double", "drop", "dummy", "dump",
		"else", "end", "errlvl", "errorexit", "escape", "except", "exec", "execute", "exists", "exit", "each", 
		"fetch", "file", "fillfactor", "floppy", "for", "foreign", "freetext", "freetexttable", "from", "full", "function", 
		"go", "goto", "grant", "group",
		"having", "holdlock",
		"identity", "identity_insert", "identitycol", "if", "in", "index", "inner", "insert", "intersect", "into", "is", "isolation",
		"join",
		"key", "kill",
		"left", "level", "like", "lineno", "load",
		"max", "min", "mirrorexit",
		"national", "", "nocheck", "nonclustered", "not", "null", "nullif",
		"of", "off", "offsets", "on", "once", "only", "open", "opendatasource", "openquery", "openrowset", "option", "or", "order", "outer", "over", "out",
		"percent", "perm", "permanent", "pipe", "plan", "precision", "prepare", "primary", "print", "privileges", "proc", "procedure", "processexit", "public",
		"raiserror", "read", "readtext", "reconfigure", "references", "repeatable", "replication", "restore", "restrict", "return", "revoke", "right", "rollback", "rowcount", "rowguidcol", "rule", "returns", "row",  
		"save", "schema", "select", "serializable", "session_user", "set", "setuser", "shutdown", "some", "statistics", "sum", "system_user",
		"table", "tape", "temp", "temporary", "textsize", "then", "to", "top", "tran", "transaction", "trigger", "truncate", "tsequal",
		"uncommitted", "union", "unique", "update", "updatetext", "use", "user",
		"values", "varying", "view",
		"waitfor", "when", "where", "while", "with", "work", "writetext",
		"binary", "bit",
		"char", "character",
		"datetime", "dec", "decimal",
		"float",
		"image", "int", "integer",
		"money",
		"nchar", "ntext", "numeric", "nvarchar",
		"real",
		"smalldatetime", "smallint", "smallmoney",
		"text", "timestamp", "tinyint",
		"uniqueidentifier",
		"varbinary", "varchar", 
		"sp_abort_xact", "sp_add_agent_parameter", "sp_add_agent_profile", "sp_add_server_sortinfo", "sp_addalias", "sp_addapprole", "sp_addarticle", "sp_adddistpublisher", "sp_adddistributiondb", 
		"sp_adddistributor", "sp_addextendedproc", "sp_addgroup", "sp_addlinkedserver", "sp_addlinkedsrvlogin", "sp_addlogin", "sp_addmergearticle", "sp_addmergefilter", "sp_addmergepublication", 
		"sp_addmergepullsubscription", "sp_addmergepullsubscription_agent", "sp_addmergesubscription", "sp_addmessage", "sp_addpublication", "sp_addpublication_snapshot", "sp_addpublisher", 
		"sp_addpullsubscription", "sp_addpullsubscription_agent", "sp_addremotelogin", "sp_addrole", "sp_addrolemember", "sp_addserver", "sp_addsrvrolemember", "sp_addsubscriber", "sp_addsubscriber_schedule", 
		"sp_addsubscription", "sp_addsynctriggers", "sp_addtype", "sp_addumpdevice", "sp_adduser", "sp_altermessage", "sp_approlepassword", "sp_article_validation", "sp_articlecolumn", "sp_articlefilter", 
		"sp_articlesynctranprocs", "sp_articleview", "sp_attach_db", "sp_attach_single_file_db", "sp_autostats", "sp_bindefault", "sp_bindrule", "sp_bindsession", "sp_blockcnt", "sp_catalogs", "sp_catalogs_rowset", 
		"sp_certify_removable", "sp_change_subscription_properties", "sp_change_users_login", "sp_changearticle", "sp_changedbowner", "sp_changedistpublisher", "sp_changedistributiondb", 
		"sp_changedistributor_password", "sp_changedistributor_property", "sp_changegroup", "sp_changemergearticle", "sp_changemergefilter", "sp_changemergepublication", "sp_changemergepullsubscription", 
		"sp_changemergesubscription", "sp_changeobjectowner", "sp_changepublication", "sp_changesubscriber", "sp_changesubscriber_schedule", "sp_changesubscription", "sp_changesubstatus", 
		"sp_check_for_sync_trigger", "sp_check_removable", "sp_check_removable_sysusers", "sp_check_sync_trigger", "sp_checknames", "sp_cleanupwebtask", "sp_column_privileges", "sp_column_privileges_ex", 
		"sp_column_privileges_rowset", "sp_columns", "sp_columns_ex", "sp_columns_rowset", "sp_commit_xact", "sp_configure", "sp_create_removable", "sp_createorphan", "sp_createstats", "sp_cursor", "sp_cursor_list", 
		"sp_cursorclose", "sp_cursorexecute", "sp_cursorfetch", "sp_cursoropen", "sp_cursoroption", "sp_cursorprepare", "sp_cursorunprepare", "sp_databases", "sp_datatype_info", "sp_db_upgrade", "sp_dbcmptlevel", 
		"sp_dbfixedrolepermission", "sp_dboption", "sp_dbremove", "sp_ddopen", "sp_defaultdb", "sp_defaultlanguage", "sp_deletemergeconflictrow", "sp_denylogin", "sp_depends", "sp_describe_cursor", 
		"sp_describe_cursor_columns", "sp_describe_cursor_tables", "sp_detach_db", "sp_diskdefault", "sp_distcounters", "sp_drop_agent_parameter", "sp_drop_agent_profile", "sp_dropalias", "sp_dropapprole", 
		"sp_droparticle", "sp_dropdevice", "sp_dropdistpublisher", "sp_dropdistributiondb", "sp_dropdistributor", "sp_dropextendedproc", "sp_dropgroup", "sp_droplinkedsrvlogin", "sp_droplogin", 
		"sp_dropmergearticle", "sp_dropmergefilter", "sp_dropmergepublication", "sp_dropmergepullsubscription", "sp_dropmergesubscription", "sp_dropmessage", "sp_droporphans", "sp_droppublication", 
		"sp_droppublisher", "sp_droppullsubscription", "sp_dropremotelogin", "sp_droprole", "sp_droprolemember", "sp_dropserver", "sp_dropsrvrolemember", "sp_dropsubscriber", "sp_dropsubscription", 
		"sp_droptype", "sp_dropuser", "sp_dropwebtask", "sp_dsninfo", "sp_enumcodepages", "sp_enumcustomresolvers", "sp_enumdsn", "sp_enumfullsubscribers", "sp_enumoledbdatasources", "sp_execute", "sp_executesql", 
		"sp_fallback_MS_sel_fb_svr", "sp_fetchshowcmdsinput", "sp_fixindex", "sp_fkeys", "sp_foreign_keys_rowset", "sp_foreignkeys", "sp_fulltext_catalog", "sp_fulltext_column", "sp_fulltext_database", 
		"sp_fulltext_getdata", "sp_fulltext_service", "sp_fulltext_table", "sp_generatefilters", "sp_get_distributor", "sp_getarticlepkcolbitmap", "sp_getbindtoken", "sp_GetMBCSCharLen", 
		"sp_getmergedeletetype", "sp_gettypestring", "sp_grant_publication_access", "sp_grantdbaccess", "sp_grantlogin", "sp_help", "sp_help_agent_default", "sp_help_agent_parameter", "sp_help_agent_profile", 
		"sp_help_fulltext_catalogs", "sp_help_fulltext_catalogs_cursor", "sp_help_fulltext_columns", "sp_help_fulltext_columns_cursor", "sp_help_fulltext_tables", "sp_help_fulltext_tables_cursor", 
		"sp_help_publication_access", "sp_helpallowmerge_publication", "sp_helparticle", "sp_helparticlecolumns", "sp_helpconstraint", "sp_helpdb", "sp_helpdbfixedrole", "sp_helpdevice", "sp_helpdistpublisher", 
		"sp_helpdistributiondb", "sp_helpdistributor", "sp_helpdistributor_properties", "sp_helpextendedproc", "sp_helpfile", "sp_helpfilegroup", "sp_helpgroup", "sp_helpindex", "sp_helplanguage", "sp_helplog", 
		"sp_helplogins", "sp_helpmergearticle", "sp_helpmergearticleconflicts", "sp_helpmergeconflictrows", "sp_helpmergedeleteconflictrows", "sp_helpmergefilter", "sp_helpmergepublication", 
		"sp_helpmergepullsubscription", "sp_helpmergesubscription", "sp_helpntgroup", "sp_helppublication", "sp_helppublication_snapshot", "sp_helppublicationsync", "sp_helppullsubscription", 
		"sp_helpremotelogin", "sp_helpreplicationdb", "sp_helpreplicationdboption", "sp_helpreplicationoption", "sp_helprole", "sp_helprolemember", "sp_helprotect", "sp_helpserver", "sp_helpsort", "sp_helpsql", 
		"sp_helpsrvrole", "sp_helpsrvrolemember", "sp_helpstartup", "sp_helpsubscriber", "sp_helpsubscriberinfo", "sp_helpsubscription", "sp_helpsubscription_properties", "sp_helptext", "sp_helptrigger", 
		"sp_helpuser", "sp_indexes", "sp_indexes_rowset", "sp_indexoption", "sp_isarticlecolbitset", "sp_IsMBCSLeadByte", "sp_link_publication", "sp_linkedservers", "sp_linkedservers_rowset", "sp_lock", 
		"sp_lockinfo", "sp_logdevice", "sp_makestartup", "sp_makewebtask", "sp_mergedummyupdate", "sp_mergesubscription_cleanup", "sp_mergesubscriptioncleanup", "sp_monitor", "sp_MS_marksystemobject", 
		"sp_MS_replication_installed", "sp_MS_upd_sysobj_category", "sp_MSactivate_auto_sub", "sp_MSadd_distributor_alerts_and_responses", "sp_MSadd_mergereplcommand", "sp_MSadd_repl_job", 
		"sp_MSaddanonymousreplica", "sp_MSaddarticletocontents", "sp_MSaddexecarticle", "sp_MSaddguidcolumn", "sp_MSaddguidindex", "sp_MSaddinitialarticle", "sp_MSaddinitialpublication", 
		"sp_MSaddinitialsubscription", "sp_MSaddlogin_implicit_ntlogin", "sp_MSaddmergepub_snapshot", "sp_MSaddmergetriggers", "sp_MSaddpub_snapshot", "sp_MSaddpubtocontents", "sp_MSaddupdatetrigger", 
		"sp_MSadduser_implicit_ntlogin", "sp_MSarticlecleanup", "sp_MSarticletextcol", "sp_MSbelongs", "sp_MSchange_priority", "sp_MSchangearticleresolver", "sp_MScheck_agent_instance", 
		"sp_MScheck_uid_owns_anything", "sp_MScheckatpublisher", "sp_MScheckexistsgeneration", "sp_MScheckmetadatamatch", "sp_MScleanup_subscription", "sp_MScleanuptask", "sp_MScontractsubsnb", 
		"sp_MScreate_dist_tables", "sp_MScreate_distributor_tables", "sp_MScreate_mergesystables", "sp_MScreate_pub_tables", "sp_MScreate_replication_checkup_agent", 
		"sp_MScreate_replication_status_table", "sp_MScreate_sub_tables", "sp_MScreateglobalreplica", "sp_MScreateretry", "sp_MSdbuseraccess", "sp_MSdbuserpriv", "sp_MSdeletecontents", 
		"sp_MSdeletepushagent", "sp_MSdeleteretry", "sp_MSdelrow", "sp_MSdelsubrows", "sp_MSdependencies", "sp_MSdoesfilterhaveparent", "sp_MSdrop_6x_replication_agent", 
		"sp_MSdrop_distributor_alerts_and_responses", "sp_MSdrop_mergesystables", "sp_MSdrop_object", "sp_MSdrop_pub_tables", "sp_MSdrop_replcom", "sp_MSdrop_repltran", "sp_MSdrop_rladmin", 
		"sp_MSdrop_rlcore", "sp_MSdrop_rlrecon", "sp_MSdroparticleprocs", "sp_MSdroparticletombstones", "sp_MSdroparticletriggers", "sp_MSdropconstraints", "sp_MSdropmergepub_snapshot", "sp_MSdropretry", 
		"sp_MSdummyupdate", "sp_MSenum_replication_agents", "sp_MSenum_replication_job", "sp_MSenum3rdpartypublications", "sp_MSenumallpublications", "sp_MSenumchanges", "sp_MSenumcolumns", 
		"sp_MSenumdeletesmetadata", "sp_MSenumgenerations", "sp_MSenummergepublications", "sp_MSenumpartialchanges", "sp_MSenumpartialdeletes", "sp_MSenumpubreferences", "sp_MSenumreplicas", 
		"sp_MSenumretries", "sp_MSenumschemachange", "sp_MSenumtranpublications", "sp_MSexists_file", "sp_MSexpandbelongs", "sp_MSexpandnotbelongs", "sp_MSexpandsubsnb", "sp_MSfilterclause", 
		"sp_MSflush_access_cache", "sp_MSflush_command", "sp_MSforeach_worker", "sp_MSforeachdb", "sp_MSforeachtable", "sp_MSgen_sync_tran_procs", "sp_MSgenreplnickname", "sp_MSgentablenickname", 
		"sp_MSget_col_position", "sp_MSget_colinfo", "sp_MSget_oledbinfo", "sp_MSget_publisher_rpc", "sp_MSget_qualifed_name", "sp_MSget_synctran_commands", "sp_MSget_type", "sp_MSgetalertinfo", 
		"sp_MSgetchangecount", "sp_MSgetconflictinsertproc", "sp_MSgetlastrecgen", "sp_MSgetlastsentgen", "sp_MSgetonerow", "sp_MSgetreplicainfo", "sp_MSgetreplnick", "sp_MSgetrowmetadata", "sp_MSguidtostr", 
		"sp_MShelp_distdb", "sp_MShelp_replication_status", "sp_MShelpcolumns", "sp_MShelpfulltextindex", "sp_MShelpindex", "sp_MShelpmergearticles", "sp_MShelpobjectpublications", "sp_MShelptype", 
		"sp_MSIfExistsRemoteLogin", "sp_MSindexcolfrombin", "sp_MSindexspace", "sp_MSinit_replication_perfmon", "sp_MSinsertcontents", "sp_MSinsertdeleteconflict", "sp_MSinsertgenhistory", 
		"sp_MSinsertschemachange", "sp_MSis_col_replicated", "sp_MSis_pk_col", "sp_MSkilldb", "sp_MSload_replication_status", "sp_MSlocktable", "sp_MSloginmappings", "sp_MSmakearticleprocs", 
		"sp_MSmakeconflictinsertproc", "sp_MSmakeexpandproc", "sp_MSmakegeneration", "sp_MSmakeinsertproc", "sp_MSmakejoinfilter", "sp_MSmakeselectproc", "sp_MSmakesystableviews", "sp_MSmaketempinsertproc", 
		"sp_MSmakeupdateproc", "sp_MSmakeviewproc", "sp_MSmaptype", "sp_MSmark_proc_norepl", "sp_MSmatchkey", "sp_MSmergepublishdb", "sp_MSmergesubscribedb", "sp_MSobjectprivs", "sp_MSpad_command", 
		"sp_MSproxiedmetadata", "sp_MSpublicationcleanup", "sp_MSpublicationview", "sp_MSpublishdb", "sp_MSrefcnt", "sp_MSregistersubscription", "sp_MSreinit_failed_subscriptions", "sp_MSrepl_addrolemember", 
		"sp_MSrepl_dbrole", "sp_MSrepl_droprolemember", "sp_MSrepl_encrypt", "sp_MSrepl_linkedservers_rowset", "sp_MSrepl_startup", "sp_MSreplcheck_connection", "sp_MSreplcheck_publish", 
		"sp_MSreplcheck_pull", "sp_MSreplcheck_subscribe", "sp_MSreplicationcompatlevel", "sp_MSreplrole", "sp_MSreplsup_table_has_pk", "sp_MSscript_beginproc", "sp_MSscript_begintrig1", 
		"sp_MSscript_begintrig2", "sp_MSscript_delete_statement", "sp_MSscript_dri", "sp_MSscript_endproc", "sp_MSscript_endtrig", "sp_MSscript_insert_statement", "sp_MSscript_multirow_trigger", 
		"sp_MSscript_params", "sp_MSscript_security", "sp_MSscript_singlerow_trigger", "sp_MSscript_sync_del_proc", "sp_MSscript_sync_del_trig", "sp_MSscript_sync_ins_proc", "sp_MSscript_sync_ins_trig", 
		"sp_MSscript_sync_upd_proc", "sp_MSscript_sync_upd_trig", "sp_MSscript_trigger_assignment", "sp_MSscript_trigger_exec_rpc", "sp_MSscript_trigger_fetch_statement", 
		"sp_MSscript_trigger_update_checks", "sp_MSscript_trigger_updates", "sp_MSscript_trigger_variables", "sp_MSscript_update_statement", "sp_MSscript_where_clause", "sp_MSscriptdatabase", 
		"sp_MSscriptdb_worker", "sp_MSsetaccesslist", "sp_MSsetalertinfo", "sp_MSsetartprocs", "sp_MSsetbit", "sp_MSsetconflictscript", "sp_MSsetconflicttable", "sp_MSsetfilteredstatus", 
		"sp_MSsetfilterparent", "sp_MSsetlastrecgen", "sp_MSsetlastsentgen", "sp_MSsetreplicainfo", "sp_MSsetreplicastatus", "sp_MSsetrowmetadata", "sp_MSsettopology", "sp_MSsetupbelongs", 
		"sp_MSSQLDMO70_version", "sp_MSSQLOLE_version", "sp_MSSQLOLE65_version", "sp_MSsubscribedb", "sp_MSsubscriptions", "sp_MSsubscriptionvalidated", "sp_MSsubsetpublication", 
		"sp_MStable_has_unique_index", "sp_MStable_not_modifiable", "sp_MStablechecks", "sp_MStablekeys", "sp_MStablenamefromnick", "sp_MStablenickname", "sp_MStablerefs", "sp_MStablespace", 
		"sp_MStestbit", "sp_MStextcolstatus", "sp_MSunc_to_drive", "sp_MSuniquecolname", "sp_MSuniquename", "sp_MSuniqueobjectname", "sp_MSuniquetempname", "sp_MSunmarkreplinfo", 
		"sp_MSunregistersubscription", "sp_MSupdate_agenttype_default", "sp_MSupdate_replication_status", "sp_MSupdatecontents", "sp_MSupdategenhistory", "sp_MSupdateschemachange", 
		"sp_MSupdatesysmergearticles", "sp_msupg_createcatalogcomputedcols", "sp_msupg_dosystabcatalogupgrades", "sp_msupg_dropcatalogcomputedcols", "sp_msupg_recreatecatalogfaketables", 
		"sp_msupg_recreatesystemviews", "sp_msupg_removesystemcomputedcolumns", "sp_msupg_upgradecatalog", "sp_MSuplineageversion", "sp_MSvalidatearticle", "sp_OACreate", "sp_OADestroy", 
		"sp_OAGetErrorInfo", "sp_OAGetProperty", "sp_OAMethod", "sp_OASetProperty", "sp_OAStop", "sp_objectfilegroup", "sp_oledbinfo", "sp_password", "sp_pkeys", "sp_prepare", "sp_primary_keys_rowset", 
		"sp_primarykeys", "sp_probe_xact", "sp_procedure_params_rowset", "sp_procedures_rowset", "sp_processinfo", "sp_processmail", "sp_procoption", "sp_provider_types_rowset", "sp_publication_validation", 
		"sp_publishdb", "sp_recompile", "sp_refreshsubscriptions", "sp_refreshview", "sp_reinitmergepullsubscription", "sp_reinitmergesubscription", "sp_reinitpullsubscription", "sp_reinitsubscription", 
		"sp_remoteoption", "sp_remove_tempdb_file", "sp_remove_xact", "sp_removedbreplication", "sp_removesrvreplication", "sp_rename", "sp_renamedb", "sp_replcmds", "sp_replcounters", "sp_repldone", "sp_replflush", 
		"sp_replica", "sp_replication_agent_checkup", "sp_replicationdboption", "sp_replicationoption", "sp_replincrementlsn", "sp_replpostcmd", "sp_replsetoriginator", "sp_replshowcmds", "sp_replsync", 
		"sp_repltrans", "sp_replupdateschema", "sp_reset_connection", "sp_revoke_publication_access", "sp_revokedbaccess", "sp_revokelogin", "sp_runwebtask", "sp_scan_xact", "sp_schemata_rowset", 
		"sp_script_synctran_commands", "sp_scriptdelproc", "sp_scriptinsproc", "sp_scriptmappedupdproc", "sp_scriptpkwhereclause", "sp_scriptupdateparams", "sp_scriptupdproc", "sp_sdidebug", 
		"sp_sem_start_mail", "sp_server_info", "sp_serveroption", "sp_setapprole", "sp_setnetname", "sp_spaceused", "sp_special_columns", "sp_sproc_columns", "sp_sqlexec", "sp_sqlregister", "sp_srvrolepermission", 
		"sp_start_xact", "sp_stat_xact", "sp_statistics", "sp_statistics_rowset", "sp_stored_procedures", "sp_subscribe", "sp_subscription_cleanup", "sp_subscriptioncleanup", "sp_table_privileges", 
		"sp_table_privileges_ex", "sp_table_privileges_rowset", "sp_table_validation", "sp_tableoption", "sp_tables", "sp_tables_ex", "sp_tables_info_rowset", "sp_tables_rowset", "sp_tempdbspace", 
		"sp_unbindefault", "sp_unbindrule", "sp_unmakestartup", "sp_unprepare", "sp_unsubscribe", "sp_updatestats", "sp_user_counter1", "sp_user_counter10", "sp_user_counter2", "sp_user_counter3", 
		"sp_user_counter4", "sp_user_counter5", "sp_user_counter6", "sp_user_counter7", "sp_user_counter8", "sp_user_counter9", "sp_validatelogins", "sp_validlang", "sp_validname", "sp_who", "sp_who2", 
		"spt_committab", "spt_datatype_info", "spt_datatype_info_ext", "spt_fallback_db", "spt_fallback_dev", "spt_fallback_usg", "spt_monitor", "spt_provider_types", "spt_server_info", "spt_values", 
		"sysallocations", "sysalternates", "sysaltfiles", "syscacheobjects", "syscharsets", "syscolumns", "syscomments", "sysconfigures", "sysconstraints", "syscurconfigs", "syscursorcolumns", "syscursorrefs", 
		"syscursors", "syscursortables", "sysdatabases", "sysdepends", "sysdevices", "sysfilegroups", "sysfiles", "sysfiles1", "sysforeignkeys", "sysfulltextcatalogs", "sysindexes", "sysindexkeys", "syslanguages", 
		"syslockinfo", "syslocks", "syslogins", "sysmembers", "sysmessages", "sysobjects", "sysoledbusers", "sysperfinfo", "syspermissions", "sysprocesses", "sysprotects", "sysreferences", "SYSREMOTE_CATALOGS", 
		"SYSREMOTE_COLUMN_PRIVILEGES", "SYSREMOTE_COLUMNS", "SYSREMOTE_FOREIGN_KEYS", "SYSREMOTE_INDEXES", "SYSREMOTE_PRIMARY_KEYS", "SYSREMOTE_PROVIDER_TYPES", "SYSREMOTE_SCHEMATA", "SYSREMOTE_STATISTICS", 
		"SYSREMOTE_TABLE_PRIVILEGES", "SYSREMOTE_TABLES", "SYSREMOTE_VIEWS", "sysremotelogins", "syssegments", "sysservers", "systypes", "sysusers", "sysxlogins",
		"xp_availablemedia", "xp_check_query_results", "xp_cleanupwebtask", "xp_cmdshell", "xp_deletemail", "xp_dirtree", "xp_displayparamstmt", "xp_dropwebtask", "xp_dsninfo", "xp_enum_activescriptengines", 
		"xp_enum_oledb_providers", "xp_enumcodepages", "xp_enumdsn", "xp_enumerrorlogs", "xp_enumgroups", "xp_eventlog", "xp_execresultset", "xp_fileexist", "xp_findnextmsg", "xp_fixeddrives", 
		"xp_get_mapi_default_profile", "xp_get_mapi_profiles", "xp_get_tape_devices", "xp_getfiledetails", "xp_getnetname", "xp_grantlogin", "xp_initcolvs", "xp_intersectbitmaps", "xp_load_dummy_handlers", 
		"xp_logevent", "xp_loginconfig", "xp_logininfo", "xp_makewebtask", "xp_mergexpusage", "xp_msver", "xp_msx_enlist", "xp_ntsec_enumdomains", "xp_ntsec_enumgroups", "xp_ntsec_enumusers", "xp_oledbinfo", 
		"xp_param_dump", "xp_perfend", "xp_perfmonitor", "xp_perfsample", "xp_perfstart", "xp_printstatements", "xp_proxiedmetadata", "xp_qv", "xp_readerrorlog", "xp_readmail", "xp_regaddmultistring", "xp_regdeletekey", 
		"xp_regdeletevalue", "xp_regenumvalues", "xp_regread", "xp_regremovemultistring", "xp_regwrite", "xp_revokelogin", "xp_runwebtask", "xp_sendmail", "xp_servicecontrol", "xp_showcolv", "xp_showlineage", 
		"xp_snmp_getstate", "xp_snmp_raisetrap", "xp_sprintf", "xp_sqlagent_enum_jobs", "xp_sqlagent_is_starting", "xp_sqlagent_monitor", "xp_sqlagent_notify", "xp_sqlinventory", "xp_sqlmaint", "xp_sqlregister", 
		"xp_sqltrace", "xp_sscanf", "xp_startmail", "xp_stopmail", "xp_subdirs", "xp_terminate_process", "xp_test_mapi_profile", "xp_trace_addnewqueue", "xp_trace_deletequeuedefinition", "xp_trace_destroyqueue", 
		"xp_trace_enumqueuedefname", "xp_trace_enumqueuehandles", "xp_trace_eventclassrequired", "xp_trace_flushqueryhistory", "xp_trace_generate_event", "xp_trace_getappfilter", 
		"xp_trace_getconnectionidfilter", "xp_trace_getcpufilter", "xp_trace_getdbidfilter", "xp_trace_getdurationfilter", "xp_trace_geteventfilter", "xp_trace_geteventnames", "xp_trace_getevents", 
		"xp_trace_gethostfilter", "xp_trace_gethpidfilter", "xp_trace_getindidfilter", "xp_trace_getntdmfilter", "xp_trace_getntnmfilter", "xp_trace_getobjidfilter", "xp_trace_getqueueautostart", 
		"xp_trace_getqueuecreateinfo", "xp_trace_getqueuedestination", "xp_trace_getqueueproperties", "xp_trace_getreadfilter", "xp_trace_getserverfilter", "xp_trace_getseverityfilter", 
		"xp_trace_getspidfilter", "xp_trace_gettextfilter", "xp_trace_getuserfilter", "xp_trace_getwritefilter", "xp_trace_loadqueuedefinition", "xp_trace_opentracefile", "xp_trace_pausequeue", 
		"xp_trace_restartqueue", "xp_trace_savequeuedefinition", "xp_trace_setappfilter", "xp_trace_setconnectionidfilter", "xp_trace_setcpufilter", "xp_trace_setdbidfilter", "xp_trace_setdurationfilter", 
		"xp_trace_seteventclassrequired", "xp_trace_seteventfilter", "xp_trace_sethostfilter", "xp_trace_sethpidfilter", "xp_trace_setindidfilter", "xp_trace_setntdmfilter", "xp_trace_setntnmfilter", 
		"xp_trace_setobjidfilter", "xp_trace_setqueryhistory", "xp_trace_setqueueautostart", "xp_trace_setqueuecreateinfo", "xp_trace_setqueuedestination", "xp_trace_setreadfilter", 
		"xp_trace_setserverfilter", "xp_trace_setseverityfilter", "xp_trace_setspidfilter", "xp_trace_settextfilter", "xp_trace_setuserfilter", "xp_trace_setwritefilter", "xp_trace_startconsumer", 
		"xp_trace_toeventlogconsumer", "xp_trace_tofileconsumer", "xp_unc_to_drive", "xp_unload_dummy_handlers", "xp_updatecolvbm", "xp_updatelineage", "xp_varbintohexstr", "xp_writesqlinfo",
		"LIKE",
		 "commit", "rollback"
		 ];
	
	/** hive ql -  https://cwiki.apache.org/confluence/display/Hive/LanguageManual+DDL#LanguageManualDDL-CreateTable */
	var HQL_KEYWORDS = MYSQL_KEYWORDS;

	// Scanner constants
	var UNKOWN = 1;
	var KEYWORD = 2;
	var NUMBER = 3;
	var STRING = 4;
	var MULTILINE_STRING = 5;
	var SINGLELINE_COMMENT = 6;
	var MULTILINE_COMMENT = 7;
	var DOC_COMMENT = 8;
	var WHITE = 9;
	var WHITE_TAB = 10;
	var WHITE_SPACE = 11;
	var HTML_MARKUP = 12;
	var DOC_TAG = 13;
	var TASK_TAG = 14;

	// Styles 
	var singleCommentStyle = {styleClass: "token_singleline_comment"};
	var multiCommentStyle = {styleClass: "token_multiline_comment"};
	var docCommentStyle = {styleClass: "token_doc_comment"};
	var htmlMarkupStyle = {styleClass: "token_doc_html_markup"};
	var tasktagStyle = {styleClass: "token_task_tag"};
	var doctagStyle = {styleClass: "token_doc_tag"};
	var stringStyle = {styleClass: "token_string"};
	var numberStyle = {styleClass: "token_number"};
	var keywordStyle = {styleClass: "token_keyword"};
	var spaceStyle = {styleClass: "token_space"};
	var tabStyle = {styleClass: "token_tab"};
	var caretLineStyle = {styleClass: "line_caret"};
	
	var rulerStyle = {styleClass:"ruler"};
	var rulerAnnotationsStyle = {styleCLass:"ruler.annotations"};
	var rulerFoldingStyle = {styleClass:"ruler.lines"};
	var rulerOverviewStyle = {styleClass:"ruler.overview"};
	var rulerLinesStyle = {styleCLass:"rulerLines"};
	var rulerLinesEvenStyle = {styleClass:"rulerLines.even"};
	var rulerLinesOddStyle = {styleClass:"rulerLines.odd"};
	
	function Scanner (keywords, whitespacesVisible) {
		this.keywords = keywords;
		this.whitespacesVisible = whitespacesVisible;
		this.setText("");
	}
	
	Scanner.prototype = {
		getOffset: function() {
			return this.offset;
		},
		getStartOffset: function() {
			return this.startOffset;
		},
		getData: function() {
			/** convert the text lowcase - hangum */
			return this.text.substring(this.startOffset, this.offset).toLowerCase();
		},
		getDataLength: function() {
			return this.offset - this.startOffset;
		},
		_default: function(c) {
			switch (c) {
				case 32: // SPACE
				case 9: // TAB
					if (this.whitespacesVisible) {
						return c === 32 ? WHITE_SPACE : WHITE_TAB;
					}
					do {
						c = this._read();
					} while(c === 32 || c === 9);
					this._unread(c);
					return WHITE;
				case 123: // {
				case 125: // }
				case 40: // (
				case 41: // )
				case 91: // [
				case 93: // ]
				case 60: // <
				case 62: // >
					// BRACKETS
					return c;
				default:
					var isCSS = this.isCSS;
					var off = this.offset - 1;
					if (!isCSS && 48 <= c && c <= 57) {
						var floating = false, exponential = false, hex = false, firstC = c;
						do {
							c = this._read();
							if (c === 46 /* dot */ && !floating) {
								floating = true;
							} else if (c === 101 /* e */ && !exponential) {
								floating = exponential = true;
								c = this._read();
								if (c !== 45 /* MINUS */) {
									this._unread(c);
								}
							} else if (c === 120 /* x */ && firstC === 48 && (this.offset - off === 2)) {
								floating = exponential = hex = true;
							} else if (!(48 <= c && c <= 57 || (hex && ((65 <= c && c <= 70) || (97 <= c && c <= 102))))) { //NUMBER DIGIT or HEX
								break;
							}
						} while(true);
						this._unread(c);
						return NUMBER;
					}
					if ((97 <= c && c <= 122) || (65 <= c && c <= 90) || c === 95 || (45 /* DASH */ === c && isCSS)) { //LETTER OR UNDERSCORE OR NUMBER
						do {
							c = this._read();
						} while((97 <= c && c <= 122) || (65 <= c && c <= 90) || c === 95 || (48 <= c && c <= 57) || (45 /* DASH */ === c && isCSS));  //LETTER OR UNDERSCORE OR NUMBER
						this._unread(c);
						var keywords = this.keywords;
						if (keywords.length > 0) {
							/** convert the text lowcase - hangum */ 
							var word = this.text.substring(off, this.offset).toLowerCase();
							//TODO slow
							for (var i=0; i<keywords.length; i++) {
								if (this.keywords[i] === word) { return KEYWORD; }
							}
						}
					}
					return UNKOWN;
			}
		},
		_read: function() {
			if (this.offset < this.text.length) {
				return this.text.charCodeAt(this.offset++);
			}
			return -1;
		},
		_unread: function(c) {
			if (c !== -1) { this.offset--; }
		},
		nextToken: function() {
			this.startOffset = this.offset;
			while (true) {
				var c = this._read(), result;
				switch (c) {
					case -1: return null;
					// [start] -- comment add - hangum
					case 45:
						while (true) {
							c = this._read();
							if ((c === -1) || (c === 10) || (c === 13)) {
								this._unread(c);
								return SINGLELINE_COMMENT;
							}
						}
						break;
					// [start] -- comment add - hangum						
					case 47:	// SLASH -> comment
						c = this._read();
//						if (!this.isCSS) {
//							if (c === 47) { // SLASH -> single line
//								while (true) {
//									c = this._read();
//									if ((c === -1) || (c === 10) || (c === 13)) {
//										this._unread(c);
//										return SINGLELINE_COMMENT;
//									}
//								}
//							}
//						}
						if (c === 42) { // STAR -> multi line 
							c = this._read();
							var token = MULTILINE_COMMENT;
							if (c === 42) {
								token = DOC_COMMENT;
							}
							while (true) {
								while (c === 42) {
									c = this._read();
									if (c === 47) {
										return token;
									}
								}
								if (c === -1) {
									this._unread(c);
									return token;
								}
								c = this._read();
							}
						}
						this._unread(c);
						return UNKOWN;
					case 39:	// SINGLE QUOTE -> char const
						result = STRING;
						while(true) {
							c = this._read();
							switch (c) {
								case 39:
									return result;
								case 13:
								case 10:
								case -1:
									this._unread(c);
									return result;
								case 92: // BACKSLASH
									c = this._read();
									switch (c) {
										case 10: result = MULTILINE_STRING; break;
										case 13:
											result = MULTILINE_STRING;
											c = this._read();
											if (c !== 10) {
												this._unread(c);
											}
											break;
									}
									break;
							}
						}
						break;
					case 34:	// DOUBLE QUOTE -> string
						result = STRING;
						while(true) {
							c = this._read();
							switch (c) {
								case 34: // DOUBLE QUOTE
									return result;
								case 13:
								case 10:
								case -1:
									this._unread(c);
									return result;
								case 92: // BACKSLASH
									c = this._read();
									switch (c) {
										case 10: result = MULTILINE_STRING; break;
										case 13:
											result = MULTILINE_STRING;
											c = this._read();
											if (c !== 10) {
												this._unread(c);
											}
											break;
									}
									break;
							}
						}
						break;
					default:
						return this._default(c);
				}
			}
		},
		setText: function(text) {
			this.text = text;
			this.offset = 0;
			this.startOffset = 0;
		}
	};
	
	function WhitespaceScanner () {
		Scanner.call(this, null, true);
	}
	WhitespaceScanner.prototype = new Scanner(null);
	WhitespaceScanner.prototype.nextToken = function() {
		this.startOffset = this.offset;
		while (true) {
			var c = this._read();
			switch (c) {
				case -1: return null;
				case 32: // SPACE
					return WHITE_SPACE;
				case 9: // TAB
					return WHITE_TAB;
				default:
					do {
						c = this._read();
					} while(!(c === 32 || c === 9 || c === -1));
					this._unread(c);
					return UNKOWN;
			}
		}
	};
	
	function CommentScanner (whitespacesVisible) {
		Scanner.call(this, null, whitespacesVisible);
	}
	CommentScanner.prototype = new Scanner(null);
	CommentScanner.prototype.setType = function(type) {
		this._type = type;
	};
	CommentScanner.prototype.nextToken = function() {
		this.startOffset = this.offset;
		while (true) {
			var c = this._read();
			switch (c) {
				case -1: return null;
				case 32: // SPACE
				case 9: // TAB
					if (this.whitespacesVisible) {
						return c === 32 ? WHITE_SPACE : WHITE_TAB;
					}
					do {
						c = this._read();
					} while(c === 32 || c === 9);
					this._unread(c);
					return WHITE;
				case 60: // <
					if (this._type === DOC_COMMENT) {
						do {
							c = this._read();
						} while(!(c === 62 || c === -1)); // >
						if (c === 62) {
							return HTML_MARKUP;
						}
					}
					return UNKOWN;
				case 64: // @
					if (this._type === DOC_COMMENT) {
						do {
							c = this._read();
						} while((97 <= c && c <= 122) || (65 <= c && c <= 90) || c === 95 || (48 <= c && c <= 57));  //LETTER OR UNDERSCORE OR NUMBER
						this._unread(c);
						return DOC_TAG;
					}
					return UNKOWN;
				case 84: // T
					if ((c = this._read()) === 79) { // O
						if ((c = this._read()) === 68) { // D
							if ((c = this._read()) === 79) { // O
								c = this._read();
								if (!((97 <= c && c <= 122) || (65 <= c && c <= 90) || c === 95 || (48 <= c && c <= 57))) {
									this._unread(c);
									return TASK_TAG;
								}
								this._unread(c);
							} else {
								this._unread(c);
							}
						} else {
							this._unread(c);
						}
					} else {
						this._unread(c);
					}
					//FALL THROUGH
				default:
					do {
						c = this._read();
					} while(!(c === 32 || c === 9 || c === -1 || c === 60 || c === 64 || c === 84));
					this._unread(c);
					return UNKOWN;
			}
		}
	};
	
	function FirstScanner () {
		Scanner.call(this, null, false);
	}
	FirstScanner.prototype = new Scanner(null);
	FirstScanner.prototype._default = function(c) {
		while(true) {
			c = this._read();
			switch (c) {
				case 47: // SLASH
				case 34: // DOUBLE QUOTE
				case 39: // SINGLE QUOTE
				case -1:
					this._unread(c);
					return UNKOWN;
			}
		}
	};
	
	function TextStyler (view, lang, annotationModel) {
		this.commentStart = "/*";
		this.commentEnd = "*/";
		var keywords = [];
		
		switch (lang) {	
			case "oracle": 
				keywords = ORACLE_KEYWORDS; 
				break;
			case "mysql":
				keywords = MYSQL_KEYWORDS; 
				break;
			case "mssql": 
				keywords = MSSQL_KEYWORDS; 
				break;
			case "sqlite": 
				keywords = SQLITE_KEYWORDS; 
				break;
			case "hql":
				keywords = HQL_KEYWORDS;
				break;
			default:
				keywords = MYSQL_KEYWORDS;
		}
		this.whitespacesVisible = false;
		this.detectHyperlinks = true;
		this.highlightCaretLine = false;
		this.foldingEnabled = true;
		this.detectTasks = true;
		this._scanner = new Scanner(keywords, this.whitespacesVisible);
		this._firstScanner = new FirstScanner();
		this._commentScanner = new CommentScanner(this.whitespacesVisible);
		this._whitespaceScanner = new WhitespaceScanner();
		//TODO these scanners are not the best/correct way to parse CSS
		if (lang === "css") {
			this._scanner.isCSS = true;
			this._firstScanner.isCSS = true;
		}
		this.view = view;
		this.annotationModel = annotationModel;
		this._bracketAnnotations = undefined; 
		
		var self = this;
		this._listener = {
			onChanged: function(e) {
				self._onModelChanged(e);
			},
			onDestroy: function(e) {
				self._onDestroy(e);
			},
			onLineStyle: function(e) {
				self._onLineStyle(e);
			},
			onSelection: function(e) {
				self._onSelection(e);
			}
		};
		var model = view.getModel();
		if (model.getBaseModel) {
			model.getBaseModel().addEventListener("Changed", this._listener.onChanged);
		} else {
			//TODO still needed to keep the event order correct (styler before view)
			view.addEventListener("ModelChanged", this._listener.onChanged);
		}
		view.addEventListener("Selection", this._listener.onSelection);
		view.addEventListener("Destroy", this._listener.onDestroy);
		view.addEventListener("LineStyle", this._listener.onLineStyle);
		this._computeComments ();
		this._computeFolding();
		view.redrawLines();
	}
	
	TextStyler.prototype = {
		getClassNameForToken: function(token) {
			switch (token) {
			
				case "singleLineComment": return singleCommentStyle.styleClass;
				case "multiLineComment": return multiCommentStyle.styleClass;
				case "docComment": return docCommentStyle.styleClass;
				case "docHtmlComment": return htmlMarkupStyle.styleClass;
				case "tasktag": return tasktagStyle.styleClass;
				case "doctag": return doctagStyle.styleClass;
				case "string": return stringStyle.styleClass;
				case "number": return numberStyle.styleClass;
				case "keyword": return keywordStyle.styleClass;
				case "space": return spaceStyle.styleClass;
				case "tab": return tabStyle.styleClass;
				case "caretLine": return caretLineStyle.styleClass;
				
				case "rulerStyle": return rulerStyle.styleClass;
				case "annotationsStyle": return rulerAnnotationsStyle.styleClass;
				case "rulerFolding": return rulerLinesStyle.styleClass;
				case "rulerOverview": return rulerOverviewStyle.styleClass;
				case "rulerLines": return rulerLinesStyle.styleClass;
				case "rulerLinesEven": return rulerLinesEvenStyle.styleClass;
				case "rulerLinesOdd": return rulerLinesOddStyle.styleClass;
			}
			return null;
		},
		destroy: function() {
			var view = this.view;
			if (view) {
				var model = view.getModel();
				if (model.getBaseModel) {
					model.getBaseModel().removeEventListener("Changed", this._listener.onChanged);
				} else {
					view.removeEventListener("ModelChanged", this._listener.onChanged);
				}
				view.removeEventListener("Selection", this._listener.onSelection);
				view.removeEventListener("Destroy", this._listener.onDestroy);
				view.removeEventListener("LineStyle", this._listener.onLineStyle);
				this.view = null;
			}
		},
		setHighlightCaretLine: function(highlight) {
			this.highlightCaretLine = highlight;
		},
		setWhitespacesVisible: function(visible) {
			this.whitespacesVisible = visible;
			this._scanner.whitespacesVisible = visible;
			this._commentScanner.whitespacesVisible = visible;
		},
		setDetectHyperlinks: function(enabled) {
			this.detectHyperlinks = enabled;
		},
		setFoldingEnabled: function(enabled) {
			this.foldingEnabled = enabled;
		},
		setDetectTasks: function(enabled) {
			this.detectTasks = enabled;
		},
		_binarySearch: function (array, offset, inclusive, low, high) {
			var index;
			if (low === undefined) { low = -1; }
			if (high === undefined) { high = array.length; }
			while (high - low > 1) {
				index = Math.floor((high + low) / 2);
				if (offset <= array[index].start) {
					high = index;
				} else if (inclusive && offset < array[index].end) {
					high = index;
					break;
				} else {
					low = index;
				}
			}
			return high;
		},
		_computeComments: function() {
			var model = this.view.getModel();
			if (model.getBaseModel) { model = model.getBaseModel(); }
			this.comments = this._findComments(model.getText());
		},
		_computeFolding: function() {
			if (!this.foldingEnabled) { return; }
			var view = this.view;
			var viewModel = view.getModel();
			if (!viewModel.getBaseModel) { return; }
			var annotationModel = this.annotationModel;
			if (!annotationModel) { return; }
			annotationModel.removeAnnotations(mAnnotations.AnnotationType.ANNOTATION_FOLDING);
			var add = [];
			var baseModel = viewModel.getBaseModel();
			var comments = this.comments;
			for (var i=0; i<comments.length; i++) {
				var comment = comments[i];
				var annotation = this._createFoldingAnnotation(viewModel, baseModel, comment.start, comment.end);
				if (annotation) { 
					add.push(annotation);
				}
			}
			annotationModel.replaceAnnotations(null, add);
		},
		_createFoldingAnnotation: function(viewModel, baseModel, start, end) {
			var startLine = baseModel.getLineAtOffset(start);
			var endLine = baseModel.getLineAtOffset(end);
			if (startLine === endLine) {
				return null;
			}
			return new (mAnnotations.AnnotationType.getType(mAnnotations.AnnotationType.ANNOTATION_FOLDING))(start, end, viewModel);
		},
		_computeTasks: function(type, commentStart, commentEnd) {
			if (!this.detectTasks) { return; }
			var annotationModel = this.annotationModel;
			if (!annotationModel) { return; }
			var view = this.view;
			var viewModel = view.getModel(), baseModel = viewModel;
			if (viewModel.getBaseModel) { baseModel = viewModel.getBaseModel(); }
			var annotations = annotationModel.getAnnotations(commentStart, commentEnd);
			var remove = [];
			var annotationType = mAnnotations.AnnotationType.ANNOTATION_TASK;
			while (annotations.hasNext()) {
				var annotation = annotations.next();
				if (annotation.type === annotationType) {
					remove.push(annotation);
				}
			}
			var add = [];
			var scanner = this._commentScanner;
			scanner.setText(baseModel.getText(commentStart, commentEnd));
			var token;
			while ((token = scanner.nextToken())) {
				var tokenStart = scanner.getStartOffset() + commentStart;
				if (token === TASK_TAG) {
					var end = baseModel.getLineEnd(baseModel.getLineAtOffset(tokenStart));
					if (type !== SINGLELINE_COMMENT) {
						end = Math.min(end, commentEnd - this.commentEnd.length);
					}
					add.push(mAnnotations.AnnotationType.createAnnotation(annotationType, tokenStart, end, baseModel.getText(tokenStart, end)));
				}
			}
			annotationModel.replaceAnnotations(remove, add);
		},
		_getLineStyle: function(lineIndex) {
			if (this.highlightCaretLine) {
				var view = this.view;
				var model = view.getModel();
				var selection = view.getSelection();
				if (selection.start === selection.end && model.getLineAtOffset(selection.start) === lineIndex) {
					return caretLineStyle;
				}
			}
			return null;
		},
		_getStyles: function(model, text, start) {
			if (model.getBaseModel) {
				start = model.mapOffset(start);
			}
			var end = start + text.length;
			
			var styles = [];
			
			// for any sub range that is not a comment, parse code generating tokens (keywords, numbers, brackets, line comments, etc)
			var offset = start, comments = this.comments;
			var startIndex = this._binarySearch(comments, start, true);
			for (var i = startIndex; i < comments.length; i++) {
				if (comments[i].start >= end) { break; }
				var commentStart = comments[i].start;
				var commentEnd = comments[i].end;
				if (offset < commentStart) {
					this._parse(text.substring(offset - start, commentStart - start), offset, styles);
				}
				var type = comments[i].type, style;
				switch (type) {
					case DOC_COMMENT: style = docCommentStyle; break;
					case MULTILINE_COMMENT: style = multiCommentStyle; break;
					case MULTILINE_STRING: style = stringStyle; break;
				}
				var s = Math.max(offset, commentStart);
				var e = Math.min(end, commentEnd);
				if ((type === DOC_COMMENT || type === MULTILINE_COMMENT) && (this.whitespacesVisible || this.detectHyperlinks)) {
					this._parseComment(text.substring(s - start, e - start), s, styles, style, type);
				} else if (type === MULTILINE_STRING && this.whitespacesVisible) {
					this._parseString(text.substring(s - start, e - start), s, styles, stringStyle);
				} else {
					styles.push({start: s, end: e, style: style});
				}
				offset = commentEnd;
			}
			if (offset < end) {
				this._parse(text.substring(offset - start, end - start), offset, styles);
			}
			if (model.getBaseModel) {
				for (var j = 0; j < styles.length; j++) {
					var length = styles[j].end - styles[j].start;
					styles[j].start = model.mapOffset(styles[j].start, true);
					styles[j].end = styles[j].start + length;
				}
			}
			return styles;
		},
		_parse: function(text, offset, styles) {
			var scanner = this._scanner;
			scanner.setText(text);
			var token;
			while ((token = scanner.nextToken())) {
				var tokenStart = scanner.getStartOffset() + offset;
				var style = null;
				switch (token) {
					case KEYWORD: style = keywordStyle; break;
					case NUMBER: style = numberStyle; break;
					case MULTILINE_STRING:
					case STRING:
						if (this.whitespacesVisible) {
							this._parseString(scanner.getData(), tokenStart, styles, stringStyle);
							continue;
						} else {
							style = stringStyle;
						}
						break;
					case DOC_COMMENT: 
						this._parseComment(scanner.getData(), tokenStart, styles, docCommentStyle, token);
						continue;
					case SINGLELINE_COMMENT:
						this._parseComment(scanner.getData(), tokenStart, styles, singleCommentStyle, token);
						continue;
					case MULTILINE_COMMENT: 
						this._parseComment(scanner.getData(), tokenStart, styles, multiCommentStyle, token);
						continue;
					case WHITE_TAB:
						if (this.whitespacesVisible) {
							style = tabStyle;
						}
						break;
					case WHITE_SPACE:
						if (this.whitespacesVisible) {
							style = spaceStyle;
						}
						break;
				}
				styles.push({start: tokenStart, end: scanner.getOffset() + offset, style: style});
			}
		},
		_parseComment: function(text, offset, styles, s, type) {
			var scanner = this._commentScanner;
			scanner.setText(text);
			scanner.setType(type);
			var token;
			while ((token = scanner.nextToken())) {
				var tokenStart = scanner.getStartOffset() + offset;
				var style = s;
				switch (token) {
					case WHITE_TAB:
						if (this.whitespacesVisible) {
							style = tabStyle;
						}
						break;
					case WHITE_SPACE:
						if (this.whitespacesVisible) {
							style = spaceStyle;
						}
						break;
					case HTML_MARKUP:
						style = htmlMarkupStyle;
						break;
					case DOC_TAG:
						style = doctagStyle;
						break;
					case TASK_TAG:
						style = tasktagStyle;
						break;
					default:
						if (this.detectHyperlinks) {
							style = this._detectHyperlinks(scanner.getData(), tokenStart, styles, style);
						}
				}
				if (style) {
					styles.push({start: tokenStart, end: scanner.getOffset() + offset, style: style});
				}
			}
		},
		_parseString: function(text, offset, styles, s) {
			var scanner = this._whitespaceScanner;
			scanner.setText(text);
			var token;
			while ((token = scanner.nextToken())) {
				var tokenStart = scanner.getStartOffset() + offset;
				var style = s;
				switch (token) {
					case WHITE_TAB:
						if (this.whitespacesVisible) {
							style = tabStyle;
						}
						break;
					case WHITE_SPACE:
						if (this.whitespacesVisible) {
							style = spaceStyle;
						}
						break;
				}
				if (style) {
					styles.push({start: tokenStart, end: scanner.getOffset() + offset, style: style});
				}
			}
		},
		_detectHyperlinks: function(text, offset, styles, s) {
			var href = null, index, linkStyle;
			if ((index = text.indexOf("://")) > 0) {
				href = text;
				var start = index;
				while (start > 0) {
					var c = href.charCodeAt(start - 1);
					if (!((97 <= c && c <= 122) || (65 <= c && c <= 90) || 0x2d === c || (48 <= c && c <= 57))) { //LETTER OR DASH OR NUMBER
						break;
					}
					start--;
				}
				if (start > 0) {
					var brackets = "\"\"''(){}[]<>";
					index = brackets.indexOf(href.substring(start - 1, start));
					if (index !== -1 && (index & 1) === 0 && (index = href.lastIndexOf(brackets.substring(index + 1, index + 2))) !== -1) {
						var end = index;
						linkStyle = this._clone(s);
						linkStyle.tagName = "A";
						linkStyle.attributes = {href: href.substring(start, end)};
						styles.push({start: offset, end: offset + start, style: s});
						styles.push({start: offset + start, end: offset + end, style: linkStyle});
						styles.push({start: offset + end, end: offset + text.length, style: s});
						return null;
					}
				}
			} else if (text.toLowerCase().indexOf("bug#") === 0) {
				href = "https://bugs.eclipse.org/bugs/show_bug.cgi?id=" + parseInt(text.substring(4), 10);
			}
			if (href) {
				linkStyle = this._clone(s);
				linkStyle.tagName = "A";
				linkStyle.attributes = {href: href};
				return linkStyle;
			}
			return s;
		},
		_clone: function(obj) {
			if (!obj) { return obj; }
			var newObj = {};
			for (var p in obj) {
				if (obj.hasOwnProperty(p)) {
					var value = obj[p];
					newObj[p] = value;
				}
			}
			return newObj;
		},
		_findComments: function(text, offset) {
			offset = offset || 0;
			var scanner = this._firstScanner, token;
			scanner.setText(text);
			var result = [];
			while ((token = scanner.nextToken())) {
				if (token === MULTILINE_COMMENT || token === DOC_COMMENT || token === MULTILINE_STRING) {
					result.push({
						start: scanner.getStartOffset() + offset,
						end: scanner.getOffset() + offset,
						type: token
					});
				}
				if (token === SINGLELINE_COMMENT || token === MULTILINE_COMMENT || token === DOC_COMMENT) {
					//TODO can we avoid this work if edition does not overlap comment?
					this._computeTasks(token, scanner.getStartOffset() + offset, scanner.getOffset() + offset);
				}
			}
			return result;
		}, 
		_findMatchingBracket: function(model, offset) {
			var brackets = "{}()[]<>";
			var bracket = model.getText(offset, offset + 1);
			var bracketIndex = brackets.indexOf(bracket, 0);
			if (bracketIndex === -1) { return -1; }
			var closingBracket;
			if (bracketIndex & 1) {
				closingBracket = brackets.substring(bracketIndex - 1, bracketIndex);
			} else {
				closingBracket = brackets.substring(bracketIndex + 1, bracketIndex + 2);
			}
			var lineIndex = model.getLineAtOffset(offset);
			var lineText = model.getLine(lineIndex);
			var lineStart = model.getLineStart(lineIndex);
			var lineEnd = model.getLineEnd(lineIndex);
			brackets = this._findBrackets(bracket, closingBracket, lineText, lineStart, lineStart, lineEnd);
			for (var i=0; i<brackets.length; i++) {
				var sign = brackets[i] >= 0 ? 1 : -1;
				if (brackets[i] * sign === offset) {
					var level = 1;
					if (bracketIndex & 1) {
						i--;
						for (; i>=0; i--) {
							sign = brackets[i] >= 0 ? 1 : -1;
							level += sign;
							if (level === 0) {
								return brackets[i] * sign;
							}
						}
						lineIndex -= 1;
						while (lineIndex >= 0) {
							lineText = model.getLine(lineIndex);
							lineStart = model.getLineStart(lineIndex);
							lineEnd = model.getLineEnd(lineIndex);
							brackets = this._findBrackets(bracket, closingBracket, lineText, lineStart, lineStart, lineEnd);
							for (var j=brackets.length - 1; j>=0; j--) {
								sign = brackets[j] >= 0 ? 1 : -1;
								level += sign;
								if (level === 0) {
									return brackets[j] * sign;
								}
							}
							lineIndex--;
						}
					} else {
						i++;
						for (; i<brackets.length; i++) {
							sign = brackets[i] >= 0 ? 1 : -1;
							level += sign;
							if (level === 0) {
								return brackets[i] * sign;
							}
						}
						lineIndex += 1;
						var lineCount = model.getLineCount ();
						while (lineIndex < lineCount) {
							lineText = model.getLine(lineIndex);
							lineStart = model.getLineStart(lineIndex);
							lineEnd = model.getLineEnd(lineIndex);
							brackets = this._findBrackets(bracket, closingBracket, lineText, lineStart, lineStart, lineEnd);
							for (var k=0; k<brackets.length; k++) {
								sign = brackets[k] >= 0 ? 1 : -1;
								level += sign;
								if (level === 0) {
									return brackets[k] * sign;
								}
							}
							lineIndex++;
						}
					}
					break;
				}
			}
			return -1;
		},
		_findBrackets: function(bracket, closingBracket, text, textOffset, start, end) {
			var result = [];
			var bracketToken = bracket.charCodeAt(0);
			var closingBracketToken = closingBracket.charCodeAt(0);
			// for any sub range that is not a comment, parse code generating tokens (keywords, numbers, brackets, line comments, etc)
			var offset = start, scanner = this._scanner, token, comments = this.comments;
			var startIndex = this._binarySearch(comments, start, true);
			for (var i = startIndex; i < comments.length; i++) {
				if (comments[i].start >= end) { break; }
				var commentStart = comments[i].start;
				var commentEnd = comments[i].end;
				if (offset < commentStart) {
					scanner.setText(text.substring(offset - start, commentStart - start));
					while ((token = scanner.nextToken())) {
						if (token === bracketToken) {
							result.push(scanner.getStartOffset() + offset - start + textOffset);
						} else if (token === closingBracketToken) {
							result.push(-(scanner.getStartOffset() + offset - start + textOffset));
						}
					}
				}
				offset = commentEnd;
			}
			if (offset < end) {
				scanner.setText(text.substring(offset - start, end - start));
				while ((token = scanner.nextToken())) {
					if (token === bracketToken) {
						result.push(scanner.getStartOffset() + offset - start + textOffset);
					} else if (token === closingBracketToken) {
						result.push(-(scanner.getStartOffset() + offset - start + textOffset));
					}
				}
			}
			return result;
		},
		_onDestroy: function(e) {
			this.destroy();
		},
		_onLineStyle: function (e) {
			if (e.textView === this.view) {
				e.style = this._getLineStyle(e.lineIndex);
			}
			e.ranges = this._getStyles(e.textView.getModel(), e.lineText, e.lineStart);
		},
		_onSelection: function(e) {
			var oldSelection = e.oldValue;
			var newSelection = e.newValue;
			var view = this.view;
			var model = view.getModel();
			var lineIndex;
			if (this.highlightCaretLine) {
				var oldLineIndex = model.getLineAtOffset(oldSelection.start);
				lineIndex = model.getLineAtOffset(newSelection.start);
				var newEmpty = newSelection.start === newSelection.end;
				var oldEmpty = oldSelection.start === oldSelection.end;
				if (!(oldLineIndex === lineIndex && oldEmpty && newEmpty)) {
					if (oldEmpty) {
						view.redrawLines(oldLineIndex, oldLineIndex + 1);
					}
					if ((oldLineIndex !== lineIndex || !oldEmpty) && newEmpty) {
						view.redrawLines(lineIndex, lineIndex + 1);
					}
				}
			}
			if (!this.annotationModel) { return; }
			var remove = this._bracketAnnotations, add, caret;
			if (newSelection.start === newSelection.end && (caret = view.getCaretOffset()) > 0) {
				var mapCaret = caret - 1;
				if (model.getBaseModel) {
					mapCaret = model.mapOffset(mapCaret);
					model = model.getBaseModel();
				}
				var bracket = this._findMatchingBracket(model, mapCaret);
				if (bracket !== -1) {
					add = [
						mAnnotations.AnnotationType.createAnnotation(mAnnotations.AnnotationType.ANNOTATION_MATCHING_BRACKET, bracket, bracket + 1),
						mAnnotations.AnnotationType.createAnnotation(mAnnotations.AnnotationType.ANNOTATION_CURRENT_BRACKET, mapCaret, mapCaret + 1)
					];
				}
			}
			this._bracketAnnotations = add;
			this.annotationModel.replaceAnnotations(remove, add);
		},
		_onModelChanged: function(e) {
			var start = e.start;
			var removedCharCount = e.removedCharCount;
			var addedCharCount = e.addedCharCount;
			var changeCount = addedCharCount - removedCharCount;
			var view = this.view;
			var viewModel = view.getModel();
			var baseModel = viewModel.getBaseModel ? viewModel.getBaseModel() : viewModel;
			var end = start + removedCharCount;
			var charCount = baseModel.getCharCount();
			var commentCount = this.comments.length;
			var lineStart = baseModel.getLineStart(baseModel.getLineAtOffset(start));
			var commentStart = this._binarySearch(this.comments, lineStart, true);
			var commentEnd = this._binarySearch(this.comments, end, false, commentStart - 1, commentCount);
			
			var ts;
			if (commentStart < commentCount && this.comments[commentStart].start <= lineStart && lineStart < this.comments[commentStart].end) {
				ts = this.comments[commentStart].start;
				if (ts > start) { ts += changeCount; }
			} else {
				if (commentStart === commentCount && commentCount > 0 && charCount - changeCount === this.comments[commentCount - 1].end) {
					ts = this.comments[commentCount - 1].start;
				} else {
					ts = lineStart;
				}
			}
			var te;
			if (commentEnd < commentCount) {
				te = this.comments[commentEnd].end;
				if (te > start) { te += changeCount; }
				commentEnd += 1;
			} else {
				commentEnd = commentCount;
				te = charCount;//TODO could it be smaller?
			}
			var text = baseModel.getText(ts, te), comment;
			var newComments = this._findComments(text, ts), i;
			for (i = commentStart; i < this.comments.length; i++) {
				comment = this.comments[i];
				if (comment.start > start) { comment.start += changeCount; }
				if (comment.start > start) { comment.end += changeCount; }
			}
			var redraw = (commentEnd - commentStart) !== newComments.length;
			if (!redraw) {
				for (i=0; i<newComments.length; i++) {
					comment = this.comments[commentStart + i];
					var newComment = newComments[i];
					if (comment.start !== newComment.start || comment.end !== newComment.end || comment.type !== newComment.type) {
						redraw = true;
						break;
					} 
				}
			}
			var args = [commentStart, commentEnd - commentStart].concat(newComments);
			Array.prototype.splice.apply(this.comments, args);
			if (redraw) {
				var redrawStart = ts;
				var redrawEnd = te;
				if (viewModel !== baseModel) {
					redrawStart = viewModel.mapOffset(redrawStart, true);
					redrawEnd = viewModel.mapOffset(redrawEnd, true);
				}
				view.redrawRange(redrawStart, redrawEnd);
			}

			if (this.foldingEnabled && baseModel !== viewModel && this.annotationModel) {
				var annotationModel = this.annotationModel;
				var iter = annotationModel.getAnnotations(ts, te);
				var remove = [], all = [];
				var annotation;
				while (iter.hasNext()) {
					annotation = iter.next();
					if (annotation.type === mAnnotations.AnnotationType.ANNOTATION_FOLDING) {
						all.push(annotation);
						for (i = 0; i < newComments.length; i++) {
							if (annotation.start === newComments[i].start && annotation.end === newComments[i].end) {
								break;
							}
						}
						if (i === newComments.length) {
							remove.push(annotation);
							annotation.expand();
						} else {
							var annotationStart = annotation.start;
							var annotationEnd = annotation.end;
							if (annotationStart > start) {
								annotationStart -= changeCount;
							}
							if (annotationEnd > start) {
								annotationEnd -= changeCount;
							}
							if (annotationStart <= start && start < annotationEnd && annotationStart <= end && end < annotationEnd) {
								var startLine = baseModel.getLineAtOffset(annotation.start);
								var endLine = baseModel.getLineAtOffset(annotation.end);
								if (startLine !== endLine) {
									if (!annotation.expanded) {
										annotation.expand();
										annotationModel.modifyAnnotation(annotation);
									}
								} else {
									annotationModel.removeAnnotation(annotation);
								}
							}
						}
					}
				}
				var add = [];
				for (i = 0; i < newComments.length; i++) {
					comment = newComments[i];
					for (var j = 0; j < all.length; j++) {
						if (all[j].start === comment.start && all[j].end === comment.end) {
							break;
						}
					}
					if (j === all.length) {
						annotation = this._createFoldingAnnotation(viewModel, baseModel, comment.start, comment.end);
						if (annotation) {
							add.push(annotation);
						}
					}
				}
				annotationModel.replaceAnnotations(remove, add);
			}
		}
	};
	
	return {TextStyler: TextStyler};
});

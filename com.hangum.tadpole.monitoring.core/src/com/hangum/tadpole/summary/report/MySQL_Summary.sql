		-- show full processlit
		SHOW FULL PROCESSLIST
		;
		-- schema summary
		SELECT   round(SUM(data_length+index_length)/1024/1024, 2)  AS 'Total(MB)',
		         round(SUM(data_length)/1024/1024, 2)               AS 'Data(MB)',
		         round(SUM(index_length)/1024/1024, 2)              AS 'Index(MB)',
		         COUNT(*)                                           AS 'Tables'
		FROM     information_schema.tables
		WHERE    table_schema = #schemaName#
		;
		-- engine Summary
		SELECT   engine                     AS 'Engine',
		         table_collation            AS 'Collation',
		         COUNT(*)                   AS 'Tables'
		FROM     information_schema.tables
		WHERE    table_schema = #schemaName#
		GROUP BY engine, table_collation
		;
		-- table summary
		SELECT   table_name                                     AS 'Name',
		         engine                                         AS 'Engine',
		         row_format                                     AS 'Format', 
		         table_rows                                     AS 'Row', 
		         avg_row_length                                 AS 'Average Row',
		         round((data_length+index_length)/1024/1024, 2) AS 'Total(MB)', 
		         round((data_length)/1024/1024, 2)              AS 'Data(MB)', 
		         round((index_length)/1024/1024, 2)             AS 'Index(MB)'
		FROM     information_schema.tables 
		WHERE    table_schema   = #schemaName#
		    AND  table_type     = 'BASE TABLE'
		ORDER BY 'Total(MB)' DESC
		;	
		-- blob/text column list
		SELECT  table_name      AS 'Table Name',
		        column_name     AS 'Column Name',
		        data_type       AS 'Data Type' 
		FROM    information_schema.columns 
		WHERE   table_schema = #schemaName#
		    AND data_type LIKE '%TEXT' 
		    OR data_type LIKE '%BLOB'
		ORDER BY table_name
		;
		-- large text column list
		SELECT  table_name                  AS 'Table Name',
		        column_name                 AS 'Column Name',
		        character_maximum_length    AS 'Character lenght' 
		FROM    information_schema.columns 
		WHERE   table_schema                = #schemaName#
		    AND data_type                   = 'varchar' 
		    AND character_maximum_length    > 2000 
		ORDER BY table_name
		;
		-- large integer
		SELECT  table_name                  AS 'Table Name',
		        column_name                 AS 'Column Name',
		        data_type                   AS 'Data Type',
		        extra                       As 'Extra' 
		FROM    information_schema.columns 
		WHERE   table_schema    = #schemaName#
		    AND data_type       = 'bigint' 
		    AND extra           LIKE '%auto_increment%' 
		;
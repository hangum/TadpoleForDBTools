package com.hangum.tadpole.system.internal.migration;


/**
 * System migration sr9 to sr10
 * 
 * @author hangum
 *
 */
public class SystemMigrationSR9TOSR10 {

	/**
	 * user_db table add column 
	 * 		ext1 VARCHAR(2000),
     		ext2 VARCHAR(2000), 
     		ext3 VARCHAR(2000),
	 * 
	 */
	public static void migration() throws Exception {
		SystemMigration.runSQLExecuteBatch("ALTER TABLE user_db ADD COLUMN ext1 VARCHAR(2000)");
		SystemMigration.runSQLExecuteBatch("ALTER TABLE user_db ADD COLUMN ext2 VARCHAR(2000)");
		SystemMigration.runSQLExecuteBatch("ALTER TABLE user_db ADD COLUMN ext3 VARCHAR(2000)");
	}
}

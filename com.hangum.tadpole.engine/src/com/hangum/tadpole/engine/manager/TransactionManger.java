package com.hangum.tadpole.engine.manager;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

/**
 * SQL의 transaction을 처리합니다.
 * 
 * @author hangum
 *
 */
public class TransactionManger {
	/** define begin statement */
	public static final String BEGIN_STATEMENT 		= "begin";// + PublicTadpoleDefine.SQL_DELIMITER;
	public static final String BEGIN_MYSQL_STATEMENT = "start transaction";
	
	/** define commit statement */
	public static final String COMMIT_STATEMENT 	= "commit";// + PublicTadpoleDefine.SQL_DELIMITER;
	
	/** rollback statement */
	public static final String ROLLBACK_STATEMENT 	= "rollback";// + PublicTadpoleDefine.SQL_DELIMITER;
	
	
	/**
	 * is transaction
	 * 
	 * @param query
	 * @return
	 */
	public static boolean isTransaction(String query) {
		final String strTestQuery = SQLUtil.removeComment(query);
		if(
				StringUtils.startsWithIgnoreCase(strTestQuery, BEGIN_STATEMENT) ||
				StringUtils.startsWithIgnoreCase(strTestQuery, BEGIN_MYSQL_STATEMENT) ||
				StringUtils.startsWithIgnoreCase(strTestQuery, COMMIT_STATEMENT) || 
				StringUtils.startsWithIgnoreCase(strTestQuery, ROLLBACK_STATEMENT) 
		) { //$NON-NLS-1$
			return true;
		}
		
		return false;
	}

	/**
	 * transaction 쿼리인지 검사합니다.
	 * 
	 * @param query
	 * @param connectId
	 * @param userEmail
	 * @param userDB
	 * @return
	 */
	public static boolean calledCommitOrRollback(String query, String connectId, String userEmail, UserDBDAO userDB) {
		final String strTestQuery = SQLUtil.removeComment(query);
		if(StringUtils.startsWithIgnoreCase(strTestQuery, COMMIT_STATEMENT)) {
			TadpoleSQLTransactionManager.commit(connectId, userEmail, userDB);
			return true;
		} else if(StringUtils.startsWithIgnoreCase(strTestQuery, ROLLBACK_STATEMENT)) {
			TadpoleSQLTransactionManager.rollback(connectId, userEmail, userDB);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Is statement are begin?
	 * 
	 * @param query
	 * @return
	 */
	public static boolean isStartTransaction(String query) {
		final String strTestQuery = SQLUtil.removeComment(query);
		if(StringUtils.startsWithIgnoreCase(strTestQuery, BEGIN_STATEMENT) ||
				StringUtils.startsWithIgnoreCase(strTestQuery, BEGIN_MYSQL_STATEMENT)) return true;
		return false;
	}

}

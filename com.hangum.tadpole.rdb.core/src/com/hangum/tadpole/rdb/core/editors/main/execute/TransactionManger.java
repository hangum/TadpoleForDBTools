package com.hangum.tadpole.rdb.core.editors.main.execute;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * SQL의 transaction을 처리합니다.
 * 
 * @author hangum
 *
 */
public class TransactionManger {
	/** define begin statement */
	public static final String BEGIN_STATEMENT 		= "begin";// + PublicTadpoleDefine.SQL_DELIMITER;
	
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
		if(
				StringUtils.startsWithIgnoreCase(query, BEGIN_STATEMENT) ||
				StringUtils.startsWithIgnoreCase(query, COMMIT_STATEMENT) || 
				StringUtils.startsWithIgnoreCase(query, ROLLBACK_STATEMENT) 
		) { //$NON-NLS-1$
			return true;
		}
		
		return false;
	}

	/**
	 * transaction 쿼리인지 검사합니다.
	 * 
	 * @param query
	 * @return
	 */
	public static boolean calledCommitOrRollback(String query, String userEmail, UserDBDAO userDB) {
		if(StringUtils.startsWithIgnoreCase(query, COMMIT_STATEMENT)) {
			TadpoleSQLTransactionManager.commit(userEmail, userDB);
			return true;
		} else if(StringUtils.startsWithIgnoreCase(query, ROLLBACK_STATEMENT)) {
			TadpoleSQLTransactionManager.rollback(userEmail, userDB);
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
		if(StringUtils.startsWithIgnoreCase(query, BEGIN_STATEMENT)) return true;
		return false;
	}

}

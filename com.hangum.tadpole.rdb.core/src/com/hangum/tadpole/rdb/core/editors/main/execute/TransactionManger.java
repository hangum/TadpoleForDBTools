package com.hangum.tadpole.rdb.core.editors.main.execute;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * SQL의 transaction을 처리합니다.
 * 
 * @author hangum
 *
 */
public class TransactionManger {
	
	/**
	 * is transaction
	 * 
	 * @param query
	 * @return
	 */
	public static boolean isTransaction(String query) {
		if(StringUtils.startsWith(query, "commit") || StringUtils.startsWith(query, "rollback")) { //$NON-NLS-1$
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
	public static boolean transactionQuery(String query, String userEmail, UserDBDAO userDB) {
		if(StringUtils.startsWith(query, "commit")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.commit(userEmail, userDB);
			return true;
		}
		// 
		if(StringUtils.startsWith(query, "rollback")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.rollback(userEmail, userDB);
			return true;
		}
		
		return false;
	}

}

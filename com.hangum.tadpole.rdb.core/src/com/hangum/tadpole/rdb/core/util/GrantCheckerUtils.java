/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.util;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.TadpoleSecurityManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.UpdateDeleteConfirmDialog;

/**
 * grant checker
 * 
 * @author hangum
 *
 */
public class GrantCheckerUtils {
	private static final Logger logger = Logger.getLogger(GrantCheckerUtils.class);
	/**
	 * execute query
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static boolean ifExecuteQuery(UserDBDAO userDB) throws Exception {
		// security check.
		if(!TadpoleSecurityManager.getInstance().isLock(userDB)) {
			throw new Exception(Messages.get().ResultMainComposite_1);
		}
		
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getQuestion_dml())
				|| PermissionChecker.isProductBackup(userDB) 
		) {
			MessageDialog dialog = new MessageDialog(null, CommonMessages.get().Confirm, null, Messages.get().GrantCheckerUtils_0, MessageDialog.ERROR, new String[] {CommonMessages.get().Yes, CommonMessages.get().No}, 1);
			if(dialog.open() == 1) return false;
		}
		
		return true;
	}

	/**
	 * 쿼리가 실행 가능한 상태인지 검사한다.
	 * 
	 * - DB lock 상태인지?
	 * - dml 을 묻는 상태인지?
	 * - 프러덕, 백업 디비라서 select가 아닌지 묻는지?
	 * 
	 * @param userDB
	 * @param reqQuery
	 * @throws Exception
	 */
	public static boolean ifExecuteQuery(UserDBDAO userDB, RequestQuery reqQuery) throws Exception {
		// security check.
		if(!TadpoleSecurityManager.getInstance().isLock(userDB)) {
			throw new Exception(Messages.get().ResultMainComposite_1);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("==[start]====================================================");
			logger.debug("==> ddl type : " + reqQuery.getSqlDDLType() );
			logger.debug("==> getSqlDMLType : " + reqQuery.getSqlDMLType() );
			logger.debug("==[end]====================================================");
		}
		
		
		// 실행해도 되는지 묻는다.
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getQuestion_dml())
				|| PermissionChecker.isProductBackup(userDB)
		) {
			boolean isDDLQuestion = !reqQuery.isStatement();
			// all 이면 
			if(reqQuery.getExecuteType() == EditorDefine.EXECUTE_TYPE.ALL) {						
				for (String strSQL : reqQuery.getOriginalSql().split(PublicTadpoleDefine.SQL_DELIMITER)) {							
					if(!SQLUtil.isStatement(strSQL)) {
						isDDLQuestion = true;
						break;
					}
				}
				
				if(isDDLQuestion) {
					MessageDialog dialog = new MessageDialog(null, Messages.get().Execute, null, Messages.get().GrantCheckerUtils_0, MessageDialog.QUESTION, new String[] {CommonMessages.get().Yes, CommonMessages.get().No}, 1);
					if(dialog.open() != MessageDialog.OK) return false;
				}
			} else {
				
				// 단일 select update 이면. 
				if(isDDLQuestion) {
					PublicTadpoleDefine.QUERY_DML_TYPE dmlType = reqQuery.getSqlDMLType();
					if(PublicTadpoleDefine.QUERY_DML_TYPE.UPDATE == dmlType || PublicTadpoleDefine.QUERY_DML_TYPE.DELETE == dmlType) {
						UpdateDeleteConfirmDialog dialog = new UpdateDeleteConfirmDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB, reqQuery);
						if(dialog.open() != Dialog.OK) return false;
					} else {
						MessageDialog dialog = new MessageDialog(null, Messages.get().Execute, null, Messages.get().GrantCheckerUtils_0, MessageDialog.QUESTION, new String[] {CommonMessages.get().Yes, CommonMessages.get().No}, 1);
						if(dialog.open() != MessageDialog.OK) return false;
					}
					
				}
			}
		}

		return true;
	}
}

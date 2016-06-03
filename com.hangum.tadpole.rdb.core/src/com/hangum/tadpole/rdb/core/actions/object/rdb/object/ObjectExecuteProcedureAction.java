/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object.rdb.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.executer.ProcedureExecuterManager;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.procedure.ExecuteProcedureDialog;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * Procedure를 실행합니다.
 * 
 * @author hangum
 * 
 */
public class ObjectExecuteProcedureAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectExecuteProcedureAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.execute.procedure";

	public ObjectExecuteProcedureAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		if(logger.isDebugEnabled()) logger.debug("ObjectExecuteProcedureAction run...");

		ProcedureFunctionDAO procedureDAO;
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT 
				|| userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT || userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT 
		) {
			procedureDAO = (ProcedureFunctionDAO) selection.getFirstElement();
			ProcedureExecuterManager pm = new ProcedureExecuterManager(userDB, procedureDAO);
			pm.isExecuted(procedureDAO, userDB);
			
			try {
				String strScript = pm.getExecuter().getMakeExecuteScript();
				FindEditorAndWriteQueryUtil.run(userDB, strScript, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
				
			} catch(Exception e) {
				logger.error("procedure execute", e);
			}
		} else if((userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT ) && actionType == OBJECT_TYPE.FUNCTIONS) {
			procedureDAO = (ProcedureFunctionDAO) selection.getFirstElement();
			ProcedureExecuterManager pm = new ProcedureExecuterManager(userDB, procedureDAO);
			pm.isExecuted(procedureDAO, userDB);
			
			try {
				String strScript = pm.getExecuter().getMakeExecuteScript();
				FindEditorAndWriteQueryUtil.run(userDB, strScript, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
				
			} catch(Exception e) {
				logger.error("procedure execute", e);
			}
		} else if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
			procedureDAO = (ProcedureFunctionDAO) selection.getFirstElement();
			ProcedureExecuterManager pm = new ProcedureExecuterManager(userDB, procedureDAO);
			pm.isExecuted(procedureDAO, userDB);
			
			try {
				String strScript = pm.getExecuter().getMakeExecuteScript();
				FindEditorAndWriteQueryUtil.run(userDB, strScript, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
				
			} catch(Exception e) {
				logger.error("procedure execute", e);
			}
		} else {
			if (PublicTadpoleDefine.OBJECT_TYPE.SYNONYM.equals(actionType)) {
				OracleSynonymDAO synonym = (OracleSynonymDAO) selection.getFirstElement();
				procedureDAO = new ProcedureFunctionDAO();
				procedureDAO.setName(synonym.getName());
				if (synonym.getObject_type().startsWith("PACKAGE"))
					procedureDAO.setPackagename(synonym.getName());
			} else {
				procedureDAO = (ProcedureFunctionDAO) selection.getFirstElement();
			}
			ProcedureExecuterManager pm = new ProcedureExecuterManager(userDB, procedureDAO);
			if (pm.isExecuted(procedureDAO, userDB)) {
				ExecuteProcedureDialog epd = new ExecuteProcedureDialog(null, userDB, procedureDAO);
				epd.open();
			}
		}
	}// end method

}

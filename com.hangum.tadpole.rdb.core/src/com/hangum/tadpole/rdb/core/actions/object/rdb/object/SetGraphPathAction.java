/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     nilriri - agens그래프를 위한 set graph_path=x; 실행...
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object.rdb.object;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.agens.AgensGraphPathDAO;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBIndexDAO;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableConstraintsDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleDBLinkDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJavaDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJobDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSequenceDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.executer.ProcedureExecuterManager;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.GrantCheckerUtils;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author nilriri
 *
 */
public class SetGraphPathAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger .getLogger(SetGraphPathAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.setpath"; //$NON-NLS-1$

	public SetGraphPathAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		
		setId(ID + actionType);
		setText(title);
	}
	
	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		try {
			if(!GrantCheckerUtils.ifExecuteQuery(userDB)) return;
		} catch (Exception e) {
			MessageDialog.openError(getWindow().getShell(),CommonMessages.get().Error, e.getMessage());
			return;
		}
		
		 if(actionType == PublicTadpoleDefine.OBJECT_TYPE.GRAPHPATH) {			
			AgensGraphPathDAO dao = (AgensGraphPathDAO)selection.getFirstElement();
			if(MessageDialog.openConfirm(getWindow().getShell(), CommonMessages.get().Confirm, "Set graph path?")) {
				try {
					
					executeSQL(userDB, "SET GRAPH_PATH = "+dao.getGraphname()); //$NON-NLS-1$ //$NON-NLS-2$
										
				} catch(Exception e) {
					logger.error("set graph path", e);
				}
			}
		}
				
	}	// end method
	
	/**
	 * executeSQL
	 * 
	 * @param userDB
	 * @param cmd
	 * @throws Exception
	 */
	private void executeSQL(UserDBDAO userDB, String cmd) throws Exception {
		RequestResultDAO reqReResultDAO = new RequestResultDAO();
		ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, cmd); //$NON-NLS-1$
		if(PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(reqReResultDAO.getResult())) {
			exeMessage(Messages.get().ObjectDeleteAction_0, reqReResultDAO.getException());		
		}
	}
	
}

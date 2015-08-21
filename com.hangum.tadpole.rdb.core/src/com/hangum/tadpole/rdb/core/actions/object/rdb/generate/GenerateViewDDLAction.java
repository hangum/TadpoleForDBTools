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
package com.hangum.tadpole.rdb.core.actions.object.rdb.generate;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.sqlscripts.DDLScriptManager;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;

/**
 * generate ddl view
 * 
 * @author hangum
 * 
 */
public class GenerateViewDDLAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateViewDDLAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateViewDDLAction"; //$NON-NLS-1$

	/**
	 * generated view ddl
	 * 
	 * @param window
	 * @param actionType
	 * @param target
	 */
	public GenerateViewDDLAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String target) {
		super(window, actionType);

		setId(ID + actionType.toString());
		setText(target + " DDL");
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		try {
			DDLScriptManager scriptManager;

			Object obj = null;
			if (PublicTadpoleDefine.DB_ACTION.SYNONYM.equals(actionType)) {
				OracleSynonymDAO synonym = (OracleSynonymDAO) selection.getFirstElement();
				
				if (PublicTadpoleDefine.DB_ACTION.TABLES.toString().startsWith(synonym.getObject_type())) {
					scriptManager = new DDLScriptManager(userDB, PublicTadpoleDefine.DB_ACTION.TABLES);
					TableDAO dao = new TableDAO();
					dao.setName(synonym.getName());
					obj = dao;
				} else if (PublicTadpoleDefine.DB_ACTION.VIEWS.toString().startsWith(synonym.getObject_type())) {
					scriptManager = new DDLScriptManager(userDB, PublicTadpoleDefine.DB_ACTION.VIEWS);
					obj = synonym.getName();
				} else if (PublicTadpoleDefine.DB_ACTION.PACKAGES.toString().startsWith(synonym.getObject_type())) {
					scriptManager = new DDLScriptManager(userDB, PublicTadpoleDefine.DB_ACTION.PACKAGES);
					ProcedureFunctionDAO dao = new ProcedureFunctionDAO();
					dao.setName(synonym.getName());
					dao.setPackagename(synonym.getName());
					obj = dao;
				} else if (PublicTadpoleDefine.DB_ACTION.PROCEDURES.toString().startsWith(synonym.getObject_type())) {
					scriptManager = new DDLScriptManager(userDB, PublicTadpoleDefine.DB_ACTION.PROCEDURES);
					ProcedureFunctionDAO dao = new ProcedureFunctionDAO();
					dao.setName(synonym.getName());
					obj = dao;
				} else if (PublicTadpoleDefine.DB_ACTION.FUNCTIONS.toString().startsWith(synonym.getObject_type())) {
					scriptManager = new DDLScriptManager(userDB, PublicTadpoleDefine.DB_ACTION.FUNCTIONS);
					ProcedureFunctionDAO dao = new ProcedureFunctionDAO();
					dao.setName(synonym.getName());
					obj = dao;
				} else {
					scriptManager = new DDLScriptManager(userDB, actionType);
					obj = synonym;
				}
			} else {
				scriptManager = new DDLScriptManager(userDB, actionType);
				obj = selection.getFirstElement();
			}

			FindEditorAndWriteQueryUtil.run(userDB, scriptManager.getScript(obj), true, actionType);
		} catch (Exception e) {
			logger.error("view ddl", e);
			MessageDialog.openError(null, "Confirm", "Not support this function.");
		}
	}

}

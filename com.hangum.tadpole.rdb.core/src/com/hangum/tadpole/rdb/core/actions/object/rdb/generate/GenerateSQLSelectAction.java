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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.util.GenerateDDLScriptUtils;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * generate sql statement     
 * 
 * @author hangum
 *
 */
public class GenerateSQLSelectAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLSelectAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateSQLSelectAction"; //$NON-NLS-1$
	
	public GenerateSQLSelectAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
	
		setId(ID + actionType.toString());
		setText(Messages.GenerateSQLSelectAction_1 + title);
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		TableDAO tableDAO = (TableDAO)selection.getFirstElement();
		FindEditorAndWriteQueryUtil.run(userDB, GenerateDDLScriptUtils.genTableScript(userDB, tableDAO));
	}
}

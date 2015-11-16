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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.util.GenerateDDLScriptUtils;

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
	
	public GenerateSQLSelectAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
	
		setId(ID + actionType.toString());
		setText(Messages.get().GenerateSQLSelectAction_1 + title);
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableDAO tableDAO = (TableDAO)selection.getFirstElement();
		
		String strSQL = GenerateDDLScriptUtils.genTableScript(userDB, tableDAO);
		if(StringUtils.isNotEmpty(strSQL)) {
			FindEditorAndWriteQueryUtil.run(userDB, strSQL, actionType);
		}
	}
}

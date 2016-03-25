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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;

/**
 * Table Column 선택 action입니다. 
 * 
 * @author hangum
 *
 */
public class TableColumnSelectionAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableColumnSelectionAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.table.column.selection"; //$NON-NLS-1$

	public TableColumnSelectionAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(Messages.get().TableColumnSelectionAction_1);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		if(selection.isEmpty()) return;
		
		String strColumnName = "";
		Object[] arryObj = selection.toArray();
		for(int i=0; i<arryObj.length; i++) {
			Object obj = arryObj[arryObj.length-i-1];
			
			TableColumnDAO tcDAO = (TableColumnDAO)obj;
			strColumnName += tcDAO.getField() + ", "; //$NON-NLS-1$
		}
		strColumnName = StringUtils.removeEnd(strColumnName, ", "); //$NON-NLS-1$
		
		FindEditorAndWriteQueryUtil.runAtPosition(strColumnName);
	}// end method
	
}

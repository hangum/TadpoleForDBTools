/*******************************************************************************
 * Copyright (c) 2016 hangum.
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
import com.hangum.tadpole.engine.query.dao.mysql.StructObjectDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;

/**
 * Object explorer의 선택 된것을에디터에 붙여 넣기. 
 * 
 * @author hangum
 *
 */
public class ObjectExplorerSelectionToEditorAction extends AbstractObjectSelectAction {
	private static final Logger logger = Logger.getLogger(ObjectExplorerSelectionToEditorAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.selection"; //$NON-NLS-1$

	public ObjectExplorerSelectionToEditorAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(Messages.get().TableColumnSelectionAction_1);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		if(selection.isEmpty()) return;
		
		String strObjectName = "";
		Object[] arryObj = selection.toArray();
		
		if(arryObj[0] instanceof StructObjectDAO) {
			for(int i=0; i<arryObj.length; i++) {
				StructObjectDAO tcDAO = (StructObjectDAO)arryObj[i];
				strObjectName += tcDAO.getFullName() + ", "; //$NON-NLS-1$
			}
		}
		
		strObjectName = StringUtils.removeEnd(strObjectName, ", "); //$NON-NLS-1$
		FindEditorAndWriteQueryUtil.runAtPosition(strObjectName);
	}	// end method
	
}

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
package com.hangum.tadpole.rdb.core.actions.object.rdb.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TableColumnObjectQuery;

/**
 * Delete table column action 
 * 
 * @author hangum
 *
 */
public class TableColumnDeleteAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableColumnDeleteAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.table.column.delete"; //$NON-NLS-1$

	public TableColumnDeleteAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(Messages.get().DeleteColumn);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		if(selection.isEmpty()) return;
		if(!MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.get().Confirm, Messages.get().TableColumnDeleteAction_2)) return;
		
		try {
			TableColumnObjectQuery.deleteColumn(userDB, selection.toList());
		} catch (Exception e) {
			logger.error("Table delete column", e);
			MessageDialog.openError(null, Messages.get().Error, Messages.get().TableColumnDeleteAction_3+"\n"+e.getMessage());
		}
		
		// 테이블 컬럼을 리프레쉬 합니다.
		refreshTableColumn();
	}// end method
	
}

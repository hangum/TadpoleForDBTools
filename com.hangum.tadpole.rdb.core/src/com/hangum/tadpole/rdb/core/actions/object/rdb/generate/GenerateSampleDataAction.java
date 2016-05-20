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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataGenerateDialog;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class GenerateSampleDataAction extends AbstractObjectSelectAction {
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.generatesample.data";
	
	public GenerateSampleDataAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(Messages.get().GenerateSampleData);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableDAO tableDao = (TableDAO)selection.getFirstElement();
		
		SampleDataGenerateDialog dialog = new SampleDataGenerateDialog(getWindow().getShell(), userDB, SQLUtil.getTableName(userDB, tableDao));
		dialog.open();
	}
}

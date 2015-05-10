/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.objectmain;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Object Editor
 * 
 * 
 * @author hangum
 *
 */
public class ObjectEditor extends MainEditor {
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.main.procedure";

	public ObjectEditor() {
		super();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		ObjectEditorInput qei = (ObjectEditorInput)input;
		userDB = qei.getUserDB();
		initDefaultEditorStr = qei.getDefaultStr();
		dbAction = qei.getDbAction();

		strRoleType = userDB.getRole_id();//SessionManager.getRoleType(userDB);
		dBResource = qei.getResourceDAO();
		if(dBResource == null) setPartName(qei.getName());
		else  setPartName(dBResource.getName());
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}


//	@Override
//	public void createPartControl(Composite parent) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void setFocus() {
	}

}

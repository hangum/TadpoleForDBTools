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
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.rdb.core.actions.connections.CompilePackageAction;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class ObjectCompileAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectCompileAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.compile";
	
	public ObjectCompileAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Compile " + title);
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run() {
		if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {			
			
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {

		} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
		
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
		
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PACKAGES) {
			logger.debug("Compile Package...");
			CompilePackageAction cia = new CompilePackageAction(window, actionType, sel, userDB);
			cia.run();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
		
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
		
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
		
		}
	}
	
}

/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.browser.rap.core.actions.connections;

import org.eclipse.jface.action.IAction;

import com.hangum.tadpole.browser.rap.core.util.QueryTemplateUtils;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.Define;

/**
 * trigger 생성 action
 * 
 * @author hangumNote
 *
 */
public class CreateTriggerAction extends AbstractQueryAction {

	public CreateTriggerAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		run(userDB, QueryTemplateUtils.getQuery(userDB, Define.DB_ACTION.TRIGGERS));
	}
	
}

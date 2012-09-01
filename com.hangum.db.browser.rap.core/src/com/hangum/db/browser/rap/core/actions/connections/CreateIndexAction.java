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
package com.hangum.db.browser.rap.core.actions.connections;

import org.eclipse.jface.action.IAction;

import com.hangum.db.browser.rap.core.util.QueryTemplateUtils;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.define.Define;

/**
 * index 생성 action
 * 
 * @author hangumNote
 *
 */
public class CreateIndexAction extends AbstractQueryAction {

	public CreateIndexAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		run(userDB, QueryTemplateUtils.getQuery(userDB, Define.DB_ACTION.INDEXES));
	}
	
}

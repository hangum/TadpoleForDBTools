/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.connections;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJavaDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.java.CreateJavaDialog;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * Oracle Database job create action
 * 
 * @author nilriri
 *
 */
public class CreateJavaAction extends AbstractQueryAction {
	private OracleJavaDAO javaDao;

	public CreateJavaAction(OracleJavaDAO dao) {
		super();
		this.javaDao = dao;
	}

	@Override
	public void run(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE actionType) {
		CreateJavaDialog epd = new CreateJavaDialog(null, userDB, javaDao);
		epd.open();

		refreshExplorerViewer(actionType);
	}
}

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

import org.eclipse.jface.action.IAction;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJobDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.job.CreateJobDialog;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.util.QueryTemplateUtils;

/**
 * Oracle Database job create action
 * 
 * @author nilriri
 *
 */
public class CreateJobsAction extends AbstractQueryAction {
	private OracleJobDAO jobDao;

	public CreateJobsAction(OracleJobDAO dao) {
		super();
		this.jobDao = dao;
	}

	@Override
	public void run(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE actionType) {
		CreateJobDialog epd = new CreateJobDialog(null, userDB, jobDao);
		epd.open();
	}
}

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
package com.hangum.tadpole.rdb.core.editors.sessionlist.composite;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * MySQL Session composite
 * 
 * @author hangum
 *
 */
public class MySQLSessionListCompoiste extends ASessionListComposite {
	
	/**
	 * 
	 * @param parent
	 * @param userDB
	 */
	public MySQLSessionListCompoiste(Composite parent, UserDBDAO userDB) {
		super(parent, userDB);
	}

	@Override
	protected void initUI() {
		getTableViewerSessionList().setLabelProvider(new SessionListLabelProvider());
		initSessionListData();
	}

	@Override
	protected void initSessionListData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void killProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createColumn() {
		// TODO Auto-generated method stub
		
	}

}

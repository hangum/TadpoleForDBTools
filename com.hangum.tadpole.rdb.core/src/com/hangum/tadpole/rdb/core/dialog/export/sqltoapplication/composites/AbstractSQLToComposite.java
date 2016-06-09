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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine.SQL_TO_APPLICATION;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * abstract sql to composite
 * 
 * @author hangum
 *
 */
public class AbstractSQLToComposite extends Composite {
	protected UserDBDAO userDB;
	protected EditorDefine.SQL_TO_APPLICATION type;
	protected String sql = ""; //$NON-NLS-1$
	
	public AbstractSQLToComposite(Composite parent, int style, UserDBDAO userDB, String sql, SQL_TO_APPLICATION type) {
		super(parent, style);
		
		this.userDB = userDB;
		this.sql = sql;
		this.type = type;
	}
	
	/**
	 * parese sql
	 * @return
	 */
	protected String[] parseSQL() {
		String[] arry = sql.split(PublicTadpoleDefine.SQL_DELIMITER); //$NON-NLS-1$
		 if( arry.length == 1) {
			 String ars[] = { sql };
			 return ars;
		 }
		 return arry;
	}

	@Override
	protected void checkSubclass() {
	}
}

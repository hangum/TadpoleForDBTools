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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;

/**
 * Other connection group
 * 
 * @author hangum
 *
 */
public abstract class AbstractOthersConnection extends Group {
	/**
	 * select database
	 */
	private DBDefine selectDB;

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param selectDB
	 */
	public AbstractOthersConnection(Composite parent, int style, DBDefine selectDB) {
		super(parent, style);
		
		this.selectDB = selectDB;
	}
	
	public DBDefine getSelectDB() {
		return selectDB;
	}
	
	/**
	 * get Default external browser 
	 * 
	 * @return
	 */
	public abstract List<ExternalBrowserInfoDAO> getDefaultExternalBrowserInfo();
	
//	/**
//	 * setting initialize value
//	 * @param str
//	 */
//	public abstract void callBackUIInit(String str);
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}

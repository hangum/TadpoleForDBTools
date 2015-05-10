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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * MariaDB login composite
 * 
 * default port : 4306
 * user			: root
 * password		: tadpole
 * 
 * @author hangum
 *
 */
public class MariaDBLoginComposite extends MySQLLoginComposite {
	private static final Logger logger = Logger.getLogger(MariaDBLoginComposite.class);
	
	public MariaDBLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample MariaDB", DBDefine.MARIADB_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
	
}

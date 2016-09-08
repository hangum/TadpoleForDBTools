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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * agens login composite
 * 
 * @author hangum
 *
 */
public class AgensLoginComposite extends PostgresLoginComposite {
	private static final Logger logger = Logger.getLogger(AgensLoginComposite.class);
	protected Combo comboSSL;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AgensLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample AgensGraph", DBDefine.AGENSGRAPH_DEFAULT, parent, style, listGroupName, selGroupName, userDB); //$NON-NLS-1$
	}

}

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
package com.hangum.tadpole.rdb.core.editors.objectmain;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;

/**
 * Object editor input
 * 
 * @author hangum
 *
 */
public class ObjectEditorInput extends MainEditorInput {
	/** object name */
	private String objectName = "";

	public ObjectEditorInput(UserDBDAO userDB) {
		super(userDB);
	}

	public ObjectEditorInput(UserDBDAO userDB, String lowSQL, OBJECT_TYPE initAction) {
		super(userDB, lowSQL, initAction);
	}
	
	public ObjectEditorInput(UserDBDAO userDB, String objectName, String lowSQL, OBJECT_TYPE initAction) {
		super(userDB, lowSQL, initAction);
		
		this.objectName = objectName;
	}
	
	/**
	 * object name
	 * 
	 * @return
	 */
	public String getObjectName() {
		return objectName;
	}

}

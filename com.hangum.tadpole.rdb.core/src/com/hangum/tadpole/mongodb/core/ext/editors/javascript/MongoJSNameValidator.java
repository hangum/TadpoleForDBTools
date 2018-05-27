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
package com.hangum.tadpole.mongodb.core.ext.editors.javascript;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IInputValidator;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * mongodb javascript name
 * 
 * @author hangum
 *
 */
public class MongoJSNameValidator implements IInputValidator {
	private static final Logger logger = Logger.getLogger(MongoJSNameValidator.class);
	private UserDBDAO userDB;
	private String fileName;
	
	public MongoJSNameValidator(UserDBDAO userDB) {
		super();
		this.userDB = userDB;
	}
	
	@Override
	public String isValid(String newText) {
		int len = newText.length();
		if(len < 5) return Messages.get().FileNameValidator_0;
		
		fileName = newText;
				
		return null;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}

/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.ext.editors.javascript;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IInputValidator;

import com.hangum.tadpole.dao.system.UserDBDAO;
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
		if(len < 5) return Messages.FileNameValidator_0;
		
		fileName = newText;
				
		return null;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}

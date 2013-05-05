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
package com.hangum.tadpole.rdb.core.editors.main;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IInputValidator;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.DB_Define;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.system.TadpoleSystem_UserDBResource;

/**
 * save file name validator
 * 
 * @author hangum
 *
 */
public class FileNameValidator implements IInputValidator {
	private static final Logger logger = Logger.getLogger(FileNameValidator.class);
	private UserDBDAO userDB;
	private String fileName;
	
	public FileNameValidator(UserDBDAO userDB) {
		super();
		this.userDB = userDB;
	}
	
	@Override
	public String isValid(String newText) {
		int len = newText.length();
		if(len < 5) return Messages.FileNameValidator_0;
		try {
			if(!TadpoleSystem_UserDBResource.userDBResourceDuplication(DB_Define.RESOURCE_TYPE.SQL, userDB.getUser_seq(), userDB.getSeq(), newText)) {
				return Messages.FileNameValidator_1;
			}
		} catch (Exception e) {
			logger.error(Messages.FileNameValidator_2, e);
		}
		
		fileName = newText;
				
		return null;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}

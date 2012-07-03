package com.hangum.db.browser.rap.core.editors.main;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IInputValidator;

import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.define.Define;
import com.hangum.db.system.TadpoleSystem_UserDBResource;

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
			if(!TadpoleSystem_UserDBResource.userDBResourceDuplication(Define.RESOURCE_TYPE.SQL, userDB.getUser_seq(), userDB.getSeq(), newText)) {
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
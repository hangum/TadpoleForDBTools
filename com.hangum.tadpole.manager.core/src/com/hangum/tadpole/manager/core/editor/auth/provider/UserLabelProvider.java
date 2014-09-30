package com.hangum.tadpole.manager.core.editor.auth.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;

/**
* 유저 정보 레이블 
* 
* @author hangum
*
*/
public class UserLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(UserLabelProvider.class);
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserGroupAUserDAO user = (UserGroupAUserDAO)element;

		switch(columnIndex) {
		case 0: return user.getUser_group_name();
		case 1: return user.getEmail();
		case 2: return user.getName();
		case 3: return user.getRole_type();
		case 4: return user.getApproval_yn();
		case 5: return user.getDelYn();
		case 6: return user.getCreate_time();
		}
		
		return "*** not set column ***";
	}
	
}

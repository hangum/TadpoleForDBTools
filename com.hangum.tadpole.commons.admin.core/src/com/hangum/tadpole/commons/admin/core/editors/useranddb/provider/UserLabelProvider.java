package com.hangum.tadpole.commons.admin.core.editors.useranddb.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.utils.TimeZoneUtil;

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
		UserDAO user = (UserDAO)element;

		switch(columnIndex) {
		case 0: return user.getEmail();
		case 1: return user.getName();
		case 2: return user.getAllow_ip();
		case 3: return user.getIs_regist_db();

		case 4: return user.getIs_shared_db();
		case 5: return ""+user.getLimit_add_db_cnt();
		case 6: return TimeZoneUtil.dateToStr(user.getService_end());

		case 7: return user.getEmail_key();
		case 8: return user.getApproval_yn();
		case 9: return user.getIs_email_certification();
		

		case 10: return user.getDelYn();
		case 11: return user.getCreate_time();
		}
		
		return "*** not set column ***";
	}
	
}

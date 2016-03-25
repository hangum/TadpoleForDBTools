/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.initialize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;

/**
 * Add default sample db to user
 * 
 * @author hangum
 *
 */
public class AddDefaultSampleDBToUser {
	private static final Logger logger = Logger.getLogger(AddDefaultSampleDBToUser.class);
	
	private static String strDefaultGroup = "SAMPLE DB";
	private static String strDefaultDisplayName = "SAMPLE_Chinnok_Sqlite.sqlite";
	private static String strFileName = "Chinook_Sqlite.sqlite";

	/**
	 * Sampledb copy
	 * 
	 * @param userSeq
	 * @param userEmail
	 * @return
	 */
	public static void addUserDefaultDB(int userSeq, String userEmail) throws Exception {
		String ROOT_RESOURCE_DIR = String.format(ApplicationArgumentUtils.getResourcesDir() + "%s" + PublicTadpoleDefine.DIR_SEPARATOR, userEmail);
		
		File fileRootResource = new File(ROOT_RESOURCE_DIR);
		if(!fileRootResource.isDirectory()) {
			fileRootResource.mkdirs();
		}
		
		// 파일을 옮겨 놓는다.
		copyToSampleDB(ROOT_RESOURCE_DIR);
		
		UserDBDAO userDB = new UserDBDAO();
		userDB.setDbms_type(DBDefine.SQLite_DEFAULT.getDBToString());
		String strUrl = String.format(DBDefine.SQLite_DEFAULT.getDB_URL_INFO(), ROOT_RESOURCE_DIR + strDefaultDisplayName);
		userDB.setUrl(strUrl);
		userDB.setDb(strFileName); //$NON-NLS-1$
		userDB.setGroup_name(strDefaultGroup);
		userDB.setDisplay_name(strDefaultDisplayName);
		userDB.setOperation_type(PublicTadpoleDefine.DBOperationType.DEVELOP.name());
		userDB.setRole_id(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		
		userDB.setPasswd(""); //$NON-NLS-1$
		userDB.setUsers(""); //$NON-NLS-1$
		
		userDB.setIs_readOnlyConnect(PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_autocommit(PublicTadpoleDefine.YES_NO.YES.name());
		userDB.setIs_showtables(PublicTadpoleDefine.YES_NO.YES.name());
		
		userDB.setIs_profile(PublicTadpoleDefine.YES_NO.YES.name());
		userDB.setQuestion_dml(PublicTadpoleDefine.YES_NO.NO.name());
		
		userDB.setIs_external_browser(PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setListExternalBrowserdao(new ArrayList<ExternalBrowserInfoDAO>());
		userDB.setIs_summary_report(PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_monitoring(PublicTadpoleDefine.YES_NO.NO.name());

		TadpoleSystem_UserDBQuery.newUserDB(userDB, userSeq);
	}
	
	/**
	 * copy to sampel db
	 * 
	 * @param toLocation
	 * @throws Exception
	 */
	private static void copyToSampleDB(String toLocation) throws Exception {
		ClassLoader classLoader = TadpoleSystemInitializer.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("com/hangum/tadpole/engine/initialize/Chinook_Sqlite.sqlite");
		
		byte[] dataByte = new byte[1024];
		int len = 0;
		
		FileOutputStream fos = new FileOutputStream(toLocation + strDefaultDisplayName);
		while((len = is.read(dataByte)) > 0) {
			fos.write(dataByte, 0, len);
		}
		
		fos.close();
		is.close();
	}

}

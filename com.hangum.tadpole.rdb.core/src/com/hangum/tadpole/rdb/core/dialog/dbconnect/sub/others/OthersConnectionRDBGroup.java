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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Others RDB connection information
 * 
 * @author hangum
 *
 */
public class OthersConnectionRDBGroup extends OthersConnectionGroup {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OthersConnectionRDBGroup(Composite parent, int style, DBDefine selectDB) {
		super(parent, style, selectDB);
//		btnSendMonitoring.setText("Summary ");
	}
	
	/**
	 * 기존에 데이터를 가지고 있었을 경우에 값을 설정 합니다.
	 * @param oldUserDB
	 */
	public void setUserData(UserDBDAO oldUserDB) {
		setBtnReadOnlyConnection(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_readOnlyConnect())?true:false);
		setBtnAutoCommit(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_autocommit())?true:false);
		setBtnShowTables(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_showtables())?true:false);
		
		setBtnProfiler(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_profile())?true:false);
		
		setBtnExecuteQuestionDml(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getQuestion_dml())?true:false);
		
//		setIsVisible(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_visible())?true:false);
//		setSendMonitoring(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_summary_report())?true:false);
//		setIsMonitoring(PublicTadpoleDefine.YES_NO.YES.name().equals(oldUserDB.getIs_monitoring())?true:false);
	}
	
	@Override
	public void initUI() {
//		if(getSelectDB() == DBDefine.MYSQL_DEFAULT || getSelectDB() == DBDefine.MARIADB_DEFAULT) {
//			btnSendMonitoring.setEnabled(true);
////			btnIsMonitoring.setEnabled(true);
//		} else {
//			btnSendMonitoring.setEnabled(false);
//			btnIsMonitoring.setEnabled(false);
//		}
	}
	
}

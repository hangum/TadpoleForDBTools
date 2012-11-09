/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.PingTest;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 로그인시에 사용하게될 디비의 abstract composite
 * 
 * @author hangum
 *
 */
public abstract class AbstractLoginComposite extends Group {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3434604591881525231L;
	private static final Logger logger = Logger.getLogger(AbstractLoginComposite.class);
	
	protected String strOtherGroupName = "Other Group"; //$NON-NLS-1$
	protected String selGroupName = ""; //$NON-NLS-1$
	
	protected List<String> listGroupName = new ArrayList<String>();

	/** 기존에 접속한 user db */
	protected UserDBDAO oldUserDB = null;

	protected UserDBDAO userDB;
	protected DBDefine selectDB;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractLoginComposite(DBDefine dbDefine, Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO oldUserDB) {
		super(parent, style);
		this.selectDB = dbDefine;
		this.listGroupName = listGroupName;
		this.selGroupName = selGroupName;
		this.oldUserDB = oldUserDB;
		
		crateComposite();
	}

	/**
	 * 
	 * @param parent
	 * @return
	 */
	protected abstract void crateComposite();
	
	/**
	 * 초기데이터 로드 및 처리 
	 */
	protected abstract void init();
	
	/**
	 * DB 연결
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean connection();

	/**
	 * DB 가 정상 연결되었을때 객체
	 * 
	 * @return
	 */
	protected UserDBDAO getDBDTO() {
		return userDB;
	}
	
	/**
	 * host, port에 ping
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public boolean isPing(String host, String port) throws NumberFormatException {
		
		// TO DO db가 드릴경우(?) 핑이 늦게와서 좀 늘려... 방법이 없을까? - hangum
		int stats = PingTest.ping(host, Integer.parseInt(port), 2500);
		if(PingTest.SUCCESS == stats) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * db 연결정보가 올바른지 검증합니다.
	 * 
	 * @param loginInfo
	 * @param searchTable 디비의 테이블 검증을위한 쿼리 
	 * @return
	 */
	protected boolean connectValidate(UserDBDAO loginInfo) {
		
		try {
			// 이미 등록된 디비 인지 검사합니다.
			if(TadpoleSystem_UserDBQuery.isAlreadyExistDB(SessionManager.getSeq(), loginInfo.getUrl(), loginInfo.getUsers())) {
				MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.AbstractLoginComposite_2);
				
				return false;
			}
		} catch(Exception e) {
			logger.error("DB Connecting... ", e); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.DBLoginDialog_26, Messages.DBLoginDialog_27 + "\n" + e.getMessage()); //$NON-NLS-1$
			
			return false;
		}
		
		// 디비가 정상적인지 등록하려는 디비의 테이블 정보를 검사합니다.
		if(checkDatabase(loginInfo)) return true;
		else return false;
	}
	
	/**
	 * db가 정상적으로 접속가능한지 검사합니다.
	 * @param loginInfo
	 * @return
	 */
	protected boolean checkDatabase(UserDBDAO loginInfo) {
		if(DBDefine.getDBDefine(loginInfo.getTypes()) != DBDefine.MONGODB_DEFAULT) {
			try {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(loginInfo);
				List showTables = sqlClient.queryForList("tableList", loginInfo.getDb()); //$NON-NLS-1$
				
			} catch (Exception e) {
				logger.error("DB Connecting... ", e); //$NON-NLS-1$
				MessageDialog.openError(null, Messages.DBLoginDialog_26, Messages.DBLoginDialog_27 + "\n" + e.getMessage()); //$NON-NLS-1$
				
				return false;
			}
		}
		
		return true;
	}

}

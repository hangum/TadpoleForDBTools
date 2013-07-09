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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DATA_STATUS;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionRDBGroup;
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
public abstract class AbstractLoginComposite extends Composite {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3434604591881525231L;
	private static final Logger logger = Logger.getLogger(AbstractLoginComposite.class);
	
	protected DATA_STATUS dalog_status = DATA_STATUS.NEW;
	
	protected String displayName = "";
	
	protected PreConnectionInfoGroup preDBInfo;
	protected OthersConnectionRDBGroup othersConnectionInfo;
	
	protected String strOtherGroupName = "Other Group"; //$NON-NLS-1$
	protected String selGroupName = ""; //$NON-NLS-1$
	
	protected List<String> listGroupName = new ArrayList<String>();
	
	// start table filters define
	protected boolean isTableFilter = false;
	protected String strTableFilterInclude = "";
	protected String strTableFilterExclude = "";
	// end table filters define

	/** 기존에 접속한 user db */
	protected UserDBDAO oldUserDB = null;

	protected UserDBDAO userDB;
	protected DBDefine selectDB;
	
	/**
	 * Create the composite.
	 * @param displayName 
	 * @param parent
	 * @param style
	 */
	public AbstractLoginComposite(String displayName, DBDefine dbDefine, Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO oldUserDB) {
		super(parent, style);
		
		this.displayName = displayName;
		this.selectDB = dbDefine;
		this.listGroupName = listGroupName;
		this.selGroupName = selGroupName;
		this.oldUserDB = oldUserDB;
		
		crateComposite();
	}
	
	/**
	 * dialog 상태 신규데이터 인지 수정상태인지.
	 * @return
	 */
	public DATA_STATUS getDalog_status() {
		return dalog_status;
	}
	
	/**
	 * dialog 상태 신규데이터 인지 수정상태인지.
	 * 
	 * @param dalog_status
	 */
	public void setDalog_status(DATA_STATUS dalog_status) {
		this.dalog_status = dalog_status;
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
	public abstract boolean connection();
	
	/**
	 * test connection
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract boolean testConnection();
	
	/**
	 * DB 가 정상 연결되었을때 객체
	 * 
	 * @return
	 */
	public UserDBDAO getDBDTO() {
		return userDB;
	}
	
	/**
	 * host, port에 ping
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @deprecated 서버에따라 핑 서비스를 막아 놓는 경우도 있어, 막아 놓습니다.
	 */
	public boolean isPing(String host, String port) throws NumberFormatException {
		
//		TODO system 네트웍 속도가 느릴경우(?) 핑이 늦게와서 좀 늘려... 방법이 없을까? - hangum
		int stats = PingTest.ping(host, Integer.parseInt(port), 2500);
		if(PingTest.SUCCESS == stats) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * database의 중복 입력, 실제 연결할 수 있는지 검사합니다.
	 * 
	 * @param userDB
	 * @return
	 */
	protected boolean isValidateDatabase(final UserDBDAO userDB) {
		if(!checkDatabase(userDB)) return false;
		if(!isAlreadExistDB(userDB)) return false;
		
		return true;
	}
	
	/**
	 * db가 이미 저장되어 있는지 검사합니다.
	 * 
	 * @param loginInfo
	 * @param searchTable 디비의 테이블 검증을위한 쿼리 
	 * @return
	 */
	private boolean isAlreadExistDB(UserDBDAO loginInfo) {
		
		try {
			// 이미 등록된 디비 인지 검사합니다.
			if(TadpoleSystem_UserDBQuery.isAlreadyExistDB(SessionManager.getSeq(), loginInfo.getUrl(), loginInfo.getUsers())) {
//				MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.AbstractLoginComposite_2);
				// 중복 디비 등록시 사용자의 의견을 묻습니다.
				if(MessageDialog.openConfirm(null, Messages.DBLoginDialog_23, Messages.AbstractLoginComposite_2)) {
					return true;
				} 
				
				return false;
			}
		} catch(Exception e) {
			logger.error("DB Connecting... ", e); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.DBLoginDialog_26, Messages.DBLoginDialog_27 + "\n" + e.getMessage()); //$NON-NLS-1$
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * db가 정상적으로 접속가능한지 검사합니다.
	 * @param loginInfo
	 * @return
	 */
	private boolean checkDatabase(UserDBDAO loginInfo) {
		if(DBDefine.getDBDefine(loginInfo.getDbms_types()) != DBDefine.MONGODB_DEFAULT) {
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
	
	/**
	 * text message
	 * 
	 * @param text
	 * @param msg
	 * @return
	 */
	protected boolean checkTextCtl(Text text, String msg) {
		if("".equals(StringUtils.trimToEmpty(text.getText()))) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.DBLoginDialog_10, msg + Messages.MySQLLoginComposite_10);
			text.setFocus();
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * combo message
	 * 
	 * @param text
	 * @param msg
	 * @return
	 */
	protected boolean checkTextCtl(Combo text, String msg) {
		if("".equals(StringUtils.trimToEmpty(text.getText()))) { //$NON-NLS-1$
			MessageDialog.openError(null, Messages.DBLoginDialog_10, msg + Messages.MySQLLoginComposite_10);
			text.setFocus();
			
			return false;
		}
		
		return true;
	}

	/**
	 * @return the isTableFilter
	 */
	public boolean isTableFilter() {
		return isTableFilter;
	}

	/**
	 * @param isTableFilter the isTableFilter to set
	 */
	public void setTableFilter(boolean isTableFilter) {
		this.isTableFilter = isTableFilter;
	}

	/**
	 * @return the strTableFilterInclude
	 */
	public String getStrTableFilterInclude() {
		return strTableFilterInclude;
	}

	/**
	 * @param strTableFilterInclude the strTableFilterInclude to set
	 */
	public void setStrTableFilterInclude(String strTableFilterInclude) {
		this.strTableFilterInclude = strTableFilterInclude;
	}

	/**
	 * @return the strTableFilterExclude
	 */
	public String getStrTableFilterExclude() {
		return strTableFilterExclude;
	}

	/**
	 * @param strTableFilterExclude the strTableFilterExclude to set
	 */
	public void setStrTableFilterExclude(String strTableFilterExclude) {
		this.strTableFilterExclude = strTableFilterExclude;
	}
	
	/**
	 * display name
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	public DBDefine getSelectDB() {
		return selectDB;
	}
	
	public List<String> getListGroupName() {
		return listGroupName;
	}
	
	public String getSelGroupName() {
		return selGroupName;
	}
	
}

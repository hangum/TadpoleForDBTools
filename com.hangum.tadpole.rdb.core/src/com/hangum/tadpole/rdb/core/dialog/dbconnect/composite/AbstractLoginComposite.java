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

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DATA_STATUS;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.PingTest;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
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

	/** 현재 창의 저장, 수정 상태인지 상태. */
	protected DATA_STATUS dataActionStatus = DATA_STATUS.NEW;
	
	protected String displayName = ""; //$NON-NLS-1$
	
	protected PreConnectionInfoGroup preDBInfo;
	protected OthersConnectionGroup othersConnectionInfo;
	
	protected String strOtherGroupName = "Other Group"; //$NON-NLS-1$
	protected String selGroupName = ""; //$NON-NLS-1$
	
	protected List<String> listGroupName = new ArrayList<String>();
	
	// start table filters define
	protected boolean isTableFilter = false;
	protected String strTableFilterInclude = ""; //$NON-NLS-1$
	protected String strTableFilterExclude = ""; //$NON-NLS-1$
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
	 * 현재 창의 저장, 수정 상태인지 상태.
	 * @return
	 */
	public DATA_STATUS getDataActionStatus() {
		return dataActionStatus;
	}
	
	/**
	 * dialog 상태 신규데이터 인지 수정상태인지.
	 * 
	 * @param dalog_status
	 */
	public void setDataActionStatus(DATA_STATUS dalog_status) {
		this.dataActionStatus = dalog_status;
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
	public boolean connection() {
		if(!testConnection(false)) return false;
		
		// 기존 데이터 업데이트
		if(getDataActionStatus() == DATA_STATUS.MODIFY) {
			if(!MessageDialog.openConfirm(null, "Confirm", Messages.SQLiteLoginComposite_13)) return false; //$NON-NLS-1$
			
			try {
				TadpoleSystem_UserDBQuery.updateUserDB(userDB, oldUserDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error(Messages.SQLiteLoginComposite_8, e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.SQLiteLoginComposite_5, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
		// 신규 데이터 저장.
		} else {
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getSeq());
			} catch (Exception e) {
				logger.error(Messages.AbstractLoginComposite_0, e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.MySQLLoginComposite_2, errStatus); //$NON-NLS-1$
				
				return false;
			}
		}

		return true;
	}
	
	/**
	 * input validation
	 * 
	 * @return
	 */
	public abstract boolean isValidateInput(boolean isTest);
	
	/**
	 * test connection
	 * 
	 * @param isTest
	 * @return
	 * @throws Exception
	 */
	public boolean testConnection(boolean isTest) {
		if(!makeUserDBDao(isTest)) return false;
		if(!isValidateDatabase(userDB, isTest)) return false;
		
		return true;
	}
	
	/**
	 * 1. input validation
	 * 2. make UserDBDAO
	 */
	public abstract boolean makeUserDBDao(boolean isTest);
	
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
	 * @param isTest
	 * @return
	 */
	protected boolean isValidateDatabase(final UserDBDAO userDB, boolean isTest) {
		if(!checkDatabase(userDB, isTest)) return false;
		if(!isAlreadExistDB(userDB)) return false;
		
		return true;
	}
	
	/**
	 * db가 이미 저장되어 있는지 검사합니다.
	 * 
	 * @param userDBDao
	 * @param searchTable 디비의 테이블 검증을위한 쿼리 
	 * @return
	 */
	private boolean isAlreadExistDB(UserDBDAO userDBDao) {

		try {
			// 기존 데이터 업데이트
			if(getDataActionStatus() == DATA_STATUS.MODIFY) {
				// 정보가 완전 같아 입력이 안되는 아이가 있는지 검사합니다.
				// 최소한 display_name이라도 틀려야 한다.
				if(TadpoleSystem_UserDBQuery.isOldDBValidate(SessionManager.getSeq(), userDBDao, oldUserDB)) {
					MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.AbstractLoginComposite_4);
					return false;
				}
				
			} else {
				// 정보가 완전 같아 입력이 안되는 아이가 있는지 검사합니다.
				// 최소한 display_name이라도 틀려야 한다.
				if(TadpoleSystem_UserDBQuery.isNewDBValidate(SessionManager.getSeq(), userDBDao)) {
					MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.AbstractLoginComposite_4);
					
					return false;
				}
				
				// 이름은 틀리지만, 다른 정보는 같은 이미 등록된 디비 인지 검사합니다.
				if(TadpoleSystem_UserDBQuery.isAlreadyExistDB(SessionManager.getSeq(), userDBDao)){
					
					// 중복 디비 등록시 사용자의 의견을 묻습니다.
					if(MessageDialog.openConfirm(null, Messages.DBLoginDialog_23, Messages.AbstractLoginComposite_2)) {
						return true;
					} 
					
					return false;
				}
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
	 * 
	 * @param loginInfo
	 * @param isTest
	 * @return
	 */
	private boolean checkDatabase(UserDBDAO loginInfo, boolean isTest) {
		try {
			if(DBDefine.getDBDefine(loginInfo) == DBDefine.MONGODB_DEFAULT) {
				MongoConnectionManager.getInstance(userDB);
				
			} else if(DBDefine.getDBDefine(loginInfo) == DBDefine.TAJO_DEFAULT) {
				new TajoConnectionManager().connectionCheck(loginInfo);
				
			} else if(DBDefine.getDBDefine(loginInfo) == DBDefine.SQLite_DEFAULT) {
				String strFileLoc = StringUtils.difference(StringUtils.remove(loginInfo.getDBDefine().getDB_URL_INFO(), "%s"), loginInfo.getUrl());
				File fileDB = new File(strFileLoc);
				if(fileDB.exists()) {
					List<String> strArr = FileUtils.readLines(fileDB);
					
					if(!StringUtils.contains(strArr.get(0), "SQLite format")) {
						throw new SQLException("Doesn't SQLite files.");
					}
				}
				
			} else {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(loginInfo);
				sqlClient.queryForList("connectionCheck", loginInfo.getDb()); //$NON-NLS-1$
			}
			
			return true;
		} catch (Exception e) {
			logger.error("DB Connecting... ", e); //$NON-NLS-1$
			// If UserDBDao is not invalid, remove UserDBDao at internal cache
			TadpoleSQLManager.removeInstance(loginInfo);

			// mssql 데이터베이스가 연결되지 않으면 등록되면 안됩니다. 하여서 제외합니다.
			if(!isTest && loginInfo.getDBDefine() != DBDefine.MSSQL_DEFAULT) {
				if(MessageDialog.openConfirm(null, Messages.DBLoginDialog_26, Messages.AbstractLoginComposite_3  + PublicTadpoleDefine.DOUBLE_LINE_SEPARATOR + e.getMessage())) return true;
			} else {
				MessageDialog.openError(null, "Confirm", Messages.AbstractLoginComposite_1 + PublicTadpoleDefine.DOUBLE_LINE_SEPARATOR + e.getMessage());
			}
			
			return false;
		}
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
	
	public OthersConnectionGroup getOthersConnectionInfo() {
		return othersConnectionInfo;
	}
	
	/**
	 * Set others connection info
	 */
	protected void setOtherConnectionInfo() {
		OthersConnectionInfoDAO otherConnectionDAO =  othersConnectionInfo.getOthersConnectionInfo();
		userDB.setIs_readOnlyConnect(otherConnectionDAO.isReadOnlyConnection()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setIs_autocommit(otherConnectionDAO.isAutoCommit()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setIs_showtables(otherConnectionDAO.isShowTables()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		
		userDB.setIs_table_filter(otherConnectionDAO.isTableFilter()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setTable_filter_include(otherConnectionDAO.getStrTableFilterInclude());
		userDB.setTable_filter_exclude(otherConnectionDAO.getStrTableFilterExclude());
		
		userDB.setIs_profile(otherConnectionDAO.isProfiling()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setQuestion_dml(otherConnectionDAO.isDMLStatement()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		
		userDB.setIs_external_browser(otherConnectionDAO.isExterBrowser()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setListExternalBrowserdao(otherConnectionDAO.getListExterBroswer());
		
		userDB.setIs_visible(otherConnectionDAO.isVisible()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setIs_summary_report(otherConnectionDAO.isSummaryReport()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
	}

}

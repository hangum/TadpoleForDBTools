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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DATA_STATUS;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBYesNoErroDialog;
import com.hangum.tadpole.session.manager.SessionManager;
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
	 * ping test
	 * 
	 * @param host
	 * @param port
	 */
	protected void pingTest(String host, String port) {
		host 	= StringUtils.trimToEmpty(host);
		port 	= StringUtils.trimToEmpty(port);
		
		if("".equals(host) || "".equals(port)) { //$NON-NLS-1$ //$NON-NLS-2$
			MessageDialog.openWarning(null, Messages.get().Warning, String.format(Messages.get().DBLoginDialog_11, Messages.get().Host, Messages.get().Port));
			return;
		}
		
		try {
			if(ValidChecker.isPing(host, port)) {
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().DBLoginDialog_13);
			} else {
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().DBLoginDialog_15);
			}
		} catch(NumberFormatException nfe) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().MySQLLoginComposite_4);
		}
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
	 * DB 정보 저장.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean saveDBData() {
		if(!testConnection(false)) return false;
		
		// 기존 데이터 업데이트
		if(getDataActionStatus() == DATA_STATUS.MODIFY) {
			if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().SQLiteLoginComposite_13)) return false; //$NON-NLS-1$
			
			try {
				TadpoleSystem_UserDBQuery.updateUserDB(userDB, oldUserDB, SessionManager.getUserSeq());
			} catch (Exception e) {
				logger.error("DB moidfy data", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), Messages.get().Error, Messages.get().SQLiteLoginComposite_5, errStatus); //$NON-NLS-1$
				
				return false;
			}
			
		// 신규 데이터 저장.
		} else {
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB, SessionManager.getUserSeq());
			} catch (Exception e) {
				logger.error("Add database", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), Messages.get().Error, Messages.get().MySQLLoginComposite_2, errStatus); //$NON-NLS-1$
				
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
	 * database의 중복 입력, 실제 연결할 수 있는지 검사합니다.
	 * 
	 * @param userDB
	 * @param isTest
	 * @return
	 */
	protected boolean isValidateDatabase(final UserDBDAO userDB, boolean isTest) {
		if(!checkDatabase(userDB, isTest)) return false;
		if(!isTest) if(!isAlreadExistDB(userDB)) return false;
		
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
				if(TadpoleSystem_UserDBQuery.isOldDBValidate(SessionManager.getUserSeq(), userDBDao, oldUserDB)) {
					MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().AbstractLoginComposite_4);
					return false;
				}
				
			} else {
				// 정보가 완전 같아 입력이 안되는 아이가 있는지 검사합니다.
				// 최소한 display_name이라도 틀려야 한다.
				if(TadpoleSystem_UserDBQuery.isNewDBValidate(SessionManager.getUserSeq(), userDBDao)) {
					MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().AbstractLoginComposite_4);
					
					return false;
				}
				
				// 이름은 틀리지만, 다른 정보는 같은 이미 등록된 디비 인지 검사합니다.
				if(TadpoleSystem_UserDBQuery.isAlreadyExistDB(SessionManager.getUserSeq(), userDBDao)){
					
					// 중복 디비 등록시 사용자의 의견을 묻습니다.
					if(MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().AbstractLoginComposite_2)) {
						return true;
					} 
					
					return false;
				}
			}
			
		} catch(Exception e) {
			logger.error("DB Connecting... ", e); //$NON-NLS-1$
			MessageDialog.openError(null, Messages.get().Error, Messages.get().DBLoginDialog_27 + "\n" + e.getMessage()); //$NON-NLS-1$
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * db가 정상적으로 접속가능한지 검사합니다.
	 * 
	 * @param userDB
	 * @param isTest
	 * @return
	 */
	private boolean checkDatabase(final UserDBDAO userDB, boolean isTest) {
		try {
			if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
				MongoConnectionManager.getInstance(userDB);
				
			} else if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
				new TajoConnectionManager().connectionCheck(userDB);
				
			} else if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {
				String strFileLoc = StringUtils.difference(StringUtils.remove(userDB.getDBDefine().getDB_URL_INFO(), "%s"), userDB.getUrl());
				File fileDB = new File(strFileLoc);
				if(fileDB.exists()) {
					List<String> strArr = FileUtils.readLines(fileDB);
					
					if(!StringUtils.contains(strArr.get(0), "SQLite format")) {
						throw new SQLException("Doesn't SQLite files.");
					}
				}
				
			} else {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
				sqlClient.queryForList("connectionCheck", userDB.getDb()); //$NON-NLS-1$
			}
			
			return true;
		} catch (Exception e) {
			String errMsg = e.getMessage();

			// If UserDBDao is not invalid, remove UserDBDao at internal cache
			logger.error("DB Connecting... [url]"+ userDB.getUrl(), e); //$NON-NLS-1$
			TadpoleSQLManager.removeInstance(userDB);
			
			// driver 가 없을때 메시지 추가.
			try {
				Throwable cause = e.getCause().getCause();
				if(cause instanceof ClassNotFoundException) {
					errMsg = String.format(Messages.get().TadpoleTableComposite_driverMsg, userDB.getDbms_type(), e.getMessage());
				}
			} catch(Exception ee) {
				// igonre exception
			}
			
			// mssql 데이터베이스가 연결되지 않으면 등록되면 안됩니다. 하여서 제외합니다.
			// https://github.com/hangum/TadpoleForDBTools/issues/512 
			if(!isTest) {// && loginInfo.getDBDefine() != DBDefine.MSSQL_DEFAULT) {
				TDBYesNoErroDialog dialog = new TDBYesNoErroDialog(getShell(), 
													Messages.get().DBLoginDialog_9, 
													String.format(Messages.get().AbstractLoginComposite_3, errMsg));
				if(dialog.open() == IDialogConstants.OK_ID) return true;
			
			} else {
				TDBErroDialog dialog = new TDBErroDialog(getShell(), Messages.get().DBLoginDialog_43, errMsg);
				dialog.open();
			}
			
			return false;
		}
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
		userDB.setIs_readOnlyConnect(otherConnectionDAO.isReadOnlyConnection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_autocommit(otherConnectionDAO.isAutoCommit()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_showtables(otherConnectionDAO.isShowTables()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		
//		userDB.setIs_table_filter(otherConnectionDAO.isTableFilter()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
//		userDB.setTable_filter_include(otherConnectionDAO.getStrTableFilterInclude());
//		userDB.setTable_filter_exclude(otherConnectionDAO.getStrTableFilterExclude());
		
		userDB.setIs_profile(otherConnectionDAO.isProfiling()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setQuestion_dml(otherConnectionDAO.isDMLStatement()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		
		userDB.setIs_external_browser(otherConnectionDAO.isExterBrowser()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setListExternalBrowserdao(otherConnectionDAO.getListExterBroswer());
		
//		userDB.setIs_visible(otherConnectionDAO.isVisible()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_summary_report(otherConnectionDAO.isSummaryReport()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		userDB.setIs_monitoring(otherConnectionDAO.isMonitoring()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
	}

}

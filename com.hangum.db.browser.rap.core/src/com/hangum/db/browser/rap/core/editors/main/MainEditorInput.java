package com.hangum.db.browser.rap.core.editors.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.util.QueryTemplateUtils;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.dao.system.UserDBResourceDAO;
import com.hangum.db.dao.system.UserDBResourceDataDAO;
import com.hangum.db.define.Define;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.system.TadpoleSystem_UserDBResource;

/**
 * main editor의 input
 * 
 * @author hangumNote
 *
 */
public class MainEditorInput implements IEditorInput {
	private static final Logger logger = Logger.getLogger(MainEditorInput.class);
	
	/** 에디터의 오픈 타입 정의 */
	private Define.EDITOR_OPEN_TYPE OPEN_TYPE = Define.EDITOR_OPEN_TYPE.NONE;
	
	private UserDBDAO userDB;
	private Define.DB_ACTION action;
	private String defaultStr = ""; //$NON-NLS-1$
	private UserDBResourceDAO resourceDAO;
	
	
	/**
	 * query창에을 공백으로
	 * 
	 * @param userDB
	 */
	public MainEditorInput(UserDBDAO userDB) {
		this.userDB = userDB;
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.NONE;
	}
	
	/**
	 * query창에 기본텍스트 출력된 체로
	 * 
	 * @param userDB
	 * @param defaultStr
	 */
	public MainEditorInput(UserDBDAO userDB, String defaultStr) {
		this.userDB = userDB;
		this.defaultStr = defaultStr;
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.STRING;
	}
	
	/**
	 * 기존 리소스 호출
	 * @param userDB
	 * @param dao
	 */
	public MainEditorInput(UserDBResourceDAO dao) throws Exception {
		this.userDB = dao.getParent();
		this.resourceDAO = dao;
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.FILE;
		
		this.defaultStr = TadpoleSystem_UserDBResource.getResourceData(dao);
		
//		try {
//			this.defaultStr = fileLoad(Define.SQL_FILE_LOCATION + resourceDAO.getFilepath() + resourceDAO.getFilename() + ".sql"); //$NON-NLS-1$
//		} catch(Exception e) {
//			logger.error("file load", e); //$NON-NLS-1$
//			
//		}
	}
	
//	/**
//	 * 초기 파일을 로드
//	 * @return
//	 */
//	private String fileLoad(String fileName) throws Exception {
//		StringBuffer sb = new StringBuffer();
//		FileReader fr = null;
//		BufferedReader br = null;
//		try {
//			fr = new FileReader(new File(fileName));
//			br = new BufferedReader(fr);
//		    String eachLine = br.readLine();
//
//		    while (eachLine != null) {
//		      sb.append(eachLine);
//		      sb.append("\n"); //$NON-NLS-1$
//		      eachLine = br.readLine();
//		    }
//			
//		} finally {
//			try { br.close(); } catch(Exception e) {}
//			try { fr.close(); } catch(Exception e) {}
//		}
//		
//		return sb.toString();
//	}
	
	/**
	 * query창에 action 타입에 따른 기본 텍스트 출력
	 * @param userDB
	 * @param action
	 */
	public MainEditorInput(UserDBDAO userDB, Define.DB_ACTION initAction) {
		this.userDB = userDB;
		this.action = initAction;
		this.defaultStr = QueryTemplateUtils.getQuery(userDB, initAction);
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.STRING;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return userDB.getDisplay_name();//User() + "@" + userDB.getDatabase();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return userDB.getDisplay_name();// + "@" + userDB.getDatabase();
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}

	public String getDefaultStr() {
		return defaultStr;
	}
	
	public Define.EDITOR_OPEN_TYPE getOPEN_TYPE() {
		return OPEN_TYPE;
	}

	public UserDBResourceDAO getResourceDAO() {
		return resourceDAO;
	}
}

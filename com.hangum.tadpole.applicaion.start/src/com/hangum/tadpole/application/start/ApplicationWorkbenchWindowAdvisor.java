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
package com.hangum.tadpole.application.start;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ExitConfirmation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hangum.tadpole.application.start.dialog.login.LoginDialog;
import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.preference.define.SystemDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.connections.ConnectDatabase;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystemInitializer;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.system.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.system.TadpoleSystem_UserQuery;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final Logger logger = Logger.getLogger(ApplicationWorkbenchWindowAdvisor.class);

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
//    	not support rap yet.
//    	String prop = IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS;
//    	PlatformUI.getPreferenceStore().setValue(prop, false);
    	
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();        
        configurer.setInitialSize(new Point(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        
        configurer.setShowProgressIndicator(false);
        configurer.setTitle(SystemDefine.NAME + " " + SystemDefine.MAJOR_VERSION + " SR" + SystemDefine.SUB_VERSION); //$NON-NLS-1$ //$NON-NLS-2$
        
        // browser화면 최대화 되도록 하고, 최소화 최대화 없도록 수정
        getWindowConfigurer().setShellStyle(SWT.NO_TRIM);
        getWindowConfigurer().setShowMenuBar(false);
    
        // 시스템 종료 메시지 출력시킬 것인지.
        ExitConfirmation service = RWT.getClient().getService( ExitConfirmation.class );
    	service.setMessage(Messages.ApplicationWorkbenchWindowAdvisor_4);
    
        initSystem();
    }
    
    /**
     * 시스템 초기화 
     */
    private void initSystem() {
    	// tadpole의 시스템 테이블이 존재 하지 않는다면 테이블을 생성합니다.
    	try {
    		TadpoleSystemInitializer.initSystem();
    		
    	} catch(Exception e) {
    		logger.error("System initialize", e); //$NON-NLS-1$
    		Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", com.hangum.tadpole.application.start.Messages.ApplicationWorkbenchWindowAdvisor_2, errStatus); //$NON-NLS-1$
    		
    		System.exit(0);
    	}
    	
    	// 이미 로그인 되어 있는지 검사합니다.
    	if(0 == SessionManager.getSeq()) {
    	
	    	// login dialog를 뜨도록합니다.    
	    	LoginDialog loginDialog = new LoginDialog(Display.getCurrent().getActiveShell());
	    	
	    	// 로그인 취소 버튼을 누르면 manager 권한으로 사용하도록 합니다.
	    	if(Dialog.OK != loginDialog.open()) {
	    		
	    		String userId = TadpoleSystemInitializer.MANAGER_EMAIL;
				String password = TadpoleSystemInitializer.MANAGER_PASSWD;
		    	try {
					UserDAO login = TadpoleSystem_UserQuery.login(userId, password);
					if(!Define.USER_TYPE.MANAGER.toString().equals(login.getUser_type())) {
						UserDAO groupManagerUser =  TadpoleSystem_UserQuery.getGroupManager(login.getGroup_seq());
						
						SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), groupManagerUser.getSeq());
					}  else {
						SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), -1);
					}
					
					initSession();
				} catch (Exception e) {
					logger.error("demo mode user login", e); //$NON-NLS-1$
					MessageDialog.openError(getWindowConfigurer().getWindow().getShell(), Messages.LoginDialog_7, e.getMessage());
					return;
				}	
	    	} else {
	    		try {
		    		// 사용자 세션을 저장한다.
					List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.allUserInfoData();
					Map<String, Object> mapUserInfoData = new HashMap<String, Object>();
					for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
//						if(logger.isDebugEnabled()) logger.debug("[userInfoDataDAO] " + userInfoDataDAO.getName() + ":" + userInfoDataDAO);
						
						mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
					}
					SessionManager.setUserInfos(mapUserInfoData);
					
					initSession();
					
	    		} catch(Exception e) {
	    			logger.error("session set", e); //$NON-NLS-1$
	    		}
	    	}
    	} 
    }
    
    @Override
    public void postWindowOpen() {
    	// 로그인 이후 디비가 없을 경우 디비 커넥트 다이얼로그가 출력되도록 합니다.
    	try {
    		List<UserDBDAO> listUserDB = TadpoleSystem_UserDBQuery.getUserDB();
    		if(listUserDB.size() == 0) {
    			ConnectDatabase cd = new ConnectDatabase();
    			cd.run();
    		}    		
    	} catch(Exception e) {
    		logger.error("list user db", e); //$NON-NLS-1$
    	}    	
    }
    
    /**
	 * session을 설정합니다.
	 */
	private void initSession() {
		HttpSession iss = RWT.getRequest().getSession();
		
		int sessionTimeOut = Integer.parseInt(GetPreferenceGeneral.getSessionTimeout());		
		if(sessionTimeOut <= 0) {
			iss.setMaxInactiveInterval( 60 * 60 * 24 );
		} else {
			iss.setMaxInactiveInterval(Integer.parseInt(GetPreferenceGeneral.getSessionTimeout()) * 60);
		}
	}	// end method
    
    @Override
    public void postWindowCreate() {
    	
        // browser화면 최대화 되도록 하고, 최소화 최대화 없도록 수정
//    	현재는 rcp 버전은 삭제
//    	if(getTadpoleRAPBundle()) {
	    	Shell shell = getWindowConfigurer().getWindow().getShell();
	    	if(shell == null) {
	    		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	    	}
	    	shell.setMaximized(true);
//        }	    
    }
    
//
//	/**
//     * 현재 rap번들이 실행중인지
//     * @return
//     */
//    public boolean getTadpoleRAPBundle() {
//    	Bundle bundle = Platform.getBundle("com.hangum.db.browser.rap"); //$NON-NLS-1$
//        if(bundle != null) return true;
//        
//        return false;
//    }
}
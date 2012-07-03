package com.hangum.db.browser.rap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.dialog.login.LoginDialog;
import com.hangum.db.commons.session.SessionManager;
import com.hangum.db.dao.system.UserDAO;
import com.hangum.db.define.Define;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.system.TadpoleSystemConnector;
import com.hangum.db.system.TadpoleSystem_UserQuery;

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
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        
        configurer.setInitialSize(new Point(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        
        configurer.setShowProgressIndicator(true);
        configurer.setTitle("Tadpole for DB Tools Beta"); //$NON-NLS-1$
        
        // browser화면 최대화 되도록 하고, 최소화 최대화 없도록 수정
//        if(getTadpoleRAPBundle()) {
	        getWindowConfigurer().setShellStyle(SWT.NO_TRIM);
	        getWindowConfigurer().setShowMenuBar(false);
//        }
        
	       	// tadpole의 시스템 테이블이 존재 하지 않는다면 테이블을 생성합니다.
	    	try {
	    		TadpoleSystemConnector.createSystemTable();
	    	} catch(Exception e) {
//		    		MessageDialog.openError(null, com.hangum.db.browser.rap.Messages.ApplicationWorkbenchWindowAdvisor_1, com.hangum.db.browser.rap.Messages.ApplicationWorkbenchWindowAdvisor_2 + e.getMessage());
	    		
	    		Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", com.hangum.db.browser.rap.Messages.ApplicationWorkbenchWindowAdvisor_2, errStatus); //$NON-NLS-1$
	    		
	    		System.exit(0);
	    	}
	    	
	    	// login dialog를 뜨도록합니다.    
	    	LoginDialog loginDialog = new LoginDialog(Display.getCurrent().getActiveShell());
	    	
	    	// 로그인 취소 버튼을 누르면 manager 권한으로 사용하도록 합니다.
	    	if(Dialog.OK != loginDialog.open()) {
	    		
	    		String userId = TadpoleSystemConnector.MANAGER_EMAIL;
				String password = TadpoleSystemConnector.MANAGER_PASSWD;
		    	try {
					UserDAO login = TadpoleSystem_UserQuery.login(userId, password);
					if(!Define.USER_TYPE.MANAGER.toString().equals(login.getUser_type())) {
						UserDAO groupManagerUser =  TadpoleSystem_UserQuery.getGroupManager(login.getGroup_seq());
						
						SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), groupManagerUser.getSeq());
					}  else {
						SessionManager.newLogin(login.getGroup_seq(), login.getSeq(), login.getEmail(), login.getPasswd(), login.getName(), login.getUser_type(), -1);
					}
					
				} catch (Exception e) {
					logger.error("demo mode user login", e);
					MessageDialog.openError(getWindowConfigurer().getWindow().getShell(), Messages.LoginDialog_7, e.getMessage());
					return;
				}	
	    		
	    	}
	    	
////    	SessionManager.newLogin(0, "adi.tadpole@gmail.com", "admin");
    }
    
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

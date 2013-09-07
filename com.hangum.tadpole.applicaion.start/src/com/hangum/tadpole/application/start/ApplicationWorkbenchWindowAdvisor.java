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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.views.IViewDescriptor;

import com.hangum.tadpold.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.application.start.dialog.login.LoginDialog;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.connections.ConnectDatabase;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
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
        configurer.setShowMenuBar(false);
        
        configurer.setShowProgressIndicator(false);
        configurer.setTitle(SystemDefine.NAME + " " + SystemDefine.MAJOR_VERSION + " SR" + SystemDefine.SUB_VERSION); //$NON-NLS-1$ //$NON-NLS-2$
        
        // Browser screen max, not min.
        getWindowConfigurer().setShellStyle(SWT.NO_TRIM);
        getWindowConfigurer().setShowMenuBar(false);
    
        // Set system exist message.
        ExitConfirmation service = RWT.getClient().getService( ExitConfirmation.class );
    	service.setMessage(Messages.ApplicationWorkbenchWindowAdvisor_4);
    
        initSystem();
    }
    
    /**
     * System initialize 
     */
    private void initSystem() {
//    	try {
//	    	// Add HttpListener(User data collection
//			System.out.println("================= start add session ==========================");
//			TadpoleSessionListener listener = new TadpoleSessionListener();
//			RWT.getUISession().getHttpSession().getServletContext().addListener(listener);//"com.hangum.tadpole.application.start.sessions.TadpoleSessionListener");
//			System.out.println("================= end add session ==========================");
//    	} catch(Exception e) {
//    		e.printStackTrace();
//    	}
    			
//    	// Show Information Dialog(Is not Firefox, Chrome, Safari)
//    	String isBrowser = RequestInfoUtils.isTadpoleRunning();
//    	if(!"".equals(isBrowser)) {
//    		UserInformationDialog uiDialog = new UserInformationDialog(Display.getCurrent().getActiveShell(), isBrowser);
//    		uiDialog.open();
//    	}
    	
    	// If the system table does not exist, create a table.
    	try {
    		TadpoleSystemInitializer.initSystem();
    	} catch(Exception e) {
    		logger.error("System initialize", e); //$NON-NLS-1$
    		Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", com.hangum.tadpole.application.start.Messages.ApplicationWorkbenchWindowAdvisor_2, errStatus); //$NON-NLS-1$
    		
    		System.exit(0);
    	}
    	
    	// If you already login?
    	if(0 == SessionManager.getSeq()) {
    	
	    	// Open login dialog    
	    	LoginDialog loginDialog = new LoginDialog(Display.getCurrent().getActiveShell());
	    	
	    	// When login cancel button, i use in manager authority.
	    	if(Dialog.OK != loginDialog.open()) {
	    		
	    		String userId = TadpoleSystemInitializer.MANAGER_EMAIL;
				String password = TadpoleSystemInitializer.MANAGER_PASSWD;
		    	try {
					SessionManager.addSession(TadpoleSystem_UserQuery.login(userId, password));
					initSession();
				} catch (Exception e) {
					logger.error("demo mode user login", e); //$NON-NLS-1$
					MessageDialog.openError(getWindowConfigurer().getWindow().getShell(), Messages.LoginDialog_7, e.getMessage());
					return;
				}	
	    	} else {
	    		try {
		    		// Stored user session.
					List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.allUserInfoData();
					Map<String, Object> mapUserInfoData = new HashMap<String, Object>();
					for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
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
    	// If login after does not DB exist, DB connect Dialog open.
    	try {
    		ManagerViewer mv = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
    		if(0 == mv.getAllTreeList().size()) {
    			ConnectDatabase cd = new ConnectDatabase();
    			cd.run();
    		}
    	} catch(Exception e) {
    		logger.error("Is DB list?", e); //$NON-NLS-1$
    	}    	
    }
    
    /**
	 * Set initialize session
	 */
	private void initSession() {
		HttpSession iss = RWT.getRequest().getSession();
		
		int sessionTimeOut = Integer.parseInt(GetPreferenceGeneral.getSessionTimeout());		
		if(sessionTimeOut <= 0) {
			iss.setMaxInactiveInterval( 60 * 60 * 24 );
		} else {
			iss.setMaxInactiveInterval(Integer.parseInt(GetPreferenceGeneral.getSessionTimeout()) * 60);
		}
	}
    
    @Override
    public void postWindowCreate() {
    	Shell shell = getWindowConfigurer().getWindow().getShell();
    	if(shell == null) {
    		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    	}
    	shell.setMaximized(true);
    }
}

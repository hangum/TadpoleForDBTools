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
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ExitConfirmation;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hangum.tadpole.application.start.dialog.login.LoginDialog;
import com.hangum.tadpole.application.start.dialog.login.ServiceLoginDialog;
import com.hangum.tadpole.application.start.update.checker.NewVersionChecker;
import com.hangum.tadpole.application.start.update.checker.NewVersionObject;
import com.hangum.tadpole.application.start.update.checker.NewVersionViewDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.CookieUtils;
import com.hangum.tadpole.commons.util.IPFilterUtil;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final Logger logger = Logger.getLogger(ApplicationWorkbenchWindowAdvisor.class);
	
	// UI callback
	final ServerPushSession pushSession = new ServerPushSession();
//	private boolean isUIThreadRunning = true;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
//    	if("YES".equals(GetAdminPreference.getSupportMonitoring())) {
//	    	try {
	//    		logger.info("Schedule and Summary Report start.........");
	//			DBSummaryReporter.executer();
//	    		ScheduleManager.getInstance();
//			} catch(Exception e) {
//				logger.error("Schedule", e);
//			}
//    	}
    	
//    	not support rap yet.
//    	String prop = IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS;
//    	PlatformUI.getPreferenceStore().setValue(prop, false);
    	
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        
        // remove this line(fixed at https://github.com/hangum/TadpoleForDBTools/issues/350)
//        configurer.setInitialSize(new Point(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        configurer.setShowMenuBar(true);
        configurer.setShowProgressIndicator(false);
        configurer.setTitle(SystemDefine.NAME + " " + SystemDefine.MAJOR_VERSION + " SR" + SystemDefine.SUB_VERSION); //$NON-NLS-1$ //$NON-NLS-2$
        
        // fullscreen
        getWindowConfigurer().setShellStyle(SWT.NO_TRIM);
        getWindowConfigurer().setShowMenuBar(true);
    
        // Set system exist message.
        ExitConfirmation service = RWT.getClient().getService( ExitConfirmation.class );
    	service.setMessage(Messages.get().ApplicationWorkbenchWindowAdvisor_4);
    	
//    	checkSupportBrowser();
        login();
    }
    
    /**
     * new version checker
     */
    private void newVersionChecker() {
    	
		if(!CookieUtils.isUpdateChecker()) {
	    	boolean isNew = NewVersionChecker.getInstance().check();
	    	if(isNew) {
	    		NewVersionObject newVersionObj = NewVersionChecker.getInstance().getNewVersionObj();
	    		NewVersionViewDialog dialog = new NewVersionViewDialog(null, newVersionObj);
	    		dialog.open();
    		}	// is nuew
    	}	// is update checker
    }
    
    @Override
    public void postWindowOpen() {
    	if(ApplicationArgumentUtils.isOnlineServer()) return;    	
    	if(SessionManager.isSystemAdmin()) {
    		newVersionChecker();
    	}
    	
    	// fullscreen
//    	getWindowConfigurer().getWindow().getShell().setMaximized(true);
//    	
//    	 쪽지 기능의 역할에 비해 리소스를 너무 많이 먹는 것으로 판단되어 기능을 막습니다.
//    	더 의미를 찾을때까지요. - 14.08.25
//    	
    	// main ui callback thread
//    	mainUICallback();

    	// If login after does not DB exist, DB connect Dialog open.
//    	try {
////    		// fix https://github.com/hangum/TadpoleForDBTools/issues/221
//			ManagerViewer mv = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
//    		if(0 == mv.getAllTreeList().size()) {
//    			if(MessageDialog.openConfirm(null, Messages.get().ApplicationWorkbenchWindowAdvisor_0, Messages.get().ApplicationWorkbenchWindowAdvisor_3)) {
//    			ConnectDatabase cd = new ConnectDatabase();
//    			cd.run();
//	    		}
//    		}
//    	} catch(Exception e) {
//    		logger.error("Is DB list?", e); //$NON-NLS-1$
//    	}
    	
    }
    
//    /**
//     * check support browser
//     */
//    private void checkSupportBrowser() {
//	//    	try {
//	//    	// Add HttpListener(User data collection
//	//		System.out.println("================= start add session ==========================");
//	//		TadpoleSessionListener listener = new TadpoleSessionListener();
//	//		RWT.getUISession().getHttpSession().getServletContext().addListener(listener);//"com.hangum.tadpole.application.start.sessions.TadpoleSessionListener");
//	//		System.out.println("================= end add session ==========================");
//	//	} catch(Exception e) {
//	//		e.printStackTrace();
//	//	}
//				
//		// Show Information Dialog(Is not Firefox, Chrome, Safari)
////		if(!RequestInfoUtils.isSupportBrowser()) {
////			UserInformationDialog uiDialog = new UserInformationDialog(Display.getCurrent().getActiveShell(), RequestInfoUtils.getUserBrowser());
////			uiDialog.open();
////		}
//    }
    
//    /**
//     * 시스템에서 사용자에게 메시지를 전해 줍니다.
//     * 
//     */
//    private void mainUICallback() {
//    	final Display display = PlatformUI.getWorkbench().getDisplay();
//    	
//    	Runnable runnable = new Runnable() {
//    		public void run() {
//    			while(isUIThreadRunning) {
//				    
//    				if(display.isDisposed()) {
//    					isUIThreadRunning = false;
//    				} else {
//    				
//	    				try {
//	 					     display.asyncExec( new Runnable() {
//	 					    	public void run() {
//	 					    		
//	 					    		// note list
//	 					    		List<NotesDAO> listNotes = NoteSystemAlert.getSystemNoteAlert();
//	 					    		if(!listNotes.isEmpty()) {
//	 					    			// refresh note view
//	 					    			NoteListViewPart nlvPart = (NoteListViewPart)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(NoteListViewPart.ID);
//	 					    			nlvPart.initData();
//
//	 					    			// show note 
//	 					    			for (NotesDAO notesDAO : listNotes) {
//		 					    			ViewDialog dialog = new ViewDialog(display.getActiveShell(), notesDAO, NotesDefine.NOTE_TYPES.RECEIVE);
//		 									dialog.open();
//										}	 					    			
//	 					    		}
//	 					    		// note list 
//	 					    		
//	 					    	}
//	 					    } );
//					    } catch(Exception e) {
//					    	logger.error("main ui call", e);
//					    } // end try
//    				
//	    				try {
//							Thread.sleep(20 * 1000);
//	    				} catch(Exception e){}
//    				}
//    			}	// end while
//    		}	// end run
//		};
//    	pushSession.start();
//    	new Thread(runnable).start();
//    }
    
    /**
     * login 
     */
    private void login() {
    	// 이미 로그인 되어 있다.
    	if(0 != SessionManager.getUserSeq()) return;
    	
    	try {
    		if(TadpoleApplicationContextManager.isPersonOperationType()) {
    			UserDAO userDao = TadpoleSystem_UserQuery.findUser(PublicTadpoleDefine.SYSTEM_DEFAULT_USER);
    			
    			String strAllowIP = userDao.getAllow_ip();
    			boolean isAllow = IPFilterUtil.ifFilterString(strAllowIP, RequestInfoUtils.getRequestIP());
    			if(logger.isDebugEnabled())logger.debug(Messages.get().LoginDialog_21 + userDao.getEmail() + Messages.get().LoginDialog_22 + strAllowIP + Messages.get().LoginDialog_23+ RequestInfoUtils.getRequestIP());
    			if(!isAllow) {
    				logger.error(Messages.get().LoginDialog_21 + userDao.getEmail() + Messages.get().LoginDialog_22 + strAllowIP + Messages.get().LoginDialog_26+ RequestInfoUtils.getRequestIP());
    				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().LoginDialog_28);
    				return;
    			}
    			
    			SessionManager.addSession(userDao);
    			// save login_history
    			TadpoleSystem_UserQuery.saveLoginHistory(userDao.getSeq());
    			
    			initializeUserSession();
    			
    			initializeDefaultLocale();
    		} else {
    			// Open login dialog
    			if(ApplicationArgumentUtils.isOnlineServer()) {
    				ServiceLoginDialog loginDialog = new ServiceLoginDialog(Display.getCurrent().getActiveShell());
	    	    	if(Dialog.OK == loginDialog.open()) {
	    	    		initializeUserSession();
	    	    	}
    			} else {
    				LoginDialog loginDialog = new LoginDialog(Display.getCurrent().getActiveShell());
	    	    	if(Dialog.OK == loginDialog.open()) {
	    	    		initializeUserSession();
	    	    	}
    			}
    		}

    	} catch (Exception e) {
    		logger.error("System login fail", e); //$NON-NLS-1$
    		MessageDialog.openError(null, Messages.get().Confirm, "System login fail.  Please contact admin"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	
    }

    /**
     * initialize default locale
     */
    private void initializeDefaultLocale() {
		HttpServletRequest request = RWT.getRequest();
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			for (Cookie cookie : cookies) {				
				if(PublicTadpoleDefine.TDB_COOKIE_USER_LANGUAGE.equals(cookie.getName())) {
					if(cookie.getValue().equalsIgnoreCase(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH))) {
						RWT.getUISession().setLocale(Locale.ENGLISH);	
					} else if(cookie.getValue().equalsIgnoreCase(Locale.ENGLISH.getDisplayLanguage(Locale.KOREAN))) {
						RWT.getUISession().setLocale(Locale.KOREAN);
					}
					break;
				}
			}
		}
    }

	/**
     * initialize user session
     */
    private void initializeUserSession() {
    	try {
			// Stored user session.
			List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData();
			Map<String, Object> mapUserInfoData = new HashMap<String, Object>();
			for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
				mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
			}
			SessionManager.setUserAllPreferenceData(mapUserInfoData);
			
			if ("".equals(SessionManager.getPerspective())) { //$NON-NLS-1$
				SessionManager.setPerspective(Perspective.DEFAULT);
			
//					// user 사용자는 default perspective를 사용합니다.
//					if(PublicTadpoleDefine.USER_TYPE.USER.toString().equals(SessionManager.getRepresentRole())) {
//						SessionManager.setPerspective(Perspective.DEFAULT);
//					} else {
//							String persp;
//							SelectPerspectiveDialog dialog = new SelectPerspectiveDialog(Display.getCurrent().getActiveShell());
//							
//							if (Dialog.OK == dialog.open()) {
//								persp = dialog.getResult();
//							} else {
//								persp = Perspective.DEFAULT;
//							}
//							SessionManager.setPerspective(persp);
//					}
			}
			
			initSession();
			
		} catch(Exception e) {
			logger.error("session set", e); //$NON-NLS-1$
		}
    }
    
    /**
	 * Set initialize session
	 */
	private void initSession() {
		HttpSession iss = RWT.getUISession().getHttpSession();
		
		final int sessionTimeOut = Integer.parseInt(GetPreferenceGeneral.getSessionTimeout());
		if(sessionTimeOut <= 0) {
			iss.setMaxInactiveInterval(90 * 60);
		} else {
			iss.setMaxInactiveInterval(sessionTimeOut * 60);
		}
		
//		// user logout
//		RWT.getUISession().addUISessionListener( new UISessionListener() {
//			public void beforeDestroy( UISessionEvent event ) {
//				logger.info(String.format("User has logout. session id is %s", event.getUISession().getId()));
//			}
//		});
	}
    
    @Override
    public void postWindowCreate() {
    	Shell shell = getWindowConfigurer().getWindow().getShell();
    	if(shell == null) {
    		shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    	}
    	shell.setMaximized(true);
    }
    
//    @Override
//    public boolean preWindowShellClose() {
//    	logger.debug("======================> preShutdown execute ================");
//    	ScheduleManager.getInstance().stopSchedule();
//    	logger.debug("======================> preShutdown execute ================");
//    	
//    	return super.preWindowShellClose();
//    }
    
}

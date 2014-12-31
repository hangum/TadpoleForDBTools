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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.service.UISessionEvent;
import org.eclipse.rap.rwt.service.UISessionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hangum.tadpold.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.application.start.dialog.login.LoginDialog;
import com.hangum.tadpole.application.start.dialog.perspective.SelectPerspectiveDialog;
import com.hangum.tadpole.monitoring.core.manager.ScheduleManager;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.actions.connections.ConnectDatabase;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.sql.query.TadpoleSystemInitializer;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;
import com.hangum.tadpole.summary.report.DBSummaryReporter;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final Logger logger = Logger.getLogger(ApplicationWorkbenchWindowAdvisor.class);
	
	// UI callback
	final ServerPushSession pushSession = new ServerPushSession();
	private boolean isUIThreadRunning = true;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
//    	try {
//    		logger.info("Schedule start.........");
//			DBSummaryReporter.executer();
//			ScheduleManager.getInstance();
//		} catch(Exception e) {
//			logger.error("Schedule", e);
//		}
    	
//    	not support rap yet.
//    	String prop = IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS;
//    	PlatformUI.getPreferenceStore().setValue(prop, false);
    	
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        
        // remove this line(fixed at https://github.com/hangum/TadpoleForDBTools/issues/350)
//        configurer.setInitialSize(new Point(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        configurer.setShowMenuBar(false);
        configurer.setShowProgressIndicator(false);
        configurer.setTitle(SystemDefine.NAME + " " + SystemDefine.MAJOR_VERSION + " SR" + SystemDefine.SUB_VERSION); //$NON-NLS-1$ //$NON-NLS-2$
        
        // fullscreen
        getWindowConfigurer().setShellStyle(SWT.NO_TRIM);
        getWindowConfigurer().setShowMenuBar(false);
    
//        // Set system exist message.
//        ExitConfirmation service = RWT.getClient().getService( ExitConfirmation.class );
//    	service.setMessage(Messages.ApplicationWorkbenchWindowAdvisor_4);
    	
//    	checkSupportBrowser();
    	
        login();
    }
    
    @Override
    public void postWindowOpen() {
    	// fullscreen
    	getWindowConfigurer().getWindow().getShell().setMaximized(true);;
    	
//    	
//    	
//    	 쪽지 기능의 역할에 비해 리소스를 너무 많이 먹는 것으로 판단되어 기능을 막습니다.
//    	더 의미를 찾을때까지요. - 14.08.25
//    	
    	// main ui callback thread
//    	mainUICallback();
    	   
    	// If login after does not DB exist, DB connect Dialog open.
    	try {
//    		// fix https://github.com/hangum/TadpoleForDBTools/issues/221
//    		if(!PublicTadpoleDefine.USER_TYPE.USER.toString().equals(SessionManager.getRepresentRole())) {
    			ManagerViewer mv = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
	    		if(0 == mv.getAllTreeList().size()) {
	    			ConnectDatabase cd = new ConnectDatabase();
	    			cd.run();
	    		}
//    		}
    	} catch(Exception e) {
    		logger.error("Is DB list?", e); //$NON-NLS-1$
    	}
    	
    }
    
    /**
     * check support browser
     */
    private void checkSupportBrowser() {
	//    	try {
	//    	// Add HttpListener(User data collection
	//		System.out.println("================= start add session ==========================");
	//		TadpoleSessionListener listener = new TadpoleSessionListener();
	//		RWT.getUISession().getHttpSession().getServletContext().addListener(listener);//"com.hangum.tadpole.application.start.sessions.TadpoleSessionListener");
	//		System.out.println("================= end add session ==========================");
	//	} catch(Exception e) {
	//		e.printStackTrace();
	//	}
				
		// Show Information Dialog(Is not Firefox, Chrome, Safari)
//		if(!RequestInfoUtils.isSupportBrowser()) {
//			UserInformationDialog uiDialog = new UserInformationDialog(Display.getCurrent().getActiveShell(), RequestInfoUtils.getUserBrowser());
//			uiDialog.open();
//		}
    }
    
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
    	// If you already login?
    	if(0 == SessionManager.getUserSeq()) {
    	
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
					List<UserInfoDataDAO> listUserInfo = TadpoleSystem_UserInfoData.getUserInfoData();
					Map<String, Object> mapUserInfoData = new HashMap<String, Object>();
					for (UserInfoDataDAO userInfoDataDAO : listUserInfo) {						
						mapUserInfoData.put(userInfoDataDAO.getName(), userInfoDataDAO);
					}
					SessionManager.setUserInfos(mapUserInfoData);
					
					if ("".equals(SessionManager.getPerspective())) {
					
//						// user 사용자는 default perspective를 사용합니다.
//						if(PublicTadpoleDefine.USER_TYPE.USER.toString().equals(SessionManager.getRepresentRole())) {
//							SessionManager.setPerspective(Perspective.DEFAULT);
//						} else {
							String persp;
							SelectPerspectiveDialog dialog = new SelectPerspectiveDialog(Display.getCurrent().getActiveShell());
							
							if (Dialog.OK == dialog.open()) {
								persp = dialog.getResult();
							} else {
								persp = Perspective.DEFAULT;
							}
							SessionManager.setPerspective(persp);
//						}
					}
					
					initSession();
					
	    		} catch(Exception e) {
	    			logger.error("session set", e); //$NON-NLS-1$
	    		}
	    		
	    	}
    	} 
    }
    
    /**
	 * Set initialize session
	 */
	private void initSession() {
		HttpSession iss = RWT.getUISession().getHttpSession();
		
		int sessionTimeOut = Integer.parseInt(GetPreferenceGeneral.getSessionTimeout());		
		if(sessionTimeOut <= 0) {
			iss.setMaxInactiveInterval( 60 * 90 );
		} else {
			iss.setMaxInactiveInterval(Integer.parseInt(GetPreferenceGeneral.getSessionTimeout()) * 60);
		}
		
		// cleanup code
		// user logout
		RWT.getUISession().addUISessionListener( new UISessionListener() {
			public void beforeDestroy( UISessionEvent event ) {
				
			}
		});
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

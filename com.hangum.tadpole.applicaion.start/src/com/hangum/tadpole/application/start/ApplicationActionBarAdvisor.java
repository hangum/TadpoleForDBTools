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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.hangum.tadpole.application.start.action.AboutAction;
import com.hangum.tadpole.application.start.action.BugIssueAction;
import com.hangum.tadpole.manager.core.actions.global.UserPermissionAction;
import com.hangum.tadpole.rdb.core.actions.global.ConnectDatabaseAction;
import com.hangum.tadpole.rdb.core.actions.global.DeleteResourceAction;
import com.hangum.tadpole.rdb.core.actions.global.ExitAction;
import com.hangum.tadpole.rdb.core.actions.global.OpenDBRelationAction;
import com.hangum.tadpole.rdb.core.actions.global.OpenQueryEditorAction;
import com.hangum.tadpole.rdb.core.actions.global.PreferenceAction;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.permission.PermissionChecker;

/**
 * 올챙이에서 사용하려는 workbench의 action과 toolbar, menu를 생성합니다.
 * 
 * @author hangum
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction exitAction;
    
    private IAction saveAction;
//    private IAction saveAsAction;
    
    private IAction connectAction;
    private IAction queryOpenAction;
    private IAction dbRelationOpenAction;
    private IAction deleteResourceAction;
    
    /** 사용자 권한 */
    private IAction userPermissionAction;
    /** 실행된 sql */
//    private IAction executedSQLAction;
    
    private IAction preferenceAction;
    private IAction aboutAction;
    private IAction bugIssueAction;
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
   	
    	saveAction = ActionFactory.SAVE.create(window);
    	register(saveAction);
    	
//    	saveAsAction = ActionFactory.SAVE_AS.create(window);
//    	register(saveAsAction);
    	
    	connectAction = new ConnectDatabaseAction(window);
    	register(connectAction);
    	
    	queryOpenAction = new OpenQueryEditorAction(window);
    	register(queryOpenAction);
    	
    	dbRelationOpenAction = new OpenDBRelationAction(window);
    	register(dbRelationOpenAction);
    	
    	deleteResourceAction = new DeleteResourceAction(window);
    	register(deleteResourceAction);
    	
    	userPermissionAction = new UserPermissionAction(window);
    	register(userPermissionAction);
    	
//    	executedSQLAction = new ExecutedSQLAction(window);
//    	register(executedSQLAction);

        exitAction = new ExitAction(window);
        register(exitAction);
        
        preferenceAction = new PreferenceAction(window);
        register(preferenceAction);        
        
        aboutAction = new AboutAction(window);
        register(aboutAction);
        
        bugIssueAction = new BugIssueAction(window);
        register(bugIssueAction);
        
    }
    
    /**
     * Comment at 2.1 RC3 has error(https://bugs.eclipse.org/bugs/show_bug.cgi?id=410260) 
     * 
     * 
     */
    protected void fillMenuBar(IMenuManager menuBar) {
//        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
//        MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
//        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
//        
//        menuBar.add(fileMenu);
//        
//        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
//        menuBar.add(windowMenu);
//        
//        // Add a group marker indicating where action set menus will appear.
//        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
//        menuBar.add(helpMenu);
//        
//        // File
//        fileMenu.add(new Separator());
//        fileMenu.add(saveAction);
////        fileMenu.add(saveAsAction);
//        fileMenu.add(new Separator());
//        fileMenu.add(connectAction);
//        fileMenu.add(new Separator());
//        fileMenu.add(queryOpenAction);
//        fileMenu.add(dbRelationOpenAction);
//        fileMenu.add(deleteResourceAction);
//        fileMenu.add(new Separator());
////        fileMenu.add(exitAction);
//        
//        windowMenu.add(preferenceAction);        
//        //        
//        // Help
//        helpMenu.add(bugIssueAction);
//        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        
        if(PermissionChecker.isShow(SessionManager.representRole())) {
	        toolbar.add(connectAction);
	        toolbar.add(new Separator());
        }
        
        toolbar.add(saveAction);
//        toolbar.add(saveAsAction);
        toolbar.add(new Separator());        
        
        toolbar.add(queryOpenAction);
        toolbar.add(dbRelationOpenAction);
        toolbar.add(new Separator());
        
        toolbar.add(deleteResourceAction);
        toolbar.add(new Separator());
        
        if(PermissionChecker.isShow(SessionManager.representRole())) {
        	toolbar.add(userPermissionAction);
//        	toolbar.add(executedSQLAction);
            toolbar.add(new Separator());        	
        }
        
        toolbar.add(preferenceAction);
        toolbar.add(new Separator());
        
        toolbar.add(bugIssueAction);
        toolbar.add(aboutAction);
        
//        if(ApplicationArgumentUtils.isStandaloneMode()) {
        	toolbar.add(new Separator());
        	toolbar.add(exitAction);
//        }
        
    }
    
}

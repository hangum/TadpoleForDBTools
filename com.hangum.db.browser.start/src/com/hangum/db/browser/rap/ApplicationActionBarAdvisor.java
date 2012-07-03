package com.hangum.db.browser.rap;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.hangum.db.browser.rap.action.AboutAction;
import com.hangum.db.browser.rap.action.BugIssueAction;
import com.hangum.db.browser.rap.core.actions.global.ConnectDatabaseAction;
import com.hangum.db.browser.rap.core.actions.global.DeleteResourceAction;
import com.hangum.db.browser.rap.core.actions.global.ExitAction;
import com.hangum.db.browser.rap.core.actions.global.OpenDBRelationAction;
import com.hangum.db.browser.rap.core.actions.global.OpenQueryEditorAction;
import com.hangum.db.browser.rap.core.actions.global.PreferenceAction;
import com.hangum.db.browser.rap.core.actions.global.UserPermissionAction;
import com.hangum.db.commons.session.SessionManager;
import com.hangum.db.define.Define;
import com.hangum.db.util.ApplicationArgumentUtils;

/**
 * Creates, adds and disposes actions for the menus and action bars of
 * each workbench window.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction;
    
    private IAction saveAction;
//    private IAction saveAsAction;
    
    private IAction connectAction;
    private IAction queryOpenAction;
    private IAction dbRelationOpenAction;
    private IAction deleteResourceAction;
    
    private IAction userPermissionAction;
    
    private IAction preferenceAction;
    private IAction aboutAction;
    private IAction bugIssueAction;
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.
    	
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

        exitAction = new ExitAction(window);
        register(exitAction);
        
        preferenceAction = new PreferenceAction(window);//ActionFactory.PREFERENCES.create(window);
        register(preferenceAction);        
        
        aboutAction = new AboutAction(window);
        register(aboutAction);
        
        bugIssueAction = new BugIssueAction(window);
        register(bugIssueAction);
        
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(fileMenu);
        
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(windowMenu);
        
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(new Separator());
        fileMenu.add(saveAction);
//        fileMenu.add(saveAsAction);
        fileMenu.add(new Separator());
        fileMenu.add(connectAction);
        fileMenu.add(new Separator());
        fileMenu.add(queryOpenAction);
        fileMenu.add(dbRelationOpenAction);
        fileMenu.add(deleteResourceAction);
        fileMenu.add(new Separator());
//        fileMenu.add(exitAction);
        
        windowMenu.add(preferenceAction);        
        //        
        // Help
        helpMenu.add(bugIssueAction);
        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));
        toolbar.add(connectAction);
        toolbar.add(new Separator());
        
        toolbar.add(saveAction);
//        toolbar.add(saveAsAction);
        toolbar.add(new Separator());        
        
        toolbar.add(queryOpenAction);
        toolbar.add(dbRelationOpenAction);
        toolbar.add(new Separator());
        
        toolbar.add(deleteResourceAction);
        toolbar.add(new Separator());
        
        if(Define.USER_TYPE.MANAGER.toString().equals( SessionManager.getLoginType() )
        		||
        		Define.USER_TYPE.ADMIN.toString().equals( SessionManager.getLoginType() )
        ) {
        	toolbar.add(userPermissionAction);
            toolbar.add(new Separator());        	
        }
        
        toolbar.add(preferenceAction);
        toolbar.add(new Separator());
        
        toolbar.add(bugIssueAction);
        toolbar.add(aboutAction);
        
        if(ApplicationArgumentUtils.isStandaloneMode()) {
        	toolbar.add(new Separator());
        	toolbar.add(exitAction);
        }
        
    }
    
}

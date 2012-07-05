package com.hangum.db.browser.rap.action;

import org.eclipse.jface.action.Action;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.browser.rap.Messages;

public class BugIssueAction extends Action {
	private final IWorkbenchWindow window;
	
	public BugIssueAction(IWorkbenchWindow window) {
		super(Messages.BugIssueAction_0);
		setId(this.getClass().getName());
		
		this.window = window;
	}
	
	public void run() {
		ExternalBrowser.open( "Issues", "http://code.google.com/p/tadpole-for-db-tools/issues/entry", ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR | ExternalBrowser.STATUS );
	}
}

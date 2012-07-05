package com.hangum.db.browser.rap.core.actions.object;

import org.apache.log4j.Logger;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.define.Define;
import com.hangum.db.define.Define.DB_ACTION;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangumNote
 *
 */
public class ObjectRefreshAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectRefreshAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.refresh";

	public ObjectRefreshAction(IWorkbenchWindow window, Define.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Refresh " + title);
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run() {
		if(actionType == DB_ACTION.TABLES) {
			refreshTable();
		} else if(actionType == DB_ACTION.VIEWS) {
			refreshView();
		} else if(actionType == DB_ACTION.INDEXES) {
			refreshIndexes();
		} else if(actionType == DB_ACTION.PROCEDURES) {
			refreshProcedure();
		} else if(actionType == DB_ACTION.FUNCTIONS) {
			refreshFunction();
		} else if(actionType == DB_ACTION.TRIGGERS) {
			refreshTrigger();
		} 
	}
	
}

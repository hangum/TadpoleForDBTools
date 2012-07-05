package com.hangum.db.browser.rap.core.actions.object;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.browser.rap.core.viewers.object.ExplorerViewer;
import com.hangum.db.define.Define;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangumNote
 *
 */
public class ObjectModifyAction extends AbstractObjectAction {

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.modify"; //$NON-NLS-1$

	public ObjectModifyAction(IWorkbenchWindow window, Define.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Alert " + title); //$NON-NLS-1$
		
		window.getSelectionService().addSelectionListener(this);
	}
	

	@Override
	public void run() {
//		System.out.println(Messages.ObjectModifyAction_2);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
			
			if(userDB != null) setEnabled(true);
			else setEnabled(false);
		}
	}
	
}

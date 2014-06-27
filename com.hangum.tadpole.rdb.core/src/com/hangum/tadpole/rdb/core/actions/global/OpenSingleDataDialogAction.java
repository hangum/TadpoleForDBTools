package com.hangum.tadpole.rdb.core.actions.global;

import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.rdb.core.dialog.record.RecordViewDialog;
import com.hangum.tadpole.sql.util.resultset.QueryExecuteResultDTO;

public class OpenSingleDataDialogAction extends Action implements IWorkbenchAction {
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.OpenSingleDataDialogAction"; //$NON-NLS-1$
	private IStructuredSelection iss;
	private QueryExecuteResultDTO dto;
	
	public OpenSingleDataDialogAction() {
		setId(ID);
		setText("View Detail");
		setToolTipText("View Detail");
		setEnabled(false);
	}
	
	@Override
	public void run() {
		Shell[] shellList = PlatformUI.getWorkbench().getDisplay().getShells();
		for (Shell shell : shellList) {
			if (shell.getData().getClass() == RecordViewDialog.class) {
				shell.close();
				break;
			}
		}
		RecordViewDialog dialog = new RecordViewDialog(PlatformUI
				.getWorkbench().getDisplay().getActiveShell(), dto,
				iss.getFirstElement());
		dialog.open();
	}
	
	@Override
	public void dispose() {
	}

	public void selectionChanged(QueryExecuteResultDTO dto, ISelection selection) {
		this.dto = dto;
		System.out.println(selection.toString());
		IStructuredSelection sel = (IStructuredSelection)selection;
		if(sel != null) {
			if( sel.getFirstElement() instanceof HashMap<?, ?> ) {
				iss = sel;
				setEnabled(true);
				return;
			} 
		} 
		
		setEnabled(false);
	}
}

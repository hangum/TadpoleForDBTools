/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.resultView;

import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.record.RecordViewDialog;
import com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail.ResultTableComposite;

/**
 * Query Result 창에서 선택된 컬럼 데이터를 한번에 보기 위한 창을 열어 줍니다.
 * 
 * @author hangum
 *
 */
public class ColumnRowDataDialogAction extends Action implements IWorkbenchAction {
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.OpenColumnRowDataDialogAction"; //$NON-NLS-1$
	private IStructuredSelection iss;
	private ResultTableComposite resultTableComposite;
	
	public ColumnRowDataDialogAction(ResultTableComposite resultTableComposite) {
		setId(ID);
		setText(Messages.get().ResultSetComposite_0);
		setToolTipText(Messages.get().ResultSetComposite_0);
		setEnabled(false);
		this.resultTableComposite = resultTableComposite;
	}
	
	@Override
	public void run() {
		resultTableComposite.openSinglColumViewDialog();
	}
	
	@Override
	public void dispose() {
	}

	
	/**
	 * Selection 서비스를 이용하지 않고, 내부적으로 데이터 변경이 있을때 마다
	 * 아래 메소드를 직접 실행해 줍니다. 
	 *
	 * 원래 WorkbenchWindow를 가져오려해도 실제 Editor의 WorkbenchWindow가 
	 * 초기화 되지 않아(Null을 반환) 내부적으로 직접 실행하도록 되었습니다. 
	 * 
	 * @param selection
	 */
	public void selectionChanged(ISelection selection) {
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

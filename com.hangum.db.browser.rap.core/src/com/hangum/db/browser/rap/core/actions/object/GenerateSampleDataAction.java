/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.browser.rap.core.actions.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.browser.rap.core.viewers.object.ExplorerViewer;
import com.hangum.db.dao.mysql.TableDAO;
import com.hangum.db.define.Define;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataGenerateDialog;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangumNote
 *
 */
public class GenerateSampleDataAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSampleDataAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.generatesample.data";
	
	public GenerateSampleDataAction(IWorkbenchWindow window, Define.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Generate Sample data");
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run() {
		TableDAO tableDao = (TableDAO)sel.getFirstElement();
		
		SampleDataGenerateDialog dialog = new SampleDataGenerateDialog(window.getShell(), userDB, tableDao.getName());
		dialog.open();
	}
	

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
			
			if(userDB != null) {
				if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
					this.sel = (IStructuredSelection)selection;
					setEnabled(this.sel.size() > 0);
				} else setEnabled(false);
			}
			else setEnabled(false);
		}
	}
	
}

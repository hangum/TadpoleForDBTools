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
package com.hangum.tadpole.erd.core.actions;

import org.apache.log4j.Logger;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.erd.core.part.TableEditPart;
import com.hangum.tadpole.rdb.model.Table;

/**
 * table selection action
 * 
 * @author hangum
 *
 */
public class TableSelectionAction extends SelectionAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableSelectionAction.class);

	public static final String ID = "com.hangum.tadpole.erd.core.actions.TableSelectionAction";
	private GraphicalViewer viewer;
	
	public TableSelectionAction(IWorkbenchPart part, GraphicalViewer graphicalViewer) {
		super(part);
	
		setId(ID);
		setText("Table Selection");
		
		setLazyEnablementCalculation(false);		
		this.viewer = graphicalViewer;
	}
	
	public GraphicalViewer getViewer() {
		return viewer;
	}

	@Override
	protected boolean calculateEnabled() {
		IStructuredSelection is = (IStructuredSelection)getViewer().getSelection();
		if(!is.isEmpty()) {
			if(is.getFirstElement() instanceof TableEditPart) {
				TableEditPart tablePart = (TableEditPart)is.getFirstElement();
				Table tableModel = (Table)tablePart.getModel();

				PlatformUI.getPreferenceStore().setValue(Define.SELECT_ERD_TABLE, tableModel.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		
		return true;
	}
	
	@Override
	public void run() {
		super.run();
	}
	
}

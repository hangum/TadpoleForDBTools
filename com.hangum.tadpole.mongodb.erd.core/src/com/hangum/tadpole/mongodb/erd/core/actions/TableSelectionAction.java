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
package com.hangum.tadpole.mongodb.erd.core.actions;

import org.apache.log4j.Logger;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.mongodb.erd.core.part.TableEditPart;
import com.hangum.tadpole.mongodb.model.Table;

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

				PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.SELECT_ERD_TABLE, tableModel.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		
		return true;
	}
	
	@Override
	public void run() {
		super.run();
	}
	
}

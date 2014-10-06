/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.actions;

import java.util.List;

import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.EDITOR_OPEN_TYPE;
import com.hangum.tadpole.rdb.erd.core.dialogs.ERDViewStyleDailog;
import com.hangum.tadpole.rdb.erd.core.editor.TadpoleRDBEditor;
import com.hangum.tadpole.rdb.erd.core.part.TableEditPart;
import com.hangum.tadpole.rdb.erd.stanalone.Activator;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.Table;
import com.swtdesigner.ResourceManager;

/**
 * ERD view styled select action
 * 
 * @author hangum
 *
 */
public class ERDViewStyleAction extends SelectionAction {
	public final static String ID = "com.hangum.tadpole.rdb.erd.actions.global.ERDViewStyleAction"; //$NON-NLS-1$
	private GraphicalViewer viewer;
	private TadpoleRDBEditor rdbEditor;
	
	public ERDViewStyleAction(IWorkbenchPart part, GraphicalViewer graphicalViewer) {
		super(part);
		setLazyEnablementCalculation(false);
		
		rdbEditor = (TadpoleRDBEditor)part;
		this.viewer = graphicalViewer;
		
		setId(ID);
		setText("View Style Select");
		setToolTipText("View Style Select");
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/viewStyle.png"));
	}
	
	public GraphicalViewer getViewer() {
		return viewer;
	}
	
	@Override
	public void run() {
		DB dbModel = rdbEditor.getDb();
		
		ERDViewStyleDailog dialog = new ERDViewStyleDailog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), dbModel.getStyle());
		if(Dialog.OK == dialog.open()) {
			
			dbModel.setStyle( dialog.getErdStyle() );
			dbModel.getStyle().setGrid("YES");
			
			List models = getViewer().getContents().getChildren();
			// nodes
			for(int i=0;i<models.size();i++){
				Object obj = models.get(i);
				if(obj instanceof TableEditPart){
					TableEditPart tableEditPart = (TableEditPart) obj;
					tableEditPart.refresh();
				}
			}
		}
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

}

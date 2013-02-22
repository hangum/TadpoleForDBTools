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
package com.hangum.tadpole.application.start;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * Configures the perspective layout. This class is contributed 
 * through the plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		
		IFolderLayout leftFolder = layout.createFolder("id"+ManagerViewer.ID, IPageLayout.LEFT, 0.25f, editorArea);
		leftFolder.addView(ManagerViewer.ID);
		
		IFolderLayout leftUnderFolder = layout.createFolder("id"+ExplorerViewer.ID, IPageLayout.BOTTOM, 0.25f, "id"+ManagerViewer.ID);		
		leftUnderFolder.addView(ExplorerViewer.ID);
	}
}

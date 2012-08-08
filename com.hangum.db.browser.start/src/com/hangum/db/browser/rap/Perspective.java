package com.hangum.db.browser.rap;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.browser.rap.core.viewers.object.ExplorerViewer;

/**
 * Configures the perspective layout. This class is contributed 
 * through the plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		
		IFolderLayout leftFolder = layout.createFolder("id"+ManagerViewer.ID, IPageLayout.LEFT, 0.27f, editorArea);
		leftFolder.addView(ManagerViewer.ID);
		
		IFolderLayout leftUnderFolder = layout.createFolder("id"+ExplorerViewer.ID, IPageLayout.BOTTOM, 0.3f, "id"+ManagerViewer.ID);		
		leftUnderFolder.addView(ExplorerViewer.ID);
//		leftUnderFolder.addView(IPageLayout.ID_OUTLINE);
		 
	}
}

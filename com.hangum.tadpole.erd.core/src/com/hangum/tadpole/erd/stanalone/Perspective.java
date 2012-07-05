package com.hangum.tadpole.erd.stanalone;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Configures the perspective layout. This class is contributed 
 * through the plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView(IPageLayout.ID_OUTLINE, true, IPageLayout.LEFT, 0.3f, editorArea);
	}
}

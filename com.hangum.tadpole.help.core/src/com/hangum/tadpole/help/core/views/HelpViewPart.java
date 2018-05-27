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
package com.hangum.tadpole.help.core.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.help.core.views.sub.doc.DBDocComposite;

/**
 * Help main view
 * 
 * @author hangum
 *
 */
public class HelpViewPart extends ViewPart {
	public static final String ID = "com.hamgum.tadpole.help.core.view.main";

	public HelpViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		// ------------- shortcut keys --------------------------------------------
		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("Tadpole DB Hub - Document ");
		
		Composite composite = new DBDocComposite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		// ------------- shortcut keys --------------------------------------------
		
		// mongodb manual ( http://docs.mongodb.org/manual/ )
		
		tabFolder.setSelection(0);
		
		// google analytic
		AnalyticCaller.track(HelpViewPart.ID);
	}
	
	@Override
	public void setFocus() {
	}

}

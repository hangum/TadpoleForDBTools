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
package com.hangum.tadpole.rdb.core.actions.global;

import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.erd.rdb.RDBERDViewAction;
import com.swtdesigner.ResourceManager;

/**
 * 현재 선택된 디비의 릴레이션 화면을 오픈합니다.
 * 
 * @author hangum
 *
 */
public class OpenDBRelationAction extends OpenQueryEditorAction {
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.DBRelationOpenAction"; //$NON-NLS-1$
	
	public OpenDBRelationAction(IWorkbenchWindow window) {
		super(window);
		
		setId(ID);
		setText(Messages.get().ERDView);
		setToolTipText(Messages.get().ERDView);
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/erd.png"));
		setEnabled(false);
		
		window.getSelectionService().addPostSelectionListener(this);
	}
	
	@Override
	public void run() {
		RDBERDViewAction qea = new RDBERDViewAction();
		qea.run(userDB);
	}
	
}

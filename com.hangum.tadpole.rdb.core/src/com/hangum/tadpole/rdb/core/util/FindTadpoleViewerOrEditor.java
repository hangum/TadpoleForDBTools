/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.util;

import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * Find Connection, Object explorer viewer
 * 
 * @author hangum
 *
 */
public class FindTadpoleViewerOrEditor {

	/**
	 * explorer viewer
	 * 
	 * @param userDB 
	 * @return
	 */
	public static ExplorerViewer getExplorerView(UserDBDAO userDB) {
		try {
			ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
			if(ev.getUserDB().getSeq() == userDB.getSeq()) return ev;
			else return null;
		} catch(Exception e) {
			return null;
		}
	}

}

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
package com.hangum.tadpole.engine.sql.util.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

/**
 * swt tree utils
 * 
 * @author hangum
 *
 */
public class TreeUtil {

	/**
	 * column pack util
	 * 
	 * @param tree
	 */
	public static void packTree(Tree tree) {
		if(null == tree) return;
		
		for(int i=0; i<tree.getColumnCount(); i++) {
			tree.getColumn(i).pack();
		}
		
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				final TreeItem treeItem = (TreeItem)e.item;
				
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						for (TreeColumn tc: treeItem.getParent().getColumns()) {
							tc.pack();
						}
					}	// end run
				});
			}
		};
		tree.addListener(SWT.Collapse, listener);
		tree.addListener(SWT.Expand, listener);
	}
}

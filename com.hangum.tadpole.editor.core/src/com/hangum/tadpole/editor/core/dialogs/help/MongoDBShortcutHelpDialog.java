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
package com.hangum.tadpole.editor.core.dialogs.help;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.util.ShortcutPrefixUtils;

/**
 * mongodb editor 단축키 도움말.  
 * 
 * @author hangum
 *
 */
public class MongoDBShortcutHelpDialog extends RDBShortcutHelpDialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBShortcutHelpDialog.class);

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public MongoDBShortcutHelpDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public void createContents() {
		super.createContents();
		initData();
	}

	/**
	 * 단축키 데이터
	 */
	private void initData() {
		listShortcut.clear();
		String prefixOSShortcut = ShortcutPrefixUtils.getCtrlShortcut();
		listShortcut.add( new ShortcutHelpDAO("Collection Assist", 	prefixOSShortcut + "+ Space") 	);
		
		listShortcut.add( new ShortcutHelpDAO("To Lower case", 	prefixOSShortcut + "+ Shift + Y") );
		listShortcut.add( new ShortcutHelpDAO("To Upper case", 	prefixOSShortcut + "+ Shift + X") );
		listShortcut.add( new ShortcutHelpDAO("Shortcut Help", 	prefixOSShortcut + "+ Shift + L") );
		
		listShortcut.add( new ShortcutHelpDAO("Clear page", 	"F7") 				);
		listShortcut.add( new ShortcutHelpDAO("Select All", 	prefixOSShortcut + "+ A") 		);
		listShortcut.add( new ShortcutHelpDAO("Go to Line", 	prefixOSShortcut + "+ L") 		);
		listShortcut.add( new ShortcutHelpDAO("Copy text", 		prefixOSShortcut + "+ C") 		);
		listShortcut.add( new ShortcutHelpDAO("Past text", 		prefixOSShortcut + "+ V") 		);
		
		listShortcut.add( new ShortcutHelpDAO("Delete Line",	prefixOSShortcut + "+ D") 		);		
		tableViewer.refresh(listShortcut);		
	}
	
}

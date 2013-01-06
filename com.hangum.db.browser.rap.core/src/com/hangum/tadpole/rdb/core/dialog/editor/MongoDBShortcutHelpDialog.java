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
package com.hangum.tadpole.rdb.core.dialog.editor;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;

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

	/**
	 * 단축키 데이터
	 */
	private void initData() {
//		listShortcut.add( new ShortcutHelpDAO("Save", 			"Ctrl + S") 		);
//		listShortcut.add( new ShortcutHelpDAO("Execute Query", 	"Ctrl + enter") 	);
//		listShortcut.add( new ShortcutHelpDAO("Execute Query", 	"F5") 				);
//		listShortcut.add( new ShortcutHelpDAO("Execute Plan", 	"Ctrl + E") 		);
//		listShortcut.add( new ShortcutHelpDAO("Query Format", 	"Ctrl + Shift + F") );
//		listShortcut.add( new ShortcutHelpDAO("Query History", 	"Ctrl + H") 		);
		
		listShortcut.add( new ShortcutHelpDAO("To Lower case", 	"Ctrl + Shift + Y") );
		listShortcut.add( new ShortcutHelpDAO("To Upper case", 	"Ctrl + Shift + X") );
		listShortcut.add( new ShortcutHelpDAO("Shortcut Help", 	"Ctrl + Shift + L") );
		
		listShortcut.add( new ShortcutHelpDAO("Clear page", 	"F7") 				);
		listShortcut.add( new ShortcutHelpDAO("Select All", 	"Ctrl + A") 		);
		listShortcut.add( new ShortcutHelpDAO("Go to Line", 	"Ctrl + L") 		);
		listShortcut.add( new ShortcutHelpDAO("Copy text", 		"Ctrl + C") 		);
		listShortcut.add( new ShortcutHelpDAO("Past text", 		"Ctrl + V") 		);
		
		listShortcut.add( new ShortcutHelpDAO("Delete Line",	"Ctrl + D") 		);		
		tableViewer.refresh(listShortcut);		
	}
	
}

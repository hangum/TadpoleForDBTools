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
package com.hangum.tadpole.ace.editor.core.dialogs.help;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;

/**
 * abstract editor short dialog
 * 
 * @author hangum
 *
 */
public abstract class AbstractShortCutDialog extends Dialog {
	protected String prefixCtrlShortcut = "";
	protected String prefixAltShortcut = "";
	protected String prefixShiftShortcut = "";
	
	protected List<ShortcutHelpDAO> listShortcut = new ArrayList<ShortcutHelpDAO>();

	public AbstractShortCutDialog(Shell parentShell, int style) {
		super(parentShell, style);
		
		initShortCut();
	}
	
	/**
	 * short cut
	 */
	protected void initShortCut() {
		prefixCtrlShortcut  = ShortcutPrefixUtils.getCtrlShortcut();
		prefixAltShortcut = ShortcutPrefixUtils.getAltShortcut();
		prefixShiftShortcut = "Shift";
	}
	
	protected void initShortList() {
		listShortcut.add(new ShortcutHelpDAO("Add Block Comment", 		prefixCtrlShortcut +"+ /"));
		
		listShortcut.add(new ShortcutHelpDAO("Delete Line", 			prefixCtrlShortcut + "+ D") 		);
		listShortcut.add(new ShortcutHelpDAO("Go to Line", 				prefixCtrlShortcut + "+ L") 		);
		
		listShortcut.add(new ShortcutHelpDAO("To Lower case", 			prefixCtrlShortcut + "+ Shift + Y") );
		listShortcut.add(new ShortcutHelpDAO("To Upper case", 			prefixCtrlShortcut + "+ Shift + X") );
		listShortcut.add(new ShortcutHelpDAO("Shortcut Help", 			prefixCtrlShortcut + "+ Shift + L") );
		
		listShortcut.add(new ShortcutHelpDAO("Clear page", 				prefixCtrlShortcut + "+ F7") 				);
		listShortcut.add(new ShortcutHelpDAO("Select All", 				prefixCtrlShortcut + "+ A") 		);
		listShortcut.add(new ShortcutHelpDAO("Go to Line", 				prefixCtrlShortcut + "+ L") 		);
		listShortcut.add(new ShortcutHelpDAO("Remove Line", 			prefixCtrlShortcut + "+ D") 		);
		listShortcut.add(new ShortcutHelpDAO("Remove to line start", 	prefixCtrlShortcut + "+ backspace") 		);
		
		listShortcut.add(new ShortcutHelpDAO("Undo", 					prefixCtrlShortcut + "+ Z") 		);
		listShortcut.add(new ShortcutHelpDAO("Redo", 					prefixCtrlShortcut + "+ Y") 		);
		
		listShortcut.add(new ShortcutHelpDAO("Copy text", 				prefixCtrlShortcut + "+ C") 		);
		listShortcut.add(new ShortcutHelpDAO("Past text", 				prefixCtrlShortcut + "+ V") 		);
		listShortcut.add(new ShortcutHelpDAO("Duplicate select text", 	prefixCtrlShortcut + " + " + prefixShiftShortcut +  "+ D") 		);
		
		listShortcut.add(new ShortcutHelpDAO("Find text", 				prefixCtrlShortcut + "+ F") 		);
		listShortcut.add(new ShortcutHelpDAO("Find next text", 			prefixCtrlShortcut + "+ G") 		);
		listShortcut.add(new ShortcutHelpDAO("Find previous text", 		prefixCtrlShortcut + " + " + prefixShiftShortcut + "+ G") );
		listShortcut.add(new ShortcutHelpDAO("Find and replace text", 	prefixCtrlShortcut  + " + " + prefixAltShortcut + "+ F") );
	}

}

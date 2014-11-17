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
package com.hangum.tadpole.help.core.views.sub.shortcutkey;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;
import com.hangum.tadpole.help.core.Messages;

/**
 * abstract shortcut key
 * 
 * @author hangum
 *
 */
public abstract class AbstraceShortcutKeyComposite extends Composite {

	protected String prefixCtrlShortcut = ""; //$NON-NLS-1$
	protected String prefixAltShortcut = ""; //$NON-NLS-1$
	protected String prefixShiftShortcut = ""; //$NON-NLS-1$
	
	protected List<ShortcutHelpDAO> listShortcut = new ArrayList<ShortcutHelpDAO>();

	public AbstraceShortcutKeyComposite(Composite parent, int style) {
		super(parent, style);
		
		initShortCut();
	}
	
	/**
	 * short cut
	 */
	protected void initShortCut() {
		prefixCtrlShortcut  = ShortcutPrefixUtils.getCtrlShortcut();
		prefixAltShortcut 	= ShortcutPrefixUtils.getAltShortcut();
		prefixShiftShortcut = Messages.AbstractShortCutDialog_3;
	}
	
	protected void initShortList() {
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_4, 	prefixCtrlShortcut +"+ /")); //$NON-NLS-2$
		
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_6, 	prefixCtrlShortcut + "+ D") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_8, 	prefixCtrlShortcut + "+ L") 		); //$NON-NLS-2$
		
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_10, 	prefixCtrlShortcut + "+ Shift + Y") ); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_12, 	prefixCtrlShortcut + "+ Shift + X") ); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_14, 	prefixCtrlShortcut + "+ Shift + L") ); //$NON-NLS-2$
		
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_16, 	prefixCtrlShortcut + "+ F7") 				); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_18, 	prefixCtrlShortcut + "+ A") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_20, 	prefixCtrlShortcut + "+ L") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_22, 	prefixCtrlShortcut + "+ D") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_24, 	prefixCtrlShortcut + "+ backspace") 		); //$NON-NLS-2$
		
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_26, 	prefixCtrlShortcut + "+ Z") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_28, 	prefixCtrlShortcut + "+ Y") 		); //$NON-NLS-2$
		
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_30, 	prefixCtrlShortcut + "+ C") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_32, 	prefixCtrlShortcut + "+ V") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_34, 	prefixCtrlShortcut + " + " + prefixShiftShortcut +  "+ D") 		); //$NON-NLS-2$ //$NON-NLS-3$
		
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_37, 	prefixCtrlShortcut + "+ F") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_39, 	prefixCtrlShortcut + "+ G") 		); //$NON-NLS-2$
		listShortcut.add(new ShortcutHelpDAO(Messages.AbstractShortCutDialog_41, 	prefixCtrlShortcut + " + " + prefixShiftShortcut + "+ G") ); //$NON-NLS-2$ //$NON-NLS-3$
		
	}

	@Override
	protected void checkSubclass() {
	}
}

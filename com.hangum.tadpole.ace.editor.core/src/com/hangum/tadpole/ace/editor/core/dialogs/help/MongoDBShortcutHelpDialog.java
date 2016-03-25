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
package com.hangum.tadpole.ace.editor.core.dialogs.help;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.ace.editor.core.Messages;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ServletUserAgent;

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
		super.initShortList();
		if(RequestInfoUtils.findOSSimpleType() == ServletUserAgent.OS_SIMPLE_TYPE.MACOSX) {
			listShortcut.add( new ShortcutHelpDAO(Messages.get().MongoDBShortcutHelpDialog_0, 	"Ctrl + Space|Option + Space ") 		); //$NON-NLS-2$
		} else {
			listShortcut.add( new ShortcutHelpDAO(Messages.get().MongoDBShortcutHelpDialog_0, 	"Ctrl + Space") 		); //$NON-NLS-2$
		}
		
		tableViewer.refresh(listShortcut);		
	}
	
}

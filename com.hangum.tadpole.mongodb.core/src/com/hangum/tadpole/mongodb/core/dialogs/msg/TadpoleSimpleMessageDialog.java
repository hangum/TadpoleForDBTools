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
package com.hangum.tadpole.mongodb.core.dialogs.msg;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.mongodb.core.Messages;

/**
 * tadpole message dialog
 * 
 * @author hangum
 *
 */
public class TadpoleSimpleMessageDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(TadpoleSimpleMessageDialog.class);
	private TadpoleEditorWidget tadpoleEditor;
	
	String title;
	String content;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TadpoleSimpleMessageDialog(Shell parentShell, String title, String content) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.title = title;
		this.content = content;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tadpoleEditor = new TadpoleEditorWidget(compositeBody, SWT.BORDER, EditorDefine.EXT_JSON, JSONUtil.getPretty(content), "");
		tadpoleEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	private void initUI() {
		try {
			tadpoleEditor.setText(JSONUtil.getPretty(content));
		} catch(Exception e) {
			logger.error("server status", e); //$NON-NLS-1$
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 450);
	}
}

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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
public class TadpoleMessageDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(TadpoleMessageDialog.class);
	private TadpoleEditorWidget textMessage;
	
	String title;
	String head;
	String message;
	private Text text;
	private Label lblDate;
	private Label lblMessage;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TadpoleMessageDialog(Shell parentShell, String title, String head, String message) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.title = title;
		this.head = head;
		this.message = message;
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
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		lblDate = new Label(container, SWT.NONE);
		lblDate.setText(Messages.get().Date);
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.setText(head);
		
		lblMessage = new Label(container, SWT.NONE);
		lblMessage.setText(Messages.get().Message);
		new Label(container, SWT.NONE);
		
		textMessage = new TadpoleEditorWidget(container, SWT.BORDER, EditorDefine.EXT_JSON, "", "");
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return area;
	}
	
	private void initUI() {
		try {
			textMessage.setText(JSONUtil.getPretty(message));
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
		return new Point(450, 422);
	}
}

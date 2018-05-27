/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.msg;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.rdb.core.Messages;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * tadpole clipboard dialog
 * 
 * @author hangum
 *
 */
public class TDBClipboardDialog extends TitleAreaDialog {
	private static final Logger logger = Logger.getLogger(TDBClipboardDialog.class);
	protected Text textMessage;

	protected String title;
	protected String strResultData;
	protected Label lblMessage;
	private Label label;
	private Text txtSeperate;
	private Composite compositeExtension;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public TDBClipboardDialog(Shell parentShell, String title, String strResultData) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);

		this.title = Messages.get().ClipboardDialog;
		this.strResultData = strResultData;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ClipboardDialog);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage(title, IMessageProvider.INFORMATION);

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		lblMessage = new Label(container, SWT.NONE);
		lblMessage.setText(Messages.get().TDBInfoDialog_0);
		new Label(container, SWT.NONE);

		textMessage = new Text(container, SWT.BORDER  | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.stateMask == SWT.CTRL && e.keyCode == 'c') {
					closeWindow();
				} else if (e.stateMask == SWT.COMMAND && e.keyCode == 'c') {
					closeWindow();
				}
			}
		});
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		compositeExtension = new Composite(container, SWT.NONE);
		compositeExtension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		compositeExtension.setLayout(new GridLayout(2, false));

		label = new Label(compositeExtension, SWT.NONE);
		label.setText(Messages.get().Separator);

		txtSeperate = new Text(compositeExtension, SWT.BORDER);
		txtSeperate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.Selection) {
					initData();
				}
			}
		});
		txtSeperate.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					e.doit = false;
				}
			}
		});
		txtSeperate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtSeperate.setText("\\t");

		initData();

		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return area;
	}

	/**
	 * 초기 데이터를 바꾼다.
	 */
	private void initData() {
		String strSep = txtSeperate.getText();
		if(strSep.equals("\\t")) {
			strSep = "	";
		}
		String strMsg = StringUtils.replace(strResultData, PublicTadpoleDefine.DELIMITER, strSep) + PublicTadpoleDefine.LINE_SEPARATOR;
		
		//
		if(strSep.equals(",")) {
			strMsg = StringUtils.replace(strMsg, ",''", ",") + PublicTadpoleDefine.LINE_SEPARATOR;
		}

		textMessage.setText(strMsg);
		textMessage.setFocus();
		textMessage.setSelection(0, strMsg.length());
	}

	/**
	 * close window
	 */
	private void closeWindow() {
		super.close();
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(670, 350);
	}
}

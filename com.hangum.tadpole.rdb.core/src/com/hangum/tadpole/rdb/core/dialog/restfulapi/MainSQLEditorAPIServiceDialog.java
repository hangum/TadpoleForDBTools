/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.restfulapi;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.utils.VelocityUtils;
import com.hangum.tadpole.engine.restful.RESTfulAPIUtils;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * Test API service dialog
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 19.
 *
 */
public class MainSQLEditorAPIServiceDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(MainSQLEditorAPIServiceDialog.class);
	
	private String strOriginalSQL;
	
	private Text textArgument;
	
	private Text textOriginal;
	private Text textResultSQL;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param strOriginalSQL
	 */
	public MainSQLEditorAPIServiceDialog(Shell parentShell, String strOriginalSQL) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.strOriginalSQL = strOriginalSQL;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.MainSQLEditorAPIServiceDialog_0);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite compositeTitle = new Composite(container, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(2, false));
		
		Label lblArgument = new Label(compositeTitle, SWT.NONE);
		lblArgument.setText(Messages.MainSQLEditorAPIServiceDialog_1);
		
		textArgument = new Text(compositeTitle, SWT.BORDER);
		textArgument.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		SashForm sashForm = new SashForm(container, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpOriginalSql = new Group(sashForm, SWT.NONE);
		grpOriginalSql.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpOriginalSql.setText(Messages.MainSQLEditorAPIServiceDialog_2);
		grpOriginalSql.setLayout(new GridLayout(1, false));
		
		textOriginal = new Text(grpOriginalSql, SWT.BORDER | SWT.MULTI);
		textOriginal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpResultSql = new Group(sashForm, SWT.NONE);
		grpResultSql.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpResultSql.setText(Messages.MainSQLEditorAPIServiceDialog_3);
		grpResultSql.setLayout(new GridLayout(1, false));
		
		textResultSQL = new Text(grpResultSql, SWT.BORDER | SWT.MULTI);
		textResultSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();

		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	/**
	 * initialize UI
	 */
	private void initUI() {
		textOriginal.setText(strOriginalSQL);
		textArgument.setText(RESTfulAPIUtils.getParameter(strOriginalSQL));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		try {
			Map<String, String> mapParameter = RESTfulAPIUtils.maekArgumentTOMap(textArgument.getText());
			String strResult = VelocityUtils.getTemplate("MainEditorTest", textOriginal.getText(), mapParameter); //$NON-NLS-1$
			if(logger.isDebugEnabled()) logger.debug(strResult);
			textResultSQL.setText(strResult);
			
			textResultSQL.getParent().layout();
			
		} catch(Exception e) {
			logger.error("Template Exception", e); //$NON-NLS-1$
			MessageDialog.openError(getShell(), Messages.MainSQLEditorAPIServiceDialog_6, Messages.MainSQLEditorAPIServiceDialog_7);
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.MainSQLEditorAPIServiceDialog_9, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.MainSQLEditorAPIServiceDialog_8, false);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(650, 500);
	}
}

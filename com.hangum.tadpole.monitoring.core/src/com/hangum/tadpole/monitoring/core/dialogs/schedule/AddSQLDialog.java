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
package com.hangum.tadpole.monitoring.core.dialogs.schedule;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.system.ScheduleDAO;
import com.hangum.tadpole.monitoring.core.Messages;

/**
 * add sql dialoa
 * 
 * @author hangum
 *
 */
public class AddSQLDialog extends Dialog {
	ScheduleDAO dao = new ScheduleDAO();  
			
	private Text textTitle;
	private Text textSQL;
	private Text textDesc;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddSQLDialog(Shell parentShell, int intTotCount) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.dao.setSend_seq(intTotCount++);
	}
	
	public AddSQLDialog(Shell parentShell, ScheduleDAO dao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.dao = dao;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add SQL"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.numColumns = 2;
		
		Label lblSubTitle = new Label(container, SWT.NONE);
		lblSubTitle.setText(Messages.get().AddSQLDialog_0);
		
		textTitle = new Text(container, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDesc = new Label(container, SWT.NONE);
		lblDesc.setText(Messages.get().Description);
		
		textDesc = new Text(container, SWT.BORDER);
		textDesc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSql = new Label(container, SWT.NONE);
		lblSql.setText(Messages.get().SQL);
		
		textSQL = new Text(container, SWT.BORDER | SWT.MULTI);
		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	private void initUI() {
		textTitle.setText(dao.getName());
		textDesc.setText(dao.getDescription());
		textSQL.setText(dao.getSql());
	}
	
	@Override
	protected void okPressed() {
		String txtTitle = StringUtils.trimToEmpty(textTitle.getText());
		String txtDesc = StringUtils.trimToEmpty(textDesc.getText());
		String txtSQL = StringUtils.trimToEmpty(textSQL.getText());
		
		if(StringUtils.isEmpty(txtTitle)) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().AddSQLDialog_4);
			textTitle.setFocus();
			return;
		}
		
		if(StringUtils.isEmpty(txtSQL)) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().AddSQLDialog_6);
			textSQL.setFocus();
			return;
		}
		
		dao.setName(txtTitle);
		dao.setDescription(txtDesc);
		dao.setSql(txtSQL);
		
		super.okPressed();
	}
	
	public ScheduleDAO getDao() {
		return dao;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(445, 363);
	}

}

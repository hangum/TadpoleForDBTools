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
package com.hangum.tadpole.rdb.core.viewers.sql.template;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.query.dao.system.TadpoleTemplateDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SQLTemplate;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * SQLTemplate dialog
 * 
 * @author hangum
 *
 */
public class SQLTemplateDialog extends Dialog {
	private static Logger logger = Logger.getLogger(SQLTemplateDialog.class);
	
	private Combo comboGroupName;
	private Text textName;
	private Text textDescription;
	private Text textSQL;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SQLTemplateDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblGroupname = new Label(container, SWT.NONE);
		lblGroupname.setText("GroupName");
		
		comboGroupName = new Combo(container, SWT.NONE);
		comboGroupName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name");
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textDescription.heightHint = 30;
		gd_textDescription.minimumHeight = 30;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblSql = new Label(container, SWT.NONE);
		lblSql.setText("SQL");
		
		textSQL = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_textSQL = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_textSQL.minimumHeight = 1;
		textSQL.setLayoutData(gd_textSQL);

		return container;
	}
	
	@Override
	protected void okPressed() {
		String strGroupName = comboGroupName.getText();
		String strTextName = textName.getText();
		String strDescription = textDescription.getText();
		String strSQL = textSQL.getText();
		
		// check validation
		
		// 
		TadpoleTemplateDAO dao = new TadpoleTemplateDAO();
		dao.setUser_seq(SessionManager.getUserSeq());
		dao.setCategory("00");
		dao.setGroup_name(strGroupName);
		dao.setName(strTextName);
		dao.setDescription(strDescription);
		dao.setContent(strSQL);
		try {
			TadpoleSystem_SQLTemplate.insertSQLTemplate(dao);
		} catch (Exception e) {
			logger.error("SQL template insert", e);
		}
		
		
		super.okPressed();
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(487, 387);
	}

}

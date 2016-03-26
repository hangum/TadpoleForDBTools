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

import org.apache.log4j.Logger;
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

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.SQLTemplateDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SQLTemplate;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.sql.template.SQLTemplateView.SQL_TEMPLATE_TYPE;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * SQLTemplate dialog
 * 
 * @author hangum
 *
 */
public class SQLTemplateDialog extends Dialog {
	private static Logger logger = Logger.getLogger(SQLTemplateDialog.class);
	
	private SQL_TEMPLATE_TYPE sqlTemplateType;
	
	private Text textName;
	private Text textDescription;
	private Text textSQL;
	
	private SQLTemplateDAO oldSQLTemplateDAO;
	private SQLTemplateDAO sqlTemplateDAO;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param SQLTemplateType sql template type
	 */
	public SQLTemplateDialog(Shell parentShell, SQL_TEMPLATE_TYPE SQLTemplateType) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.sqlTemplateType = SQLTemplateType;
	}
	
	public SQLTemplateDialog(Shell parentShell, SQLTemplateDAO oldSQLTemplateDAO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.oldSQLTemplateDAO = oldSQLTemplateDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().SQLTemplate);
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
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText(Messages.get().Name);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setText(Messages.get().Description);
		
		textDescription = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textDescription.heightHint = 30;
		gd_textDescription.minimumHeight = 30;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblSql = new Label(container, SWT.NONE);
		lblSql.setText(Messages.get().SQL);
		
		textSQL = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_textSQL = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_textSQL.minimumHeight = 1;
		textSQL.setLayoutData(gd_textSQL);
		
		textName.setFocus();
		
		initUIData();

		return container;
	}
	
	/**
	 * initialize ui data 
	 */
	private void initUIData() {
		if(oldSQLTemplateDAO != null) {
			textName.setText(oldSQLTemplateDAO.getName());
			textDescription.setText(oldSQLTemplateDAO.getDescription());
			textSQL.setText(oldSQLTemplateDAO.getContent());
		}
	}
	
	@Override
	protected void okPressed() {
		String strTextName = textName.getText();
		String strDescription = textDescription.getText();
		String strSQL = textSQL.getText();
		
		// check validation
		if(strTextName.equals("")) {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().SQLTemplateDialog_NameEmpty);
			textName.setFocus();
			return;
		} else if(strSQL.equals("")) {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().SQLTemplateDialog_SQLEmpty);
			textSQL.setFocus();
			return;
		}
		
		if(oldSQLTemplateDAO != null) {
			oldSQLTemplateDAO.setName(strTextName);
			oldSQLTemplateDAO.setDescription(strDescription);
			oldSQLTemplateDAO.setContent(strSQL);
			try {
				TadpoleSystem_SQLTemplate.updateSQLTemplate(oldSQLTemplateDAO);
				
				super.okPressed();
			} catch (Exception e) {
				logger.error("SQL template insert", e);
			}
		} else {
			// 
			sqlTemplateDAO = new SQLTemplateDAO();
			sqlTemplateDAO.setUser_seq(SessionManager.getUserSeq());
			sqlTemplateDAO.setCategory(sqlTemplateType.toString());
			sqlTemplateDAO.setGroup_name("");//strGroupName);
			sqlTemplateDAO.setName(strTextName);
			sqlTemplateDAO.setDescription(strDescription);
			sqlTemplateDAO.setContent(strSQL);
			try {
				TadpoleSystem_SQLTemplate.insertSQLTemplate(sqlTemplateDAO);
				
				super.okPressed();
			} catch (Exception e) {
				logger.error("SQL template insert", e);
			}
		}
		
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().CANCEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(487, 387);
	}
	
	public SQLTemplateDAO getSqlTemplateDAO() {
		return sqlTemplateDAO;
	}
	
	public SQLTemplateDAO getOldSqlTemplateDAO() {
		return oldSQLTemplateDAO;
	}

}

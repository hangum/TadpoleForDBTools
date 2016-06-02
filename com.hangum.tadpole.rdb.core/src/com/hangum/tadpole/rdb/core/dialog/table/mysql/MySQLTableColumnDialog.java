/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.table.mysql;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DATA_STATUS;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.dbms.MySQLUtils;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TableColumnObjectQuery;

/**
 * TableColumn dialog
 * 
 * @author hangum
 *
 */
public class MySQLTableColumnDialog extends TitleAreaDialog {
	private static final Logger logger = Logger.getLogger(MySQLTableColumnDialog.class);
	private PublicTadpoleDefine.DATA_STATUS COMP_STATUS = DATA_STATUS.NEW;
	
	private UserDBDAO userDB;
	private TableDAO tableDAO;
	private TableColumnDAO tableColumnDAO;
	
	private Text textColumnName;
	private Combo comboType;
	private Text textDefault;
	private Button btnPrimaryKey;
	private Button btnNotNull;
	private Button btnAutoIncrement;
	private Combo comboCollation;
	private Text textComment;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public MySQLTableColumnDialog(Shell parentShell, UserDBDAO userDB, TableDAO tableDAO) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.userDB = userDB;
		this.tableDAO = tableDAO;
	}

	/**
	 * modify table column dialog
	 * 
	 * @param shell
	 * @param userDB
	 * @param tableDAO
	 * @param tableColumnDAO
	 */
	public MySQLTableColumnDialog(Shell parentShell, UserDBDAO userDB, TableDAO tableDAO, TableColumnDAO tableColumnDAO) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		COMP_STATUS = DATA_STATUS.MODIFY;
		
		this.userDB = userDB;
		this.tableDAO = tableDAO;
		this.tableColumnDAO = tableColumnDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		super.setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		if(COMP_STATUS == DATA_STATUS.MODIFY) {
			newShell.setText(String.format(Messages.get().MySQLTableColumnDialog_0, tableDAO.getName()));
		} else {
			newShell.setText(String.format(Messages.get().MySQLTableColumnDialog_1, tableDAO.getName()));
		}
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		if(COMP_STATUS == DATA_STATUS.MODIFY) {
			setTitle(String.format(Messages.get().MySQLTableColumnDialog_2, tableColumnDAO.getField()));
		} else {
			setTitle(String.format(Messages.get().MySQLTableColumnDialog_3, tableDAO.getName()));
		}
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblColumnName = new Label(container, SWT.NONE);
		lblColumnName.setText(Messages.get().Name);
		
		textColumnName = new Text(container, SWT.BORDER);
		textColumnName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(container, SWT.NONE);
		lblType.setText(Messages.get().Type);
		
		comboType = new Combo(container, SWT.NONE);
		comboType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(String strType : ColumnDataTypeDef.getAllTypeNames(userDB.getDBDefine())) {
			comboType.add(strType);
		}
		comboType.setText("VARCHAR(45)"); //$NON-NLS-1$
		
		Label lblPrimaryKey = new Label(container, SWT.NONE);
		lblPrimaryKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		btnPrimaryKey = new Button(composite, SWT.CHECK);
		btnPrimaryKey.setText("Primary Key"); //$NON-NLS-1$
		
		btnNotNull = new Button(composite, SWT.CHECK);
		btnNotNull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textDefault.setText(""); //$NON-NLS-1$
				textDefault.setFocus();
			}
		});
		btnNotNull.setText("Not Null"); //$NON-NLS-1$
		
		btnAutoIncrement = new Button(composite, SWT.CHECK);
		btnAutoIncrement.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnPrimaryKey.setSelection(true);
				comboType.setText("INT"); //$NON-NLS-1$
			}
		});
		btnAutoIncrement.setText("Auto Increment"); //$NON-NLS-1$
		
		Label lblDefault = new Label(container, SWT.NONE);
		lblDefault.setText("Default"); //$NON-NLS-1$
		
		textDefault = new Text(container, SWT.BORDER);
		textDefault.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textDefault.setText("NULL"); //$NON-NLS-1$
		
		Label lblCollation = new Label(container, SWT.NONE);
		lblCollation.setText("Collation"); //$NON-NLS-1$
		
		comboCollation = new Combo(container, SWT.NONE);
		comboCollation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(String strCollation : MySQLUtils.getCollation(userDB)) {
			comboCollation.add(strCollation);
		}
		
		Label lblComment = new Label(container, SWT.NONE);
		lblComment.setText("Comment"); //$NON-NLS-1$
		
		textComment = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_textComment = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textComment.heightHint = 90;
		gd_textComment.minimumHeight = 90;
		textComment.setLayoutData(gd_textComment);
		
		initUI();
		
		textColumnName.setFocus();

		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return area;
	}
	
	/**
	 * initialize ui
	 */
	private void initUI() {
		if(COMP_STATUS == DATA_STATUS.MODIFY) {
			textColumnName.setText(tableColumnDAO.getField());
			comboType.setText(tableColumnDAO.getType());
			textDefault.setText(StringUtils.trimToEmpty(tableColumnDAO.getDefault()));
			
			boolean isPK = false;
			for(String strPK : PublicTadpoleDefine.DB_PRIMARY_KEY) {
				if(strPK.equals(tableColumnDAO.getPk())) isPK = true; 
			}
			btnPrimaryKey.setSelection(isPK);
			btnNotNull.setSelection("YES".equals(tableColumnDAO.getNull())); //$NON-NLS-1$
			btnAutoIncrement.setSelection("auto_increment".equals(tableColumnDAO.getExtra())); //$NON-NLS-1$
			comboCollation.setText(StringUtils.trimToEmpty(tableColumnDAO.getCollation_name()));
			textComment.setText(StringUtils.trimToEmpty(tableColumnDAO.getComment()));
		}
	}
	
	@Override
	protected void okPressed() {
		String strName = textColumnName.getText();
		String strType = comboType.getText();
		String strDefault = textDefault.getText();
		boolean isPrimaryKey = btnPrimaryKey.getSelection();
		boolean isNotNull = btnNotNull.getSelection();
		boolean isAutoIncrement = btnAutoIncrement.getSelection();
		String strCollation = comboCollation.getText();
		String strComment = textComment.getText();
		
		if(StringUtils.trimToEmpty(strName).equals("")) { //$NON-NLS-1$
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().MySQLTableColumnDialog_20);
			textColumnName.setFocus();
			
			return;
		} else if(StringUtils.trimToEmpty(strType).equals("")) { //$NON-NLS-1$
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().MySQLTableColumnDialog_23);
			textColumnName.setFocus();
			
			return;
		}
		
		TableColumnUpdateDAO metaDataDao = new TableColumnUpdateDAO();
		metaDataDao.setColumnName(strName);
		metaDataDao.setDataType(strType);
		metaDataDao.setDefaultValue(strDefault);
		metaDataDao.setPrimaryKey(isPrimaryKey);
		metaDataDao.setNotNull(isNotNull);
		metaDataDao.setAutoIncrement(isAutoIncrement);
		metaDataDao.setCollation(strCollation);
		metaDataDao.setComment(strComment);
		
		if(COMP_STATUS == DATA_STATUS.NEW) {
			try {
				TableColumnObjectQuery.addColumn(userDB, tableDAO, metaDataDao);
				refreshTableColumn();
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().MySQLTableColumnDialog_25);
				
				textColumnName.setText("");
				textComment.setText("");
				textColumnName.setFocus();
			} catch (Exception e) {
				logger.error("add colum exception", e);
				
				TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, Messages.get().MySQLTableColumnDialog_27 + e.getMessage());
				errDialog.open();
			}
		} else {
			try {
				TableColumnObjectQuery.updateColumn(userDB, tableDAO, tableColumnDAO, metaDataDao);
				refreshTableColumn();
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().MySQLTableColumnDialog_29);
				
				super.okPressed();
			} catch (Exception e) {
				logger.error("add column exception", e); //$NON-NLS-1$
				
				TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, Messages.get().MySQLTableColumnDialog_31 + e.getMessage());
				errDialog.open();
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
		return new Point(450, 405);
	}
	
	/**
	 * refresh table column
	 * @return
	 */
	private void refreshTableColumn() {
		try {
			ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
			if(ev != null) ev.refreshTableColumn();
		} catch(Exception e) {
		}
	}
}

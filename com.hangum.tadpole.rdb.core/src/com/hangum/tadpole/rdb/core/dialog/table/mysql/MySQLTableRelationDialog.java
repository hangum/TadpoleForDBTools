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
package com.hangum.tadpole.rdb.core.dialog.table.mysql;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDDLCommand;
import com.hangum.tadpole.engine.utils.RequestQueryUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * mysql table relation
 * 
 * @author hangum
 *
 */
public class MySQLTableRelationDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(MySQLTableRelationDialog.class);
	public static final String TEMP_REFERENCE_SQL = "ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s (%s) ON DELETE %s ON UPDATE %s";
	
	private UserDBDAO userDB;
	private TableDAO tableDAO;
	
	private Text textRefName;
	private Combo comboOriColumn;
	private Combo comboRefTableName;
	private Combo comboRefColumnName;
	private Combo comboOnUpdate;
	private Combo comboOnDelete;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MySQLTableRelationDialog(Shell parentShell, UserDBDAO userDB, TableDAO tableDAO) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.userDB = userDB;
		this.tableDAO = tableDAO; 
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(Messages.get().TadpoleTableComposite_Relation);
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
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		textRefName = new Text(container, SWT.BORDER);
		textRefName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpTableEmployees = new Group(container, SWT.NONE);
		grpTableEmployees.setLayout(new GridLayout(2, false));
		grpTableEmployees.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpTableEmployees.setText("Table : " + tableDAO.getFullName());
		
		Label lblColumn = new Label(grpTableEmployees, SWT.NONE);
		lblColumn.setText("Column");
		
		comboOriColumn = new Combo(grpTableEmployees, SWT.READ_ONLY);
		comboOriColumn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpReference = new Group(container, SWT.NONE);
		grpReference.setLayout(new GridLayout(2, false));
		grpReference.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpReference.setText("Reference");
		
		Label lblTableName = new Label(grpReference, SWT.NONE);
		lblTableName.setText("Table Name");
		
		comboRefTableName = new Combo(grpReference, SWT.READ_ONLY);
		comboRefTableName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeReferenceTable();
			}
		});
		comboRefTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblColumnName = new Label(grpReference, SWT.NONE);
		lblColumnName.setText("Column Name");
		
		comboRefColumnName = new Combo(grpReference, SWT.READ_ONLY);
		comboRefColumnName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpAction = new Group(container, SWT.NONE);
		grpAction.setLayout(new GridLayout(2, false));
		grpAction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpAction.setText("Action");
		
		Label lblOnUpdate = new Label(grpAction, SWT.NONE);
		lblOnUpdate.setText("On update");
		
		comboOnUpdate = new Combo(grpAction, SWT.READ_ONLY);
		comboOnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOnUpdate.add("RESTRICT");
		comboOnUpdate.add("CASCADE");
		comboOnUpdate.add("SET NULL");
		comboOnUpdate.add("NO ACTION");
		comboOnUpdate.setText("NO ACTION");
		
		Label lblOnDelete = new Label(grpAction, SWT.NONE);
		lblOnDelete.setText("On delete");
		
		comboOnDelete = new Combo(grpAction, SWT.READ_ONLY);
		comboOnDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboOnDelete.add("RESTRICT");
		comboOnDelete.add("CASCADE");
		comboOnDelete.add("SET NULL");
		comboOnDelete.add("NO ACTION");
		comboOnDelete.setText("NO ACTION");

		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		textRefName.setFocus();
		
		return container;
	}
	
	/**
	 * Initialize combo column
	 * 
	 * @param comboColumn
	 * @param tableDAO
	 */
	private void initComboColumn(Combo comboColumn, TableDAO tableDAO) {
		comboColumn.removeAll();
		
		try {
			List<TableColumnDAO> tmpTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDAO);
			for (TableColumnDAO tableColumnDAO : tmpTableColumns) {
				String strTitle = "";
				if("".equals(tableColumnDAO.getComment())) {
					strTitle = String.format("%s (%s)-%s", tableColumnDAO.getField(), tableColumnDAO.getType(), tableColumnDAO.getKey());
				} else {
					strTitle = String.format("%s (%s)-%s-%s", tableColumnDAO.getField(), tableColumnDAO.getType(), tableColumnDAO.getKey(), tableColumnDAO.getComment());
				}
				comboColumn.add(strTitle);
				comboColumn.setData(strTitle, tableColumnDAO);
			}
			comboColumn.select(0);
		} catch(Exception e) {
			logger.error("initialize column combo", e);
		}
	}
	
	/**
	 * change reference table
	 */
	private void changeReferenceTable() {
		TableDAO selectTableDAO = (TableDAO)comboRefTableName.getData(comboRefTableName.getText());
		initComboColumn(comboRefColumnName, selectTableDAO);
	}
	
	/**
	 * initialize UI
	 */
	private void initUI() {
		try {
			initComboColumn(comboOriColumn, tableDAO);
			
			// table 목록
			List<TableDAO> listTablesDAO = TadpoleObjectQuery.getTableList(userDB);
			for (TableDAO tmpTableDAO : listTablesDAO) {
				if(!tableDAO.getName().equals(tmpTableDAO.getName())) {
					String strTitle = "";
					if("".equals(tmpTableDAO.getComment())) {
						strTitle = tmpTableDAO.getName();
					} else {
						strTitle = String.format("%s (%s)", tmpTableDAO.getName(), tmpTableDAO.getComment());
					}
					comboRefTableName.add(strTitle);
					comboRefTableName.setData(strTitle, tmpTableDAO);
				}
			}
			comboRefTableName.select(0);
			
			// setting reference table column
			changeReferenceTable();
		} catch (Exception e) {
			logger.error("init relation", e);
		}
	}
	
	@Override
	protected void okPressed() {
		// ALTER TABLE `actor` ADD CONSTRAINT `bacdef` FOREIGN KEY (`actor_id`) REFERENCES `city` (`city_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
		// ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s (%s) ON DELETE %s ON UPDATE %s;
		String strReferenceName = StringUtils.trimToEmpty(textRefName.getText());
		
		if(StringUtils.isEmpty(strReferenceName)) {
			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, "Please input the reference name");
			
			textRefName.setFocus();
			return ;
		}
		String strOriColumn = ((TableColumnDAO)comboOriColumn.getData(comboOriColumn.getText())).getField();
		String strRefTable = ((TableDAO)comboRefTableName.getData(comboRefTableName.getText())).getName();
		String strRefColumn = ((TableColumnDAO)comboRefColumnName.getData(comboRefColumnName.getText())).getField();
		String strSQL = String.format(TEMP_REFERENCE_SQL, tableDAO.getFullName(), 
				    SQLUtil.makeIdentifierName(userDB, strReferenceName), 
				    SQLUtil.makeIdentifierName(userDB, strOriColumn),
				    SQLUtil.makeIdentifierName(userDB, tableDAO.getSchema_name()) +"."+ SQLUtil.makeIdentifierName(userDB,strRefTable), 
				    SQLUtil.makeIdentifierName(userDB, strRefColumn),
					comboOnUpdate.getText(), comboOnDelete.getText()
				);
		try {
			ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, strSQL));
			
			ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
			if(ev != null) ev.refreshTable(true, tableDAO.getName());
	
			super.okPressed();
		} catch (Exception e) {
			logger.error("table create exception", e); //$NON-NLS-1$

			TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, Messages.get().TableCreationError + e.getMessage());
			errDialog.open();
			
			textRefName.setFocus();
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Confirm, true);
		createButton(parent, IDialogConstants.CANCEL_ID,  CommonMessages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 359);
	}

}

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

import java.util.Map;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.template.MySQLDMLTemplate;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDMLCommand;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.utils.RequestQueryUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * 테이블 생성 다이얼로그
 * 
 * @author hangum
 *
 */
public class MySQLTaableCreateDialog extends TitleAreaDialog {
	private static final Logger logger = Logger.getLogger(MySQLTaableCreateDialog.class);
	
	/** 테이블 생성이 정상 되었는지 채크하기 위함 */
	private boolean isCreated = false;
	
	private TableCreateDAO tableCreateDao;
	
	private UserDBDAO userDB;
	private Text textTableName;
	private Combo comboTableEncoding;
	private Combo comboTableCollation;
	private Combo comboTableType;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MySQLTaableCreateDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(Messages.get().MySQLTaableCreateDialog);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.get().MySQLTaableCreateDialog);
		
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblTableName = new Label(container, SWT.NONE);
		lblTableName.setText(Messages.get().TableName);
		
		textTableName = new Text(container, SWT.BORDER);
		textTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTableEncoding = new Label(container, SWT.NONE);
		lblTableEncoding.setText(Messages.get().TableEncoding);
		
		comboTableEncoding = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		comboTableEncoding.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeEncoding();
			}
		});
		comboTableEncoding.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTableCollation = new Label(container, SWT.NONE | SWT.READ_ONLY);
		lblTableCollation.setText(Messages.get().TableCollation);
		
		comboTableCollation = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		comboTableCollation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTableType = new Label(container, SWT.NONE);
		lblTableType.setText(Messages.get().TableType);
		
		comboTableType = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		comboTableType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return area;
	}
	
	/**
	 * change encoding
	 */
	private void changeEncoding() {
		comboTableCollation.removeAll();
		
		Map<Integer, Object> selColumnData = (Map<Integer, Object>)comboTableEncoding.getData(comboTableEncoding.getText());
		if(selColumnData != null) {
			try {
				// 으로 가져와서 comboTableEncoding 에 설정해 준다.
				QueryExecuteResultDTO showCharacterSet = ExecuteDMLCommand.executeQuery(userDB, 
						String.format("SELECT * FROM information_schema.collations WHERE character_set_name = '%s' ORDER BY collation_name ASC", selColumnData.get(0)), 0, 100);
				for (Map<Integer, Object> columnData : showCharacterSet.getDataList().getData()) {
					comboTableCollation.add(""+columnData.get(0));
					comboTableCollation.setData(""+columnData.get(0), ""+columnData.get(0));
				}
				comboTableCollation.select(0);
			} catch(Exception e) {
				logger.error("init table encoding ui", e);
			}
		}
		
	}
	
	/**
	 * initialize UI
	 */
	private void initUI() {
		try {
			/*
			 * default collation
			 	SHOW VARIABLES LIKE 'collation_database'
			 */
			String strDefaultCollation = "";
			QueryExecuteResultDTO showCollationDatabase = ExecuteDMLCommand.executeQuery(userDB, "SHOW VARIABLES LIKE 'collation_database'", 0, 10);
			for (Map<Integer, Object> columnData : showCollationDatabase.getDataList().getData()) {
				strDefaultCollation = ""+columnData.get(1);
			}
			
			// 으로 가져와서 comboTableEncoding 에 설정해 준다.
			QueryExecuteResultDTO showCharacterSet = ExecuteDMLCommand.executeQuery(userDB, "SELECT * FROM information_schema.character_sets ORDER BY character_set_name ASC", 0, 100);
			for (Map<Integer, Object> columnData : showCharacterSet.getDataList().getData()) {
				String strViewData = String.format("%s (%s)", columnData.get(2), columnData.get(1));
				if(StringUtils.startsWithIgnoreCase(""+columnData.get(1), strDefaultCollation)) {
					strDefaultCollation = strViewData;
				}
				comboTableEncoding.add(strViewData);
				comboTableEncoding.setData(strViewData, columnData);
			}
			comboTableEncoding.setText(strDefaultCollation);

			// default database encoding
			changeEncoding();
			
			/*
			 * default engine
			 */
			TadpoleResultSet tdbEngine = ExecuteDMLCommand.executeQuery(userDB, "SELECT engine, support, comment FROM information_schema.engines WHERE support IN ('DEFAULT', 'YES')", 0, 20).getDataList();
			String strDefaultEngine = "";
			for (Map<Integer, Object> mapColumnData : tdbEngine.getData()) {
				String strViewData = ""+mapColumnData.get(1);
				if(StringUtils.startsWithIgnoreCase(strViewData, "default")) {
					strViewData = String.format("%s (%s)", mapColumnData.get(1), mapColumnData.get(0));
					strDefaultEngine = strViewData;
				} else {
					strViewData = ""+mapColumnData.get(0);
				}
				comboTableType.add(strViewData);
				comboTableType.setData(strViewData, ""+mapColumnData.get(0));
			}
			comboTableType.setText(strDefaultEngine);

		} catch (Exception e) {
			logger.error("init table create ui", e);
		}
		
		textTableName.setFocus();		
	}
	
	@Override
	protected void okPressed() {
		String strTableName = StringUtils.trimToEmpty(textTableName.getText());
		if("".equals(strTableName)) {
			MessageDialog.openWarning(null, CommonMessages.get().Warning, Messages.get().TableCreationNameAlter);
			textTableName.setFocus();
			return;
		}
		
		tableCreateDao = new TableCreateDAO();
		tableCreateDao.setName(strTableName);
		Map<Integer, Object> selEncodingData = (Map<Integer, Object>)comboTableEncoding.getData(comboTableEncoding.getText());
		tableCreateDao.setEncoding(""+selEncodingData.get(0));
		tableCreateDao.setCollation(""+comboTableCollation.getData(comboTableCollation.getText()));
		tableCreateDao.setType(""+comboTableType.getData(comboTableType.getText()));
		
		if(MessageDialog.openConfirm(null, CommonMessages.get().Confirm, Messages.get().TableCreationWantToCreate)) {
			String strSQL = String.format(MySQLDMLTemplate.TMP_DIALOG_CREATE_TABLE,
					tableCreateDao.getFullName(userDB),
					tableCreateDao.getEncoding(),
					tableCreateDao.getCollation(),
					tableCreateDao.getType());
			
			try {
				ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, strSQL));
				
				// 테이블이 
				isCreated = true;
				
				ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
				if(ev != null) ev.refreshTable(true, strTableName);
		
				super.okPressed();
			} catch (Exception e) {
				logger.error("table create exception", e); //$NON-NLS-1$

				TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, Messages.get().TableCreationError + e.getMessage());
				errDialog.open();
				
				textTableName.setFocus();
			}
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
		return new Point(450, 300);
	}
	
	/**
	 * get table create
	 * @return
	 */
	public TableCreateDAO getTableCreateDao() {
		return tableCreateDao;
	}
	
	/**
	 * 테이블이 정상생성 되었는지 구분
	 * @return
	 */
	public boolean isCreated() {
		return isCreated;
	}
}

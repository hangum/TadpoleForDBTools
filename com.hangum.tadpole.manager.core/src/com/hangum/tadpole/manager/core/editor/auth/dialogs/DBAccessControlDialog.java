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
package com.hangum.tadpole.manager.core.editor.auth.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_AccessControl;

/**
 *  SQLAudit
 *  	Table, column filter dialog
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 1.
 *
 */
public class DBAccessControlDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(DBAccessControlDialog.class);
	
	private UserDBDAO useDB;
	private TadpoleUserDbRoleDAO userRoleDB;
	private TableViewer tvSelect;
//	private List<TableFilterDAO> listTableColumns = new ArrayList<TableFilterDAO>();
	private List<AccessCtlObjectDAO> listTableColumn = new ArrayList<AccessCtlObjectDAO>();
	private Text textDBName;

	private Combo comboUser;
	
	private Button btnSelect;
	private Button btnInsert;
	private Button btnUpdate;
	private Button btnDelete;
	private Button btnDdl;
	
	/** select user access control data */
	private DBAccessControlDAO dbAccessDetail;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DBAccessControlDialog(Shell parentShell, final TadpoleUserDbRoleDAO userRoleDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.userRoleDB = userRoleDB;
		this.useDB = userRoleDB.getParent();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText("DB Access Control dialog"); //$NON-NLS-1$
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
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblDbName = new Label(compositeHead, SWT.NONE);
		lblDbName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDbName.setText("DB Name");
		
		textDBName = new Text(compositeHead, SWT.BORDER);
		textDBName.setEditable(false);
		textDBName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textDBName.setText(useDB.getDisplay_name());
		
		Label lblUser = new Label(compositeHead, SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText("User");
		
		comboUser = new Combo(compositeHead, SWT.READ_ONLY);
		comboUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		comboUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		List<TadpoleUserDbRoleDAO> listUser = useDB.getListChildren();
		for (int i=0; i<listUser.size(); i++) {
			TadpoleUserDbRoleDAO tadpoleUserDbRoleDAO = listUser.get(i);
			comboUser.add(String.format("%s (%s)", tadpoleUserDbRoleDAO.getName(), tadpoleUserDbRoleDAO.getEmail()));
			comboUser.setData(""+i, tadpoleUserDbRoleDAO);
		}
		comboUser.select(0);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpAuthority = new Group(compositeBody, SWT.NONE);
		grpAuthority.setLayout(new GridLayout(2, false));
		grpAuthority.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpAuthority.setText("Authority");
		
		btnSelect = new Button(grpAuthority, SWT.CHECK);
		btnSelect.setEnabled(false);
		btnSelect.setSelection(true);
		btnSelect.setText("SELECT");
		
		Composite compositeSelectBtn = new Composite(grpAuthority, SWT.NONE);
		compositeSelectBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeSelectBtn.setLayout(new GridLayout(3, false));
		
		Button btnSelectAdd = new Button(compositeSelectBtn, SWT.NONE);
		btnSelectAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableColumnFilterDialog tableColumnDialog = new TableColumnFilterDialog(getShell(), dbAccessDetail);
				if(Dialog.OK == tableColumnDialog.open()) {
					listTableColumn.add(tableColumnDialog.getUpdateData());
					tvSelect.refresh();
				}
			}
		});
		btnSelectAdd.setText("Add");
		
		Button btnSelectDelete = new Button(compositeSelectBtn, SWT.NONE);
		btnSelectDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tvSelect.getSelection();
				if(!iss.isEmpty()) {
					if(!MessageDialog.openConfirm(getShell(), "Confirm", "Do you want delete")) return;
					
					AccessCtlObjectDAO dao = (AccessCtlObjectDAO)iss.getFirstElement();
					
					listTableColumn.remove(dao);
					tvSelect.refresh();
				}
			}
		});
		btnSelectDelete.setText("Delete");
		new Label(compositeSelectBtn, SWT.NONE);
		
//		Button btnSelectUpdate = new Button(compositeSelectBtn, SWT.NONE);
//		btnSelectUpdate.setText("Update");
		new Label(grpAuthority, SWT.NONE);
		
		tvSelect = new TableViewer(grpAuthority, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvSelect.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tvSelect, SWT.NONE);
		TableColumn tblclmnTable = tableViewerColumn.getColumn();
		tblclmnTable.setWidth(150);
		tblclmnTable.setText("Table");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tvSelect, SWT.NONE);
		TableColumn tblclmnColumn = tableViewerColumn_1.getColumn();
		tblclmnColumn.setWidth(500);
		tblclmnColumn.setText("Column");
		
		tvSelect.setContentProvider(new ArrayContentProvider());
		tvSelect.setLabelProvider(new SelectTableFilterLabelprovider());
		tvSelect.setInput(listTableColumn);
		
		
		btnInsert = new Button(grpAuthority, SWT.CHECK);
		btnInsert.setText("INSERT");
		new Label(grpAuthority, SWT.NONE);
		
		btnUpdate = new Button(grpAuthority, SWT.CHECK);
		btnUpdate.setText("UPDATE");
		new Label(grpAuthority, SWT.NONE);
		
		btnDelete = new Button(grpAuthority, SWT.CHECK);
		btnDelete.setText("DELETE");
		new Label(grpAuthority, SWT.NONE);
		
		btnDdl = new Button(grpAuthority, SWT.CHECK);
		btnDdl.setText("DDL");
		new Label(grpAuthority, SWT.NONE);
	
		firstInit();
		initData();

		return container;
	}
	
	/**
	 * Initialize UI
	 */
	private void firstInit() {
		comboUser.setText(String.format("%s (%s)", 	userRoleDB.getName(), userRoleDB.getEmail()));
	}
		
	/**
	 * init data
	 */
	private void initData() {
		listTableColumn.clear();
		TadpoleUserDbRoleDAO tadpoleUserDbRoleDAO = (TadpoleUserDbRoleDAO)comboUser.getData(""+comboUser.getSelectionIndex());
	
		try {
			dbAccessDetail = TadpoleSystem_AccessControl.getDBAccessControl(tadpoleUserDbRoleDAO);
			
			btnSelect.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getSelect_lock()));
			btnInsert.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getInsert_lock()));
			btnUpdate.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getUpdate_lock()));
			btnDelete.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getDelete_locl()));
			btnDdl.setSelection(PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getDdl_lock()));
			
			listTableColumn.addAll(dbAccessDetail.getMapSelectAccessCtl().values());
			tvSelect.refresh();
			
		} catch (Exception e) {
			logger.error("get access control", e);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if(!MessageDialog.openConfirm(getShell(), "Confirm", "Do you want to update?")) return;
		
		DBAccessControlDAO dao = new DBAccessControlDAO();
		dao.setSeq(dbAccessDetail.getSeq());
		
		dao.setSelect_lock(btnSelect.getSelection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		dao.setInsert_lock(btnInsert.getSelection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		dao.setUpdate_lock(btnUpdate.getSelection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		dao.setDelete_locl(btnDelete.getSelection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		dao.setDdl_lock(btnDdl.getSelection()?PublicTadpoleDefine.YES_NO.YES.name():PublicTadpoleDefine.YES_NO.NO.name());
		
		Map<String, AccessCtlObjectDAO> mapSelectAccessCtl = new HashMap<String, AccessCtlObjectDAO>();
		for(AccessCtlObjectDAO objectDao : listTableColumn) {
			mapSelectAccessCtl.put(objectDao.getObj_name(), objectDao);
		}
		dao.setMapSelectAccessCtl(mapSelectAccessCtl);
		
		// 
		try {
			TadpoleSystem_AccessControl.updateDBAccessControl(dao);
		} catch (Exception e) {
			logger.error("Update dbAccessContorl error", e);
			MessageDialog.openError(getShell(), "Error", "During update : " + e.getMessage());
			return;
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
		return new Point(500, 450);
	}
}

/**
 * table colum filter labelprovider
 * 
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 29.
 *
 */
class SelectTableFilterLabelprovider extends LabelProvider implements ITableLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		AccessCtlObjectDAO dao = (AccessCtlObjectDAO)element;
		
		switch (columnIndex) {
		case 0: return dao.getObj_name();
		default: return dao.getDetail_obj();
		}
		
//		return "### not set column ###";
	}
	
}
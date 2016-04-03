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
package com.hangum.tadpole.manager.core.editor.db.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.AccessCtlObjectDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_AccessControl;
import com.hangum.tadpole.manager.core.Messages;

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
	
	private UserDBDAO userDB;
	private Text textDBName;
	private Combo comboUser;
	
	private Button btnSelectDelete;
	private TadpoleUserDbRoleDAO userRoleDB;
	private TableViewer tvSelect;
	private Button btnSelectAdd;
	private List<AccessCtlObjectDAO> listTableColumn = new ArrayList<AccessCtlObjectDAO>();
	
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
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE| SWT.APPLICATION_MODAL);
		
		this.userRoleDB = userRoleDB;
		this.userDB = userRoleDB.getParent();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText(Messages.get().DBAccessControlDialog_0);
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
		lblDbName.setText(Messages.get().DBName);
		
		textDBName = new Text(compositeHead, SWT.BORDER);
		textDBName.setEditable(false);
		textDBName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textDBName.setText(userDB.getDisplay_name());
		
		Label lblUser = new Label(compositeHead, SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText(Messages.get().User);
		
		comboUser = new Combo(compositeHead, SWT.READ_ONLY);
		comboUser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		comboUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		List<TadpoleUserDbRoleDAO> listUser = userDB.getListChildren();
		for (int i=0; i<listUser.size(); i++) {
			TadpoleUserDbRoleDAO tadpoleUserDbRoleDAO = listUser.get(i);
			comboUser.add(String.format("%s (%s)", tadpoleUserDbRoleDAO.getName(), tadpoleUserDbRoleDAO.getEmail())); //$NON-NLS-1$
			comboUser.setData(""+i, tadpoleUserDbRoleDAO); //$NON-NLS-1$
		}
		comboUser.select(0);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpAuthority = new Group(compositeBody, SWT.NONE);
		grpAuthority.setLayout(new GridLayout(2, false));
		grpAuthority.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpAuthority.setText(Messages.get().Authority);
		
		btnSelect = new Button(grpAuthority, SWT.CHECK);
		btnSelect.setEnabled(false);
		btnSelect.setSelection(true);
		btnSelect.setText(Messages.get().SELECT);
		
		Composite compositeSelectBtn = new Composite(grpAuthority, SWT.NONE);
		compositeSelectBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeSelectBtn.setLayout(new GridLayout(3, false));
		
		btnSelectAdd = new Button(compositeSelectBtn, SWT.NONE);
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
		btnSelectAdd.setText(Messages.get().Add);
		btnSelect.setEnabled(false);
//		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT) btnSelect.setEnabled(true);
		
		btnSelectDelete = new Button(compositeSelectBtn, SWT.NONE);
		btnSelectDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tvSelect.getSelection();
				if(!iss.isEmpty()) {
					if(!MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().DBAccessControlDialog_9)) return;
					
					AccessCtlObjectDAO dao = (AccessCtlObjectDAO)iss.getFirstElement();
					
					listTableColumn.remove(dao);
					tvSelect.refresh();
				}
			}
		});
		btnSelectDelete.setText(Messages.get().Delete);
		btnSelectDelete.setEnabled(false);
		new Label(compositeSelectBtn, SWT.NONE);
		
		new Label(grpAuthority, SWT.NONE);
		
		tvSelect = new TableViewer(grpAuthority, SWT.BORDER | SWT.FULL_SELECTION);
		tvSelect.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				btnSelectDelete.setEnabled(true);
			}
		});
		tvSelect.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection)event.getSelection();
				if(!iss.isEmpty()) {
					AccessCtlObjectDAO acObject = (AccessCtlObjectDAO)iss.getFirstElement();
					
					TableColumnFilterDialog tableColumnDialog = new TableColumnFilterDialog(getShell(), dbAccessDetail, acObject);
					if(Dialog.OK == tableColumnDialog.open()) {
//						listTableColumn.add(tableColumnDialog.getUpdateData());
						tvSelect.refresh();
					}
				}
				
			}
		});
		Table table = tvSelect.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tvColumnName = new TableViewerColumn(tvSelect, SWT.NONE);
		TableColumn tblclmnTable = tvColumnName.getColumn();
		tblclmnTable.setWidth(150);
		tblclmnTable.setText(Messages.get().Table);
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tvSelect, SWT.NONE);
		TableColumn tblclmnDoNotUse = tableViewerColumn_2.getColumn();
		tblclmnDoNotUse.setWidth(60);
		tblclmnDoNotUse.setText(Messages.get().DBAccessControlDialog_12);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tvSelect, SWT.NONE);
		TableColumn tblclmnColumn = tableViewerColumn_1.getColumn();
		tblclmnColumn.setWidth(500);
		tblclmnColumn.setText(Messages.get().Column);
		
		tvSelect.setContentProvider(new ArrayContentProvider());
		tvSelect.setLabelProvider(new SelectTableFilterLabelprovider());
		tvSelect.setInput(listTableColumn);
		
		
		btnInsert = new Button(grpAuthority, SWT.CHECK);
		btnInsert.setText("INSERT"); //$NON-NLS-1$
		new Label(grpAuthority, SWT.NONE);
		
		btnUpdate = new Button(grpAuthority, SWT.CHECK);
		btnUpdate.setText("UPDATE"); //$NON-NLS-1$
		new Label(grpAuthority, SWT.NONE);
		
		btnDelete = new Button(grpAuthority, SWT.CHECK);
		btnDelete.setText("DELETE"); //$NON-NLS-1$
		new Label(grpAuthority, SWT.NONE);
		
		btnDdl = new Button(grpAuthority, SWT.CHECK);
		btnDdl.setText("DDL"); //$NON-NLS-1$
		new Label(grpAuthority, SWT.NONE);
	
		firstInit();
		initData();

		return container;
	}
	
	/**
	 * Initialize UI
	 */
	private void firstInit() {
		comboUser.setText(String.format("%s (%s)", 	userRoleDB.getName(), userRoleDB.getEmail())); //$NON-NLS-1$
		
		// oracle, tajo, hive, cubrid 는 사용하지 못하도록.
		UserDBDAO userDB = userRoleDB.getParent();
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ||
				userDB.getDBDefine() == DBDefine.TAJO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT ||
				userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT
		) {
			btnSelectAdd.setEnabled(false);
		}
			
	}
		
	/**
	 * init data
	 */
	private void initData() {
		listTableColumn.clear();
		TadpoleUserDbRoleDAO tadpoleUserDbRoleDAO = (TadpoleUserDbRoleDAO)comboUser.getData(""+comboUser.getSelectionIndex()); //$NON-NLS-1$
	
		try {
			dbAccessDetail = TadpoleSystem_AccessControl.getDBAccessControl(tadpoleUserDbRoleDAO);
			
//			btnSelect.setSelection(!PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getSelect_lock()));
			btnInsert.setSelection(!PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getInsert_lock()));
			btnUpdate.setSelection(!PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getUpdate_lock()));
			btnDelete.setSelection(!PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getDelete_locl()));
			btnDdl.setSelection(!PublicTadpoleDefine.YES_NO.YES.name().equals(dbAccessDetail.getDdl_lock()));
			
			listTableColumn.addAll(dbAccessDetail.getMapSelectAccessCtl().values());
			tvSelect.refresh();
			
		} catch (Exception e) {
			logger.error("get access control", e); //$NON-NLS-1$
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if(!MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().DBAccessControlDialog_22)) return;
		
		DBAccessControlDAO dao = new DBAccessControlDAO();
		dao.setSeq(dbAccessDetail.getSeq());
		
//		dao.setSelect_lock(btnSelect.getSelection()?PublicTadpoleDefine.YES_NO.NO.name():PublicTadpoleDefine.YES_NO.YES.name());
		dao.setInsert_lock(btnInsert.getSelection()?PublicTadpoleDefine.YES_NO.NO.name():PublicTadpoleDefine.YES_NO.YES.name());
		dao.setUpdate_lock(btnUpdate.getSelection()?PublicTadpoleDefine.YES_NO.NO.name():PublicTadpoleDefine.YES_NO.YES.name());
		dao.setDelete_locl(btnDelete.getSelection()?PublicTadpoleDefine.YES_NO.NO.name():PublicTadpoleDefine.YES_NO.YES.name());
		dao.setDdl_lock(btnDdl.getSelection()?PublicTadpoleDefine.YES_NO.NO.name():PublicTadpoleDefine.YES_NO.YES.name());
		
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
			MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().Authority + e.getMessage());
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
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Save, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().CANCEL, false);
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
		case 1: return dao.getDontuse_object();
		default: return dao.getDetail_obj();
		}
		
//		return "### not set column ###";
	}
	
}
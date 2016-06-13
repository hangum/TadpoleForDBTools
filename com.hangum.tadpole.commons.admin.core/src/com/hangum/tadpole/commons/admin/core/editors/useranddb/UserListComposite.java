/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.editors.useranddb;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.admin.core.Activator;
import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.commons.admin.core.dialogs.UserLoginHistoryDialog;
import com.hangum.tadpole.commons.admin.core.dialogs.users.ModifyUserDialog;
import com.hangum.tadpole.commons.admin.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.commons.admin.core.editors.useranddb.provider.UserCompFilter;
import com.hangum.tadpole.commons.admin.core.editors.useranddb.provider.UserLabelProvider;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.USER_ROLE_TYPE;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.manager.core.editor.db.DBMgmtEditor;
import com.hangum.tadpole.manager.core.editor.db.DBMgntEditorInput;
import com.hangum.tadpole.manager.core.editor.executedsql.SQLAuditEditor;
import com.hangum.tadpole.manager.core.editor.executedsql.SQLAuditEditorInput;
import com.swtdesigner.ResourceManager;

/**
 * 어드민 사용하는 사용자리스트 화면
 * 
 * 화면에서 사용자의 추가, 수정은 어드민, 매니저 권한을 가지 사용자 만 가능하다.
 * 
 * @author hangum
 *
 */
public class UserListComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserListComposite.class);
	
	/** toolbar button */
	private ToolItem tltmModify;
	private ToolItem tltmLoginHistory;
	private ToolItem tltmDBList;
	private ToolItem tltmSQLAudit;
	
	/** search text */
	private Text textSearch;
	private TableViewer userListViewer;
	private List<UserDAO> listUserGroup = new ArrayList<UserDAO>();
	private UserCompFilter filter;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public UserListComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 4;
		gl_composite.horizontalSpacing = 4;
		gl_composite.marginHeight = 4;
		gl_composite.marginWidth = 4;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeHead = new Composite(composite, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 4;
		gl_compositeHead.horizontalSpacing = 4;
		gl_compositeHead.marginHeight = 4;
		gl_compositeHead.marginWidth = 4;
		compositeHead.setLayout(gl_compositeHead);
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI();
			}
		});
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		new ToolItem(toolBar, SWT.SEPARATOR);
	
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setImage(GlobalImageUtils.getAdd());
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addUser();
			}
		});
		tltmAdd.setToolTipText(Messages.get().Add);
	
		tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.setImage(GlobalImageUtils.getModify());
		tltmModify.setEnabled(false);
		tltmModify.setToolTipText(Messages.get().Modify);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modifyUser();
			}
		});
		tltmModify.setEnabled(false);
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tltmLoginHistory = new ToolItem(toolBar, SWT.NONE);
		tltmLoginHistory.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/userHistory.png")); //$NON-NLS-1$
		tltmLoginHistory.setToolTipText(Messages.get().UserListComposite_1);
		tltmLoginHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loginHistory();
			}
		});
		tltmLoginHistory.setEnabled(false);
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tltmDBList = new ToolItem(toolBar, SWT.NONE);
		tltmDBList.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/db.png")); //$NON-NLS-1$
		tltmDBList.setToolTipText(Messages.get().UserListComposite_3);
		tltmDBList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dbList();
			}
		});
		tltmDBList.setEnabled(false);
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tltmSQLAudit = new ToolItem(toolBar, SWT.NONE);
		tltmSQLAudit.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/sqlaudit.png")); //$NON-NLS-1$
		tltmSQLAudit.setToolTipText(Messages.get().UserListComposite_5);
		tltmSQLAudit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewQueryHistory();
			}
		});
		tltmSQLAudit.setEnabled(false);
		
		Label lblSearch = new Label(compositeHead, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText(Messages.get().Search);
		
		textSearch = new Text(compositeHead, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textSearch.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				filter.setSearchString(textSearch.getText());
				userListViewer.refresh();
			}
		});
		
		Composite compositeBody = new Composite(composite, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		userListViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		userListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				 tltmModify.setEnabled(true);
				 tltmLoginHistory.setEnabled(true);
				 tltmDBList.setEnabled(true);
				 tltmSQLAudit.setEnabled(true);
			}
		});
		Table table = userListViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumn();
		
		userListViewer.setContentProvider(new ArrayContentProvider());
		userListViewer.setLabelProvider(new UserLabelProvider());
		
		filter = new UserCompFilter();
		userListViewer.addFilter(filter);;
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
				
	}
	
	/**
	 * create column
	 */
	private void createColumn() {
		String[] colNames = {Messages.get().email, Messages.get().Name, 
							Messages.get().AdminUserListComposite_6, Messages.get().UserListComposite_6, 
							Messages.get().IsSharedDB, Messages.get().DefaultAddDBCount, Messages.get().DefaultUseDay,
							Messages.get().AdminUserListComposite_7, Messages.get().AdminUserListComposite_8, 
							Messages.get().AdminUserListComposite_9, 
							
							Messages.get().DeleteAccounts, Messages.get().AdminUserListComposite_11};
		int[] colSize = {150, 120, 70, 70, 60, 60, 120, 60, 60, 60, 60, 120};
		
		for (int i=0; i<colSize.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(userListViewer, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setWidth(colSize[i]);
			tableColumn.setText(colNames[i]);
		}
	}
	
	/**
	 * user 화면을 초기화 한다.
	 */
	private void initUI() {
		listUserGroup.clear();
		
		try {
			listUserGroup =  TadpoleSystem_UserQuery.getAllUser();
			
			userListViewer.setInput(listUserGroup);
			userListViewer.refresh();
		} catch(Exception e) {
			logger.error(Messages.get().AdminUserListComposite_12, e);
		}
	}
	
	/**
	 * add user
	 */
	private void addUser() {
		NewUserDialog dialog = new NewUserDialog(getShell(), true);
		dialog.open();
		
		initUI();
	}
	
	/**
	 * modify user
	 */
	private void modifyUser() {
		IStructuredSelection ss = (IStructuredSelection)userListViewer.getSelection();
		if(!ss.isEmpty()) {
			UserDAO userDao = (UserDAO)ss.getFirstElement();

			ModifyUserDialog dialog = new ModifyUserDialog(getShell(), userDao);
			if(Dialog.OK == dialog.open()) {
				initUI();
			}

		}
	}
	
	/**
	 * login history
	 */
	private void loginHistory() {
		IStructuredSelection ss = (IStructuredSelection)userListViewer.getSelection();
		if(!ss.isEmpty()) {
			UserDAO userDao = (UserDAO)ss.getFirstElement();

			UserLoginHistoryDialog dialog = new UserLoginHistoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDao);
			dialog.open();
		}
	}
	
	/**
	 * 
	 */
	private void dbList() {
		IStructuredSelection ss = (IStructuredSelection)userListViewer.getSelection();
		if(!ss.isEmpty()) {
			UserDAO userDAO = ((UserDAO)ss.getFirstElement());
			
			try {
				DBMgntEditorInput userMe = new DBMgntEditorInput(PublicTadpoleDefine.USER_ROLE_TYPE.SYSTEM_ADMIN, userDAO);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(userMe, DBMgmtEditor.ID);
			} catch (PartInitException e) {
				logger.error("Database Management editor", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	/**
	 * 사용자가 실행 했던 쿼리의 히스토리를 봅니다.
	 */
	private void viewQueryHistory() {
		IStructuredSelection ss = (IStructuredSelection)userListViewer.getSelection();
		if(ss != null) {
			 UserDAO userDAO = ((UserDAO)ss.getFirstElement());
			
			try {
				SQLAuditEditorInput esei = new SQLAuditEditorInput(userDAO, USER_ROLE_TYPE.SYSTEM_ADMIN);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(esei, SQLAuditEditor.ID, true);
			} catch(Exception e) {
				logger.error("SQL Audit open", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().UserListComposite_8, errStatus); //$NON-NLS-1$
			}
		}
	}

	@Override
	protected void checkSubclass() {
	}
}
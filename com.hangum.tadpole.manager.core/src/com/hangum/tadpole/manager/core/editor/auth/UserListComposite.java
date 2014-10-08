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
package com.hangum.tadpole.manager.core.editor.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.ImageUtils;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.manager.core.dialogs.users.FindUserDialog;
import com.hangum.tadpole.manager.core.editor.auth.provider.UserCompFilter;
import com.hangum.tadpole.manager.core.editor.auth.provider.UserContentProvider;
import com.hangum.tadpole.manager.core.editor.auth.provider.UserLabelProvider;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditor;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditorInput;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserRole;

/**
 * 메니저, DBA가 사용하는 사용자리스트 화면
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
	private ToolItem tltmQuery;
	
	/** search text */
	private Text textSearch;
	private TreeViewer userListViewer;
	private List<UserGroupAUserDAO> listUserGroup = new ArrayList<UserGroupAUserDAO>();
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
		gl_composite.verticalSpacing = 2;
		gl_composite.horizontalSpacing = 2;
		gl_composite.marginHeight = 2;
		gl_composite.marginWidth = 2;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeHead = new Composite(composite, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 0;
		gl_compositeHead.horizontalSpacing = 0;
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.marginWidth = 0;
		compositeHead.setLayout(gl_compositeHead);
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setImage(ImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI();
			}
		});
		tltmRefresh.setToolTipText(Messages.UserListComposite_0);
	
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setImage(ImageUtils.getAdd());
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addUser();
			}
		});
		tltmAdd.setToolTipText(Messages.UserListComposite_1);
		
		ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setImage(ImageUtils.getDelete());
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteUser();
			}
		});
		tltmDelete.setToolTipText(Messages.UserListComposite_2);
		
		tltmQuery = new ToolItem(toolBar, SWT.NONE);
		tltmQuery.setImage(ImageUtils.getQueryHistory());
		tltmQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewQueryHistory();
			}
		});
		tltmQuery.setEnabled(false);
		tltmQuery.setToolTipText(Messages.UserListComposite_3);
		
		Label lblSearch = new Label(compositeHead, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText(Messages.UserListComposite_4);
		
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
		
		userListViewer = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		userListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				viewQueryHistory();
			}
		});
		userListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
//				if(tltmModify != null) tltmModify.setEnabled(true);
				tltmQuery.setEnabled(true);
			}
		});
		Tree tree = userListViewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumn();
		
		userListViewer.setContentProvider(new UserContentProvider());
		userListViewer.setLabelProvider(new UserLabelProvider());
		
		filter = new UserCompFilter();
		userListViewer.addFilter(filter);;
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
				
	}
	
	/**
	 * 사용자가 실행 했던 쿼리의 히스토리를 봅니다.
	 */
	private void viewQueryHistory() {
		IStructuredSelection ss = (IStructuredSelection)userListViewer.getSelection();
		if(ss != null) {
			 UserDAO userDAO = ((UserGroupAUserDAO)ss.getFirstElement());
			
			try {
				ExecutedSQLEditorInput esei = new ExecutedSQLEditorInput(userDAO);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(esei, ExecutedSQLEditor.ID, false);
			} catch(Exception e) {
				logger.error("Query History open", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.UserListComposite_5, errStatus); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * create column
	 */
	private void createColumn() {
		String[] colNames = {"Group Name", "Email", "Name", "Role", "Approval", "Delete", "Create Time"}; //$NON-NLS-1$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		int[] colSize = {130, 200, 150, 80, 60, 60, 120};
		
		for (int i=0; i<colSize.length; i++) {
			TreeViewerColumn treeViewerColumn = new TreeViewerColumn(userListViewer, SWT.NONE);
			TreeColumn treeColumn = treeViewerColumn.getColumn();
			treeColumn.setWidth(colSize[i]);
			treeColumn.setText(colNames[i]);
		}
	}
	
	/**
	 * user 화면을 초기화 한다.
	 */
	private void initUI() {
		listUserGroup.clear();
		
		try {
			listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission(""+SessionManager.getGroupSeq()); //$NON-NLS-1$
			
			userListViewer.setInput(listUserGroup);
			userListViewer.refresh();
			userListViewer.expandToLevel(2);
		} catch(Exception e) {
			logger.error("Get user list", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * add user
	 */
	private void addUser() {
		FindUserDialog dialog = new FindUserDialog(getShell());
		dialog.open();
		
		initUI();
	}
	
	/**
	 * delete user
	 */
	private void deleteUser() {
		IStructuredSelection ss = (IStructuredSelection)userListViewer.getSelection();
		if(ss != null) {
			UserGroupAUserDAO userGroupAuser = (UserGroupAUserDAO)ss.getFirstElement();
			if(userGroupAuser.getEmail().equals(SessionManager.getEMAIL())) {
				MessageDialog.openWarning(getShell(), "Warning", Messages.UserListComposite_16); //$NON-NLS-1$
			} else {
				if(MessageDialog.openConfirm(getShell(), Messages.UserListComposite_17, Messages.UserListComposite_18)) {
					try {
						TadpoleSystem_UserRole.withdrawalUserRole(SessionManager.getGroupSeq(), userGroupAuser.getSeq());
						initUI();
					} catch(Exception e) {
						logger.error("withdrawal group user", e); //$NON-NLS-1$
					}
				}
			}
		}
	}

	@Override
	protected void checkSubclass() {
	}
}
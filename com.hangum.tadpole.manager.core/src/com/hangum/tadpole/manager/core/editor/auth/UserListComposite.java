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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.dialogs.users.ModifyUserDialog;
import com.hangum.tadpole.manager.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditor;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditorInput;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserQuery;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

/**
 * 어드민, 메니저, DBA가 사용하는 사용자리스트 화면
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
	
	/** Login user email */
	protected final String strUserEMail = SessionManager.getEMAIL();
	protected final String strRepresentRole = SessionManager.getRepresentRole();
	
	/** toolbar button */
	private ToolItem tltmModify;
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
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI();
			}
		});
		tltmRefresh.setText("Refresh");
		
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addUser();
			}
		});
		tltmAdd.setText("Add");
		
		tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.setEnabled(false);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modifyUser();
			}
		});
		tltmModify.setText("Modify");
		
		tltmQuery = new ToolItem(toolBar, SWT.NONE);
		tltmQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewQueryHistory();
			}
		});
		tltmQuery.setEnabled(false);
		tltmQuery.setText("Query History");
		
		Label lblSearch = new Label(compositeHead, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText("Search");
		
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
				tltmModify.setEnabled(true);
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
				ExceptionDetailsErrorDialog.openError(null, "Error", "Query History", errStatus); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * create column
	 */
	private void createColumn() {
		String[] colNames = {"Group Name", "Email", "Name", "Role", "Approval", "Delete", "Create Time"};
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
			if(PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals( SessionManager.getRepresentRole() )) {	// manager
				listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission(SessionManager.getGroupSeq());
			} else {	// admin 
				listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission();
			}
			
			userListViewer.setInput(listUserGroup);
			userListViewer.refresh();
			userListViewer.expandToLevel(2);
		} catch(Exception e) {
			logger.error("Get user list", e);
		}
	}
	
	/**
	 * add user
	 */
	private void addUser() {
		NewUserDialog dialog = new NewUserDialog(getShell());
		if(Dialog.OK == dialog.open()) {
			initUI();
		}
	}
	
	/**
	 * modify user
	 */
	private void modifyUser() {
		IStructuredSelection ss = (IStructuredSelection)userListViewer.getSelection();
		if(ss != null) {
			
			ModifyUserDialog dialog = new ModifyUserDialog(getShell(), (UserGroupAUserDAO)ss.getFirstElement());
			if(Dialog.OK == dialog.open()) {
				initUI();
			}
		}
	}

	@Override
	protected void checkSubclass() {
	}
}

/**
* content provider
* 
* @author hangum
*
*/
class UserContentProvider implements ITreeContentProvider {
	private static final Logger logger = Logger.getLogger(UserContentProvider.class);
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<UserGroupAUserDAO>)inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		if(element == null) {
			return null;
		}
		
		return ((UserGroupAUserDAO) element).parent;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList) {
			return ((ArrayList)element).size() > 0;			
		} else if(element instanceof UserGroupAUserDAO) {
			return ((UserGroupAUserDAO)element).child.size() > 0;
		}
		
		return false;
	}
	
}

/**
* 유저 정보 레이블 
* 
* @author hangum
*
*/
class UserLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(UserLabelProvider.class);
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserGroupAUserDAO user = (UserGroupAUserDAO)element;

		switch(columnIndex) {
		case 0: return user.getUser_group_name();
		case 1: return user.getEmail();
		case 2: return user.getName();
		case 3: return user.getRole_type();
		case 4: return user.getApproval_yn();
		case 5: return user.getDelYn();
		case 6: return user.getCreate_time();
		}
		
		return "*** not set column ***";
	}
	
}

/**
* User composite filter
* 
* @author hangum
*
*/
class UserCompFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String s) {
		this.searchString = ".*" + s.toLowerCase() + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) {
			return true;
		}
			
		UserGroupAUserDAO user = (UserGroupAUserDAO)element;
		if(user.getUser_group_name().toLowerCase().matches(searchString)) return true;
		if(user.getEmail().toLowerCase().matches(searchString)) return true;
		if(user.getName().toLowerCase().matches(searchString)) return true;
		if(user.getRole_type().toLowerCase().matches(searchString)) return true;
		if(user.getApproval_yn().toLowerCase().matches(searchString)) return true;
		
		return false;
	}
	
}
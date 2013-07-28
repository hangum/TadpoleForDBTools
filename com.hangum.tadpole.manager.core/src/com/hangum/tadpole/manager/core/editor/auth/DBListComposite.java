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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.manager.core.dialogs.users.ModifyUserDialog;
import com.hangum.tadpole.manager.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_UserQuery;

/**
 * 어드민, 메니저, DBA가 사용하는 DB List composite
 * 
 * @author hangum
 *
 */
public class DBListComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DBListComposite.class);
	private TreeViewer treeViewerAdmin;
	private Tree treeAdmin;
	
//	private AdminCompFilter filter;
	private Text textSearch;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DBListComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		
		Composite compositeHead = new Composite(this, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeViewerAdmin.setInput(initData());
			}
		});
		tltmRefresh.setText("Refresh");
		
		final ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewUserDialog dialog = new NewUserDialog(getShell());
				dialog.open();
			}
		});
		tltmAdd.setText("Add");
		
		ToolItem tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection ss = (IStructuredSelection)treeViewerAdmin.getSelection();
				if(ss != null) {
					ModifyUserDialog dialog = new ModifyUserDialog(getShell(), (UserGroupAUserDAO)ss.getFirstElement());
					if(Dialog.OK == dialog.open()) treeViewerAdmin.setInput(initData());
				}	// if ss				
			}
		});
		tltmModify.setText("Modify");
		
		Label lblSearch = new Label(compositeHead, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText("Search");
		
		textSearch = new Text(compositeHead, SWT.BORDER);
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textSearch.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
//				filter.setSearchString(textSearch.getText());
				treeViewerAdmin.refresh();
			}
		});
		
		Composite compositeBody = new Composite(this, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		treeViewerAdmin = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		treeAdmin = treeViewerAdmin.getTree();
		treeAdmin.setLinesVisible(true);
		treeAdmin.setHeaderVisible(true);
		treeAdmin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn colGroupName = new TreeViewerColumn(treeViewerAdmin, SWT.NONE);
		colGroupName.getColumn().setWidth(130);
		colGroupName.getColumn().setText("Group Name");
		
		TreeViewerColumn colUserType = new TreeViewerColumn(treeViewerAdmin, SWT.NONE);
		colUserType.getColumn().setWidth(100);
		colUserType.getColumn().setText("DB Type");
		
		TreeViewerColumn colEmail = new TreeViewerColumn(treeViewerAdmin, SWT.NONE);
		colEmail.getColumn().setWidth(200);
		colEmail.getColumn().setText("DB Name");
		
		TreeViewerColumn colName = new TreeViewerColumn(treeViewerAdmin, SWT.NONE);
		colName.getColumn().setWidth(150);
		colName.getColumn().setText("DB Info");
		
		TreeViewerColumn colApproval = new TreeViewerColumn(treeViewerAdmin, SWT.NONE);
		colApproval.getColumn().setWidth(60);
		colApproval.getColumn().setText("Approval");
		
		TreeViewerColumn colDelete = new TreeViewerColumn(treeViewerAdmin, SWT.NONE);
		colDelete.getColumn().setWidth(60);
		colDelete.getColumn().setText("Delete");
		
		TreeViewerColumn colCreateTime = new TreeViewerColumn(treeViewerAdmin, SWT.NONE);
		colCreateTime.getColumn().setWidth(120);
		colCreateTime.getColumn().setText("Create tiem");
		
//		treeViewerAdmin.setContentProvider(new AdminUserContentProvider());
//		treeViewerAdmin.setLabelProvider(new AdminUserLabelProvider());
//		treeViewerAdmin.setInput(initData());
//		treeViewerAdmin.expandToLevel(2);
//		
//		filter = new AdminCompFilter();
//		treeViewerAdmin.addFilter(filter);
	}
	
	/**
	 * 데이터를 초기화 합니다.
	 * 
	 * 1. 사용자 계정 중에 어드민 계정이 있는 지 검색 합니다.
	 * 	1.1) 어드민 계정이 있다면 계정을 표시합니다.
	 * 
	 * @return
	 */
	private UserGroupAUserDAO initData() {
		UserGroupAUserDAO rootUserGroup = new UserGroupAUserDAO();
		Map<Integer, UserGroupAUserDAO> groupHash = new HashMap<Integer, UserGroupAUserDAO>();
		
		try {
			// 데이터 수집 시작
			List<UserGroupAUserDAO> listUserGroup = new ArrayList<UserGroupAUserDAO>();
//			// 로그인 타입에 따른 유저 수정
//			if(PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals( SessionManager.getRepresentRole() )) {
//				listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission(SessionManager.getSeq());
//			// admin 
//			} else {
//				listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission();
//			}
//			// 데이터 수집 종료			
//			
			// Manager 추가
//			UserGroupAUserDAO managerGroupAUserDAO = null;
//			for (UserGroupAUserDAO userGroupAUserDAO : listUserGroup) {
//				if(DB_Define.USER_TYPE.MANAGER.toString().equals(userGroupAUserDAO.getUser_type())) {
//					rootUserGroup.child.add(userGroupAUserDAO);
//					groupHash.put(userGroupAUserDAO.getGroup_seq(), userGroupAUserDAO);
//					managerGroupAUserDAO = userGroupAUserDAO;
//				}								
//			}
//			
//			// 그룹에 속한 디비를 추가한다.
//			List<UserDBDAO> userDBs = TadpoleSystem_UserDBQuery.getSpecificUserDB(managerGroupAUserDAO.getSeq());
//			for (UserDBDAO userDBDAO : userDBs) {
//				rootUserGroup.child.add(userDBDAO);							
//			}
//			
////			// 메니저 권한만 유저 정보를 표시한다.
//				// use 추가
//				for (UserGroupAUserDAO userGroupAUserDAO : listUserGroup) {
//					if(DB_Define.USER_TYPE.USER.toString().equals(userGroupAUserDAO.getUser_type())) {
//						UserGroupAUserDAO groupUserDAO = groupHash.get(userGroupAUserDAO.getGroup_seq());
//	
//						userGroupAUserDAO.setParent(groupUserDAO);
//						groupUserDAO.child.add(userGroupAUserDAO);
//					}								
//				}

			return rootUserGroup;
		} catch (Exception e) {
			logger.error("user list", e);
		}
		
		return null;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}


///**
// * content provider
// * 
// * @author hangum
// *
// */
//class AdminUserContentProvider implements ITreeContentProvider {
//	private static final Logger logger = Logger.getLogger(AdminUserContentProvider.class);
//	
//	@Override
//	public void dispose() {
//	}
//
//	@Override
//	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//	}
//
//	@Override
//	public Object[] getElements(Object inputElement) {
//		
//		if(inputElement instanceof UserDBDAO) {
//			return ((List<UserDBDAO>) inputElement).toArray();
//		} else if(inputElement instanceof UserGroupAUserDAO) {
//			return ((UserGroupAUserDAO) inputElement).child.toArray();
//		}
//		
//		return null;
//	}
//
//	@Override
//	public Object[] getChildren(Object parentElement) {
//		return getElements(parentElement);
//	}
//
//	@Override
//	public Object getParent(Object element) {
//		if(element == null) {
//			return null;
//		}
//		
//		return ((UserGroupAUserDAO) element).parent;
//	}
//
//	@Override
//	public boolean hasChildren(Object element) {
//		if(element instanceof ArrayList) {
//			return ((ArrayList)element).size() > 0;			
//		} else if(element instanceof UserGroupAUserDAO) {
//			return ((UserGroupAUserDAO)element).child.size() > 0;
//		}
//		
//		return false;
//	}
//	
//}
//
///**
// * 유저 정보 레이블 
// * 
// * @author hangum
// *
// */
//class AdminUserLabelProvider extends LabelProvider implements ITableLabelProvider {
//	private static final Logger logger = Logger.getLogger(AdminUserLabelProvider.class);
//	
//	@Override
//	public Image getColumnImage(Object element, int columnIndex) {
//		if(element instanceof UserDBDAO) {
//			if(columnIndex == 0) return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/db.png"); //$NON-NLS-1$
//		} else {
//			if(columnIndex == 0) return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/user.png"); //$NON-NLS-1$
//		}
//		return null;
//	}
//
//	@Override
//	public String getColumnText(Object element, int columnIndex) {
//		
//		if(element instanceof UserDBDAO) {
//			
//			UserDBDAO userDB = (UserDBDAO)element;
//			switch(columnIndex) {
//			case 0: return "";
//			case 1: return userDB.getDbms_types();
//			case 2: return userDB.getDisplay_name();
//			case 3:
//				if(userDB.getHost() == null) return "";
//				return userDB.getHost() + ":"  + userDB.getPort();
//			case 4: return "";
//			case 5: return "";
//			case 6: return "";
//			}
//			
//		} else if(element instanceof UserGroupAUserDAO) {
//			
//			UserGroupAUserDAO user = (UserGroupAUserDAO)element;
//			
//			switch(columnIndex) {
//			case 0:
////				if(DB_Define.USER_TYPE.MANAGER.toString().equals(user.getUser_type())) {
////					return user.getUser_group_name();	
////				} else {
////					return "";
////				}
//				
////			case 1: return user.getUser_type();
//			case 2: return user.getEmail();
//			case 3: return user.getName();
////			case 4: return user.getApproval_yn();
//			case 5: return user.getDelYn();
//			case 6: return user.getCreate_time();
//			}
//		}
//		
//		return "*** not set column ***";
//	}
//	
//}
//
///**
// * admin composite filter
// * 
// * @author hangum
// *
// */
//class AdminCompFilter extends ViewerFilter {
//	String searchString;
//	
//	public void setSearchString(String s) {
//		this.searchString = ".*" + s + ".*";
//	}
//
//	@Override
//	public boolean select(Viewer viewer, Object parentElement, Object element) {
//		
//		if(searchString == null || searchString.length() == 0) {
//			return true;
//		}
//		
//		if(element instanceof UserDBDAO) {
//			UserDBDAO userDB = (UserDBDAO)element;
//			if(userDB.getDbms_types().matches(searchString)) return true;
//			if(userDB.getDisplay_name().matches(searchString)) return true;
//			if(userDB.getHost() != null) if(userDB.getHost().matches(searchString)) return true;
//			if(userDB.getPort() != null)  if(userDB.getPort().matches(searchString)) return true;
//			
//		} else if(element instanceof UserGroupAUserDAO) {
//			
////			UserGroupAUserDAO user = (UserGroupAUserDAO)element;
////			if(user.getUser_group_name().matches(searchString)) return true;
////			if(user.getUser_type().matches(searchString)) return true;
////			
////			if(user.getUser_type().matches(searchString)) return true;
////			if(user.getEmail().matches(searchString)) return true;
////			if(user.getName().matches(searchString)) return true;
////			if(user.getApproval_yn().matches(searchString)) return true;
//		}
//		
//		return false;
//	}
//	
//}

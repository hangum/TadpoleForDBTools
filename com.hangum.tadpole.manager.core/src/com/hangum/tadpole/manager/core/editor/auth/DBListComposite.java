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
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.rwt.RWT;
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
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.ImageUtils;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserRole;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.dialogs.users.FindUserAndDBRoleDialog;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditor;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditorInput;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerLabelProvider;

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

	private TreeViewer tvDBList;
	private List<ManagerListDTO> listUserDBs = new ArrayList<ManagerListDTO>();
	
	private AdminCompFilter filter;
	private Text textSearch;

	private ToolItem tltmAdd; 
	private ToolItem tltmQueryHistory;
	private ToolItem tltmSQLEditor;
//	private ToolItem tltmModify;
	private ToolItem tltmDBDelete;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DBListComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
		
		Composite compositeHead = new Composite(this, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(3, false);
		gl_compositeHead.verticalSpacing = 0;
		gl_compositeHead.horizontalSpacing = 0;
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.marginWidth = 0;
		compositeHead.setLayout(gl_compositeHead);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setImage(ImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		tltmRefresh.setToolTipText("Refresh");

		tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setImage(ImageUtils.getAdd());
		tltmAdd.setEnabled(false);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection ss = (IStructuredSelection)tvDBList.getSelection();
				if(ss.isEmpty()) return;
				
				FindUserAndDBRoleDialog dialog = new FindUserAndDBRoleDialog(getShell(), (UserDBDAO)ss.getFirstElement());
				dialog.open();
			}
		});
		tltmAdd.setToolTipText("Add User");
		
		tltmDBDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDBDelete.setImage(ImageUtils.getDelete());
		tltmDBDelete.setEnabled(false);
		tltmDBDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDB();
			}
		});
		tltmDBDelete.setToolTipText("Delete");

//		tltmModify = new ToolItem(toolBar, SWT.NONE);
//		tltmModify.setImage(ImageUtils.getModify());
//		tltmModify.setEnabled(false);
//		tltmModify.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				modifyDB();
//			}
//		});
//		tltmModify.setToolTipText("Modify");
		
		tltmQueryHistory = new ToolItem(toolBar, SWT.NONE);
		tltmQueryHistory.setImage(ImageUtils.getQueryHistory()); //$NON-NLS-1$
		tltmQueryHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewQueryHistory();
			}
		});
		tltmQueryHistory.setEnabled(false);
		tltmQueryHistory.setToolTipText("Query History");
		
		tltmSQLEditor = new ToolItem(toolBar, SWT.NONE);
		tltmSQLEditor.setImage(ImageUtils.getSQLEditor()); //$NON-NLS-1$
		tltmSQLEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sqlEditor();
			}
		});
		tltmSQLEditor.setEnabled(false);
		tltmSQLEditor.setToolTipText("SQL Editor");
		
		new Label(compositeHead, SWT.NONE);
		
		Label lblSearch = new Label(compositeHead, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText("Search");
		
		textSearch = new Text(compositeHead, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textSearch.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				filter.setSearchString(textSearch.getText());
				tvDBList.refresh();
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
		
		tvDBList = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tvDBList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				
				IStructuredSelection ss = (IStructuredSelection)tvDBList.getSelection();
				if(ss.isEmpty()) return;

				Object objSelect = ss.getFirstElement();
				if(objSelect instanceof UserDBDAO) {
					UserDBDAO userDB = (UserDBDAO)objSelect;
					try {
						List<TadpoleUserDbRoleDAO> listUser = TadpoleSystem_UserRole.getUserList(userDB);
						if(userDB.getListChildren().isEmpty()) {
							for (TadpoleUserDbRoleDAO tadpoleUserDbRoleDAO : listUser) {
								tadpoleUserDbRoleDAO.setParent(userDB);
							}
							
							userDB.setListChildren(listUser);
							tvDBList.refresh(userDB, true);
							tvDBList.expandToLevel(2);
						}
					} catch (Exception e) {
						logger.error("get user list", e);
					}
					
					tltmAdd.setEnabled(true);
//					tltmModify.setEnabled(false);
					tltmDBDelete.setEnabled(false);
					
					tltmQueryHistory.setEnabled(true);
					tltmSQLEditor.setEnabled(true);
				} else if(objSelect instanceof TadpoleUserDbRoleDAO){
					TadpoleUserDbRoleDAO userDBRole = (TadpoleUserDbRoleDAO)objSelect;
					if(userDBRole.getParent().getUser_seq() != userDBRole.getUser_seq()) {
						tltmAdd.setEnabled(false);
//						tltmModify.setEnabled(true);
						tltmDBDelete.setEnabled(true);
					} else {
						tltmAdd.setEnabled(false);
//						tltmModify.setEnabled(false);
						tltmDBDelete.setEnabled(false);
					}
					
					tltmQueryHistory.setEnabled(true);
					tltmSQLEditor.setEnabled(true);
				} else if(objSelect instanceof ManagerListDTO) {
					
					tltmAdd.setEnabled(false);
//					tltmModify.setEnabled(false);
					tltmDBDelete.setEnabled(false);
					tltmQueryHistory.setEnabled(false);
					tltmSQLEditor.setEnabled(false);
				}
				
			}
		});
		tvDBList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				viewQueryHistory();
			}
		});
		Tree treeAdmin = tvDBList.getTree();
		treeAdmin.setLinesVisible(true);
		treeAdmin.setHeaderVisible(true);
		treeAdmin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeAdmin.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		
		TreeViewerColumn colGroupName = new TreeViewerColumn(tvDBList, SWT.NONE);
		colGroupName.getColumn().setWidth(250);
		colGroupName.getColumn().setText("Name(email)");
				
		TreeViewerColumn colRoleName = new TreeViewerColumn(tvDBList, SWT.NONE);
		colRoleName.getColumn().setWidth(80);
		colRoleName.getColumn().setText("Role");
		
//		TreeViewerColumn colEmail = new TreeViewerColumn(tvDBList, SWT.NONE);
//		colEmail.getColumn().setWidth(150);
//		colEmail.getColumn().setText("Name");
		
		TreeViewerColumn colName = new TreeViewerColumn(tvDBList, SWT.NONE);
		colName.getColumn().setWidth(180);
		colName.getColumn().setText("DB Info");
		
		TreeViewerColumn colApproval = new TreeViewerColumn(tvDBList, SWT.NONE);
		colApproval.getColumn().setWidth(70);
		colApproval.getColumn().setText("User");
		
		TreeViewerColumn colVisible = new TreeViewerColumn(tvDBList, SWT.NONE);
		colVisible.getColumn().setWidth(40);
		colVisible.getColumn().setText("Visible");
		
		TreeViewerColumn colCreateTime = new TreeViewerColumn(tvDBList, SWT.NONE);
		colCreateTime.getColumn().setWidth(150);
		colCreateTime.getColumn().setText("Create tiem");
		
		tvDBList.setContentProvider(new DBListContentProvider());
		tvDBList.setLabelProvider(new DBListLabelProvider());
		tvDBList.setInput(listUserDBs);
		initData();
		tvDBList.expandToLevel(2);
		
		filter = new AdminCompFilter();
		tvDBList.addFilter(filter);
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
				
	}
	
//	/**
//	 * toolbar control
//	 * 
//	 * @param isTrue
//	 */
//	private void toolbarCotrol(boolean isTrue) {
//		tltmAdd.setEnabled(isTrue);
//		tltmModify.setEnabled(!isTrue);
//		tltmDBDelete.setEnabled(!isTrue);	
//		
//		tltmQueryHistory.setEnabled(true);
//		tltmSQLEditor.setEnabled(true);
//	}
	
//	private void refreshConnections() {
//		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
//		Display.getCurrent().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				managerView.init();
//			}
//		});	// end display
//	}

//	/**
//	 * modify db
//	 */
//	private void modifyDB() {
//		IStructuredSelection ss = (IStructuredSelection)treeViewerDBList.getSelection();
//		if(ss.isEmpty()) return;
//		
//		UserDBDAO userDB = (UserDBDAO)ss.getFirstElement();
//		
////			if(userDB.getGroup_seq() == SessionManager.getGroupSeq()) {
////				final ModifyDBDialog dialog = new ModifyDBDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
////				final int ret = dialog.open();
////				
////				if(ret == Dialog.OK) {
////					treeViewerDBList.setInput(initData());
////					refreshConnections();
////				}
////			}
//	}
	
	/**
	 * delete db (delete marking)
	 */
	private void deleteDB(){
		IStructuredSelection ss = (IStructuredSelection)tvDBList.getSelection();
		if(ss.isEmpty()) return;

		TadpoleUserDbRoleDAO userDBRole = (TadpoleUserDbRoleDAO)ss.getFirstElement();
		if(userDBRole.getParent().getUser_seq() == userDBRole.getUser_seq()) return;
		
		if(!MessageDialog.openConfirm(null, "Confirm", "Do you want to delete the selected database?") ) return; //$NON-NLS-1$

		try {
			TadpoleSystem_UserDBQuery.removeUserRoleDB(userDBRole.getSeq());
			initData();
		} catch (Exception e) { 
			logger.error("disconnection exception", e);				
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Disconnection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * 사용자가 실행 했던 쿼리의 히스토리를 봅니다.
	 */
	private void viewQueryHistory() {
		IStructuredSelection ss = (IStructuredSelection)tvDBList.getSelection();
		if(ss.isEmpty()) return;
		
		Object objSelect = ss.getFirstElement();
		if(objSelect instanceof UserDBDAO) {
			try {
				ExecutedSQLEditorInput esei = new ExecutedSQLEditorInput((UserDBDAO)ss.getFirstElement());
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(esei, ExecutedSQLEditor.ID, false);
			} catch(Exception e) {
				logger.error("Query History open", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", "Query History", errStatus); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * SQL editor
	 */
	private void sqlEditor() {
		IStructuredSelection ss = (IStructuredSelection)tvDBList.getSelection();
		if(ss.isEmpty()) return;
		
		try {
			MainEditorInput esei = new MainEditorInput((UserDBDAO)ss.getFirstElement());
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(esei, MainEditor.ID, false);
		} catch(Exception e) {
			logger.error("SQL Editor open", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "SQL Editor", errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * 데이터를 초기화 합니다.
	 * 
	 * 1. 사용자 계정 중에 어드민 계정이 있는 지 검색 합니다.
	 * 	1.1) 어드민 계정이 있다면 계정을 표시합니다.
	 * 
	 * @return
	 */
	private void initData() {
		listUserDBs.clear();
		try {
			List<UserDBDAO> userDBS = TadpoleSystem_UserDBQuery.getCreateUserDB();
			for (UserDBDAO userDB : userDBS) {
				boolean isAdd = false;
				
				for(ManagerListDTO dto: listUserDBs) {
					if(dto.getName().equals(userDB.getGroup_name())) {
						dto.addLogin(userDB);
						
						isAdd = true;
						break;
					}	// end if(dto.getname()....		
				}	// end for
				
				// 신규 그룹이면...
				if(!isAdd) {
					ManagerListDTO managerDto = new ManagerListDTO(userDB.getGroup_name());
					managerDto.addLogin(userDB);
					listUserDBs.add(managerDto);
				}
			}

			tvDBList.refresh();
			tvDBList.expandToLevel(2);
			
		} catch (Exception e) {
			logger.error("db list", e);
		}
	}
	
	@Override
	protected void checkSubclass() {
	}
}

/**
 * 유저 정보 레이블 
 * 
 * @author hangum
 *
 */
class DBListLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(DBListLabelProvider.class);
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if(element instanceof UserDBDAO) {
			if(columnIndex == 0) return ManagerLabelProvider.getDBImage((UserDBDAO)element);
		} else if(element instanceof ManagerListDTO) {
			if(columnIndex == 0) return ManagerLabelProvider.getGroupImage();
		}
		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if(element instanceof UserDBDAO) {
			UserDBDAO userDB = (UserDBDAO)element;
//			if(PermissionChecker.isDBAdminRole(userDB)) {
				switch(columnIndex) {
					case 0: return ManagerLabelProvider.getDBText(userDB);
					case 1: return "";//userDB.getRole_id();
					case 2:
						// sqlite
						if("".equals(userDB.getHost())) return userDB.getUrl();
						return userDB.getHost() + ":"  + userDB.getPort();
					case 3: return userDB.getUsers();
					case 4: return userDB.getIs_visible();
					case 5: return ""+userDB.getCreate_time();
				}
//			} else {
//				switch(columnIndex) {
//					case 0: return userDB.getGroup_name();
//					case 1: return "";//userDB.getRole_id();
//					case 2: return ManagerLabelProvider.getDBText(userDB);
//					case 3:
//						// sqlite
//						if("".equals(userDB.getHost())) return userDB.getUrl(userDB.getRole_id());
//						return userDB.getHost(userDB.getRole_id()) + ":"  + userDB.getPort(userDB.getRole_id());
//					case 4: return userDB.getUsers(userDB.getRole_id());
//					case 5: return userDB.getIs_visible();
//					case 6: return ""+userDB.getCreate_time();
//				}
//			}

		} else if(element instanceof ManagerListDTO) {
			ManagerListDTO mgDto = (ManagerListDTO)element;
			if(columnIndex == 0) return mgDto.getName();
			else return "";
		} else if(element instanceof TadpoleUserDbRoleDAO) {
			TadpoleUserDbRoleDAO roleDao = (TadpoleUserDbRoleDAO)element;
			switch(columnIndex) {
				case 0: return String.format("%s ( %s )", roleDao.getName(), roleDao.getEmail());
				case 1: return roleDao.getRole_id();
				case 5: return ""+roleDao.getCreate_time();
			}
		}
		
		return "";
	}
	
}

/**
 * admin composite filter
 * 
 * @author hangum
 *
 */
class AdminCompFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String s) {
		this.searchString = ".*" + s.toLowerCase() + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		if(searchString == null || searchString.length() == 0) {
			return true;
		}
		
		UserDBDAO userDB = (UserDBDAO)element;
		if(userDB.getGroup_name().toLowerCase().matches(searchString)) return true;
		if(userDB.getDbms_type().toLowerCase().matches(searchString)) return true;
		if(userDB.getDisplay_name().toLowerCase().matches(searchString)) return true;
		if(userDB.getUrl().toLowerCase().matches(searchString)) return true;
		if(userDB.getHost() != null) if(userDB.getHost().toLowerCase().matches(searchString)) return true;
		if(userDB.getPort() != null)  if(userDB.getPort().toLowerCase().matches(searchString)) return true;
		if((""+userDB.getCreate_time()).toLowerCase().matches(searchString)) return true;
		
		return false;
	}
	
}

/**
 * content provider
 * 
 * @author hangum
 *
 */
class DBListContentProvider implements ITreeContentProvider {
	private static final Logger logger = Logger.getLogger(DBListContentProvider.class);
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof ManagerListDTO) {
			ManagerListDTO dto = (ManagerListDTO)inputElement;
			return dto.getManagerList().toArray();
		} else if(inputElement instanceof UserDBDAO) {
			return ((List<UserDBDAO>) inputElement).toArray();
		} else {
			return ((List<TadpoleUserDbRoleDAO>) inputElement).toArray();
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof ManagerListDTO) {
			ManagerListDTO dto = (ManagerListDTO)parentElement;
			return dto.getManagerList().toArray();
		} else if(parentElement instanceof UserDBDAO) {
			UserDBDAO userDB = (UserDBDAO)parentElement;
			return userDB.getListChildren().toArray();
		}
		return getElements(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		if(element == null) {
			return null;
		}
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof ManagerListDTO) {
			ManagerListDTO dto = (ManagerListDTO)element;
			return dto.getManagerList().size() > 0;
		} else if(element instanceof ArrayList) {
			return ((ArrayList)element).size() > 0;			
		} else if(element instanceof UserDBDAO) {
			UserDBDAO userDB = (UserDBDAO)element;
			return userDB.getListChildren().size() > 0;
		}
		
		return false;
	}
	
}
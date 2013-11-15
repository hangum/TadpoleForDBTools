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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.ImageUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.commons.util.fileupload.FileUploadDialog;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditor;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditorInput;
import com.hangum.tadpole.manager.core.export.SystemDBDataManager;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.DBLoginDialog;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.ModifyDBDialog;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserDBQuery;

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

	private TreeViewer treeViewerDBList;
	private List<UserDBDAO> listUserDBs = new ArrayList<UserDBDAO>();

	private AdminCompFilter filter;
	private Text textSearch;

	private ToolItem tltmQueryHistory;
	private ToolItem tltmModify;
	private ToolItem tltmDBDelete;

	/** download dumy compoiste */
	private Composite compositeDumy;
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;

	/**
	 * Create the composite.
	 * 
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
				treeViewerDBList.setInput(initData());
			}
		});
		tltmRefresh.setToolTipText("Refresh");

		// access control
		if (PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals(SessionManager.getRepresentRole())) {
			final ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
			tltmAdd.setImage(ImageUtils.getAdd());
			tltmAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final DBLoginDialog dialog = new DBLoginDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "");
					final int ret = dialog.open();

					if (ret == Dialog.OK) {
						treeViewerDBList.setInput(initData());
						refreshConnections();
					}
				}
			});
			tltmAdd.setToolTipText("Add");

			tltmDBDelete = new ToolItem(toolBar, SWT.NONE);
			tltmDBDelete.setImage(ImageUtils.getDelete());
			tltmDBDelete.setEnabled(false);
			tltmDBDelete.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					deleteDB();
				}
			});
			tltmDBDelete.setToolTipText("DB Delete");

			tltmModify = new ToolItem(toolBar, SWT.NONE);
			tltmModify.setImage(ImageUtils.getModify());
			tltmModify.setEnabled(false);
			tltmModify.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					modifyDB();
				}
			});
			tltmModify.setToolTipText("Modify");

			ToolItem tltmDbExport = new ToolItem(toolBar, SWT.NONE);
			tltmDbExport.setImage(ImageUtils.getExport());
			tltmDbExport.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					exportDB();
				}
			});
			tltmDbExport.setToolTipText("DB Export");

			ToolItem tltmDbImport = new ToolItem(toolBar, SWT.NONE);
			tltmDbImport.setImage(ImageUtils.getImport()); //$NON-NLS-1$
			tltmDbImport.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					importDB();
				}
			});
			tltmDbImport.setToolTipText("DB Import");
		}

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
		new Label(compositeHead, SWT.NONE);

		Label lblSearch = new Label(compositeHead, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText("Search");

		textSearch = new Text(compositeHead, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		textSearch.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				filter.setSearchString(textSearch.getText());
				treeViewerDBList.refresh();
			}
		});

		// download dumy
		compositeDumy = new Composite(compositeHead, SWT.NONE);
		compositeDumy.setLayout(new GridLayout(1, false));

		Composite compositeBody = new Composite(this, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		treeViewerDBList = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		treeViewerDBList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (tltmModify != null)
					tltmModify.setEnabled(true);
				if (tltmDBDelete != null)
					tltmDBDelete.setEnabled(true);
				tltmQueryHistory.setEnabled(true);
			}
		});
		treeViewerDBList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				viewQueryHistory();
			}
		});
		Tree treeAdmin = treeViewerDBList.getTree();
		treeAdmin.setLinesVisible(true);
		treeAdmin.setHeaderVisible(true);
		treeAdmin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TreeViewerColumn colGroupName = new TreeViewerColumn(treeViewerDBList, SWT.NONE);
		colGroupName.getColumn().setWidth(130);
		colGroupName.getColumn().setText("Group Name");

		TreeViewerColumn colUserType = new TreeViewerColumn(treeViewerDBList, SWT.NONE);
		colUserType.getColumn().setWidth(100);
		colUserType.getColumn().setText("DB Type");

		TreeViewerColumn colEmail = new TreeViewerColumn(treeViewerDBList, SWT.NONE);
		colEmail.getColumn().setWidth(200);
		colEmail.getColumn().setText("DB Name");

		TreeViewerColumn colName = new TreeViewerColumn(treeViewerDBList, SWT.NONE);
		colName.getColumn().setWidth(150);
		colName.getColumn().setText("DB Info");

		TreeViewerColumn colApproval = new TreeViewerColumn(treeViewerDBList, SWT.NONE);
		colApproval.getColumn().setWidth(60);
		colApproval.getColumn().setText("User");

		TreeViewerColumn colCreateTime = new TreeViewerColumn(treeViewerDBList, SWT.NONE);
		colCreateTime.getColumn().setWidth(120);
		colCreateTime.getColumn().setText("Create tiem");

		treeViewerDBList.setContentProvider(new AdminUserContentProvider());
		treeViewerDBList.setLabelProvider(new AdminUserLabelProvider());
		treeViewerDBList.setInput(initData());
		treeViewerDBList.expandToLevel(2);

		filter = new AdminCompFilter();
		treeViewerDBList.addFilter(filter);

		registerServiceHandler();
	}

	private void refreshConnections() {
		final ManagerViewer managerView = (ManagerViewer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				managerView.init();
			}
		}); // end display
	}

	/**
	 * modify db
	 */
	private void modifyDB() {
		IStructuredSelection ss = (IStructuredSelection) treeViewerDBList.getSelection();
		if (ss != null) {
			final ModifyDBDialog dialog = new ModifyDBDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), (UserDBDAO) ss.getFirstElement());
			final int ret = dialog.open();

			if (ret == Dialog.OK) {
				treeViewerDBList.setInput(initData());
				refreshConnections();
			}
		}
	}

	/**
	 * delete db (delete marking)
	 */
	private void deleteDB() {
		IStructuredSelection ss = (IStructuredSelection) treeViewerDBList.getSelection();
		if (ss != null) {
			if (!MessageDialog.openConfirm(null, "Confirm", "Do you want to delete the selected database?"))return; //$NON-NLS-1$

			UserDBDAO userDB = (UserDBDAO) ss.getFirstElement();

			try {
				if (userDB != null) {
					TadpoleSystem_UserDBQuery.removeUserDB(userDB.getSeq());
					TadpoleSQLManager.removeInstance(userDB);
					treeViewerDBList.setInput(initData());
					refreshConnections();
				}
			} catch (Exception e) {
				logger.error("disconnection exception", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"Error", "Disconnection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}

	/**
	 * import db list
	 */
	private void importDB() {
		FileUploadDialog fud = new FileUploadDialog(this.getShell());
		if (Dialog.OK == fud.open()) {

			try {
				String abstFile = fud.getListFiles().get(0);
				String fileContetn = FileUtils.readFileToString(new File(abstFile));

				SystemDBDataManager.importUserDB(fileContetn);

				treeViewerDBList.setInput(initData());
				refreshConnections();
			} catch (Exception e) {
				logger.error("Import DB exception", e);

				MessageDialog.openError(null, "Error", e.getMessage());
			}
		}
	}

	/**
	 * export db list
	 */
	private void exportDB() {
		final String strDBEncrypt = SystemDBDataManager.exportUserDB(listUserDBs);

		if (!"".equals(strDBEncrypt)) {
			if (MessageDialog.openConfirm(null, "Confirm", "사용자 디비를 export하시겠습니까?")) { //$NON-NLS-1$

				try {
					byte[] arrayData = strDBEncrypt.getBytes();

					downloadServiceHandler.setContentType("txt");
					downloadServiceHandler.setName("Tadpole_DBList_Export.txt"); //$NON-NLS-1$
					downloadServiceHandler.setByteContent(arrayData);
					DownloadUtils.provideDownload(compositeDumy, downloadServiceHandler.getId());
				} catch (Exception e) {
					logger.error("DB export exception", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(null, "Error", "DB export Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	/**
	 * 사용자가 실행 했던 쿼리의 히스토리를 봅니다.
	 */
	private void viewQueryHistory() {
		IStructuredSelection ss = (IStructuredSelection) treeViewerDBList.getSelection();
		if (ss != null) {
			UserDBDAO userDBDAO = ((UserDBDAO) ss.getFirstElement());

			try {
				ExecutedSQLEditorInput esei = new ExecutedSQLEditorInput(userDBDAO);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(esei, ExecutedSQLEditor.ID, false);
			} catch (Exception e) {
				logger.error("Query History open", e); //$NON-NLS-1$

				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", "Query History", errStatus); //$NON-NLS-1$
			}
		}
	}

	/**
	 * 데이터를 초기화 합니다.
	 * 
	 * 1. 사용자 계정 중에 어드민 계정이 있는 지 검색 합니다. 1.1) 어드민 계정이 있다면 계정을 표시합니다.
	 * 
	 * @return
	 */
	private List<UserDBDAO> initData() {
		listUserDBs.clear();
		try {
			if (PublicTadpoleDefine.USER_TYPE.MANAGER.toString().equals(SessionManager.getRepresentRole())
					|| PublicTadpoleDefine.USER_TYPE.DBA.toString().equals(SessionManager.getRepresentRole())) { // manager,
																													// dba
				listUserDBs = TadpoleSystem_UserDBQuery.getAllUserDB(SessionManager.getGroupSeqs());
			} else { // admin
				listUserDBs = TadpoleSystem_UserDBQuery.getAllUserDB();
			}
		} catch (Exception e) {
			logger.error("user list", e);
		}

		return listUserDBs;
	}

	@Override
	public void dispose() {
		unregisterServiceHandler();

		super.dispose();
	}

	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

/**
 * content provider
 * 
 * @author hangum
 * 
 */
class AdminUserContentProvider implements ITreeContentProvider {
	private static final Logger logger = Logger.getLogger(AdminUserContentProvider.class);

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<UserDBDAO>) inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		if (element == null) {
			return null;
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ArrayList) {
			return ((ArrayList) element).size() > 0;
		} else if (element instanceof UserGroupAUserDAO) {
			return ((UserGroupAUserDAO) element).child.size() > 0;
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
class AdminUserLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(AdminUserLabelProvider.class);

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserDBDAO userDB = (UserDBDAO) element;

		switch (columnIndex) {
		case 0:
			return userDB.getGroup_name();
		case 1:
			return userDB.getDbms_types();
		case 2:
			return userDB.getDisplay_name();
		case 3:
			// sqlite
			if ("".equals(userDB.getHost()))
				return userDB.getUrl();
			return userDB.getHost() + ":" + userDB.getPort();
		case 4:
			return userDB.getUsers();
		case 5:
			return "" + userDB.getCreate_time();
		}

		return "*** not set column ***";
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

		if (searchString == null || searchString.length() == 0) {
			return true;
		}

		UserDBDAO userDB = (UserDBDAO) element;
		if (userDB.getGroup_name().toLowerCase().matches(searchString))
			return true;
		if (userDB.getDbms_types().toLowerCase().matches(searchString))
			return true;
		if (userDB.getDisplay_name().toLowerCase().matches(searchString))
			return true;
		if (userDB.getUrl().toLowerCase().matches(searchString))
			return true;
		if (userDB.getHost() != null)
			if (userDB.getHost().toLowerCase().matches(searchString))
				return true;
		if (userDB.getPort() != null)
			if (userDB.getPort().toLowerCase().matches(searchString))
				return true;
		if (("" + userDB.getCreate_time()).toLowerCase().matches(searchString))
			return true;

		return false;
	}

}

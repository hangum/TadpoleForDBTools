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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableColumnLabelprovider;
import com.hangum.tadpole.system.permission.PermissionChecker;
import com.hangum.tadpole.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB viewer composite
 * 
 * @author hangum
 *
 */
public class TadpoleViewerComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleViewerComposite.class);
	
	private TableViewer viewListViewer;
	private ObjectComparator viewComparator;
	private List showViews;
	private TableViewer viewColumnViewer;
	private List showViewColumns;
	private RDBViewFilter viewFilter;
	
	private ObjectCreatAction creatAction_View;
	private ObjectDeleteAction deleteAction_View;
	private ObjectRefreshAction refreshAction_View;
//	private ObjectModifyAction modifyAction_View;

	/**
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleViewerComposite(IWorkbenchPartSite site, final CTabFolder tabFolderObject, final UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		CTabItem tbtmViews = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmViews.setText("Views"); //$NON-NLS-1$

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmViews.setControl(compositeTables);
		GridLayout gl_compositeTables = new GridLayout(1, false);
		gl_compositeTables.verticalSpacing = 2;
		gl_compositeTables.horizontalSpacing = 2;
		gl_compositeTables.marginHeight = 2;
		gl_compositeTables.marginWidth = 2;
		compositeTables.setLayout(gl_compositeTables);

		SashForm sashForm = new SashForm(compositeTables, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		viewListViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		viewListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (is != null) {
						if (is.getFirstElement() != null) {
							String strTBName = is.getFirstElement().toString();
							Map<String, String> param = new HashMap<String, String>();
							param.put("db", userDB.getDb());
							param.put("table", strTBName);

							SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
							showViewColumns = sqlClient.queryForList("tableColumnList", param); //$NON-NLS-1$
						} else
							showViewColumns = null;

						viewColumnViewer.setInput(showViewColumns);
						viewColumnViewer.refresh();
						TableUtil.packTable(viewColumnViewer.getTable());
					}

				} catch (Exception e) {
					logger.error("get table list", e); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_29, errStatus); //$NON-NLS-1$
				}
			}
		});
		Table tableTableList = viewListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		// sorter
		viewComparator = new ObjectComparator();
		viewListViewer.setSorter(viewComparator);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewListViewer, SWT.NONE);
		TableColumn tblclmnTableName = tableViewerColumn.getColumn();
		tblclmnTableName.setWidth(200);
		tblclmnTableName.setText("Name"); //$NON-NLS-1$
		tblclmnTableName.addSelectionListener(getSelectionAdapter(viewListViewer, viewComparator, tblclmnTableName, 0));
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return element.toString();
			}
		});
		viewListViewer.setContentProvider(new ArrayContentProvider());
		viewListViewer.setInput(showViews);

		// columns
		viewColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		viewColumnViewer.setUseHashlookup(true);
		Table tableTableColumn = viewColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		createViewColumne(viewColumnViewer);

		viewColumnViewer.setContentProvider(new ArrayContentProvider());
		viewColumnViewer.setLabelProvider(new TableColumnLabelprovider());
//		viewColumnViewer.setInput(showViewColumns);

		sashForm.setWeights(new int[] { 1, 1 });

		viewFilter = new RDBViewFilter();
		viewListViewer.addFilter(viewFilter);

		createMenu();
	}
	
	/**
	 * create menu
	 */
	private void createMenu() {
		creatAction_View = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$
		deleteAction_View = new ObjectDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$
		refreshAction_View = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$
//		modifyAction_View = new ObjectModifyAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.VIEWS, "View");

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if(PermissionChecker.isShow(strUserType, userDB)) {
					manager.add(creatAction_View);
					manager.add(deleteAction_View);
//					manager.add(modifyAction_View);
				}
				manager.add(refreshAction_View);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(viewListViewer.getTable());
		viewListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, viewListViewer);
	}

	/**
	 * tableviewer filter
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		viewFilter.setSearchText(textSearch);
		viewListViewer.refresh();
	}

	/**
	 * view 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshView(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(showViews != null) return;
		this.userDB = userDB;
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showViews = sqlClient.queryForList("viewList", userDB.getDb()); //$NON-NLS-1$

			viewListViewer.setInput(showViews);
			viewListViewer.refresh();

		} catch (Exception e) {
			logger.error("view refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_61, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (showViews != null) showViews.clear();
		viewListViewer.setInput(showViews);
		viewListViewer.refresh();

		if (showViewColumns != null) showViewColumns.clear();
		viewColumnViewer.setInput(showViewColumns);
		viewColumnViewer.refresh();
		
		creatAction_View.setUserDB(getUserDB());
		deleteAction_View.setUserDB(getUserDB());
		refreshAction_View.setUserDB(getUserDB());		
//		modifyAction_View.setUserDB(getUserDB());
	}
	
	/**
	 * get tableViewer
	 * @return
	 */
	public TableViewer getViewListViewer() {
		return viewListViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		viewFilter.setSearchText(searchText);		
	}
}

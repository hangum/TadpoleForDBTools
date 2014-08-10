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
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableColumnLabelprovider;
import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

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
	private List<String> showViews;
	private TableViewer viewColumnViewer;
	private List<TableColumnDAO> showViewColumns;
	private RDBViewFilter viewFilter;
	
	private ObjectCreatAction creatAction_View;
	private ObjectDeleteAction deleteAction_View;
	private ObjectRefreshAction refreshAction_View;
	private GenerateViewDDLAction viewDDLAction;
	private OracleObjectCompileAction objectCompileAction;

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
		viewListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if(PublicTadpoleDefine.YES_NO.NO.toString().equals(userDB.getIs_showtables())) return;
				
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
					if (null != is) {
						try {
							String viewName = (String)is.getFirstElement();
							StringBuffer sbSQL = new StringBuffer();
		
							Map<String, String> parameter = new HashMap<String, String>();
							parameter.put("db", userDB.getDb());
							parameter.put("table", viewName);
							
							SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
							List<TableColumnDAO> showTableColumns = sqlClient.queryForList("tableColumnList", parameter); //$NON-NLS-1$
							
							sbSQL.append(" SELECT "); //$NON-NLS-1$
							for (int i=0; i<showTableColumns.size(); i++) {
								TableColumnDAO dao = showTableColumns.get(i);
								sbSQL.append(dao.getField());
								
								// 마지막 컬럼에는 ,를 않넣어주어야하니까 
								if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
								else sbSQL.append(" "); //$NON-NLS-1$
							}
							sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR + " FROM " + viewName + PublicTadpoleDefine.SQL_DELIMITER); //$NON-NLS-1$ //$NON-NLS-2$
							
							//
							FindEditorAndWriteQueryUtil.run(userDB, sbSQL.toString());
						} catch(Exception e) {
							logger.error(Messages.GenerateSQLSelectAction_8, e);
							
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
						}
					}
				}
			}
		});
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
		viewComparator.setColumn(0);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewListViewer, SWT.NONE);
		TableColumn tblclmnTableName = tableViewerColumn.getColumn();
		tblclmnTableName.setWidth(200);
		tblclmnTableName.setText("Name"); //$NON-NLS-1$
		tblclmnTableName.addSelectionListener(getSelectionAdapter(viewListViewer, viewComparator, tblclmnTableName, 0));
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public Image getImage(Object element) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/view.png"); //$NON-NLS-1$
			}
			
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

		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$
		objectCompileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$
		
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
					manager.add(creatAction_View);
					manager.add(deleteAction_View);
				}
//				manager.add(modifyAction_View);
				manager.add(refreshAction_View);
					
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(viewDDLAction);
				
				if (DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT){
					manager.add(objectCompileAction);
				}
			}
		});

		viewListViewer.getTable().setMenu(menuMgr.createContextMenu(viewListViewer.getTable()));
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
			
			TableUtil.packTable(viewListViewer.getTable());
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
		
		viewDDLAction.setUserDB(getUserDB());
		objectCompileAction.setUserDB(getUserDB());
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
	
	@Override
	public void dispose() {
		super.dispose();
		
		creatAction_View.dispose();
		deleteAction_View.dispose();
		refreshAction_View.dispose();
		viewDDLAction.dispose();
		objectCompileAction.dispose();
	}
}

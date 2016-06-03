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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.DBSystemSchema;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableColumnLabelprovider;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableFilter;
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
	private CTabItem tbtmViews;
	private TableViewer viewListViewer;
	private ObjectComparator viewComparator;
	private List<TableDAO> showViews = new ArrayList<>();
	
	private TableViewer viewColumnViewer;
	private ObjectComparator tableColumnComparator;
	private List<TableColumnDAO> showViewColumns = new ArrayList<>();
	private TableFilter viewFilter;
	
	private ObjectCreatAction creatAction_View;
	private ObjectDropAction deleteAction_View;
	private ObjectRefreshAction refreshAction_View;
	private GenerateViewDDLAction viewDDLAction;
	private OracleObjectCompileAction objectCompileAction;
	
//	private TableColumnSelectionAction tableColumnSelectionAction;
	
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
		tbtmViews = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmViews.setText(Messages.get().Views);
		tbtmViews.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.VIEWS.name());

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
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
					if (null != is) {
						try {
							TableDAO viewDao = (TableDAO)is.getFirstElement();
							StringBuffer sbSQL = new StringBuffer();

							List<TableColumnDAO> showTableColumns = DBSystemSchema.getViewColumnList(userDB, viewDao);
							sbSQL.append("SELECT "); //$NON-NLS-1$
							for (int i=0; i<showTableColumns.size(); i++) {
								TableColumnDAO dao = showTableColumns.get(i);
								sbSQL.append(dao.getSysName());
								
								// 마지막 컬럼에는 ,를 않넣어주어야하니까 
								if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
								else sbSQL.append(" "); //$NON-NLS-1$
							}
							sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR + "FROM " + viewDao.getSysName() + PublicTadpoleDefine.SQL_DELIMITER); //$NON-NLS-1$ //$NON-NLS-2$
							
							//
							FindEditorAndWriteQueryUtil.run(userDB, sbSQL.toString(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS);
						} catch(Exception e) {
							logger.error("get view listt", e);
							
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
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
					if (!is.isEmpty()) {
						if (is.getFirstElement() != null) {
							TableDAO viewDao = (TableDAO)is.getFirstElement();
							showViewColumns = DBSystemSchema.getViewColumnList(userDB, viewDao);
						} else {
							showViewColumns = new ArrayList<>();
						}
						
					} else {
						showViewColumns.clear();
					}
				} catch (Exception e) {
					logger.error("get view list", e); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_29, errStatus); //$NON-NLS-1$
				} finally {
					viewColumnViewer.setInput(showViewColumns);
					tableColumnComparator = new TableColumnComparator();
					viewColumnViewer.setSorter(tableColumnComparator);
					viewColumnViewer.refresh();
					TableUtil.packTable(viewColumnViewer.getTable());
				}
			}
		});
		Table tableTableList = viewListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		// sorter
		viewComparator = new TableComparator();
		viewListViewer.setSorter(viewComparator);
		viewComparator.setColumn(0);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewListViewer, SWT.NONE);
		TableColumn tblclmnTableName = tableViewerColumn.getColumn();
		tblclmnTableName.setWidth(200);
		tblclmnTableName.setText(Messages.get().Name);
		tblclmnTableName.addSelectionListener(getSelectionAdapter(viewListViewer, viewComparator, tblclmnTableName, 0));
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public Image getImage(Object element) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/view.png"); //$NON-NLS-1$
			}
			
			@Override
			public String getText(Object element) {
				TableDAO tdbDao = (TableDAO)element;
				if("".equals(tdbDao.getSchema_name()) || null == tdbDao.getSchema_name()) return tdbDao.getName();
				else return String.format("%s.%s", tdbDao.getSchema_name(), tdbDao.getName());
			}
		});
		viewListViewer.setContentProvider(new ArrayContentProvider());
		viewListViewer.setInput(showViews);
		
		viewFilter = new TableFilter(userDB);
		viewListViewer.addFilter(viewFilter);

		createMenu();

		// columns
		viewColumnViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		viewColumnViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (null != is) {
					TableColumnDAO tableDAO = (TableColumnDAO) is.getFirstElement();
					FindEditorAndWriteQueryUtil.runAtPosition(StringUtils.trim(tableDAO.getField()));
				}
			}
		});
		Table tableTableColumn = viewColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
		tableColumnComparator = new TableColumnComparator();
		viewColumnViewer.setSorter(tableColumnComparator);

		createViewColumne();

		viewColumnViewer.setContentProvider(new ArrayContentProvider());
		viewColumnViewer.setLabelProvider(new TableColumnLabelprovider());
		
//		createTableColumnMenu();

		sashForm.setWeights(new int[] { 1, 1 });
	}
	
//	/**
//	 * create table column menu
//	 */
//	private void createTableColumnMenu() {
//		tableColumnSelectionAction = new TableColumnSelectionAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS, "View"); //$NON-NLS-1$
//		
//		// menu
//		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
//		menuMgr.setRemoveAllWhenShown(true);
//		menuMgr.addMenuListener(new IMenuListener() {
//			@Override
//			public void menuAboutToShow(IMenuManager manager) {
//				manager.add(tableColumnSelectionAction);
//			}
//		});
//		
//		viewColumnViewer.getTable().setMenu(menuMgr.createContextMenu(viewColumnViewer.getTable()));
//		getSite().registerContextMenu(menuMgr, viewColumnViewer);
//	}
	
	/**
	 * create menu
	 */
	private void createMenu() {
		if(getUserDB() == null) return;
		
		creatAction_View = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS, Messages.get().TadpoleViewerComposite_1);
		deleteAction_View = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS, Messages.get().TadpoleViewerComposite_2);
		refreshAction_View = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS, Messages.get().Refresh);
//		modifyAction_View = new ObjectModifyAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS, "View");

		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS, Messages.get().ViewDDL);
		objectCompileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VIEWS, Messages.get().TadpoleViewerComposite_6);
		
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
			if(!isDDLLock()) {
				menuMgr.add(creatAction_View);
				menuMgr.add(deleteAction_View);
				menuMgr.add(new Separator());
			}
		}
//		manager.add(modifyAction_View);
		menuMgr.add(refreshAction_View);
			
		menuMgr.add(new Separator());
		menuMgr.add(viewDDLAction);
		
		if (getUserDB().getDBDefine() == DBDefine.ORACLE_DEFAULT || getUserDB().getDBDefine() == DBDefine.TIBERO_DEFAULT){
			menuMgr.add(new Separator());
			menuMgr.add(objectCompileAction);
		}

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
	 * @param userDB
	 * @param boolRefresh
	 * @param strObjectName 
	 */
	public void refreshView(final UserDBDAO userDB, boolean boolRefresh, String strObjectName) {
		if(!boolRefresh) if(showViews != null) return;
		showViews.clear();
		this.userDB = userDB;
		
		try {
			showViews = DBSystemSchema.getViewList(userDB);
		} catch (Exception e) {
			showViews.clear();
			
			logger.error("view refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_61, errStatus); //$NON-NLS-1$
		}
		
		viewListViewer.setInput(showViews);
		viewListViewer.refresh();
		
		TableUtil.packTable(viewListViewer.getTable());
		
		// select tabitem
		getTabFolderObject().setSelection(tbtmViews);
		selectDataOfTable(strObjectName);
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		showViews.clear();
		viewListViewer.setInput(showViews);
		viewListViewer.refresh();

		showViewColumns.clear();
		viewColumnViewer.setInput(showViewColumns);
		viewColumnViewer.refresh();
		
		if(getUserDB() == null) return;
		creatAction_View.setUserDB(getUserDB());
		deleteAction_View.setUserDB(getUserDB());
		refreshAction_View.setUserDB(getUserDB());		
//		modifyAction_View.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());
		objectCompileAction.setUserDB(getUserDB());
		
		// table column
//		tableColumnSelectionAction.setUserDB(getUserDB());
	}
	
	/**
	 * get tableViewer
	 * @return
	 */
	public TableViewer getTableViewer() {
		return viewListViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		viewFilter.setSearchText(searchText);		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(creatAction_View != null) creatAction_View.dispose();
		if(deleteAction_View != null) deleteAction_View.dispose();
		if(refreshAction_View != null) refreshAction_View.dispose();
		if(viewDDLAction != null) viewDDLAction.dispose();
		if(objectCompileAction != null) objectCompileAction.dispose();
		
//		if(tableColumnSelectionAction != null) tableColumnSelectionAction.dispose();
	}
	

	/**
	 * view column
	 */
	protected void createViewColumne() {
		String[] name = {Messages.get().Field, Messages.get().Type, Messages.get().Key, Messages.get().Comment, Messages.get().Null, Messages.get().Default, Messages.get().Extra};
		int[] size = {120, 70, 50, 100, 50, 50, 50};

		ColumnViewerToolTipSupport.enableFor(viewColumnViewer);
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(viewColumnViewer, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().setMoveable(true);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tableColumn, i));
		}
	}
	
	/**
	 * selection adapter
	 * 
	 * @param tableColumn
	 * @param index
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewerColumn tableColumn, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableColumnComparator.setColumn(index);
				
				viewColumnViewer.getTable().setSortDirection(tableColumnComparator.getDirection());
				viewColumnViewer.getTable().setSortColumn(tableColumn.getColumn());
				viewColumnViewer.refresh();
			}
		};
		
		return selectionAdapter;
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if(strObjectName == null || "".equals(strObjectName)) return;
		getTableViewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<showViews.size(); i++) {
			TableDAO tableDao = (TableDAO)getTableViewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getName())) {
				getTableViewer().setSelection(new StructuredSelection(getTableViewer().getElementAt(i)), true);
				break;
			}
		}		
	}
}
